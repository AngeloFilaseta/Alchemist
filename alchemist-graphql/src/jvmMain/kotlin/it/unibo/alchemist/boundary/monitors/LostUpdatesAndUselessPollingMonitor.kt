/*
 * Copyright (C) 2010-2024, Danilo Pianini and contributors
 * listed, for each module, in the respective subproject's build.gradle.kts file.
 *
 * This file is part of Alchemist, and is distributed under the terms of the
 * GNU General Public License, with a linking exception,
 * as described in the file LICENSE in the Alchemist distribution's top directory.
 */

package it.unibo.alchemist.boundary.monitors

import it.unibo.alchemist.boundary.OutputMonitor
import it.unibo.alchemist.model.Actionable
import it.unibo.alchemist.model.Environment
import it.unibo.alchemist.model.Time
import org.apache.commons.math3.distribution.ExponentialDistribution
import org.apache.commons.math3.random.RandomGenerator
import kotlin.time.Duration
import kotlin.time.Duration.Companion.nanoseconds
import kotlin.time.Duration.Companion.seconds

class LostUpdatesAndUselessPollingMonitor(
    randomGenerator: RandomGenerator,
    frequency: Double, // Hz
    averageResponseCreationTime: Double, // s
    jitter: Double, // s
) : OutputMonitor<Nothing, Nothing> {

    private val pollingTime: Duration = (1 / frequency).seconds
    private val jDistr = ExponentialDistribution(randomGenerator, jitter)
    private val respCreationTimeDistr = ExponentialDistribution(randomGenerator, averageResponseCreationTime)

    private var lastUpdate: Duration = System.nanoTime().nanoseconds
    private var nextUpdate: Duration = lastUpdate + pollingTime

    @Volatile
    var events = 0
        private set

    @Volatile
    var lostUpdates = 0
        private set

    @Volatile
    var uselessPolling = 0
        private set

    private var eventsFromLastUpdate = 0

    private fun computeNextUpdate() {
        lastUpdate = nextUpdate
        nextUpdate = lastUpdate + jDistr.sample().seconds + respCreationTimeDistr.sample().seconds + pollingTime
        eventsFromLastUpdate = 0
        // println("Last update: $lastUpdate, next update: $nextUpdate, diff: ${nextUpdate - lastUpdate}")
    }

    override fun stepDone(
        environment: Environment<Nothing, Nothing>,
        reaction: Actionable<Nothing>?,
        time: Time,
        step: Long,
    ) {
        if (reaction != null) {
            val now = System.nanoTime().nanoseconds
            events++
            eventsFromLastUpdate++
            when {
                now < nextUpdate && eventsFromLastUpdate > 1 -> {
                    lostUpdates++
                }
                now > nextUpdate -> {
                    computeNextUpdate()
                    if (eventsFromLastUpdate == 0) {
                        uselessPolling++
                    }
                }
            }
        }
    }
}
