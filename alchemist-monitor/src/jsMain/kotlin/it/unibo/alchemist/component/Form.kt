/*
 * Copyright (C) 2010-2024, Danilo Pianini and contributors
 * listed, for each module, in the respective subproject's build.gradle.kts file.
 *
 * This file is part of Alchemist, and is distributed under the terms of the
 * GNU General Public License, with a linking exception,
 * as described in the file LICENSE in the Alchemist distribution's top directory.
 */

package it.unibo.alchemist.component

import it.unibo.alchemist.component.sub.MutationButtons
import it.unibo.alchemist.component.sub.SelectSubscriptionForm
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h2
import react.dom.html.ReactHTML.h4
import web.cssom.ClassName

val Form = FC<Props>("Form") {
    div {
        className = ClassName("col-lg-6")
        h2 {
            +"Selection Form"
        }
        div {
            className = ClassName("row")
            h4 {
                +"Subscription"
            }
            div {
                className = ClassName("col-sm-10")
                SelectSubscriptionForm()
            }
            h4 {
                +"Mutations"
            }
            MutationButtons()
        }
    }
}
