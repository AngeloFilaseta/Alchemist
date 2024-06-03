/*
 * Copyright (C) 2010-2024, Danilo Pianini and contributors
 * listed, for each module, in the respective subproject's build.gradle.kts file.
 *
 * This file is part of Alchemist, and is distributed under the terms of the
 * GNU General Public License, with a linking exception,
 * as described in the file LICENSE in the Alchemist distribution's top directory.
 */

package it.unibo.alchemist.component.sub

import it.unibo.alchemist.component.props.MutationButtonsProps
import react.FC
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.div
import web.cssom.ClassName
import web.html.ButtonType

/**
 * Component that renders two buttons to play and pause the simulation.
 */
val MutationButtons = FC<MutationButtonsProps>("MutationButtons") { props ->
    div {
        button {
            className = ClassName("btn btn-success me-1")
            type = ButtonType.button
            +"Play"
            onClick = { props.play() }
        }
        button {
            className = ClassName("btn btn-warning me-1")
            type = ButtonType.button
            +"Pause"
            onClick = { props.pause() }
        }
    }
}
