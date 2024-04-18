/*
 * Copyright (C) 2010-2024, Danilo Pianini and contributors
 * listed, for each module, in the respective subproject's build.gradle.kts file.
 *
 * This file is part of Alchemist, and is distributed under the terms of the
 * GNU General Public License, with a linking exception,
 * as described in the file LICENSE in the Alchemist distribution's top directory.
 */

package it.unibo.alchemist.data

import org.jetbrains.letsPlot.Stat
import org.jetbrains.letsPlot.geom.geomLine
import org.jetbrains.letsPlot.intern.Plot
import org.jetbrains.letsPlot.label.xlab
import org.jetbrains.letsPlot.letsPlot
import org.jetbrains.letsPlot.pos.positionFill

data class DataFrame(private val cols: List<Col<Any>>) {
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
}
