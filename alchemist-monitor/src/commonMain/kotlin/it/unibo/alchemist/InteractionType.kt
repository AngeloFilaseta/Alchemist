/*
 * Copyright (C) 2010-2024, Danilo Pianini and contributors
 * listed, for each module, in the respective subproject's build.gradle.kts file.
 *
 * This file is part of Alchemist, and is distributed under the terms of the
 * GNU General Public License, with a linking exception,
 * as described in the file LICENSE in the Alchemist distribution's top directory.
 */

package it.unibo.alchemist

/**
 * Represents an interaction type.
 */
sealed interface InteractionType {
    /**
     * The Rest interaction type.
     */
    data object Rest : InteractionType {
        override fun toString(): String {
            return "Rest (Polling)"
        }
    }

    /**
     * The GraphQL interaction type.
     */
    data object GraphQL : InteractionType {
        override fun toString(): String {
            return "GraphQL (Subscription)"
        }
    }
    companion object {
        /**
         * Create an InteractionType object from a string.
         * @param value the string to parse
         */
        fun fromString(value: String): InteractionType? {
            return when (value) {
                Rest.toString() -> Rest
                GraphQL.toString() -> GraphQL
                else -> null
            }
        }
    }
}
