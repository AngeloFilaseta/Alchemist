/*
 * Copyright (C) 2010-2024, Danilo Pianini and contributors
 * listed, for each module, in the respective subproject's build.gradle.kts file.
 *
 * This file is part of Alchemist, and is distributed under the terms of the
 * GNU General Public License, with a linking exception,
 * as described in the file LICENSE in the Alchemist distribution's top directory.
 */
package it.unibo.alchemist

import com.apollographql.apollo3.api.Query
import com.apollographql.apollo3.api.Subscription
import it.unibo.alchemist.boundary.graphql.client.AllQuery
import it.unibo.alchemist.boundary.graphql.client.AllSubscription
import it.unibo.alchemist.boundary.graphql.client.ConcentrationQuery
import it.unibo.alchemist.boundary.graphql.client.ConcentrationSubscription

sealed interface ResponseSize {

    fun asQuery(parameter: Parameter): Query<*>

    fun asSubscription(parameter: Parameter): Subscription<*>

    companion object {
        fun fromString(value: String): ResponseSize? {
            return when (value) {
                Limited.toString() -> Limited
                Full.toString() -> Full
                else -> null
            }
        }
    }
}

data object Limited : ResponseSize {

    override fun asQuery(parameter: Parameter): Query<*> {
        return ConcentrationQuery(parameter.toString())
    }

    override fun asSubscription(parameter: Parameter): Subscription<*> {
        return ConcentrationSubscription(parameter.toString())
    }

    override fun toString(): String = "Limited"
}

data object Full : ResponseSize {
    override fun asQuery(parameter: Parameter): Query<*> {
        return AllQuery()
    }

    override fun asSubscription(parameter: Parameter): Subscription<*> {
        return AllSubscription()
    }

    override fun toString(): String = "Full"
}
