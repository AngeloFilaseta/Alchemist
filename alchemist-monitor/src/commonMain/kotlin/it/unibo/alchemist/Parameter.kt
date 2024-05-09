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
 * Represents a parameter.
 */
sealed interface Parameter {
    /**
     * The LocalSuccess Parameter from the Collektive project.
     */
    data object LocalSuccess : Parameter {
        override fun toString(): String = "localSuccess"
    }

    companion object {
        /**
         * Create a Parameter object from a string.
         * @param value the string to parse
         */
        fun fromString(value: String): Parameter? {
            return when (value) {
                LocalSuccess.toString() -> LocalSuccess
                else -> null
            }
        }
    }
}
