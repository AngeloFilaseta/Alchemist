/*
 * Copyright (C) 2010-2024, Danilo Pianini and contributors
 * listed, for each module, in the respective subproject's build.gradle.kts file.
 *
 * This file is part of Alchemist, and is distributed under the terms of the
 * GNU General Public License, with a linking exception,
 * as described in the file LICENSE in the Alchemist distribution's top directory.
 */

package it.unibo.alchemist.component.sub

import it.unibo.alchemist.component.props.FormElementProps
import react.FC
import react.dom.html.ReactHTML
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h4
import web.cssom.ClassName

val FormElement: FC<FormElementProps> = FC("FormElement") { props ->
    div {
        className = ClassName("mt-2")
        h4 {
            +props.title
        }
        div {
            className = ClassName("col-sm-10")
            ReactHTML.select {
                onChange = { event ->
                    event.target.value.let {
                        props.valueChangeHandler(it)
                    }
                }
                className = ClassName("form-select")
                defaultValue = ReactHTML.option {
                    value = null
                    +"Select a ${props.title}:"
                }
                props.elements.forEach { s ->
                    ReactHTML.option {
                        +s
                        value = s
                    }
                }
            }
        }
    }
}
