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
import it.unibo.alchemist.data.PairPlottableDataSeries
import it.unibo.alchemist.data.mapper.ApolloResponseMapper
import it.unibo.alchemist.state.AddSubscripionClient
import it.unibo.alchemist.state.store
import kotlinx.browser.document
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.jetbrains.letsPlot.frontend.JsFrontendUtil
import react.EffectBuilder
import react.FC
import react.Props
import react.create
import react.dom.client.createRoot
import react.dom.html.ReactHTML.p
import react.useEffectOnce
import react.useState
import web.dom.document as webDomDocument

val scope = MainScope()

fun main() {
    val document = webDomDocument.getElementById("root") ?: error("Couldn't find container!")
    createRoot(document).render(App.create())
}

private val App = FC<Props> {
    val (series, setSeries) = useState(PairPlottableDataSeries(emptyList<Pair<Long, Double>>()))
    val subscription by useState(NodesSubscription())

    useEffectOnce {
        store.dispatch(AddSubscripionClient("localhost", 1313))
        sub {
            store.state.subscriptionManager.subscribeAndCollect(
                subscription,
                ApolloResponseMapper.nodeSubscriptionMapper(),
                { pair ->
                    pair?.let {
                        setSeries {
                            it + pair
                        }
                        val contentDiv = document.getElementById("plot")
                        val plotDiv = JsFrontendUtil.createPlotDiv(series.toPlot("time", "hits"))
                        contentDiv?.appendChild(plotDiv)
                    }
                },
            )
        }
    }
    p {
        +subscription.name()
    }
    p {
        +series.toString()
    }
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
