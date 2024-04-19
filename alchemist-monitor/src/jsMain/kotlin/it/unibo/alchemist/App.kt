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
import it.unibo.alchemist.component.AddSubscriptionClientForm
import it.unibo.alchemist.component.MutationButtons
import it.unibo.alchemist.data.Col
import it.unibo.alchemist.data.mapper.ApolloResponseMapper
import it.unibo.alchemist.monitor.GraphQLSubscriptionController
import it.unibo.alchemist.state.store
import kotlinx.browser.document
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.jetbrains.letsPlot.frontend.JsFrontendUtil
import org.jetbrains.letsPlot.geom.geomLine
import org.jetbrains.letsPlot.letsPlot
import react.EffectBuilder
import react.FC
import react.Props
import react.create
import react.dom.client.createRoot
import react.dom.html.ReactHTML.p
import react.useEffect
import react.useState
import web.dom.document as webDomDocument

/**
 * Main entry point for the application.
 * Start rendering the application in the root element using React.js.
 */
fun main() {
    val document = webDomDocument.getElementById("root") ?: error("Couldn't find container!")
    createRoot(document).render(App.create())
}

private val App = FC<Props> {

    var subscriptionController by useState(GraphQLSubscriptionController.fromClients(emptyList()))

    val (colHits, setColHits) = useState(Col("hits", emptyList<Double>()))
    val (colTime, setColTime) = useState(Col("time", emptyList<Long>()))

    val subscription by useState(NodesSubscription())

    store.subscribe {
        subscriptionController = store.state.subscriptionController
    }

    useEffect(subscriptionController) {
        sub {
            store.state.subscriptionController.subscribeAndCollect(
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
        addPlotDiv(colTime, colHits)
    }
    AddSubscriptionClientForm()
    MutationButtons()
    p {
        +subscription.name()
    }
}

private fun addPlotDiv(xCol: Col<Long>, yCol: Col<Double>) {
    val data = mapOf(
        xCol.name to xCol.data,
        yCol.name to yCol.data,
    )
    var p = letsPlot(data)
    p += geomLine(color = "red") {
        x = xCol.name
        y = yCol.name
    }
    val contentDiv = document.getElementById("plot")
    val plotDiv = JsFrontendUtil.createPlotDiv(p)
    contentDiv?.innerHTML = ""
    contentDiv?.appendChild(plotDiv)
}

private fun EffectBuilder.sub(block: suspend () -> Unit) {
    var ignore = false
    val job = MainScope().launch {
        if (!ignore) {
            block()
        }
    }
    cleanup {
        job.cancel()
        ignore = true
    }
}
