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
import com.apollographql.apollo3.api.Mutation
import com.apollographql.apollo3.api.Query
import com.apollographql.apollo3.api.Subscription
import it.unibo.alchemist.boundary.graphql.client.GraphQLClient
import it.unibo.alchemist.boundary.graphql.client.PauseSimulationMutation
import it.unibo.alchemist.boundary.graphql.client.PlaySimulationMutation
import it.unibo.alchemist.monitor.impl.GraphQLControllerImpl
import kotlinx.coroutines.flow.Flow

/**
 * Handle the subscription to the Alchemist subscription clients.
 */
interface GraphQLController {

    /**
     * The list of clients to be managed.
     */
    val clients: List<GraphQLClient>

    /**
     * Query all clients with the given query.
     * @param query the query to be executed.
     * @return a map of clients and the associated response
     * @param D the type of the data.
     */
    suspend fun <D : Query.Data> query(query: Query<D>): Map<GraphQLClient, ApolloResponse<D>> =
        clients.associateWith { client -> client.query(query).execute() }

    /**
     * Subscribe to a given subscription on all clients.
     * @param subscription the subscription to be executed
     * @return a map of clients and the associated flow of requested data
     */
    suspend fun subscribe(subscription: Subscription<*>): Map<GraphQLClient, Flow<ApolloResponse<*>>> =
        clients.associateWith { it.subscription(subscription).toFlow() }

    /**
     * Execute a mutation on all clients.
     * @param mutation the mutation to be executed.
     * @return a map of clients and the associated response
     */
    private suspend fun <D : Mutation.Data> mutation(mutation: Mutation<D>): Map<GraphQLClient, ApolloResponse<D>> =
        clients.associateWith { client -> client.mutation(mutation).execute() }

    /**
     * Play all clients.
     * @return a map of clients and the associated response
     */
    suspend fun play(): Map<GraphQLClient, ApolloResponse<PlaySimulationMutation.Data>> =
        mutation(PlaySimulationMutation())

    /**
     * Pause all clients.
     * @return a map of clients and the associated response
     */
    suspend fun pause(): Map<GraphQLClient, ApolloResponse<PauseSimulationMutation.Data>> =
        mutation(PauseSimulationMutation())

    /**
     * Close all clients that satisfy the given filter.
     * @param filter a filter to be applied to the clients.
     */
    fun close(filter: (GraphQLClient) -> Boolean) { clients.filter(filter).forEach { it.close() } }

    companion object {
        /**
         * Create a new instance from a list of clients.
         * @param clients the list of clients.
         */
        fun fromClients(clients: List<GraphQLClient>): GraphQLController =
            GraphQLControllerImpl(clients)
    }
}
