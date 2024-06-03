/*
 * Copyright (C) 2010-2024, Danilo Pianini and contributors
 * listed, for each module, in the respective subproject's build.gradle.kts file.
 *
 * This file is part of Alchemist, and is distributed under the terms of the
 * GNU General Public License, with a linking exception,
 * as described in the file LICENSE in the Alchemist distribution's top directory.
 */

package it.unibo.alchemist.component

import it.unibo.alchemist.component.props.NavbarProps
import it.unibo.alchemist.component.sub.AddSubscriptionClientForm
import react.FC
import react.dom.html.ReactHTML.a
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.nav
import web.cssom.ClassName

/**
 * Navbar component.
 */
val Navbar = FC<NavbarProps>("MutationButtons") { props ->
    nav {
        className = ClassName("navbar navbar-expand-lg bg-primary")
        a {
            className = ClassName("navbar-brand text-light ps-2 ")
            +"Alchemist Monitor"
        }
        div {
            className = ClassName("collapse navbar-collapse row")
            AddSubscriptionClientForm {
                addClient = props.addClient
            }
        }
    }
}
