/*
 * Copyright (C) 2010-2024, Danilo Pianini and contributors
 * listed, for each module, in the respective subproject's build.gradle.kts file.
 *
 * This file is part of Alchemist, and is distributed under the terms of the
 * GNU General Public License, with a linking exception,
 * as described in the file LICENSE in the Alchemist distribution's top directory.
 */

package it.unibo.alchemist.mapper.data

import com.apollographql.apollo3.api.Subscription
import it.unibo.alchemist.boundary.graphql.client.NodesSubscription

/**
 * Map the Nodes to the number of hits.
 */
class NumberOfHitsMapper : DataMapper<Subscription.Data, Double?> {
    override val outputName: String = "hits"
    override fun invoke(data: Subscription.Data?): Double? {
        return when (data) {
            is NodesSubscription.Data -> {
                return data.simulation.environment.nodes.flatMap { node ->
                    node.contents.entries.filter {
                        it.molecule.name.contains("hit")
                    }.map { entry ->
                        entry.molecule.name.filter { it.isDigit() }.toInt()
                    }
                }.average()
            }
            else -> null
        }
    }
}
