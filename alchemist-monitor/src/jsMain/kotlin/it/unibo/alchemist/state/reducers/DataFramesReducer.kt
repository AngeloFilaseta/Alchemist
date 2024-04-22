/*
 * Copyright (C) 2010-2024, Danilo Pianini and contributors
 * listed, for each module, in the respective subproject's build.gradle.kts file.
 *
 * This file is part of Alchemist, and is distributed under the terms of the
 * GNU General Public License, with a linking exception,
 * as described in the file LICENSE in the Alchemist distribution's top directory.
 */

package it.unibo.alchemist.state.reducers

import it.unibo.alchemist.boundary.graphql.client.GraphQLClient
import it.unibo.alchemist.dataframe.DataFrame
import it.unibo.alchemist.state.actions.Collect
import it.unibo.alchemist.state.actions.DataFramesAction

/**
 * Redux reducer for the [DataFrame]s.
 */
fun dataFramesReducer(
    state: Map<GraphQLClient, DataFrame>,
    action: DataFramesAction,
): Map<GraphQLClient, DataFrame> {
    return when (action) {
        is Collect<*> -> {
            val df = action.data.fold(state[action.client] ?: DataFrame.empty()) { acc, d ->
                acc.add(d.first, d.second)
            }
            state + (action.client to df)
        }
    }
}
