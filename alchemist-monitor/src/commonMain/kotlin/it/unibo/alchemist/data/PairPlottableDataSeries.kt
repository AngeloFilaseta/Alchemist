/*
 * Copyright (C) 2010-2024, Danilo Pianini and contributors
 * listed, for each module, in the respective subproject's build.gradle.kts file.
 *
 * This file is part of Alchemist, and is distributed under the terms of the
 * GNU General Public License, with a linking exception,
 * as described in the file LICENSE in the Alchemist distribution's top directory.
 */

package it.unibo.alchemist.data

import org.jetbrains.letsPlot.geom.geomDensity
import org.jetbrains.letsPlot.intern.Plot
import org.jetbrains.letsPlot.letsPlot

data class PairPlottableDataSeries<X, Y>(
    val pairs: List<Pair<X, Y>>,
) : PlottableDataSeries<Pair<X, Y>> {

    operator fun plus(pair: Pair<X, Y>): PairPlottableDataSeries<X, Y> {
        return PairPlottableDataSeries(pairs + pair)
    }

    override fun toPlot(xLabel: String, yLabel: String): Plot {
        val plot = letsPlot(mapOf(*pairs.toTypedArray())) + geomDensity(
            color = "dark-green",
            fill = "green",
        ) {
            x = xLabel
            y = yLabel
        }
        return plot
    }
}
