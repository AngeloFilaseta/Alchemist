/*
 * Copyright (C) 2010-2024, Danilo Pianini and contributors
 * listed, for each module, in the respective subproject's build.gradle.kts file.
 *
 * This file is part of Alchemist, and is distributed under the terms of the
 * GNU General Public License, with a linking exception,
 * as described in the file LICENSE in the Alchemist distribution's top directory.
 */

package it.unibo.alchemist

sealed interface Parameter {
    data object LocalSuccess : Parameter {
        override fun toString(): String = "localSuccess"
    }

    companion object {
        fun fromString(value: String): Parameter? {
            return when (value) {
                LocalSuccess.toString() -> LocalSuccess
                else -> null
            }
        }
    }
}
