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
    fun aggregate(values: List<Number>): Double

    /**
     * Strategy to calculate the average of a list of values.
     */
    data object Average : AggregationStrategy {
        override fun aggregate(values: List<Number>): Double = Sum.aggregate(values) / values.size.toDouble()

        override fun toString(): String {
            return "Average"
        }
    }

    /**
     * Strategy to calculate the sum of a list of values.
     */
    data object Sum : AggregationStrategy {
        override fun aggregate(values: List<Number>): Double = values.reduce { a, b ->
            a.toDouble() + b.toDouble()
        }.toDouble()

        override fun toString(): String {
            return "Sum"
        }
    }

    /**
     * Strategy to calculate the maximum of a list of values.
     */
    data object Max : AggregationStrategy {
        override fun aggregate(values: List<Number>): Double = values.maxOfOrNull { it.toDouble() } ?: Double.NaN

        override fun toString(): String {
            return "Max"
        }
    }

    /**
     * Strategy to calculate the minimum of a list of values.
     */
    data object Min : AggregationStrategy {
        override fun aggregate(values: List<Number>): Double = values.minOfOrNull { it.toDouble() } ?: Double.NaN

        override fun toString(): String {
            return "Min"
        }
    }

    companion object {
        /**
         * Get an [AggregationStrategy] from a string.
         * @param name the name of the strategy.
         * @return the strategy.
         */
        fun fromString(name: String): AggregationStrategy? = when (name) {
            Average.toString() -> Average
            Sum.toString() -> Sum
            Max.toString() -> Max
            Min.toString() -> Min
            else -> null
        }
    }
}
