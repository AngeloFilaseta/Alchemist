/*
 * Copyright (C) 2010-2024, Danilo Pianini and contributors
 * listed, for each module, in the respective subproject's build.gradle.kts file.
 *
 * This file is part of Alchemist, and is distributed under the terms of the
 * GNU General Public License, with a linking exception,
 * as described in the file LICENSE in the Alchemist distribution's top directory.
 */

package it.unibo.alchemist.state.reducers

import it.unibo.alchemist.dataframe.Col
import it.unibo.alchemist.dataframe.DataFrame
import it.unibo.alchemist.state.actions.AddTime
import it.unibo.alchemist.state.actions.EvaluationAction
import it.unibo.alchemist.state.actions.ResetEvaluation

/**
 * Redux reducer for the evaluation [DataFrame].
 */
fun evaluationReducer(
    state: DataFrame,
    action: EvaluationAction,
): DataFrame {
    return when (action) {
        is AddTime -> {
            state.add(Col.TIME_NAME, action.time)
        }
        is ResetEvaluation -> DataFrame.empty()
    }
}
