/*
 * Copyright (C) 2010-2024, Danilo Pianini and contributors
 * listed, for each module, in the respective subproject's build.gradle.kts file.
 *
 * This file is part of Alchemist, and is distributed under the terms of the
 * GNU General Public License, with a linking exception,
 * as described in the file LICENSE in the Alchemist distribution's top directory.
 */

package it.unibo.alchemist.state.actions

import com.apollographql.apollo3.api.Query

/**
 * An action to perform on a [Query].
 */
sealed interface QueryAction

/**
 * Set the current Query.
 * @param S the type of the data.
 * @param query the query to set.
 */
data class SetQuery<S : Query<*>>(val query: S) : QueryAction

data object ClearQuery : QueryAction
