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
import it.unibo.alchemist.boundary.graphql.client.GraphQLClientFactory
import it.unibo.alchemist.component.Form
import it.unibo.alchemist.component.Info
import it.unibo.alchemist.component.Navbar
import it.unibo.alchemist.dataframe.DataFrame
import it.unibo.alchemist.dataframe.aggregation.AggregationStrategy
import it.unibo.alchemist.logic.GraphRenderer
import it.unibo.alchemist.logic.GraphRenderer.RENDER_COL_SIZE_LIMIT
import it.unibo.alchemist.logic.RequestAll.queryAll
import it.unibo.alchemist.logic.RequestAll.subscribeAll
import it.unibo.alchemist.mapper.data.AggregateConcentrationMapper
import it.unibo.alchemist.mapper.data.LocalSuccessConcentrationMapper
import it.unibo.alchemist.mapper.data.TimeMapper
import it.unibo.alchemist.monitor.GraphQLController
import it.unibo.alchemist.state.store
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
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

    var graphQLController by useState(GraphQLController.fromClients(emptyList()))
    var dataframes by useState(emptyMap<GraphQLClient, DataFrame>())
    val mappers by useState(
        listOf(
            TimeMapper(),
            AggregateConcentrationMapper(LocalSuccessConcentrationMapper, AggregationStrategy.Max),
        ),
    )
    var subscription by useState<Subscription<*>?>(null)
    var query by useState<Query<*>?>(null)
    var aggregationStrategy: AggregationStrategy? by useState(null)

    store.subscribe {
        dataframes = store.state.dataframes
    }

    useEffect(graphQLController, subscription) {
        subscription?.let { s ->
            launchWithCleanup(Dispatchers.Main) {
                subscribeAll(graphQLController, mappers, s)
            }
        }
    }

    useEffect(graphQLController, query) {
        query?.let { q ->
            launchWithCleanup(Dispatchers.Main) {
                while (true) {
                    queryAll(graphQLController, mappers, q)
                    delay(1000)
                }
            }
        }
    }

    useEffect(dataframes) {
        launchWithCleanup(Dispatchers.Main) {
            val aggregatedDf = aggregationStrategy?.let { strategy ->
                DataFrame.aggregated(
                    dataframes.values.map { df ->
                        DataFrame.fromCols(df.cols(RENDER_COL_SIZE_LIMIT))
                    },
                    strategy,
                )
            }
            val plots = dataframes.mapValues { (client, df) ->
                df.toPlot(client.serverUrl(), "localSuccess", "#B8DE29", RENDER_COL_SIZE_LIMIT)
            }.toList().map { (_, plot) -> plot }
            val allPlots = aggregatedDf?.let {
                plots + (aggregatedDf.toPlot("Aggregated Plot", "localSuccess", "#482677", RENDER_COL_SIZE_LIMIT))
            } ?: plots
            GraphRenderer.renderPlots(allPlots)
        }
    }

    Navbar {
        addClient = { address, port ->
            graphQLController = GraphQLController.fromClients(
                graphQLController.clients + GraphQLClientFactory.subscriptionClient(address, port),
            )
        }
    }

    div {
        className = ClassName("row")
        Form {
            this.graphQLController = graphQLController
            this.setAggregationStrategy = { aggregationStrategy = AggregationStrategy.fromString(it) }
            this.setQuery = { query = it }
            this.setSubscription = { subscription = it }
        }
        Info {
            this.graphQLController = graphQLController
            this.subscription = subscription
        }
    }
}
