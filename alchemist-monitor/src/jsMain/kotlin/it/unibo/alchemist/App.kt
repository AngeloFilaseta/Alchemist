/*
 * Copyright (C) 2010-2024, Danilo Pianini and contributors
 * listed, for each module, in the respective subproject's build.gradle.kts file.
 *
 * This file is part of Alchemist, and is distributed under the terms of the
 * GNU General Public License, with a linking exception,
 * as described in the file LICENSE in the Alchemist distribution's top directory.
 */

@file:Suppress("CAST_NEVER_SUCCEEDS")

package it.unibo.alchemist

import it.unibo.alchemist.boundary.graphql.client.GraphQLClientFactory
import it.unibo.alchemist.monitor.GraphQLSubscriptionManager
import org.w3c.dom.HTMLInputElement
import react.FC
import react.Props
import react.create
import react.dom.client.createRoot
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.form
import react.dom.html.ReactHTML.input
import react.dom.html.ReactHTML.li
import react.dom.html.ReactHTML.ul
import react.useState
import web.dom.document

fun main() {
    val document = document.getElementById("root") ?: error("Couldn't find container!")
    createRoot(document).render(App.create())
}

private val App = FC<Props> {
    var subscriptionManager by useState(GraphQLSubscriptionManager.fromClients(emptyList()))
    var inputText by useState("")

    form {
        input {
            placeholder = "localhost:8080"
            value = inputText
            onChange = { inputText = (it.target as HTMLInputElement).value }
        }
    }
    button {
        onClick = {
            val port = inputText.split(":").last()
            console.log(port)
            val address = inputText.removeSuffix(":$port")
            console.log(address)
            console.log("Adding client with address $address and port $port")
            try {
                val client = GraphQLClientFactory.basicClient(address, port.toInt())
                console.log(client.serverUrl)
                subscriptionManager = GraphQLSubscriptionManager.fromClients(
                    listOf(client),
                )
            } catch (e: Exception) {
                console.error(e)
            }
            inputText = ""
        }
        +"Add client"
    }
    // Render the list of clients
    ul {
        subscriptionManager.clients.forEach { client ->
            li { +client.serverUrl }
        }
    }
}
