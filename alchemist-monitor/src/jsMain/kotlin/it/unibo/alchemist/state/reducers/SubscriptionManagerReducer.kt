/*
 * Copyright (C) 2010-2024, Danilo Pianini and contributors
 * listed, for each module, in the respective subproject's build.gradle.kts file.
 *
 * This file is part of Alchemist, and is distributed under the terms of the
 * GNU General Public License, with a linking exception,
 * as described in the file LICENSE in the Alchemist distribution's top directory.
 */

package it.unibo.alchemist.state.reducers

import it.unibo.alchemist.boundary.graphql.client.GraphQLClientFactory
import it.unibo.alchemist.monitor.GraphQLSubscriptionController
import it.unibo.alchemist.state.actions.AddSubscripionClient
import it.unibo.alchemist.state.actions.RemoveSubscriptionClient
import it.unibo.alchemist.state.actions.SubscriptionControllerAction

/**
 * Redux reducer for the subscription manager.
 * @param state the current state.
 * @param action the action to apply.
 */
fun subscriptionManagerReducer(
    state: GraphQLSubscriptionController,
    action: SubscriptionControllerAction,
): GraphQLSubscriptionController = when (action) {
    is AddSubscripionClient -> {
        GraphQLSubscriptionController.fromClients(
            state.clients + GraphQLClientFactory.subscriptionClient(action.host, action.port),
        )
    }
    is RemoveSubscriptionClient -> {
        GraphQLSubscriptionController.fromClients(
            state.clients.filterNot {
                it.host == action.host && it.port == action.port
            }.onEach { client ->
                client.close()
            },
        )
    }
}
