/*
 * Copyright (C) 2010-2024, Danilo Pianini and contributors
 * listed, for each module, in the respective subproject's build.gradle.kts file.
 *
 * This file is part of Alchemist, and is distributed under the terms of the
 * GNU General Public License, with a linking exception,
 * as described in the file LICENSE in the Alchemist distribution's top directory.
 */

package it.unibo.alchemist.server

import io.ktor.server.netty.EngineMain
import it.unibo.alchemist.GraphQLClientsController
import kotlinx.coroutines.runBlocking

data class MonitorServer(val connectionStrings: List<String>) {
    fun start() {
        return runBlocking {
            GraphQLClientsController.fromStrings(*connectionStrings.toTypedArray())
            EngineMain.main(emptyArray())
        }
    }
}
