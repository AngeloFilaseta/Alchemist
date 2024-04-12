/*
 * Copyright (C) 2010-2024, Danilo Pianini and contributors
 * listed, for each module, in the respective subproject's build.gradle.kts file.
 *
 * This file is part of Alchemist, and is distributed under the terms of the
 * GNU General Public License, with a linking exception,
 * as described in the file LICENSE in the Alchemist distribution's top directory.
 */

package it.unibo.alchemist.monitor.impl

import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Subscription
import it.unibo.alchemist.boundary.graphql.client.GraphQLClient
import it.unibo.alchemist.monitor.GraphQLSubscriptionManager
import kotlinx.coroutines.flow.Flow

/**
 * Implementation of [GraphQLSubscriptionManager].
 */
internal data class GraphQLSubscriptionManagerImpl(
    override val clients: List<GraphQLClient>,
) : GraphQLSubscriptionManager {

    override fun <D : Subscription.Data> subscribe(
        subscription: Subscription<D>,
        filter: (D) -> Boolean,
    ): Map<GraphQLClient, Flow<ApolloResponse<D>>> =
        clients.associateWith { it.subscription(subscription).toFlow() }

    override fun close(filter: (GraphQLClient) -> Boolean) {
        clients.filter(filter).forEach { it.close() }
    }
}
