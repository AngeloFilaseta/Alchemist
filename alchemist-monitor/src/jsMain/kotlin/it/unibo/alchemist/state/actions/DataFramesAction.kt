/*
 * Copyright (C) 2010-2024, Danilo Pianini and contributors
 * listed, for each module, in the respective subproject's build.gradle.kts file.
 *
 * This file is part of Alchemist, and is distributed under the terms of the
 * GNU General Public License, with a linking exception,
 * as described in the file LICENSE in the Alchemist distribution's top directory.
 */

package it.unibo.alchemist.state.actions

import it.unibo.alchemist.boundary.graphql.client.GraphQLClient

/**
 * An action to perform on a [DataFrame].
 */
sealed interface DataFramesAction

/**
 * Collect data from a [GraphQLClient].
 * @param D the type of the data.
 * @param client the client to collect data from.
 * @param data the data to collect. It consists of a list data that will be put in every column of the [DataFrame].
 */
class Collect<D>(
    val client: GraphQLClient,
    val data: List<Pair<String, D>>,
) : DataFramesAction

/**
 * Clear the [DataFrame]s.
 */
data object DataFrameClear : DataFramesAction
