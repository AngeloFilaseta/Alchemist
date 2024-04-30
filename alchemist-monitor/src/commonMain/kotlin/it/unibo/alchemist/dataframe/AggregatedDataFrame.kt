/*
 * Copyright (C) 2010-2024, Danilo Pianini and contributors
 * listed, for each module, in the respective subproject's build.gradle.kts file.
 *
 * This file is part of Alchemist, and is distributed under the terms of the
 * GNU General Public License, with a linking exception,
 * as described in the file LICENSE in the Alchemist distribution's top directory.
 */

package it.unibo.alchemist.dataframe

import it.unibo.alchemist.dataframe.aggregation.AggregationStrategy

data class AggregatedDataFrame(
    private val allDataFrames: List<DataFrame>,
    private val aggregationStrategy: AggregationStrategy,
    override val cols: List<Col<Any?>>,
) : DataFrame {

//    /**
//     * Dataframes with at least a column and the time column.
//     */
//    private val dataFrames = allDataFrames.filter {
//        it.cols.isNotEmpty() && it.cols.any { col -> col.name == Col.TIME_NAME }
//    }
//
//    /**
//     * @return a triple containing the suggested range and step for the time column.
//     * The elements are the minimum time in all times, the second is the maximum time in all times,
//     * and the third is the average number of time points in all dataframes.
//     */
//    private fun suggestedRangeWithStep(): Triple<Double, Double, Int> {
//        val allTimeData = dataFrames.map { dataframe ->
//            dataframe.cols.filter { col -> col.name == Col.TIME_NAME }.flatMap { col -> col.data }
//        }
//        val allTime = allTimeData.flatten() as List<Double>
//        return Triple(allTime.min(), allTime.max(), allTimeData.map { it.size }.average().toInt())
//    }
//
//    private fun generateCols(): List<Col<Any?>> {
//        // new time col
//        val timeCol = suggestedRangeWithStep().let { (start, stop, num) ->
//            Col(Col.TIME_NAME, linspace(start, stop, num))
//        }
//        //all columns mapped to the new values of th
//        val allMappedCols = dataFrames.map { df ->
//            val dfColTime = df.cols.first { it.name == Col.TIME_NAME }
//            //list of timeCol size, where every element is the index of the nearest time in this specific dataframe
//            val mappingList = timeCol.data.map { target -> nearestIndex(target, dfColTime.data as List<Double>) }
//            val newCols = df.cols.map { col ->
//                Col(col.name, mappingList.map { index -> col.data[index] })
//            }
//            newCols
//        }.flatten()
//        val names = allMappedCols.map { it.name }.toSet()
//        //angelo prova a continuare
//        return listOf(timeCol as )
//    }
//
//    override val cols: List<Col<Any?>> get() = generateCols()
//
//    private fun linspace(start: Double, stop: Double, num: Int): List<Double> {
//        val step = (stop - start) / (num - 1)
//        return (0 until num).map { start + it * step }
//    }
//
//    private fun nearest(target: Double, list: List<Double>): Double {
//        return list.minByOrNull { abs(it - target) } ?: Double.NaN
//    }
//
//    private fun nearestIndex(target: Double, list: List<Double>): Int {
//       return list.indexOf(nearest(target, list))
//    }
}
