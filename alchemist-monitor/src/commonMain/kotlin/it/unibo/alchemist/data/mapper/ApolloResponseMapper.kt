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

/**
 * I will probably change.
 * TODO: remove me
 */
interface ApolloResponseMapper<D : Data, P> {

    /**
     * I will change.
     * @param response the response
     * @return the result
     */
    fun invoke(response: ApolloResponse<D>): P

    companion object {
        /**
         * I will die.
         * @return death
         */
        fun nodeSubscriptionMapper(): ApolloResponseMapper<NodesSubscription.Data, Pair<Long, Double>?> {
            return NumberOfHitsMapper()
        }
    }
}
