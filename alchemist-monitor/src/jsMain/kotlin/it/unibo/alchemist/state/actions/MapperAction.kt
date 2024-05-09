/*
 * Copyright (C) 2010-2024, Danilo Pianini and contributors
 * listed, for each module, in the respective subproject's build.gradle.kts file.
 *
 * This file is part of Alchemist, and is distributed under the terms of the
 * GNU General Public License, with a linking exception,
 * as described in the file LICENSE in the Alchemist distribution's top directory.
 */

package it.unibo.alchemist.state.actions

import it.unibo.alchemist.mapper.data.DataMapper

/**
 * Represents an action that can be dispatched to edit the list of mappers.
 */
sealed interface MapperAction

/**
 * Add a list of mappers to the current list.
 * @param mappers the list of mappers to add.
 */
data class AddMapper(
    val mappers: List<DataMapper<Double>>,
) : MapperAction {
    companion object {
        /**
         * Alternative constructor.
         * @param mappers the list of mappers to add.
         */
        operator fun invoke(vararg mappers: DataMapper<Double>): AddMapper {
            return AddMapper(mappers.toList())
        }
    }
}

/**
 * Clear the list of mappers.
 */
data object ClearMappers : MapperAction
