/*
 * Copyright (C) 2010-2024, Danilo Pianini and contributors
 * listed, for each module, in the respective subproject's build.gradle.kts file.
 *
 * This file is part of Alchemist, and is distributed under the terms of the
 * GNU General Public License, with a linking exception,
 * as described in the file LICENSE in the Alchemist distribution's top directory.
 */

package it.unibo.alchemist

import it.unibo.alchemist.boundary.graphql.client.NodesSubscription
import it.unibo.alchemist.component.AddSubscriptionClientForm
import it.unibo.alchemist.state.AddSubscripionClient
import it.unibo.alchemist.state.store
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import react.FC
import react.Props
import react.StateSetter
import react.create
import react.dom.client.createRoot
import react.dom.html.ReactHTML.p
import react.useEffect
import react.useState
import web.dom.document

val scope = MainScope()

fun main() {
    val document = document.getElementById("root") ?: error("Couldn't find container!")
    createRoot(document).render(App.create())
}

fun subscribeAndCollect(setData: StateSetter<String>) {
    scope.launch {
        store.state.subscriptionManager.subscribe(NodesSubscription()).map { entry ->
            entry.value.collect {
                console.log(it.data.toString())
                setData(it.data.toString())
            }
        }
    }
}

private val App = FC<Props> {
    val (data, setData) = useState("initial")
    useEffect(*emptyArray()) { // or useEffectOnce
        store.dispatch(AddSubscripionClient("localhost", 1313))
        store.state.subscriptionManager.clients.forEach { client ->
            console.log(client.serverUrl())
        }
        subscribeAndCollect(setData)
    }
    AddSubscriptionClientForm()
    p {
        +data
    }
}
