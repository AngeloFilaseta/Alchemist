/*
 * Copyright (C) 2010-2024, Danilo Pianini and contributors
 * listed, for each module, in the respective subproject's build.gradle.kts file.
 *
 * This file is part of Alchemist, and is distributed under the terms of the
 * GNU General Public License, with a linking exception,
 * as described in the file LICENSE in the Alchemist distribution's top directory.
 */

package it.unibo.alchemist

import it.unibo.alchemist.boundary.graphql.client.NodesSubscription
import it.unibo.alchemist.data.Col
import it.unibo.alchemist.data.mapper.ApolloResponseMapper
import it.unibo.alchemist.state.AddSubscripionClient
import it.unibo.alchemist.state.store
import kotlinx.browser.document
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.jetbrains.letsPlot.frontend.JsFrontendUtil
import org.jetbrains.letsPlot.geom.geomLine
import org.jetbrains.letsPlot.ggsize
import org.jetbrains.letsPlot.letsPlot
import react.EffectBuilder
import react.FC
import react.Props
import react.create
import react.dom.client.createRoot
import react.dom.html.ReactHTML.p
import react.useEffect
import react.useEffectOnce
import react.useState
import web.dom.document as webDomDocument

val scope = MainScope()

fun main() {
    val document = webDomDocument.getElementById("root") ?: error("Couldn't find container!")
    createRoot(document).render(App.create())
}

private val App = FC<Props> {
    val (colHits, setColHits) = useState(Col("hits", emptyList<Double>()))
    val (colTime, setColTime) = useState(Col("time", emptyList<Long>()))

    val subscription by useState(NodesSubscription())

    useEffectOnce {
        store.dispatch(AddSubscripionClient("localhost", 1313))
        sub {
            store.state.subscriptionManager.subscribeAndCollect(
                subscription,
                ApolloResponseMapper.nodeSubscriptionMapper(),
                { pair ->
                    pair?.let {
                        setColTime { it + pair.first }
                        setColHits { it + pair.second }
                    }
                },
            )
        }
    }

    useEffect(listOf(colHits, colTime)) {
        addPlotDiv(colHits, colTime)
    }

    p {
        +subscription.name()
    }
}

private fun <X, Y> addPlotDiv(xCol: Col<X>, yCol: Col<Y>) {
    val data = mapOf(
        xCol.name to xCol.data,
        yCol.name to yCol.data,
    )
    var p = letsPlot(data)
    p += geomLine(color = "red", alpha = 0.3) {
        x = xCol.name
        y = yCol.name
    }
    p + ggsize(700, 350)
    val contentDiv = document.getElementById("plot")
    val plotDiv = JsFrontendUtil.createPlotDiv(p)
    contentDiv?.innerHTML = ""
    contentDiv?.appendChild(plotDiv)
}

private fun EffectBuilder.sub(block: suspend () -> Unit) {
    var ignore = false
    val job = scope.launch {
        if (!ignore) {
            block()
        }
    }
    cleanup {
        job.cancel()
        ignore = true
    }
}