/*
 * Copyright (C) 2010-2024, Danilo Pianini and contributors
 * listed, for each module, in the respective subproject's build.gradle.kts file.
 *
 * This file is part of Alchemist, and is distributed under the terms of the
 * GNU General Public License, with a linking exception,
 * as described in the file LICENSE in the Alchemist distribution's top directory.
 */

package it.unibo.alchemist.extractor

import it.unibo.alchemist.boundary.Extractor
import it.unibo.alchemist.boundary.monitors.LostUpdatesAndUselessPollingMonitor
import it.unibo.alchemist.model.Actionable
import it.unibo.alchemist.model.Environment
import it.unibo.alchemist.model.Time

class ExtractLostUpdatesAndUselessPollings : Extractor<Int> {

    private var targetMonitor: LostUpdatesAndUselessPollingMonitor? = null

    override val columnNames: List<String> = listOf(
        "events",
        "observations",
        "lostUpdates",
        "uselessPolling",
    )

    override fun <T> extractData(
        environment: Environment<T, *>,
        reaction: Actionable<T>?,
        time: Time,
        step: Long,
    ): Map<String, Int> {
        if (targetMonitor == null) {
            targetMonitor = environment.simulation
                ?.outputMonitors
                ?.filterIsInstance<LostUpdatesAndUselessPollingMonitor>()
                ?.firstOrNull()
        }
        return targetMonitor?.let {
            mapOf(
                "events" to it.events,
                "observations" to it.observations,
                "lostUpdates" to it.lostUpdates,
                "uselessPolling" to it.uselessPolling,
            )
        } ?: emptyMap()
    }
}
