/*
 * Copyright (C) 2010-2024, Danilo Pianini and contributors
 * listed, for each module, in the respective subproject's build.gradle.kts file.
 *
 * This file is part of Alchemist, and is distributed under the terms of the
 * GNU General Public License, with a linking exception,
 * as described in the file LICENSE in the Alchemist distribution's top directory.
 */

package it.unibo.alchemist

import com.apollographql.apollo3.api.Subscription
import it.unibo.alchemist.boundary.graphql.client.GraphQLClient
import it.unibo.alchemist.component.Form
import it.unibo.alchemist.component.Info
import it.unibo.alchemist.component.Navbar
import it.unibo.alchemist.dataframe.DataFrame
import it.unibo.alchemist.mapper.data.NumberOfHitsMapper
import it.unibo.alchemist.mapper.data.TimeMapper
import it.unibo.alchemist.monitor.GraphQLSubscriptionController
import it.unibo.alchemist.state.actions.Collect
import it.unibo.alchemist.state.store
import kotlinx.browser.document
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.jetbrains.letsPlot.frontend.JsFrontendUtil
import org.jetbrains.letsPlot.geom.geomLine
import org.jetbrains.letsPlot.intern.Plot
import react.EffectBuilder
import react.FC
import react.Props
import react.create
import react.dom.client.createRoot
import react.dom.html.ReactHTML.div
import react.useEffect
import react.useState
import web.cssom.ClassName
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
    var dataframes by useState(emptyMap<GraphQLClient, DataFrame>())
    var subscription by useState<Subscription<*>?>(null)

    store.subscribe {
        subscriptionController = store.state.subscriptionController
        subscription = store.state.currentSubscription
        dataframes = store.state.dataframes
    }

    useEffect(subscriptionController, subscription) {
        subscription?.let {
            sub {
                val mappers = listOf(TimeMapper(), NumberOfHitsMapper())
                store.state.subscriptionController.subscribe(it).mapValues { (client, flow) ->
                    MainScope().launch {
                        flow.collect { response ->
                            console.log("collecting...")
                            store.dispatch(
                                Collect(
                                    client,
                                    mappers.map { m ->
                                        m.outputName to m.invoke(response.data)
                                    },
                                ),
                            )
                        }
                    }
                }
            }
        }
    }

    useEffect(listOf(dataframes)) {
        addPlotDiv(dataframes)
    }

    Navbar()
    div {
        className = ClassName("row")
        Form()
        Info {
            clients = subscriptionController.clients
            currentSubscription = subscription
        }
    }
}

private fun addPlotDiv(map: Map<GraphQLClient, DataFrame>) {
    val contentDiv = document.getElementById("plot")
    contentDiv?.innerHTML = ""
    map.forEach { (_, df) ->
        console.log(df)
        val plotDiv = JsFrontendUtil.createPlotDiv(generatePlot(df))
        contentDiv?.appendChild(plotDiv)
    }
}

private fun generatePlot(df: DataFrame): Plot {
    return df.toPlot() + geomLine(color = "red") {
        x = "time"
        y = "hits"
    }
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
