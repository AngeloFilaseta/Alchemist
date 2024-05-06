/*
 * Copyright (C) 2010-2024, Danilo Pianini and contributors
 * listed, for each module, in the respective subproject's build.gradle.kts file.
 *
 * This file is part of Alchemist, and is distributed under the terms of the
 * GNU General Public License, with a linking exception,
 * as described in the file LICENSE in the Alchemist distribution's top directory.
 */

package it.unibo.alchemist.server.modules

import io.ktor.http.HttpMethod
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.cors.routing.CORS
/**
 * Ktor module that adds all the routing configuration to an application.
 * @see <a href="https://ktor.io/docs/modules.html">Ktor Modules</a>
 */
fun Application.installModule() {
    install(CORS) {
        HttpMethod.DefaultMethods.forEach {
            allowMethod(it)
        }
        allowOrigins { true }
        allowNonSimpleContentTypes = true
        allowCredentials = true
        allowSameOrigin = true
        allowHost("*", listOf("http", "https"))
        anyHost()
    }
}
