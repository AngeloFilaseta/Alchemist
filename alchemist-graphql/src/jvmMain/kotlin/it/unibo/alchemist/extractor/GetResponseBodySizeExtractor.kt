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
import it.unibo.alchemist.model.Actionable
import it.unibo.alchemist.model.Environment
import it.unibo.alchemist.model.Time
import okhttp3.OkHttpClient
import okhttp3.Request

class GetResponseBodySizeExtractor(private val getRequest: String) : Extractor<Int> {

    override val columnNames: List<String>
        get() = listOf(RESPONSE_BODY_SIZE)

    override fun <T> extractData(
        environment: Environment<T, *>,
        reaction: Actionable<T>?,
        time: Time,
        step: Long,
    ): Map<String, Int> {
        return OkHttpClient().newCall(Request.Builder().url(getRequest).build()).execute().use { response ->
            response.body?.let {
                mapOf(RESPONSE_BODY_SIZE to it.bytes().count())
            } ?: emptyMap()
        }
    }

    companion object {
        private const val RESPONSE_BODY_SIZE: String = "response_body_size"
    }
}
