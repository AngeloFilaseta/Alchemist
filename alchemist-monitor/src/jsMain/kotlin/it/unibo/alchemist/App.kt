/*
 * Copyright (C) 2010-2024, Danilo Pianini and contributors
 * listed, for each module, in the respective subproject's build.gradle.kts file.
 *
 * This file is part of Alchemist, and is distributed under the terms of the
 * GNU General Public License, with a linking exception,
 * as described in the file LICENSE in the Alchemist distribution's top directory.
 */
package it.unibo.alchemist

import com.apollographql.apollo3.api.Query
import com.apollographql.apollo3.api.Subscription
import it.unibo.alchemist.boundary.graphql.client.GraphQLClient
import it.unibo.alchemist.component.Form
import it.unibo.alchemist.component.Info
import it.unibo.alchemist.component.Navbar
import it.unibo.alchemist.dataframe.Col
import it.unibo.alchemist.dataframe.DataFrame
import it.unibo.alchemist.mapper.data.DataMapper
import it.unibo.alchemist.monitor.GraphQLSubscriptionController
import it.unibo.alchemist.state.actions.AddTime
import it.unibo.alchemist.state.actions.Collect
import it.unibo.alchemist.state.actions.ResetEvaluation
import it.unibo.alchemist.state.store
import kotlinx.browser.document
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
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

    var evaluationDf by useState(DataFrame.empty())
    var avgTime by useState(0)
    var subscriptionController by useState(GraphQLSubscriptionController.fromClients(emptyList()))
    var dataframes by useState(emptyMap<GraphQLClient, DataFrame>())
    var mappers by useState<List<DataMapper<Double>>>(listOf())
    var subscription by useState<Subscription<*>?>(null)
    var query by useState<Query<*>?>(null)

    store.subscribe {
        subscriptionController = store.state.subscriptionController
        subscription = store.state.currentSubscription
        query = store.state.currentQuery
        mappers = store.state.mappers
        dataframes = store.state.dataframes
        evaluationDf = store.state.evaluationDf
    }

    @Suppress("UNCHECKED_CAST")
    useEffect(evaluationDf) {
        val timeCol = evaluationDf.cols.firstOrNull { it.name == Col.TIME_NAME }
        timeCol?.let {
            val d = timeCol.data as List<Long>
            avgTime = d.map { it - d.min() }.zipWithNext { a, b -> b - a }.average().toInt()
        }
    }

    useEffect(subscription, query) {
        store.dispatch(ResetEvaluation)
    }

    suspend fun subscribeAll(subscription: Subscription<*>) {
        subscriptionController.subscribe(subscription).mapValues { (client, flow) ->
            MainScope().launch {
                flow.collect { response ->
                    listOf(
                        Collect(
                            client,
                            mappers.map { m ->
                                m.outputName to m.invoke(response.data as Subscription.Data)
                            },
                        ),
                        AddTime(Clock.System.now().toEpochMilliseconds()),
                    ).forEach { store.dispatch(it) }
                }
            }
        }
    }

    suspend fun queryAll(query: Query<*>) {
        subscriptionController.query(query).mapValues { (client, result) ->
            listOf(
                Collect(client, mappers.map { m -> m.outputName to m.invoke(result.data) }),
                AddTime(Clock.System.now().toEpochMilliseconds()),
            ).forEach { store.dispatch(it) }
        }
    }

    fun generatePlot(df: DataFrame, yName: String): Plot {
        return df.toPlot() + geomLine(color = "red") {
            x = "time"
            y = yName
        }
    }

    fun addPlotDiv(map: Map<GraphQLClient, DataFrame>) {
        val contentDiv = document.getElementById("plot")
        contentDiv?.innerHTML = ""
        map.forEach { (client, df) ->
            val plotDiv = JsFrontendUtil.createPlotDiv(generatePlot(df, "localSuccess"))
            contentDiv?.appendChild(plotDiv)
            contentDiv?.appendChild(
                document.createElement("p").apply {
                    innerHTML = "Data from client ${client.serverUrl()}"
                },
            )
        }
    }

    // COMPONENT
    useEffect(subscriptionController, subscription) {
        subscription?.let {
            sub {
                subscribeAll(it)
            }
        }
    }
    useEffect(subscriptionController, query) {
        query?.let {
            sub {
                while (true) {
                    queryAll(it)
                    delay(1000)
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
            averageTime = avgTime
        }
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
