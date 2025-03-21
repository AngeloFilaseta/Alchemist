/*
 * Copyright (C) 2010-2023, Danilo Pianini and contributors
 * listed, for each module, in the respective subproject's build.gradle.kts file.
 *
 * This file is part of Alchemist, and is distributed under the terms of the
 * GNU General Public License, with a linking exception,
 * as described in the file LICENSE in the Alchemist distribution's top directory.
 */

package it.unibo.alchemist.model.maps.actions;

import it.unibo.alchemist.model.GeoPosition;
import it.unibo.alchemist.model.Node;
import it.unibo.alchemist.model.Reaction;
import it.unibo.alchemist.model.RoutingService;
import it.unibo.alchemist.model.RoutingServiceOptions;
import it.unibo.alchemist.model.maps.MapEnvironment;
import it.unibo.alchemist.model.maps.movestrategies.routing.IgnoreStreets;
import it.unibo.alchemist.model.maps.movestrategies.speed.StraightLineTraceDependantSpeed;
import it.unibo.alchemist.model.maps.movestrategies.target.FollowTrace;
import it.unibo.alchemist.model.movestrategies.speed.ConstantSpeed;

import java.io.Serial;

/**
 * @param <T> Concentration type
 * @param <O> {@link RoutingServiceOptions} type
 * @param <S> {@link RoutingService} type
 */
public class ReproduceGPSTrace<T, O extends RoutingServiceOptions<O>, S extends RoutingService<GeoPosition, O>>
    extends MoveOnMapWithGPS<T, O, S> {

    @Serial
    private static final long serialVersionUID = -2291955689914046763L;

    /**
     * @param environment
     *            the environment
     * @param node
     *            the node
     * @param reaction
     *            the reaction. Will be used to compute the distance to walk in
     *            every step, relying on {@link Reaction}'s getRate() method.
     * @param path
     *            resource (file, directory, ...) with GPS trace
     * @param cycle
     *            true if the traces have to be distributed cyclically
     * @param normalizer
     *            name of the class that implement the strategy to normalize the
     *            time
     * @param normalizerArgs
     *            Args to build the normalizer
     */
    public ReproduceGPSTrace(
        final MapEnvironment<T, O, S> environment,
        final Node<T> node,
        final Reaction<T> reaction,
        final String path,
        final boolean cycle,
        final String normalizer,
        final Object... normalizerArgs
    ) {
        super(environment, node,
                new IgnoreStreets<>(),
                new StraightLineTraceDependantSpeed<>(environment, node, reaction),
                new FollowTrace<>(reaction),
                path, cycle, normalizer, normalizerArgs);
    }

    /**
     * @param environment
     *            the environment
     * @param node
     *            the node
     * @param reaction
     *            the reaction. Will be used to compute the distance to walk in
     *            every step, relying on {@link Reaction}'s getRate() method.
     * @param speed
     *            the average speed
     * @param path
     *            resource (file, directory, ...) with GPS trace
     * @param cycle
     *            true if the traces have to be distributed cyclically
     * @param normalizer
     *            name of the class that implement the strategy to normalize the
     *            time
     * @param normalizerArgs
     *            Args to build the normalizer
     */
    public ReproduceGPSTrace(
        final MapEnvironment<T, O, S> environment,
        final Node<T> node,
        final Reaction<T> reaction,
        final double speed,
        final String path,
        final boolean cycle,
        final String normalizer,
        final Object... normalizerArgs
    ) {
        super(
            environment,
            node,
            new IgnoreStreets<>(),
            new ConstantSpeed<>(reaction, speed),
            new FollowTrace<>(reaction),
            path,
            cycle,
            normalizer,
            normalizerArgs
        );
    }

}
