/*
 * Copyright (C) 2010-2024, Danilo Pianini and contributors
 * listed, for each module, in the respective subproject's build.gradle.kts file.
 *
 * This file is part of Alchemist, and is distributed under the terms of the
 * GNU General Public License, with a linking exception,
 * as described in the file LICENSE in the Alchemist distribution's top directory.
 */

package it.unibo.alchemist.component.props

import com.apollographql.apollo3.api.Subscription
import it.unibo.alchemist.boundary.graphql.client.GraphQLClient
import react.Props

/**
 * Props for the Info component.
 * @property clients the list of clients
 * @property currentSubscription the current subscription
 */
external interface InfoProps : Props {
    var clients: List<GraphQLClient>
    var currentSubscription: Subscription<*>?
}
