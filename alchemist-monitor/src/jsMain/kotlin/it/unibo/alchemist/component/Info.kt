/*
 * Copyright (C) 2010-2024, Danilo Pianini and contributors
 * listed, for each module, in the respective subproject's build.gradle.kts file.
 *
 * This file is part of Alchemist, and is distributed under the terms of the
 * GNU General Public License, with a linking exception,
 * as described in the file LICENSE in the Alchemist distribution's top directory.
 */

package it.unibo.alchemist.component

import it.unibo.alchemist.boundary.graphql.client.ConcentrationSubscription
import it.unibo.alchemist.boundary.graphql.client.NodesSubscription
import it.unibo.alchemist.component.props.InfoProps
import react.FC
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h2
import react.dom.html.ReactHTML.h4
import react.dom.html.ReactHTML.p
import react.dom.html.ReactHTML.table
import react.dom.html.ReactHTML.tbody
import react.dom.html.ReactHTML.th
import react.dom.html.ReactHTML.thead
import react.dom.html.ReactHTML.tr
import web.cssom.ClassName

/**
 * Info component.
 */
val Info = FC("Info") { props: InfoProps ->
    div {
        h2 {
            +"Info"
        }
        className = ClassName("col-lg-4 mt-3")
        h4 {
            +"Current Subscription"
        }
        p {
            +when (props.subscription) {
                is ConcentrationSubscription ->
                    "Retrieving only `${(props.subscription as ConcentrationSubscription).moleculeName}`"
                is NodesSubscription -> "Retrieving all nodes"
                else -> "-"
            }
        }
        h4 {
            +"Connected Clients"
        }
        if (props.graphQLController.clients.isNotEmpty()) {
            table {
                className = ClassName("table table-hover")
                thead {
                    tr {
                        className = ClassName("table-dark")
                        th {
                            +"Client"
                        }
                    }
                }
                tbody {
                    props.graphQLController.clients.forEach {
                        tr {
                            th {
                                +it.serverUrl()
                            }
                        }
                    }
                }
            }
        }
    }
}
