/*
 * Copyright (C) 2010-2024, Danilo Pianini and contributors
 * listed, for each module, in the respective subproject's build.gradle.kts file.
 *
 * This file is part of Alchemist, and is distributed under the terms of the
 * GNU General Public License, with a linking exception,
 * as described in the file LICENSE in the Alchemist distribution's top directory.
 */

package it.unibo.alchemist.extractor

import it.unibo.alchemist.boundary.Extractor
import it.unibo.alchemist.boundary.graphql.client.AllQuery
import it.unibo.alchemist.boundary.graphql.client.LocalSuccessQuery
import it.unibo.alchemist.model.Actionable
import it.unibo.alchemist.model.Environment
import it.unibo.alchemist.model.Time
import okhttp3.OkHttpClient
import okhttp3.Request

class LimitedAndFullResponseExtractor(private val port: Int) : Extractor<Double> {

    override val columnNames: List<String>
        get() = listOf("simulation_time", "nodes", "limited_size", "full_size")

    override fun <T> extractData(
        environment: Environment<T, *>,
        reaction: Actionable<T>?,
        time: Time,
        step: Long,
    ): Map<String, Double> {
        environment.simulation.pause()
        val sizes = listOf(
            LocalSuccessQuery.OPERATION_DOCUMENT,
            AllQuery.OPERATION_DOCUMENT,
        ).map { query ->
            OkHttpClient()
                .newCall(
                    Request.Builder()
                        .url(getRequest(port, query))
                        .build(),
                )
                .execute()
                .use { response -> response.body?.bytes()?.count()?.toDouble() }
        }
        environment.simulation.play()
        return columnNames.zip(
            listOf(
                environment.simulation.time.toDouble(),
                environment.nodeCount.toDouble(),
                sizes[0] ?: error("Cannot get limited size"),
                sizes[1] ?: error("Cannot get full size"),
            ),
        ).toMap()
    }

    private fun getRequest(port: Int, query: String) = "http://localhost:$port/graphql?query=$query"
}
