/*
 * Copyright (C) 2010-2024, Danilo Pianini and contributors
 * listed, for each module, in the respective subproject's build.gradle.kts file.
 *
 * This file is part of Alchemist, and is distributed under the terms of the
 * GNU General Public License, with a linking exception,
 * as described in the file LICENSE in the Alchemist distribution's top directory.
 */

package it.unibo.alchemist.mapper.data

import it.unibo.alchemist.boundary.graphql.client.NodesSubscription

/**
 * Map the reception of data to the current time.
 */
class TimeMapper : DataMapper<NodesSubscription.Data, Double?> {
    override val outputName: String = "time"
    override fun invoke(data: NodesSubscription.Data?): Double? = data?.simulation?.time?.doubleTime
}
