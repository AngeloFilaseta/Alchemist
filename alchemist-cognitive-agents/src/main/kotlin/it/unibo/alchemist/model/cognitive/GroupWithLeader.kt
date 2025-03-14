/*
 * Copyright (C) 2010-2023, Danilo Pianini and contributors
 * listed, for each module, in the respective subproject's build.gradle.kts file.
 *
 * This file is part of Alchemist, and is distributed under the terms of the
 * GNU General Public License, with a linking exception,
 * as described in the file LICENSE in the Alchemist distribution's top directory.
 */

package it.unibo.alchemist.model.cognitive

import it.unibo.alchemist.model.Node

/**
 * A group with a special member acting as a leader.
 */
interface GroupWithLeader<T, N : Node<T>> : Group<T> {
    /**
     * The leader of the group.
     */
    val leader: N
}
