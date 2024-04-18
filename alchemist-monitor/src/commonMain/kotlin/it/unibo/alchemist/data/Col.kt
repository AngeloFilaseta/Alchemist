/*
 * Copyright (C) 2010-2024, Danilo Pianini and contributors
 * listed, for each module, in the respective subproject's build.gradle.kts file.
 *
 * This file is part of Alchemist, and is distributed under the terms of the
 * GNU General Public License, with a linking exception,
 * as described in the file LICENSE in the Alchemist distribution's top directory.
 */

package it.unibo.alchemist.data

data class Col<D>(val name: String, val data: List<D>) {
    /**
     * Add a new element to the data list.
     */
    operator fun plus(other: D): Col<D> = Col(name, data + other)
}
