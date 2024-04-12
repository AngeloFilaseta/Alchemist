/*
 * Copyright (C) 2010-2024, Danilo Pianini and contributors
 * listed, for each module, in the respective subproject's build.gradle.kts file.
 *
 * This file is part of Alchemist, and is distributed under the terms of the
 * GNU General Public License, with a linking exception,
 * as described in the file LICENSE in the Alchemist distribution's top directory.
 */

package it.unibo.alchemist.component

import it.unibo.alchemist.state.AddSubscripionClient
import it.unibo.alchemist.state.store
import org.w3c.dom.HTMLInputElement
import react.FC
import react.Props
import react.dom.html.ReactHTML
import react.useState

val AddSubscriptionClientForm = FC<Props>("AddSubscriptionClientForm") {
    var inputText by useState("")

    ReactHTML.form {
        ReactHTML.input {
            placeholder = "localhost:8080"
            value = inputText
            onChange = {
                @Suppress("CAST_NEVER_SUCCEEDS")
                inputText = (it.target as HTMLInputElement).value
            }
        }
    }
    ReactHTML.button {
        onClick = {
            val port = inputText.split(":").last()
            val address = inputText.removeSuffix(":$port")
            store.dispatch(AddSubscripionClient(address, port.toInt()))
            inputText = ""
        }
        +"Add client"
    }
    // Render the list of clients
    ReactHTML.ul {
        store.state.subscriptionManager.clients.forEach { client ->
            ReactHTML.li { +client.serverUrl() }
        }
    }
}
