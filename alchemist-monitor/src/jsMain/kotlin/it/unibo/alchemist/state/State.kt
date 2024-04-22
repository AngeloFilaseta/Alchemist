/*
 * Copyright (C) 2010-2024, Danilo Pianini and contributors
 * listed, for each module, in the respective subproject's build.gradle.kts file.
 *
 * This file is part of Alchemist, and is distributed under the terms of the
 * GNU General Public License, with a linking exception,
 * as described in the file LICENSE in the Alchemist distribution's top directory.
 */

package it.unibo.alchemist.state

import com.apollographql.apollo3.api.Subscription
import it.unibo.alchemist.boundary.graphql.client.GraphQLClient
import it.unibo.alchemist.dataframe.DataFrame
import it.unibo.alchemist.monitor.GraphQLSubscriptionController
import it.unibo.alchemist.state.actions.DataFramesAction
import it.unibo.alchemist.state.actions.SubscriptionAction
import it.unibo.alchemist.state.actions.SubscriptionControllerAction
import it.unibo.alchemist.state.reducers.dataFramesReducer
import it.unibo.alchemist.state.reducers.subscriptionManagerReducer
import it.unibo.alchemist.state.reducers.subscriptionReducer

/**
 * State of the monitor component.
 * @param subscriptionController the [GraphQLSubscriptionController] that manages the subscriptions.
 * @param dataframes the dataframes associated with each [Subscription] to each [GraphQLClient].
 */
data class State(
    val subscriptionController: GraphQLSubscriptionController =
        GraphQLSubscriptionController.fromClients(emptyList()),
    val currentSubscription: Subscription<*>? = null,
    val dataframes: Map<GraphQLClient, DataFrame> = emptyMap(),
)

/**
 * Root reducer for the monitor component.
 * @param state the current state.
 * @param action the action to be applied.
 */
fun rootReducer(state: State, action: Any): State = when (action) {
    is SubscriptionControllerAction -> state.copy(
        subscriptionController = subscriptionManagerReducer(state.subscriptionController, action),
    )
    is SubscriptionAction -> state.copy(
        currentSubscription = subscriptionReducer(action),
    )
    is DataFramesAction -> state.copy(
        dataframes = dataFramesReducer(state.dataframes, action),
    )
    else -> state
}
