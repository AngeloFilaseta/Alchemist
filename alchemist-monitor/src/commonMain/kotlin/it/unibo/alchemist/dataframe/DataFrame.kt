/*
 * Copyright (C) 2010-2024, Danilo Pianini and contributors
 * listed, for each module, in the respective subproject's build.gradle.kts file.
 *
 * This file is part of Alchemist, and is distributed under the terms of the
 * GNU General Public License, with a linking exception,
 * as described in the file LICENSE in the Alchemist distribution's top directory.
 */

package it.unibo.alchemist.dataframe

import org.jetbrains.letsPlot.Stat
import org.jetbrains.letsPlot.geom.geomLine
import org.jetbrains.letsPlot.intern.Plot
import org.jetbrains.letsPlot.label.xlab
import org.jetbrains.letsPlot.letsPlot
import org.jetbrains.letsPlot.pos.positionFill

/**
 * A data frame, containing a list of columns.
 * @property cols the columns of the data frame.
 */
data class DataFrame internal constructor(private val cols: List<Col<Any?>>) {

    /**
     * Add a new column to the data frame.
     * @return a new data frame with the new data added.
     */
    fun add(colName: String, data: Any?): DataFrame {
        val newCol = (cols.find { it.name == colName } ?: Col(colName, emptyList())) + data
        val newCols = cols.filter { it.name != colName } + newCol
        return DataFrame(newCols)
    }

    /**
     * Create a [Plot] using the data frame.
     */
    fun toPlot(): Plot {
        val plot = letsPlot(cols.associate { it.name to it.data }) + geomLine(
            stat = Stat.identity,
            position = positionFill(),
        ) {
            x = "time"
            xlab("Time")
            y = "hits"
        }
        return plot
    }

    companion object {
        /**
         * Create a new data frame with the given columns.
         * @param cols the columns of the data frame.
         */
        fun fromCols(vararg cols: Col<Any?>): DataFrame = DataFrame(cols.toList())

        /**
         * Create a new data frame with a single column.
         * @param colName the name of the column.
         * @param data the data of the column.
         */
        fun singleCol(colName: String, vararg data: Any?): DataFrame = fromCols(Col(colName, data.toList()))
    }
}
