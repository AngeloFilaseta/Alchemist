/*
 * Copyright (C) 2010-2024, Danilo Pianini and contributors
 * listed, for each module, in the respective subproject's build.gradle.kts file.
 *
 * This file is part of Alchemist, and is distributed under the terms of the
 * GNU General Public License, with a linking exception,
 * as described in the file LICENSE in the Alchemist distribution's top directory.
 */

package it.unibo.alchemist.mapper.data

import com.apollographql.apollo3.api.Query
import com.apollographql.apollo3.api.Subscription
import it.unibo.alchemist.dataframe.aggregation.AggregationStrategy

/**
 * Map the list of concentrations of a molecule to a single value.
 * @param concentrationMapper the mapper to get the list of concentrations.
 * @param aggregationStrategy the strategy to aggregate the list of concentrations.
 */
data class AggregateConcentrationMapper(
    val concentrationMapper: ConcentrationMapper,
    val aggregationStrategy: AggregationStrategy,
) : DataMapper<Double> {
    override val outputName: String
        get() = concentrationMapper.outputName

    override fun invoke(data: Subscription.Data?): Double =
        aggregationStrategy.aggregate(concentrationMapper.invoke(data).filterNotNull())

    override fun invoke(data: Query.Data?): Double =
        aggregationStrategy.aggregate(concentrationMapper.invoke(data).filterNotNull())
}
