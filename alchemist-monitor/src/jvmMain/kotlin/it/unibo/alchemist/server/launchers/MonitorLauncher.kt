/*
 * Copyright (C) 2010-2024, Danilo Pianini and contributors
 * listed, for each module, in the respective subproject's build.gradle.kts file.
 *
 * This file is part of Alchemist, and is distributed under the terms of the
 * GNU General Public License, with a linking exception,
 * as described in the file LICENSE in the Alchemist distribution's top directory.
 */

package it.unibo.alchemist.server.launchers

import io.ktor.server.netty.EngineMain
import it.unibo.alchemist.boundary.Loader
import it.unibo.alchemist.boundary.launchers.DefaultLauncher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("MonitorLauncher")

class MonitorLauncher(
    val host: String = "127.0.0.1",
    val port: Int = 9090,
    override val batch: List<String> = emptyList(),
    override val autoStart: Boolean = true,
    override val showProgress: Boolean = true,
    override val parallelism: Int = Runtime.getRuntime().availableProcessors(),
) : DefaultLauncher(batch, autoStart, showProgress, parallelism) {
    override fun launch(loader: Loader) {
        CoroutineScope(Dispatchers.IO).launch {
            logger.info("Starting monitor server on $host:$port")
            EngineMain.main(emptyArray())
        }
        super.launch(loader)
    }
}
