/*
 * Copyright (C) 2010-2023, Danilo Pianini and contributors
 * listed, for each module, in the respective subproject's build.gradle.kts file.
 *
 * This file is part of Alchemist, and is distributed under the terms of the
 * GNU General Public License, with a linking exception,
 * as described in the file LICENSE in the Alchemist distribution's top directory.
 */

package it.unibo.alchemist.boundary.webui.client.api

import io.ktor.client.call.body
import io.ktor.client.request.get
import it.unibo.alchemist.boundary.webui.client.api.utility.JsonClient.client
import it.unibo.alchemist.boundary.webui.client.api.utility.JsonClient.endpoint
import it.unibo.alchemist.boundary.webui.common.model.serialization.decodeEnvironmentSurrogate
import it.unibo.alchemist.boundary.webui.common.model.serialization.jsonFormat
import it.unibo.alchemist.boundary.webui.common.model.surrogate.EnvironmentSurrogate
import it.unibo.alchemist.boundary.webui.common.model.surrogate.PositionSurrogate
import it.unibo.alchemist.boundary.webui.common.renderer.Bitmap32Serializer
import it.unibo.alchemist.boundary.webui.common.utility.Routes
import korlibs.image.bitmap.Bitmap

/**
 * API to retrieve the Environment of the simulation.
 */
object EnvironmentApi {
    /**
     * Get the environment of the simulation in a serialized form.
     * The client will use this to render the environment.
     * @return the [EnvironmentSurrogate] retrieved by the server.
     */
    suspend fun getEnvironmentClient(): EnvironmentSurrogate<Any, PositionSurrogate> =
        jsonFormat.decodeEnvironmentSurrogate(getEnvironment(Routes.ENVIRONMENT_CLIENT_PATH))

    /**
     * Get the environment of the simulation, already rendered by the server.
     * @return the [Bitmap] corresponding to the rendered environment.
     */
    suspend fun getEnvironmentServer(): Bitmap =
        jsonFormat.decodeFromString(Bitmap32Serializer, getEnvironment(Routes.ENVIRONMENT_SERVER_PATH))

    /**
     * Get the environment of the simulation in the form proposed by the path of retrieval.
     * @param path the path of the environment retrieval.
     * @param <B> the type of the body of the response.
     */
    private suspend inline fun <reified B> getEnvironment(path: String): B = client.get(endpoint + path).body()
}
