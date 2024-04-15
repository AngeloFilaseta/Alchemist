/*
 * Copyright (C) 2010-2024, Danilo Pianini and contributors
 * listed, for each module, in the respective subproject's build.gradle.kts file.
 *
 * This file is part of Alchemist, and is distributed under the terms of the
 * GNU General Public License, with a linking exception,
 * as described in the file LICENSE in the Alchemist distribution's top directory.
 */

package it.unibo.alchemist.data

import org.jetbrains.letsPlot.intern.Plot

/**
 * Data series that can be plotted.
 * @param P data type
 */
interface PlottableDataSeries<P> {
    /**
     * Convert the data series to a plot.
     * @param xLabel the label for the x axis
     * @param yLabel the label for the y axis
     */
    fun toPlot(xLabel: String, yLabel: String): Plot
}
