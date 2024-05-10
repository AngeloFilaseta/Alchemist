/*
 * Copyright (C) 2010-2024, Danilo Pianini and contributors
 * listed, for each module, in the respective subproject's build.gradle.kts file.
 *
 * This file is part of Alchemist, and is distributed under the terms of the
 * GNU General Public License, with a linking exception,
 * as described in the file LICENSE in the Alchemist distribution's top directory.
 */

package it.unibo.alchemist.logic

import com.apollographql.apollo3.api.Query
import com.apollographql.apollo3.api.Subscription
import it.unibo.alchemist.mapper.data.DataMapper
import it.unibo.alchemist.monitor.GraphQLController
import it.unibo.alchemist.state.actions.Collect
import it.unibo.alchemist.state.store
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Utility object to make the same request to all clients.
 */
object RequestAll {

    /**
     * Subscribe to all clients. Collect the data and apply the mappers.
     * @param subscriptionController the subscription controller.
     * @param mappers the mappers to apply to the data.
     * @param subscription the subscription to subscribe to.
     */
    suspend fun subscribeAll(
        subscriptionController: GraphQLController,
        mappers: List<DataMapper<*>>,
        subscription: Subscription<*>,
    ) {
        subscriptionController.subscribe(subscription).mapValues { (client, flow) ->
            MainScope().launch {
                flow.collectLatest { response ->
                    store.dispatch(
                        Collect(
                            client,
                            mappers.map { m ->
                                m.outputName to m.invoke(response.data as Subscription.Data)
                            },
                        ),
                    )
                }
            }
        }
    }

    /**
     * Query all clients. Collect the data and apply the mappers.
     * @param subscriptionController the subscription controller.
     * @param mappers the mappers to apply to the data.
     * @param query the query to execute.
     */
    suspend fun queryAll(
        subscriptionController: GraphQLController,
        mappers: List<DataMapper<*>>,
        query: Query<*>,
    ) {
        subscriptionController.query(query).mapValues { (client, result) ->
            store.dispatch(
                Collect(client, mappers.map { m -> m.outputName to m.invoke(result.data) }),
            )
        }
    }
}
