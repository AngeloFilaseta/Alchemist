/*
 * Copyright (C) 2010-2024, Danilo Pianini and contributors
 * listed, for each module, in the respective subproject's build.gradle.kts file.
 *
 * This file is part of Alchemist, and is distributed under the terms of the
 * GNU General Public License, with a linking exception,
 * as described in the file LICENSE in the Alchemist distribution's top directory.
 */

package it.unibo.alchemist.component.sub

import it.unibo.alchemist.component.props.AddClientProps
import react.FC
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.input
import react.useState
import web.cssom.ClassName

/**
 * Component that renders a form suitable for adding subscription clients.
 */
val AddSubscriptionClientForm = FC<AddClientProps>("AddSubscriptionClientForm") { props ->
    var inputText by useState("")

    input {
        className = ClassName("form-control me-sm-2")
        placeholder = "Client Address (ex: localhost:8080)"
        value = inputText
        onChange = {
            inputText = it.target.value
        }
    }
    button {
        className = ClassName("btn btn-secondary pr-2")
        onClick = {
            val port = inputText.split(":").last()
            val address = inputText.removeSuffix(":$port")
            props.addClient(address, port.toInt())
            inputText = ""
        }
        +"Add client"
    }
}
