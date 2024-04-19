/*
 * Copyright (C) 2010-2024, Danilo Pianini and contributors
 * listed, for each module, in the respective subproject's build.gradle.kts file.
 *
 * This file is part of Alchemist, and is distributed under the terms of the
 * GNU General Public License, with a linking exception,
 * as described in the file LICENSE in the Alchemist distribution's top directory.
 */

package it.unibo.alchemist.mapper.response

import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Subscription
import it.unibo.alchemist.mapper.data.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Maps the response of a subscription to a list of new data.
 */
class SubscriptionResponseMapper<D : Subscription.Data>(private val flow: Flow<ApolloResponse<D>>) {
    /**
     * Maps the response using the given mappers.
     * @param mappers the list of mappers to be used.
     * @return a map where keys are the names of the new data extracted by the mappers,
     * and values are the corresponding flows
     */
    fun mapUsing(mappers: List<DataMapper<D, Any?>>): Map<String, Flow<Any?>> {
        return mappers.associate { mapper ->
            mapper.outputName to flow.map { response ->
                mapper.invoke(response.data)
            }
        }
    }
}
