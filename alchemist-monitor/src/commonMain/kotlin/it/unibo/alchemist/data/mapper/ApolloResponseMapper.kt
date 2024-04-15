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
import com.apollographql.apollo3.api.Subscription.Data
import it.unibo.alchemist.boundary.graphql.client.NodesSubscription

interface ApolloResponseMapper<D : Data, P> {

    fun invoke(response: ApolloResponse<D>): P

    companion object {
        fun nodeSubscriptionMapper(): ApolloResponseMapper<NodesSubscription.Data, Pair<Long, Double>?> {
            return NumberOfHitsMapper()
        }
    }
}
