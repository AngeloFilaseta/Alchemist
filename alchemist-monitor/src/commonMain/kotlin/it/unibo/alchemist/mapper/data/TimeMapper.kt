/*
 * Copyright (C) 2010-2024, Danilo Pianini and contributors
 * listed, for each module, in the respective subproject's build.gradle.kts file.
 *
 * This file is part of Alchemist, and is distributed under the terms of the
 * GNU General Public License, with a linking exception,
 * as described in the file LICENSE in the Alchemist distribution's top directory.
 */

package it.unibo.alchemist.mapper.data

import com.apollographql.apollo3.api.Query
import com.apollographql.apollo3.api.Subscription
import it.unibo.alchemist.boundary.graphql.client.AllQuery
import it.unibo.alchemist.boundary.graphql.client.AllSubscription
import it.unibo.alchemist.boundary.graphql.client.ConcentrationQuery
import it.unibo.alchemist.boundary.graphql.client.ConcentrationSubscription
import it.unibo.alchemist.boundary.graphql.client.NodesSubscription

/**
 * Map the reception of data to the current time.
 */
class TimeMapper : DataMapper<Double> {
    override val outputName: String = "time"

    override fun invoke(data: Subscription.Data?): Double = when (data) {
        is AllSubscription.Data -> data.simulation.time
        is NodesSubscription.Data -> data.simulation.time
        is ConcentrationSubscription.Data -> data.simulation.time
        else -> 0.0
    }

    override fun invoke(data: Query.Data?): Double = when (data) {
        is AllQuery.Data -> data.simulation.time
        is ConcentrationQuery.Data -> data.simulation.time
        else -> 0.0
    }
}
