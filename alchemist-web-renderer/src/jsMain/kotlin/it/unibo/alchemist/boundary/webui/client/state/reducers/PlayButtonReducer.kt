/*
 * Copyright (C) 2010-2023, Danilo Pianini and contributors
 * listed, for each module, in the respective subproject's build.gradle.kts file.
 *
 * This file is part of Alchemist, and is distributed under the terms of the
 * GNU General Public License, with a linking exception,
 * as described in the file LICENSE in the Alchemist distribution's top directory.
 */

package it.unibo.alchemist.boundary.webui.client.state.reducers

import it.unibo.alchemist.boundary.webui.client.state.actions.SetPlayButton
import it.unibo.alchemist.boundary.webui.common.utility.Action

/**
 * Reducer for the simulation play button.
 * @param simulationAction the current simulation play button state.
 * @param action the action to perform.
 * @return the new simulation play button state.
 */
fun playButtonReducer(
    simulationAction: Action,
    action: Any,
): Action =
    when (action) {
        is SetPlayButton -> action.action
        else -> simulationAction
    }
