/*
 * Copyright (C) 2010-2024, Danilo Pianini and contributors
 * listed, for each module, in the respective subproject's build.gradle.kts file.
 *
 * This file is part of Alchemist, and is distributed under the terms of the
 * GNU General Public License, with a linking exception,
 * as described in the file LICENSE in the Alchemist distribution's top directory.
 */

@file:Suppress("UNCHECKED_CAST")

package it.unibo.alchemist.component

import com.apollographql.apollo3.api.Subscription
import it.unibo.alchemist.Full
import it.unibo.alchemist.Limited
import it.unibo.alchemist.Parameter
import it.unibo.alchemist.SubscriptionSize
import it.unibo.alchemist.boundary.graphql.client.ConcentrationSubscription
import it.unibo.alchemist.boundary.graphql.client.NodesSubscription
import it.unibo.alchemist.component.sub.MutationButtons
import it.unibo.alchemist.dataframe.aggregation.AggregationStrategy
import it.unibo.alchemist.mapper.data.AggregateConcentration
import it.unibo.alchemist.mapper.data.LocalSuccessConcentrationMapper
import it.unibo.alchemist.mapper.data.TimeMapper
import it.unibo.alchemist.state.actions.AddMapper
import it.unibo.alchemist.state.actions.ClearMappers
import it.unibo.alchemist.state.actions.SetSubscription
import it.unibo.alchemist.state.store
import react.FC
import react.Props
import react.dom.html.ReactHTML
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h2
import react.dom.html.ReactHTML.h4
import react.useState
import web.cssom.ClassName

val Form = FC<Props>("Form") {

    val parameters by useState(listOf(Parameter.LocalSuccess))
    val subSizes by useState(listOf(Limited, Full))

    var chosenParameter by useState<Parameter?>(null)
    var chosenSubscriptionSize by useState<SubscriptionSize?>(null)

    fun updateState() {
        when (chosenParameter) {
            null -> {}
            else -> {
                store.dispatch(ClearMappers)
                when (chosenSubscriptionSize) {
                    Limited -> if (chosenParameter != null) {
                        store.dispatch(
                            SetSubscription(ConcentrationSubscription(chosenParameter.toString()) as Subscription<Subscription.Data>),
                        )
                        store.dispatch(
                            AddMapper(
                                TimeMapper(),
                                AggregateConcentration(LocalSuccessConcentrationMapper, AggregationStrategy.Max),
                            ),
                        )
                    }
                    Full -> {
                        store.dispatch(SetSubscription(NodesSubscription() as Subscription<Subscription.Data>))
                        store.dispatch(
                            AddMapper(
                                TimeMapper(),
                                AggregateConcentration(LocalSuccessConcentrationMapper, AggregationStrategy.Max),
                            ),
                        )
                    }
                    null -> {}
                }
            }
        }
    }

    div {
        className = ClassName("col-lg-6")
        h2 {
            +"Selection Form"
        }
        div {
            className = ClassName("row")
            // PARAMETER
            h4 {
                +"Parameters"
            }
            ReactHTML.select {
                onChange = { event ->
                    event.target.value.let {
                        chosenParameter = Parameter.fromString(it)
                    }
                    updateState()
                }
                className = ClassName("form-select")
                ReactHTML.option {
                    value = null
                    selected = true
                    +"Select the parameter(s) to observe:"
                }
                parameters.forEach { p ->
                    ReactHTML.option {
                        +p.toString()
                        value = p
                    }
                }
            }

            // SUBSCRIPTION SIZE
            h4 {
                +"Subscription size"
            }
            div {
                className = ClassName("col-sm-10")
                ReactHTML.select {
                    onChange = { event ->
                        event.target.value.let {
                            chosenSubscriptionSize = SubscriptionSize.fromString(it)
                        }
                        updateState()
                    }
                    className = ClassName("form-select")
                    ReactHTML.option {
                        value = null
                        selected = true
                        +"Select a subscription size:"
                    }
                    subSizes.forEach { s ->
                        ReactHTML.option {
                            +s.toString()
                            value = s
                        }
                    }
                }
            }
            h4 {
                +"Mutations"
            }
            MutationButtons()
        }
    }
}
