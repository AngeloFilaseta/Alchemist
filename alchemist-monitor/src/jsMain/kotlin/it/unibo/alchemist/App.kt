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
import it.unibo.alchemist.dataframe.AggregatedDataFrame
import it.unibo.alchemist.dataframe.DataFrame
import it.unibo.alchemist.dataframe.aggregation.AggregationStrategy
import it.unibo.alchemist.logic.GraphRenderer
import it.unibo.alchemist.logic.RequestAll.queryAll
import it.unibo.alchemist.logic.RequestAll.subscribeAll
import it.unibo.alchemist.mapper.data.DataMapper
import it.unibo.alchemist.monitor.GraphQLController
import it.unibo.alchemist.state.actions.ResetEvaluation
import it.unibo.alchemist.state.store
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

    var controller by useState(GraphQLController.fromClients(emptyList()))
    var dataframes by useState(emptyMap<GraphQLClient, DataFrame>())
    var mappers by useState<List<DataMapper<Double>>>(listOf())
    var subscription by useState<Subscription<*>?>(null)
    var query by useState<Query<*>?>(null)
    var aggregatedDf by useState(AggregatedDataFrame(emptyList(), AggregationStrategy.Average))
    var aggregationStrategy: AggregationStrategy? by useState(null)

    store.subscribe {
        subscription = store.state.currentSubscription
        query = store.state.currentQuery
        mappers = store.state.mappers
        dataframes = store.state.dataframes
        aggregationStrategy?.let { strategy ->
            aggregatedDf = AggregatedDataFrame(dataframes.values.toList(), strategy)
        }
    }

    useEffect(subscription, query) {
        store.dispatch(ResetEvaluation)
    }

    useEffect(controller, subscription) {
        subscription?.let { s ->
            launchWithCleanup {
                subscribeAll(controller, mappers, s)
            }
        }
    }

    useEffect(controller, query) {
        query?.let { q ->
            launchWithCleanup {
                while (true) {
                    queryAll(controller, mappers, q)
                    delay(1000)
                }
            }
        }
    }

    useEffect(listOf(dataframes, aggregatedDf)) {
        GraphRenderer.renderPlots(dataframes, aggregatedDf)
    }

    Navbar {
        addClient = { address, port ->
            controller = GraphQLController.fromClients(
                controller.clients + GraphQLClientFactory.subscriptionClient(address, port),
            )
        }
    }

    div {
        className = ClassName("row")
        Form {
            graphQLController = controller
            setAggregationStrategy = { aggregationStrategy = AggregationStrategy.fromString(it) }
        }
        Info {
            clients = controller.clients
            currentSubscription = subscription
        }
    }
}
