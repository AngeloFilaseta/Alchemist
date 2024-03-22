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
import it.unibo.alchemist.boundary.graphql.client.GraphQLClientFactory

data class GraphQLClientsController(private val clients: List<GraphQLClient>) {

    companion object {
        fun fromStrings(vararg connectionsStrings: String): GraphQLClientsController {
            val clients = connectionsStrings.map {
                val port = it.split(":").last().toInt()
                val address = it.removeSuffix(":$port")
                address to port
            }.map {
                GraphQLClientFactory.subscriptionClient(it.first, it.second)
            }
            return GraphQLClientsController(clients)
        }
    }
}
