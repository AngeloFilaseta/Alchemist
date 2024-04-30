/*
 * Copyright (C) 2010-2023, Danilo Pianini and contributors
 * listed, for each module, in the respective subproject's build.gradle.kts file.
 *
 * This file is part of Alchemist, and is distributed under the terms of the
 * GNU General Public License, with a linking exception,
 * as described in the file LICENSE in the Alchemist distribution's top directory.
 */

package it.unibo.alchemist.boundary.graphql.schema.operations.subscriptions

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.server.operations.Subscription
import it.unibo.alchemist.boundary.graphql.schema.model.surrogates.EnvironmentSurrogate
import it.unibo.alchemist.model.Environment
import it.unibo.alchemist.model.Position
import it.unibo.alchemist.model.util.Environments.subscriptionMonitor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Exposes alchemist [it.unibo.alchemist.model.Environment] as a GraphQL subscription
 * through [it.unibo.alchemist.boundary.graphql.schema.model.surrogates.EnvironmentSurrogate].
 */
class EnvironmentSubscriptions<T, P : Position<out P>>(environment: Environment<T, P>) : Subscription {

    private val environmentMonitor = environment.subscriptionMonitor()

    /**
     * Returns a [Flow] with the updated value of the
     * [it.unibo.alchemist.boundary.graphql.schema.model.surrogates.EnvironmentSurrogate].
     */
    @GraphQLDescription("The simulation's environment")
    fun environment(): Flow<EnvironmentSurrogate<T, P>> = environmentMonitor.eventFlow

    /**
     * The time of the simulation.
     */
    @GraphQLDescription("The status of the simulation")
    fun simulationStatus(): Flow<String> = environmentMonitor.eventFlow.map { env ->
        env.origin.simulation.status.toString()
    }

    /**
     * The time of the simulation.
     */
    @GraphQLDescription("The time of the simulation")
    fun simulationTime(): Flow<Double> = environmentMonitor.eventFlow.map { env ->
        env.origin.simulation.time.toDouble()
    }
}
