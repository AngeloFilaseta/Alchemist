/*
 * Copyright (C) 2010-2024, Danilo Pianini and contributors
 * listed, for each module, in the respective subproject's build.gradle.kts file.
 *
 * This file is part of Alchemist, and is distributed under the terms of the
 * GNU General Public License, with a linking exception,
 * as described in the file LICENSE in the Alchemist distribution's top directory.
 */

package it.unibo.alchemist.component.sub

import it.unibo.alchemist.boundary.graphql.client.NodesSubscription
import it.unibo.alchemist.state.actions.SetSubscription
import it.unibo.alchemist.state.store
import react.FC
import react.Props
import react.dom.html.ReactHTML.option
import react.dom.html.ReactHTML.select
import react.useState
import web.cssom.ClassName

/**
 * Component that renders two buttons to play and pause the simulation.
 */
val SelectSubscriptionForm = FC<Props>("MutationButtons") {

    val subscriptions by useState(listOf(NodesSubscription()).associateBy { it.name() })

    select {
        onChange = { event ->
            subscriptions[event.target.value]?.let {
                console.log("Selected subscription: $it")
                store.dispatch(SetSubscription(it))
            }
        }
        className = ClassName("form-select")
        option {
            value = null
            selected = true
            +"Select a subscription"
        }
        subscriptions.forEach { (subName, _) ->
            option {
                +subName
                value = subName
            }
        }
    }
}
