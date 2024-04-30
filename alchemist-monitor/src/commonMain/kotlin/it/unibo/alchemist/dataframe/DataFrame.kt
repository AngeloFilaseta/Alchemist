/*
 * Copyright (C) 2010-2024, Danilo Pianini and contributors
 * listed, for each module, in the respective subproject's build.gradle.kts file.
 *
 * This file is part of Alchemist, and is distributed under the terms of the
 * GNU General Public License, with a linking exception,
 * as described in the file LICENSE in the Alchemist distribution's top directory.
 */

package it.unibo.alchemist.dataframe

import org.jetbrains.letsPlot.intern.Plot
import org.jetbrains.letsPlot.letsPlot

/**
 * A data frame, containing a list of columns.
 */
interface DataFrame {

    val cols: List<Col<Any?>>

    /**
     * Add a new column to the data frame.
     * @return a new data frame with the new data added.
     */
    fun add(colName: String, data: Any?): DataFrame {
        val newCol = (cols.find { it.name == colName } ?: Col(colName, emptyList())) + data
        val newCols = cols.filter { it.name != colName } + newCol
        return DataFrameImpl(newCols)
    }

    /**
     * Create a [Plot] using the data frame.
     * @return a plot using the data frame.
     */
    fun toPlot(): Plot = letsPlot(cols.associate { it.name to it.data })

    companion object {
        /**
         * Create an empty data frame.
         */
        fun empty(): DataFrame = DataFrameImpl(emptyList())

        /**
         * Create a data frame from a list of columns.
         */
        fun fromCols(cols: List<Col<Any?>>): DataFrame = DataFrameImpl(cols)
    }
}

/**
 * A data frame, containing a list of columns.
 * @property cols the columns of the data frame.
 */
data class DataFrameImpl internal constructor(override val cols: List<Col<Any?>>) : DataFrame {
    override fun toString(): String {
        return "DataFrame(${cols.map { col -> col.name + ": " + col.data}})"
    }
}
