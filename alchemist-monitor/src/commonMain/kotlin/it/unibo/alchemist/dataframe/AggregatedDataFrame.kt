/*
 * Copyright (C) 2010-2024, Danilo Pianini and contributors
 * listed, for each module, in the respective subproject's build.gradle.kts file.
 *
 * This file is part of Alchemist, and is distributed under the terms of the
 * GNU General Public License, with a linking exception,
 * as described in the file LICENSE in the Alchemist distribution's top directory.
 */
@file:Suppress("UNCHECKED_CAST")

package it.unibo.alchemist.dataframe

import it.unibo.alchemist.dataframe.aggregation.AggregationStrategy
import kotlin.math.abs

/**
 * A DataFrame that aggregates multiple dataframes.
 * @param allDataFrames the list of dataframes to aggregate.
 */
data class AggregatedDataFrame internal constructor(
    private val allDataFrames: List<DataFrame>,
    private val aggregationStrategy: AggregationStrategy,
) : DataFrame {

    /**
     * Dataframes with at least a column and the time column.
     */
    private val dataFrames = allDataFrames.filter {
        it.cols.isNotEmpty() && it.cols.any { col -> col.name == Col.TIME_NAME }
    }

    /**
     * @return a triple containing the suggested range and step for the time column.
     * The elements are the minimum time in all times, the second is the maximum time in all times,
     * and the third is the average number of time points in all dataframes.
     */
    private fun suggestedRangeWithStep(dfs: List<DataFrame>): Triple<Double, Double, Int> {
        val allTimeData = dfs.map { dataframe ->
            dataframe.cols.filter { col -> col.name == Col.TIME_NAME }.flatMap { col -> col.data }
        }
        val allTime = allTimeData.flatten() as List<Double>
        return if (allTime.isNotEmpty()) {
            Triple(allTime.min(), allTime.max(), allTimeData.map { it.size }.average().toInt())
        } else {
            Triple(0.0, 0.0, 0)
        }
    }

    private fun generateCols(): List<Col<*>> {
        // new time col
        val timeCol = suggestedRangeWithStep(dataFrames).let { (start, stop, num) ->
            Col(Col.TIME_NAME, linspace(start, stop, num))
        }
        // all columns mapped to the new values of th
        val mappedDf = dataFrames.map { df ->
            // I search for the time column in each DF
            val dfColTime = df.getColumn(Col.TIME_NAME) ?: error("Time column should be present at this point.")
                /*
                I create a list of INDEXES for each DF. These are the indexes of the nearest time to the time
                in the timeCol. Now it's time to create the new data for each param using this lookup.
                 */
            val mappingList = timeCol.data.map { target ->
                nearestIndex(target, dfColTime.data.map { (it as Number).toDouble() })
            }

            // creating the new Cols
            val newCols = df.cols.map { col ->
                Col(col.name, mappingList.map { index -> col.data[index] })
            }
            DataFrame.fromCols(newCols)
        }
        val names = mappedDf.flatMap { df -> df.cols.map { col -> col.name } }.toSet()

        val otherCols = names.map { name ->
            val namedColums = mappedDf.mapNotNull { df -> df.getColumn(name) }
            combineCols(namedColums, aggregationStrategy)
        }
        return listOf(timeCol) + otherCols
    }

    override val cols: List<Col<Any?>> get() = generateCols() as List<Col<Any?>>

    private fun linspace(start: Double, stop: Double, num: Int): List<Double> {
        val step = (stop - start) / (num - 1)
        return (0 until num).map { start + it * step }
    }

    private fun nearest(target: Double, list: List<Double>): Double {
        return list.minByOrNull { abs(it - target) } ?: Double.NaN
    }

    private fun nearestIndex(target: Double, list: List<Double>): Int {
        return list.indexOf(nearest(target, list))
    }

    private fun combineCols(lists: List<Col<*>>, aggregationStrategy: AggregationStrategy): Col<Double> {
        return Col(lists.first().name, combineLists(lists.map { it.data as List<Number> }, aggregationStrategy))
    }

    private fun combineLists(lists: List<List<Number>>, aggregationStrategy: AggregationStrategy): List<Double> {
        return (0 until lists[0].size).map { index ->
            aggregationStrategy.aggregate(lists.map { it[index] })
        }
    }
}
