/*
 * Copyright (C) 2010-2024, Danilo Pianini and contributors
 * listed, for each module, in the respective subproject's build.gradle.kts file.
 *
 * This file is part of Alchemist, and is distributed under the terms of the
 * GNU General Public License, with a linking exception,
 * as described in the file LICENSE in the Alchemist distribution's top directory.
 */

package it.unibo.alchemist.component

import it.unibo.alchemist.Full
import it.unibo.alchemist.InteractionType
import it.unibo.alchemist.Limited
import it.unibo.alchemist.Parameter
import it.unibo.alchemist.ResponseSize
import it.unibo.alchemist.boundary.graphql.client.AllQuery
import it.unibo.alchemist.boundary.graphql.client.AllSubscription
import it.unibo.alchemist.boundary.graphql.client.ConcentrationQuery
import it.unibo.alchemist.boundary.graphql.client.ConcentrationSubscription
import it.unibo.alchemist.component.props.FormProps
import it.unibo.alchemist.component.sub.FormElement
import it.unibo.alchemist.component.sub.MutationButtons
import it.unibo.alchemist.dataframe.aggregation.AggregationStrategy
import it.unibo.alchemist.monitor.GraphQLController
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import react.FC
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h2
import react.dom.html.ReactHTML.h4
import react.useEffect
import react.useState
import web.cssom.ClassName

/**
 * Form component.
 */
val Form = FC<FormProps>("Form") { props ->

    val interactiontypes by useState(listOf(InteractionType.Rest, InteractionType.GraphQL))
    val parameters by useState(listOf(Parameter.LocalSuccess))
    val responseSize by useState(listOf(Limited, Full))
    val aggregationStrategy by useState(
        listOf(AggregationStrategy.Sum, AggregationStrategy.Average, AggregationStrategy.Max, AggregationStrategy.Min),
    )

    var chosenInteractionType by useState<InteractionType?>(null)
    var chosenParameter by useState<Parameter?>(null)
    var chosenResponseSize by useState<ResponseSize?>(null)

    fun updateState(interactionType: InteractionType, parameter: Parameter, responseSize: ResponseSize) {
        props.setQuery(null)
        props.setSubscription(null)
        when (interactionType) {
            InteractionType.Rest -> props.setQuery(
                when (responseSize) {
                    is Full -> AllQuery()
                    is Limited -> ConcentrationQuery(parameter.toString())
                },
            )
            is InteractionType.GraphQL -> props.setSubscription(
                when (responseSize) {
                    is Full -> AllSubscription()
                    is Limited -> ConcentrationSubscription(parameter.toString())
                },
            )
        }
    }

    fun updateState() {
        chosenParameter?.let { parameter ->
            chosenInteractionType?.let { interactionType ->
                chosenResponseSize?.let { responseSize ->
                    updateState(interactionType, parameter, responseSize)
                }
            }
        }
    }

    useEffect(chosenInteractionType, chosenParameter, chosenResponseSize) {
        updateState()
    }

    fun onGraphQLController(action: suspend GraphQLController.() -> Unit) {
        MainScope().launch { action.invoke(props.graphQLController) }
    }

    div {
        className = ClassName("col-lg-6")
        h2 {
            +"Selection Form"
        }
        div {
            className = ClassName("row")
            // TYPE
            FormElement {
                title = "Interaction Type"
                elements = interactiontypes.map { it.toString() }
                valueChangeHandler = { chosenInteractionType = InteractionType.fromString(it) }
            }
            FormElement {
                title = "Parameter"
                elements = parameters.map { it.toString() }
                valueChangeHandler = { chosenParameter = Parameter.fromString(it) }
            }

            FormElement {
                title = "Response Size"
                elements = responseSize.map { it.toString() }
                valueChangeHandler = { chosenResponseSize = ResponseSize.fromString(it) }
            }
            FormElement {
                title = "Aggregation Strategy"
                elements = aggregationStrategy.map { it.toString() }
                valueChangeHandler = { props.setAggregationStrategy(it) }
            }
            h4 {
                +"Mutations"
            }
            MutationButtons {
                play = { onGraphQLController { play() } }
                pause = { onGraphQLController { pause() } }
            }
        }
    }
}
