/*
 * Copyright (C) 2010-2024, Danilo Pianini and contributors
 * listed, for each module, in the respective subproject's build.gradle.kts file.
 *
 * This file is part of Alchemist, and is distributed under the terms of the
 * GNU General Public License, with a linking exception,
 * as described in the file LICENSE in the Alchemist distribution's top directory.
 */

package it.unibo.alchemist.data.mapper

import com.apollographql.apollo3.api.ApolloResponse
import it.unibo.alchemist.boundary.graphql.client.NodesSubscription
import kotlinx.datetime.Clock

/**
 * I will die in a horrible way.
 * TODO: remove me
 */
class NumberOfHitsMapper : ApolloResponseMapper<NodesSubscription.Data, Pair<Long, Double>?> {
    override fun invoke(response: ApolloResponse<NodesSubscription.Data>): Pair<Long, Double>? {
        return response.data?.environment?.nodes?.flatMap { node ->
            node.contents.entries.filter {
                it.molecule.name.contains("hit")
            }.map { entry ->
                entry.molecule.name.filter { it.isDigit() }.toInt()
            }
        }?.average()?.let {
            Clock.System.now().epochSeconds to it
        }
    }
}
