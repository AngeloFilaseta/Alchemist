/*
 * Copyright (C) 2010-2023, Danilo Pianini and contributors
 * listed, for each module, in the respective subproject's build.gradle.kts file.
 *
 * This file is part of Alchemist, and is distributed under the terms of the
 * GNU General Public License, with a linking exception,
 * as described in the file LICENSE in the Alchemist distribution's top directory.
 */

package it.unibo.alchemist.model.sapere.actions;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.alchemist.model.Context;
import it.unibo.alchemist.model.Environment;
import it.unibo.alchemist.model.Node;
import it.unibo.alchemist.model.Reaction;
import it.unibo.alchemist.model.Route;
import it.unibo.alchemist.model.maps.MapEnvironment;
import it.unibo.alchemist.model.sapere.ILsaMolecule;
import it.unibo.alchemist.model.sapere.ILsaNode;
import it.unibo.alchemist.model.sapere.dsl.ITreeNode;
import it.unibo.alchemist.model.sapere.molecules.LsaMolecule;
import org.apache.commons.math3.random.RandomGenerator;

import java.io.Serial;
import java.util.List;

/**
 */
public class LsaRandomNeighborAction extends LsaStandardAction {

    @Serial
    private static final long serialVersionUID = -7128058274012426458L;

    private final Environment<List<ILsaMolecule>, ?> environment;
    private final MapEnvironment<List<ILsaMolecule>, ?, ?> menv;
    private final boolean initO;
    private final boolean initD;
    private final boolean initNeigh;
    private final boolean initRoute;
    private final boolean mapEnv;
    @SuppressFBWarnings(
            value = "SE_BAD_FIELD",
            justification = "All provided RandomGenerator implementations are Serializable"
    )
    private final RandomGenerator randomEngine;

    /**
     * Builds a new action in a neighborhood.
     * Es: +&lt;id, X, n&gt; This class extends
     * LsaAbstractAction.
     *
     * @param environment
     *            The environment to use
     * @param node
     *            The source node
     * @param molecule
     *            The IlsaMolecule instance you want to add to neighbor lsa
     *            space.
     * @param randomGenerator
     *            the random engine
     *
     */
    @SuppressWarnings("unchecked")
    public LsaRandomNeighborAction(
            final ILsaNode node,
            final ILsaMolecule molecule,
            final Environment<List<ILsaMolecule>, ?> environment,
            final RandomGenerator randomGenerator
    ) {
        super(molecule, node);
        final String molString = molecule.toString();
        initO = molString.contains(LsaMolecule.SYN_O);
        initD = molString.contains(LsaMolecule.SYN_D);
        initNeigh = molString.contains(LsaMolecule.SYN_NEIGH);
        initRoute = molString.contains(LsaMolecule.SYN_ROUTE);
        this.environment = environment;
        mapEnv = environment instanceof MapEnvironment;
        menv = mapEnv ? (MapEnvironment<List<ILsaMolecule>, ?, ?>) this.environment : null;
        randomEngine = randomGenerator;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LsaRandomNeighborAction cloneAction(
            final Node<List<ILsaMolecule>> node,
            final Reaction<List<ILsaMolecule>> reaction
    ) {
        return new LsaRandomNeighborAction((ILsaNode) node, getMolecule(), getEnvironment(), randomEngine);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() {
        final List<ILsaNode> nodes = getNodes();
        if (!nodes.isEmpty()) {
            final ITreeNode<?> nodeId = getMatches().get(LsaMolecule.SYN_SELECTED);
            if (nodeId == null) {
                /*
                 * Choose a random neighbor among those valid
                 */
                final ILsaNode node = nodes.get(Math.abs(randomEngine.nextInt() % nodes.size()));
                setSynthectics(node);
                setConcentration(node);
            } else {
                /*
                 * There was an operation that fixed a single neighbor
                 */
                final int id = ((Double) nodeId.getData()).intValue();
                for (final ILsaNode node : nodes) {
                    if (node.getId() == id) {
                        setSynthectics(node);
                        setConcentration(node);
                        return;
                    }
                }
                throw new IllegalStateException("there is probably a bug in " + getClass().getName() + "\nMatches: "
                        + getMatches() + "\nNodes: " + getNodes());
            }
        }
    }

    @Override
    public final Context getContext() {
        return Context.NEIGHBORHOOD;
    }

    /**
     * @return the current environment
     */
    protected Environment<List<ILsaMolecule>, ?> getEnvironment() {
        return environment;
    }

    /**
     * @return the current RandomGenerator
     */
    protected RandomGenerator getRandomGenerator() {
        return randomEngine;
    }

    /**
     * Sets the synthetic variables.
     *
     * @param node
     *            the node to use as reference (e.g., for computing the distance)
     */
    protected void setSynthectics(final ILsaNode node) {
        /*
         * #D and #ROUTE
         */
        double d = initD || initRoute ? computeDistance(node) : Double.NaN;
        if (initD) {
            d = computeDistance(node);
            setSyntheticD(d);
        }
        if (initRoute) {
            if (mapEnv) {
                final Route<?> route = menv.computeRoute(getNode(), node);
                if (route != null) {
                    final double dist = route.length();
                    d = Math.max(d, dist);
                }
            }
            setSyntheticRoute(d);
        }
        /*
         * #NEIGH
         */
        if (initNeigh) {
            setSyntheticNeigh(environment.getNeighborhood(node).getNeighbors());
        }
        /*
         * #O
         */
        if (initO) {
            setSyntheticO();
        }
    }

    private double computeDistance(final ILsaNode node) {
        return environment.getDistanceBetweenNodes(getNode(), node);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "+" + getMolecule().toString();
    }

}
