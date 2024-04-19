/*
 * Copyright (C) 2010-2024, Danilo Pianini and contributors
 * listed, for each module, in the respective subproject's build.gradle.kts file.
 *
 * This file is part of Alchemist, and is distributed under the terms of the
 * GNU General Public License, with a linking exception,
 * as described in the file LICENSE in the Alchemist distribution's top directory.
 */

package it.unibo.alchemist.mapper.data

/**
 * A data mapper, from [D] to [O].
 * @param D the input type.
 * @param O the output type.
 */
interface DataMapper<in D, out O> {
    /**
     * The name of the output data.
     */
    val outputName: String

    /**
     * Maps the input data to the output data.
     * @param data the input data.
     * @return the output data.
     */
    operator fun invoke(data: D?): O
}
