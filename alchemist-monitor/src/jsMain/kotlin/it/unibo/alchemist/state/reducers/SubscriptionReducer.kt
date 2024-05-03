/*
 * Copyright (C) 2010-2024, Danilo Pianini and contributors
 * listed, for each module, in the respective subproject's build.gradle.kts file.
 *
 * This file is part of Alchemist, and is distributed under the terms of the
 * GNU General Public License, with a linking exception,
 * as described in the file LICENSE in the Alchemist distribution's top directory.
 */

@file:Suppress("UNCHECKED_CAST")

package it.unibo.alchemist.state.reducers

import com.apollographql.apollo3.api.Subscription
import it.unibo.alchemist.dataframe.DataFrame
import it.unibo.alchemist.state.actions.DataFrameClear
import it.unibo.alchemist.state.actions.SetSubscription
import it.unibo.alchemist.state.actions.SubscriptionAction
import it.unibo.alchemist.state.store

/**
 * Redux reducer for the [DataFrame]s.
 */
fun subscriptionReducer(
    action: SubscriptionAction,
): Subscription<Subscription.Data> {
    return when (action) {
        is SetSubscription<*> -> {
            store.dispatch(DataFrameClear)
            action.subscription as Subscription<Subscription.Data>
        }
    }
}
