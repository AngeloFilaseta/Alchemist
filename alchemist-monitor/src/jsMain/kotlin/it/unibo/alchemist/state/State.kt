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
import it.unibo.alchemist.state.actions.DataFramesAction
import it.unibo.alchemist.state.reducers.dataFramesReducer

/**
 * State of the monitor component.
 * @param dataframes the dataframes associated with each [Subscription] to each [GraphQLClient].
 */
data class State(
    val dataframes: Map<GraphQLClient, DataFrame> = emptyMap(),
)

/**
 * Root reducer for the monitor component.
 * @param state the current state.
 * @param action the action to be applied.
 */
fun rootReducer(state: State, action: Any): State = when (action) {
    is DataFramesAction -> state.copy(
        dataframes = dataFramesReducer(state.dataframes, action),
    )
    else -> state
}
