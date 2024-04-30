/*
 * Copyright (C) 2010-2024, Danilo Pianini and contributors
 * listed, for each module, in the respective subproject's build.gradle.kts file.
 *
 * This file is part of Alchemist, and is distributed under the terms of the
 * GNU General Public License, with a linking exception,
 * as described in the file LICENSE in the Alchemist distribution's top directory.
 */

package it.unibo.alchemist.dataframe.aggregation

/**
 * Strategy to aggregate a list of values.
 */
sealed interface AggregationStrategy {
    /**
     * Aggregate a list of values.
     * @param values the list of values to aggregate.
     * @return the aggregated value.
     */
    fun aggregate(values: List<Double>): Double

    /**
     * Strategy to calculate the average of a list of values.
     */
    data object Average : AggregationStrategy {
        override fun aggregate(values: List<Double>): Double = values.average()
    }

    /**
     * Strategy to calculate the sum of a list of values.
     */
    data object Sum : AggregationStrategy {
        override fun aggregate(values: List<Double>): Double = values.sum()
    }

    /**
     * Strategy to calculate the maximum of a list of values.
     */
    data object Max : AggregationStrategy {
        override fun aggregate(values: List<Double>): Double = values.maxOrNull() ?: Double.NaN
    }

    /**
     * Strategy to calculate the minimum of a list of values.
     */
    data object Min : AggregationStrategy {
        override fun aggregate(values: List<Double>): Double = values.minOrNull() ?: Double.NaN
    }
}
