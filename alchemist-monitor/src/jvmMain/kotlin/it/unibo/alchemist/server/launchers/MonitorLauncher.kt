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

/**
 * A launcher that starts the monitor server.
 * @param host the host to bind the server to
 * @param port the port to bind the server to
 * @param batch the list of batch files to load
 * @param autoStart whether to start the simulation automatically
 * @param showProgress whether to show the progress bar
 * @param parallelism the number of threads to use
 */
open class MonitorLauncher @JvmOverloads constructor(
    private val host: String = "127.0.0.1",
    private val port: Int = DEFAULT_PORT,
    override val batch: List<String> = emptyList(),
    override val autoStart: Boolean = true,
    override val showProgress: Boolean = true,
    override val parallelism: Int = Runtime.getRuntime().availableProcessors(),
) : DefaultLauncher(batch, autoStart, showProgress, parallelism) {

    /**
     * Alternative constructor.
     * @param autoStart whether to start the simulation automatically
     * @param showProgress whether to show the progress bar
     * @param parallelism the number of threads to use
     */
    @JvmOverloads constructor(
        autoStart: Boolean,
        showProgress: Boolean = true,
        parallelism: Int = Runtime.getRuntime().availableProcessors(),
    ) : this("127.0.0.1", DEFAULT_PORT, emptyList(), autoStart, showProgress, parallelism)

    override fun launch(loader: Loader) {
        CoroutineScope(Dispatchers.IO).launch {
            logger.info("Starting monitor server on $host:$port")
            EngineMain.main(emptyArray())
        }
        super.launch(loader)
    }

    companion object {
        /**
         * The default port to bind the monitor to.
         */
        const val DEFAULT_PORT = 9090
    }
}
