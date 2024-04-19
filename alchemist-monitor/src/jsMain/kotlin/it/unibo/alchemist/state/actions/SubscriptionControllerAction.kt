/*
 * Copyright (C) 2010-2024, Danilo Pianini and contributors
 * listed, for each module, in the respective subproject's build.gradle.kts file.
 *
 * This file is part of Alchemist, and is distributed under the terms of the
 * GNU General Public License, with a linking exception,
 * as described in the file LICENSE in the Alchemist distribution's top directory.
 */

package it.unibo.alchemist.state.actions

/**
 * Action supported by the subscription manager reducer.
 */
sealed interface SubscriptionControllerAction

/**
 * Redux action to add a subscription client.
 * @param host the host of the client.
 * @param port the port of the client.
 */
data class AddSubscripionClient(val host: String, val port: Int) : SubscriptionControllerAction

/**
 * Redux action to remove a subscription client. It also closes the client.
 * @param host the host of the client.
 * @param port the port of the client.
 */
data class RemoveSubscriptionClient(val host: String, val port: Int) : SubscriptionControllerAction
