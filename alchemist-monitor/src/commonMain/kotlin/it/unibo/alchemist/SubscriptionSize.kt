/*
 * Copyright (C) 2010-2024, Danilo Pianini and contributors
 * listed, for each module, in the respective subproject's build.gradle.kts file.
 *
 * This file is part of Alchemist, and is distributed under the terms of the
 * GNU General Public License, with a linking exception,
 * as described in the file LICENSE in the Alchemist distribution's top directory.
 */

@file:Suppress("UNCHECKED_CAST")

package it.unibo.alchemist

import com.apollographql.apollo3.api.Subscription
import it.unibo.alchemist.boundary.graphql.client.ConcentrationSubscription
import it.unibo.alchemist.boundary.graphql.client.NodesSubscription

sealed interface SubscriptionSize {

    fun asSubscription(parameter: Parameter): Subscription<Subscription.Data>

    companion object {
        fun fromString(value: String): SubscriptionSize? {
            return when (value) {
                Limited.toString() -> Limited
                Full.toString() -> Full
                else -> null
            }
        }
    }
}

data object Limited : SubscriptionSize {

    override fun asSubscription(parameter: Parameter): Subscription<Subscription.Data> {
        return ConcentrationSubscription(parameter.toString()) as Subscription<Subscription.Data>
    }
    override fun toString(): String = "Limited"
}

data object Full : SubscriptionSize {
    override fun asSubscription(parameter: Parameter): Subscription<Subscription.Data> {
        return NodesSubscription() as Subscription<Subscription.Data>
    }
    override fun toString(): String = "Full"
}
