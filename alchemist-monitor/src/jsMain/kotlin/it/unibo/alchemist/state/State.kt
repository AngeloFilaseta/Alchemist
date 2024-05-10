/*
 * Copyright (C) 2010-2024, Danilo Pianini and contributors
 * listed, for each module, in the respective subproject's build.gradle.kts file.
 *
 * This file is part of Alchemist, and is distributed under the terms of the
 * GNU General Public License, with a linking exception,
 * as described in the file LICENSE in the Alchemist distribution's top directory.
 */

package it.unibo.alchemist.state

import com.apollographql.apollo3.api.Query
import com.apollographql.apollo3.api.Subscription
import it.unibo.alchemist.boundary.graphql.client.GraphQLClient
import it.unibo.alchemist.dataframe.DataFrame
import it.unibo.alchemist.mapper.data.DataMapper
import it.unibo.alchemist.state.actions.DataFramesAction
import it.unibo.alchemist.state.actions.EvaluationAction
import it.unibo.alchemist.state.actions.MapperAction
import it.unibo.alchemist.state.actions.QueryAction
import it.unibo.alchemist.state.actions.SubscriptionAction
import it.unibo.alchemist.state.reducers.dataFramesReducer
import it.unibo.alchemist.state.reducers.dataMapperReducer
import it.unibo.alchemist.state.reducers.evaluationReducer
import it.unibo.alchemist.state.reducers.queryReducer
import it.unibo.alchemist.state.reducers.subscriptionReducer

/**
 * State of the monitor component.
 * @param dataframes the dataframes associated with each [Subscription] to each [GraphQLClient].
 * @param currentSubscription the current subscription.
 * @param currentQuery the current query.
 * @param mappers the list of [DataMapper]s.
 * @param evaluationDf the dataframe used for evaluation.
 */
data class State(
    val currentSubscription: Subscription<*>? = null,
    val currentQuery: Query<*>? = null,
    val mappers: List<DataMapper<Double>> = listOf(),
    val dataframes: Map<GraphQLClient, DataFrame> = emptyMap(),
    val evaluationDf: DataFrame = DataFrame.empty(),
)

/**
 * Root reducer for the monitor component.
 * @param state the current state.
 * @param action the action to be applied.
 */
fun rootReducer(state: State, action: Any): State = when (action) {
    is SubscriptionAction -> state.copy(
        currentSubscription = subscriptionReducer(action),
    )
    is QueryAction -> state.copy(
        currentQuery = queryReducer(action),
    )
    is MapperAction -> state.copy(
        mappers = dataMapperReducer(state.mappers, action),
    )
    is DataFramesAction -> state.copy(
        dataframes = dataFramesReducer(state.dataframes, action),
    )
    is EvaluationAction -> state.copy(
        evaluationDf = evaluationReducer(state.evaluationDf, action),
    )
    else -> state
}
