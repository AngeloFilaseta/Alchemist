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

/**
 * Represents the size of the response desired by the client.
 */
sealed interface QueryType {

    /**
     * Create a Query object for the given parameter.
     * @param parameter the parameter to use in the query
     */
    fun asQuery(parameter: Parameter): Query<*>

    /**
     * Create a Subscription object for the given parameter.
     * @param parameter the parameter to use in the subscription
     */
    fun asSubscription(parameter: Parameter): Subscription<*>

    companion object {
        /**
         * Create a ResponseSize object from a string.
         * @param value the string to parse
         * @return the ResponseSize object, or null if the string is not recognized
         */
        fun fromString(value: String): QueryType? {
            return when (value) {
                Specific.toString() -> Specific
                General.toString() -> General
                else -> null
            }
        }
    }
}

/**
 * Represents a limited response size.
 */
data object Specific : QueryType {

    override fun asQuery(parameter: Parameter): Query<*> {
        return ConcentrationQuery(parameter.toString())
    }

    override fun asSubscription(parameter: Parameter): Subscription<*> {
        return ConcentrationSubscription(parameter.toString())
    }

    override fun toString(): String = "Specific"
}

/**
 * Represents a full response size.
 */
data object General : QueryType {
    override fun asQuery(parameter: Parameter): Query<*> {
        return AllQuery()
    }

    override fun asSubscription(parameter: Parameter): Subscription<*> {
        return AllSubscription()
    }

    override fun toString(): String = "General"
}
