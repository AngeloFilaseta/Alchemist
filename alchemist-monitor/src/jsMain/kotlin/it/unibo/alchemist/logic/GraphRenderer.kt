/*
 * Copyright (C) 2010-2024, Danilo Pianini and contributors
 * listed, for each module, in the respective subproject's build.gradle.kts file.
 *
 * This file is part of Alchemist, and is distributed under the terms of the
 * GNU General Public License, with a linking exception,
 * as described in the file LICENSE in the Alchemist distribution's top directory.
 */

package it.unibo.alchemist.logic

import kotlinx.browser.document
import kotlinx.dom.addClass
import org.jetbrains.letsPlot.frontend.JsFrontendUtil
import org.jetbrains.letsPlot.intern.Plot
import org.w3c.dom.HTMLDivElement

/**
 * utility object that renders the plots in the page.
 */
object GraphRenderer {

    /**
     * The limit of the columns to render.
     */
    const val RENDER_COL_SIZE_LIMIT = 1000

    private fun buildPlotDiv(plot: Plot): HTMLDivElement {
        return JsFrontendUtil.createPlotDiv(plot).apply {
            addClass("col-4")
        }
    }

    /**
     * Render the plots in the page.
     * @param map a map of caption to [Plot]s.
     */
    fun renderPlots(map: List<Plot>) {
        val plotDiv = document.getElementById("plot")
        plotDiv?.innerHTML = ""
        map.forEach { plot -> plotDiv?.appendChild(buildPlotDiv(plot)) }
    }
}
