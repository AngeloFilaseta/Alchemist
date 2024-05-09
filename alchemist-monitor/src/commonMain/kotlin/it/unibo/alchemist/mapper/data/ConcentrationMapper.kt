/*
 * Copyright (C) 2010-2024, Danilo Pianini and contributors
 * listed, for each module, in the respective subproject's build.gradle.kts file.
 *
 * This file is part of Alchemist, and is distributed under the terms of the
 * GNU General Public License, with a linking exception,
 * as described in the file LICENSE in the Alchemist distribution's top directory.
 */

package it.unibo.alchemist.mapper.data

import com.apollographql.apollo3.api.Query
import com.apollographql.apollo3.api.Subscription
import it.unibo.alchemist.boundary.graphql.client.AllQuery
import it.unibo.alchemist.boundary.graphql.client.AllSubscription
import it.unibo.alchemist.boundary.graphql.client.ConcentrationQuery
import it.unibo.alchemist.boundary.graphql.client.ConcentrationSubscription
import it.unibo.alchemist.boundary.graphql.client.NodesSubscription

/**
 * Map the concentration of a molecule to a list of values.
 * @param moleculeName the name of the molecule.
 * @param transform the transformation to apply to the concentration.
 */
sealed class ConcentrationMapper(
    private val moleculeName: String,
    private val transform: (String?) -> Double?,
) : DataMapper<List<Double?>> {
    override val outputName: String
        get() = moleculeName

    override fun invoke(data: Subscription.Data?): List<Double?> {
        return when (data) {
            is AllSubscription.Data -> {
                data.simulation.environment.nodes.map { node ->
                    node.contents.entries.filter {
                        it.molecule.name.contains(moleculeName)
                    }.map { entry -> transform(entry.concentration) }
                }.flatten()
            }
            is NodesSubscription.Data -> {
                data.simulation.environment.nodes.map { node ->
                    node.contents.entries.filter {
                        it.molecule.name.contains(moleculeName)
                    }.map { entry -> transform(entry.concentration) }
                }.flatten()
            }
            is ConcentrationSubscription.Data -> {
                data.simulation.environment.nodes.map { node ->
                    transform(node.getConcentration)
                }
            }
            else -> emptyList()
        }
    }

    override fun invoke(data: Query.Data?): List<Double?> {
        return when (data) {
            is AllQuery.Data -> {
                return data.simulation.environment.nodes.map { node ->
                    node.contents.entries.filter {
                        it.molecule.name.contains(moleculeName)
                    }.map { entry -> transform(entry.concentration) }
                }.flatten()
            }
            is ConcentrationQuery.Data -> {
                data.simulation.environment.nodes.map { node ->
                    transform(node.getConcentration)
                }
            }
            else -> emptyList()
        }
    }
}

/**
 * Map the concentration of a molecule named "localSuccess" to a list of doubles.
 */
data object LocalSuccessConcentrationMapper : ConcentrationMapper(
    moleculeName = "localSuccess",
    transform = { it?.toDoubleOrNull() },
)
