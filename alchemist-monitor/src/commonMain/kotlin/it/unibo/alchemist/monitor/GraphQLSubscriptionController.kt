/*
 * Copyright (C) 2010-2024, Danilo Pianini and contributors
 * listed, for each module, in the respective subproject's build.gradle.kts file.
 *
 * This file is part of Alchemist, and is distributed under the terms of the
 * GNU General Public License, with a linking exception,
 * as described in the file LICENSE in the Alchemist distribution's top directory.
 */

package it.unibo.alchemist.monitor

import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Subscription
import it.unibo.alchemist.boundary.graphql.client.GraphQLClient
import it.unibo.alchemist.data.mapper.ApolloResponseMapper
import it.unibo.alchemist.monitor.impl.GraphQLSubscriptionControllerImpl
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.map

/**
 * Handle the subscription to the Alchemist subscription clients.
 */
interface GraphQLSubscriptionController {

    /**
     * The list of clients to be managed.
     */
    val clients: List<GraphQLClient>

    /**
     * Subscribe to a given subscription on all clients.
     * @param subscription the subscription to be executed
     * @param filter a filter to be applied to the data
     * @return a map of clients and the associated flow of requested data
     */
    fun <D : Subscription.Data> subscribe(
        subscription: Subscription<D>,
        filter: (D) -> Boolean = { true },
    ): Map<GraphQLClient, Flow<ApolloResponse<D>>>

    /**
     * Subscribe to a given subscription on all clients and collect the data into mutable lists
     * @param subscription the subscription to be executed.
     * @param filter a filter to be applied to the data.
     */
    suspend fun <D : Subscription.Data, P> subscribeAndCollect(
        subscription: Subscription<D>,
        mapper: ApolloResponseMapper<D, P>,
        collector: FlowCollector<P>,
        filter: (D) -> Boolean = { true },
    ) {
        subscribe(subscription, filter).mapValues { entry ->
            entry.value.map { response ->
                mapper.invoke(response)
            }.collect(collector)
        }
    }

    /**
     * Close all clients that satisfy the given filter.
     * @param filter a filter to be applied to the clients.
     */
    fun close(filter: (GraphQLClient) -> Boolean = { true })

    companion object {
        /**
         * Create a new instance from a list of clients.
         * @param clients the list of clients.
         */
        fun fromClients(clients: List<GraphQLClient>): GraphQLSubscriptionController =
            GraphQLSubscriptionControllerImpl(clients)
    }
}
