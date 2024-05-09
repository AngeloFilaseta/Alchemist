/*
 * Copyright (C) 2010-2024, Danilo Pianini and contributors
 * listed, for each module, in the respective subproject's build.gradle.kts file.
 *
 * This file is part of Alchemist, and is distributed under the terms of the
 * GNU General Public License, with a linking exception,
 * as described in the file LICENSE in the Alchemist distribution's top directory.
 */

package it.unibo.alchemist.logic

import it.unibo.alchemist.boundary.graphql.client.GraphQLClient
import it.unibo.alchemist.dataframe.AggregatedDataFrame
import it.unibo.alchemist.dataframe.DataFrame
import kotlinx.browser.document
import kotlinx.dom.addClass
import org.jetbrains.letsPlot.frontend.JsFrontendUtil
import org.jetbrains.letsPlot.geom.geomLine
import org.jetbrains.letsPlot.intern.Plot
import org.w3c.dom.HTMLDivElement

/**
 * utility object that renders the plots in the page.
 */
object GraphRenderer {

    private fun generatePlot(df: DataFrame, yName: String, color: String): Plot {
        return df.toPlot() + geomLine(color = color) {
            x = "time"
            y = yName
        }
    }

    private fun generatePlotdiv(df: DataFrame, yName: String, caption: String, color: String): HTMLDivElement {
        return JsFrontendUtil.createPlotDiv(generatePlot(df, yName, color)).apply {
            addClass("col-4")
            appendChild(
                document.createElement("p").apply {
                    innerHTML = caption
                },
            )
        }
    }

    /**
     * Render the plots in the page.
     * @param map a map of [GraphQLClient] to [DataFrame]
     * @param aggregatedDataFrame the aggregated data frame
     */
    fun renderPlots(map: Map<GraphQLClient, DataFrame>, aggregatedDataFrame: AggregatedDataFrame) {
        val contentDiv = document.getElementById("plot")
        contentDiv?.innerHTML = ""
        map.map { (client, df) -> generatePlotdiv(df, "localSuccess", client.serverUrl(), "red") }.forEach {
            contentDiv?.appendChild(it)
        }
        if (aggregatedDataFrame.cols.isNotEmpty()) {
            val aggregateDiv = generatePlotdiv(aggregatedDataFrame, "localSuccess", "AGGREGATE", "blue")
            contentDiv?.appendChild(aggregateDiv)
        }
    }
}
