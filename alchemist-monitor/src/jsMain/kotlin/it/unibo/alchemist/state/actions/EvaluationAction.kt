/*
 * Copyright (C) 2010-2024, Danilo Pianini and contributors
 * listed, for each module, in the respective subproject's build.gradle.kts file.
 *
 * This file is part of Alchemist, and is distributed under the terms of the
 * GNU General Public License, with a linking exception,
 * as described in the file LICENSE in the Alchemist distribution's top directory.
 */

package it.unibo.alchemist.state.actions

/**
 * Represents an action that can be dispatched to edit the evaluation state.
 */
sealed interface EvaluationAction

/**
 * Add a time to the evaluation state.
 * @param time the time to add.
 */
data class AddTime(val time: Long) : EvaluationAction

/**
 * Reset the evaluation state.
 */
data object ResetEvaluation : EvaluationAction
