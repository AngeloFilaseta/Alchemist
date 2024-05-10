/*
 * Copyright (C) 2010-2024, Danilo Pianini and contributors
 * listed, for each module, in the respective subproject's build.gradle.kts file.
 *
 * This file is part of Alchemist, and is distributed under the terms of the
 * GNU General Public License, with a linking exception,
 * as described in the file LICENSE in the Alchemist distribution's top directory.
 */

package it.unibo.alchemist.component.props

import com.apollographql.apollo3.api.Query
import com.apollographql.apollo3.api.Subscription
import it.unibo.alchemist.component.props.state.GraphQLControllerProps

/**
 * Props for the Form component.
 */
external interface FormProps : GraphQLControllerProps {
    var setAggregationStrategy: (String) -> Unit
    var setQuery: (Query<*>?) -> Unit
    var setSubscription: (Subscription<*>?) -> Unit
}
