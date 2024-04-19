/*
 * Copyright (C) 2010-2024, Danilo Pianini and contributors
 * listed, for each module, in the respective subproject's build.gradle.kts file.
 *
 * This file is part of Alchemist, and is distributed under the terms of the
 * GNU General Public License, with a linking exception,
 * as described in the file LICENSE in the Alchemist distribution's top directory.
 */

package it.unibo.alchemist

import it.unibo.alchemist.boundary.graphql.client.GraphQLClient
import it.unibo.alchemist.boundary.graphql.client.NodesSubscription
import it.unibo.alchemist.component.AddSubscriptionClientForm
import it.unibo.alchemist.component.MutationButtons
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
    var dataframes by useState(emptyMap<GraphQLClient, DataFrame>())

    val subscription by useState(NodesSubscription())

    store.subscribe {
        subscriptionController = store.state.subscriptionController
        dataframes = store.state.dataframes
    }

    useEffect(subscriptionController) {
        sub {
            store.state.subscriptionController.subscribeAndCollect(
                subscription,
                listOf(NumberOfHitsMapper(), TimeMapper()),
                { client, name, flow ->
                    flow.collect {
                        console.log("collectiong: $client $name $it")
                        store.dispatch(Collect(client, name, it))
                    }
                },
            )
        }
    }

    useEffect(listOf(dataframes)) {
        addPlotDiv(dataframes.values.first())
    }

    AddSubscriptionClientForm()
    MutationButtons()

    p {
        +"Current Subscription: ${subscription.name()}"
    }
}

private fun addPlotDiv(df: DataFrame) {
    val p = df.toPlot() + geomLine(color = "red") {
        x = "time"
        y = "hits"
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
