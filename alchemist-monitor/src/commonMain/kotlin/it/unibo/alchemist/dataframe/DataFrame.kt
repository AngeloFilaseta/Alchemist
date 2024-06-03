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
import org.jetbrains.letsPlot.geom.geomLine
import org.jetbrains.letsPlot.intern.Plot
import org.jetbrains.letsPlot.label.ggtitle
import org.jetbrains.letsPlot.letsPlot

/**
 * A data frame, containing a list of columns.
 */
interface DataFrame {

    /**
     * The columns of the data frame.
     */
    val cols: List<Col<*>>

    /**
     * Retrieve the columns of the data frame.
     * Each column is limited to the last [limit] elements.
     */
    fun cols(limit: Int): List<Col<*>> = cols.map { col ->
        Col(col.name, col.data.takeLast(limit))
    }

    /**
     * Get a column by name.
     * @return the column with the given name, or null if it does not exist.
     */
    fun getColumn(name: String): Col<*>? = cols.firstOrNull { it.name == name }

    /**
     * Get a column by name, with the specified type.
     * @return the column with the given name, or null if it does not exist.
     */
    fun <D> getColumnTypedUnsafe(name: String): Col<D>? = getColumn(name) as Col<D>

    /**
     * Add a new column to the data frame.
     * @return a new data frame with the new data added.
     */
    fun <D : Any?>add(colName: String, data: D): DataFrame {
        val newCol = (cols.find { it.name == colName } as Col<D>? ?: Col(colName, emptyList())) + data
        val newCols = cols.filter { it.name != colName } + newCol
        return DataFrameImpl(newCols)
    }

    /**
     * Create a [Plot] using the data frame.
     * @param limit the max number of elements for each column.
     * @return a plot using the data frame.
     */
    fun toPlot(title: String, yName: String, color: String, limit: Int): Plot =
        letsPlot(cols(limit).associate { it.name to it.data }) + geomLine(color = color, size = 2.0) {
            x = "time"
            y = yName
        } + ggtitle(title)

    companion object {
        /**
         * Create an empty data frame.
         */
        fun empty(): DataFrame = DataFrameImpl(emptyList())

        /**
         * Create a data frame from a list of columns.
         * @param cols the columns of the data frame.
         */
        fun fromCols(cols: List<Col<*>>): DataFrame = DataFrameImpl(cols)

        /**
         * Create a data frame from a list of columns.
         * @param dataframes the dataframes to aggregate.
         * @param strategy the aggregation strategy to use.
         */
        fun aggregated(dataframes: List<DataFrame>, strategy: AggregationStrategy): AggregatedDataFrame {
            return AggregatedDataFrame(dataframes, strategy)
        }
    }
}

/**
 * A data frame, containing a list of columns.
 * @property cols the columns of the data frame.
 */
data class DataFrameImpl internal constructor(override val cols: List<Col<*>>) : DataFrame {
    override fun toString(): String {
        return "DataFrame(${cols.map { col -> col.name + ": " + col.data}})"
    }
}
