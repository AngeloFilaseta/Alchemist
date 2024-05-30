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
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.nanoseconds
import kotlin.time.Duration.Companion.seconds

class LostUpdatesAndUselessPollingMonitor @JvmOverloads constructor(
    randomGenerator: RandomGenerator,
    val frequency: Double, // Hz
    averageResponseCreationTime: Double, // s
    jitter: Double, // s
    val artificialSlowDown: Long = 0,
) : OutputMonitor<Nothing, Nothing> {

    private val pollingTime: Duration = (1 / frequency).seconds
    private val jDistr = ExponentialDistribution(randomGenerator, jitter)
    private val respCreationTimeDistr = ExponentialDistribution(randomGenerator, averageResponseCreationTime)

    private var lastUpdate: Duration = System.currentTimeMillis().milliseconds
    private var nextUpdate: Duration = lastUpdate + pollingTime
    private val beginning = System.currentTimeMillis().milliseconds

    @Volatile
    var events = 0
        private set

    @Volatile
    var observations = 0
        private set

    @Volatile
    var lostUpdates = 0
        private set

    @Volatile
    var uselessPolling = 0
        private set

    private var eventsFromLastUpdate = 0

    fun nextObservation() {
        lastUpdate = nextUpdate
        val jitter = jDistr.sample().seconds
        val responseCreation = respCreationTimeDistr.sample().seconds
        nextUpdate = lastUpdate + jitter + responseCreation + pollingTime
        check(nextUpdate > lastUpdate)
    }

    override fun initialized(environment: Environment<Nothing, Nothing>) {
        lastUpdate = 0.nanoseconds
        nextObservation()
    }

    override fun stepDone(
        environment: Environment<Nothing, Nothing>,
        reaction: Actionable<Nothing>?,
        time: Time,
        step: Long,
    ) {
        Thread.sleep(artificialSlowDown)
        if (reaction != null) {
            val now = System.currentTimeMillis().milliseconds
//            println("Now is ${(now - beginning).inWholeMilliseconds}ms")
            events++
            eventsFromLastUpdate++
            when {
                now < nextUpdate -> {
                    if (eventsFromLastUpdate > 1) {
                        lostUpdates++
                    }
                }
                now >= nextUpdate -> {
                    when (eventsFromLastUpdate) {
                        0 -> error("bug")
                        1 -> {
                            var observationsBetweenEvents = 0
                            do {
                                observationsBetweenEvents++
                                nextObservation()
                            } while (nextUpdate <= now)
                            uselessPolling += observationsBetweenEvents - 1
                            observations += observationsBetweenEvents
                            eventsFromLastUpdate = 0
                        }
                        else -> {
                            observations++
                            eventsFromLastUpdate = 0
                        }
                    }
                }
            }
        }
    }
}
