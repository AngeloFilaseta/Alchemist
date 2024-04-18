/*
 * Copyright (C) 2010-2024, Danilo Pianini and contributors
 * listed, for each module, in the respective subproject's build.gradle.kts file.
 *
 * This file is part of Alchemist, and is distributed under the terms of the
 * GNU General Public License, with a linking exception,
 * as described in the file LICENSE in the Alchemist distribution's top directory.
 */

package it.unibo.alchemist.component

import it.unibo.alchemist.state.store
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import react.FC
import react.Props
import react.dom.html.ReactHTML.button
import web.cssom.ClassName

val MutationButtons = FC<Props>("MutationButtons") {
    button {
        ClassName("btn btn-outline-success")
        +"Play"
        onClick = {
            MainScope().launch {
                store.state.subscriptionController.play()
            }
        }
    }
    button {
        ClassName("btn btn-outline-danger")
        +"Pause"
        onClick = {
            MainScope().launch {
                store.state.subscriptionController.pause()
            }
        }
    }
}
