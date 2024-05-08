/*
 * Copyright (C) 2010-2024, Danilo Pianini and contributors
 * listed, for each module, in the respective subproject's build.gradle.kts file.
 *
 * This file is part of Alchemist, and is distributed under the terms of the
 * GNU General Public License, with a linking exception,
 * as described in the file LICENSE in the Alchemist distribution's top directory.
 */

package it.unibo.alchemist.state.reducers

import com.apollographql.apollo3.api.Query
import it.unibo.alchemist.dataframe.DataFrame
import it.unibo.alchemist.state.actions.ClearQuery
import it.unibo.alchemist.state.actions.DataFrameClear
import it.unibo.alchemist.state.actions.QueryAction
import it.unibo.alchemist.state.actions.SetQuery
import it.unibo.alchemist.state.store

/**
 * Redux reducer for the [DataFrame]s.
 */
fun queryReducer(action: QueryAction): Query<*>? {
    return when (action) {
        is SetQuery<*> -> {
            store.dispatch(DataFrameClear)
            action.query
        }
        is ClearQuery -> null
    }
}
