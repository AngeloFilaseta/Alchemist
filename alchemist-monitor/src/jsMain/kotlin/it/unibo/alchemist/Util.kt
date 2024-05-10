/*
 * Copyright (C) 2010-2024, Danilo Pianini and contributors
 * listed, for each module, in the respective subproject's build.gradle.kts file.
 *
 * This file is part of Alchemist, and is distributed under the terms of the
 * GNU General Public License, with a linking exception,
 * as described in the file LICENSE in the Alchemist distribution's top directory.
 */

package it.unibo.alchemist

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import react.EffectBuilder
import kotlin.coroutines.CoroutineContext

/**
 * Launch a coroutine with a cleanup block that is launched when the effect is disposed.
 * @param block the block to execute
 */
fun EffectBuilder.launchWithCleanup(context: CoroutineContext, block: suspend () -> Unit) {
    var ignore = false
    val job = CoroutineScope(context).launch {
        if (!ignore) {
            block()
        }
    }
    cleanup {
        job.cancel()
        ignore = true
    }
}
