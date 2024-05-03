/*
 * Copyright (C) 2010-2024, Danilo Pianini and contributors
 * listed, for each module, in the respective subproject's build.gradle.kts file.
 *
 * This file is part of Alchemist, and is distributed under the terms of the
 * GNU General Public License, with a linking exception,
 * as described in the file LICENSE in the Alchemist distribution's top directory.
 */

package it.unibo.alchemist.state.reducers

import it.unibo.alchemist.mapper.data.DataMapper
import it.unibo.alchemist.state.actions.AddMapper
import it.unibo.alchemist.state.actions.ClearMappers
import it.unibo.alchemist.state.actions.MapperAction

/**
 * Redux reducer for the [DataMapper]s.
 */
fun dataMapperReducer(state: List<DataMapper<Double>>, action: MapperAction): List<DataMapper<Double>> =
    when (action) {
        is AddMapper -> state + action.mappers
        is ClearMappers -> emptyList()
    }
