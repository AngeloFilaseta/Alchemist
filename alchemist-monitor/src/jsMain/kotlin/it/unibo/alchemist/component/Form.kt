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
import it.unibo.alchemist.component.sub.MutationButtons
import it.unibo.alchemist.dataframe.aggregation.AggregationStrategy
import it.unibo.alchemist.mapper.data.AggregateConcentrationMapper
import it.unibo.alchemist.mapper.data.LocalSuccessConcentrationMapper
import it.unibo.alchemist.mapper.data.TimeMapper
import it.unibo.alchemist.state.actions.AddMapper
import it.unibo.alchemist.state.actions.ClearMappers
import it.unibo.alchemist.state.actions.ClearQuery
import it.unibo.alchemist.state.actions.ClearSubscription
import it.unibo.alchemist.state.actions.SetQuery
import it.unibo.alchemist.state.actions.SetSubscription
import it.unibo.alchemist.state.store
import react.FC
import react.Props
import react.dom.html.ReactHTML
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h2
import react.dom.html.ReactHTML.h4
import react.useEffect
import react.useState
import web.cssom.ClassName

val Form = FC<Props>("Form") {

    val interactiontypes by useState(listOf(InteractionType.Rest, InteractionType.GraphQL))
    val parameters by useState(listOf(Parameter.LocalSuccess))
    val responseSize by useState(listOf(Limited, Full))

    var chosenInteractionType by useState<InteractionType?>(null)
    var chosenParameter by useState<Parameter?>(null)
    var chosenResponseSize by useState<ResponseSize?>(null)

    fun updateState(interactionType: InteractionType, parameter: Parameter, responseSize: ResponseSize) {
        listOf(ClearQuery, ClearSubscription, ClearMappers).forEach { store.dispatch(it) }
        console.log("A")
        val action: Any = when (interactionType) {
            InteractionType.Rest -> when (responseSize) {
                is Full -> {
                    console.log("b")
                    SetQuery(AllQuery())
                }
                is Limited -> {
                    console.log("c")
                    SetQuery(ConcentrationQuery(parameter.toString()))
                }
            }
            is InteractionType.GraphQL -> when (responseSize) {
                is Full -> {
                    console.log("d")
                    SetSubscription(AllSubscription())
                }
                is Limited -> {
                    console.log("e")
                    SetSubscription(ConcentrationSubscription(parameter.toString()))
                }
            }
        }
        listOf(
            action,
            AddMapper(
                TimeMapper(),
                AggregateConcentrationMapper(LocalSuccessConcentrationMapper, AggregationStrategy.Max),
            ),
        ).forEach { store.dispatch(it) }
    }

    fun updateState() {
        console.log("updating state")
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

    div {
        className = ClassName("col-lg-6")
        h2 {
            +"Selection Form"
        }
        div {
            className = ClassName("row")
            // TYPE
            h4 {
                +"Interaction Type"
            }
            ReactHTML.select {
                onChange = { event ->
                    event.target.value.let {
                        chosenInteractionType = InteractionType.fromString(it)
                    }
                }
                className = ClassName("form-select")
                ReactHTML.option {
                    value = null
                    selected = true
                    +"Select the interaction type:"
                }
                interactiontypes.forEach { p ->
                    ReactHTML.option {
                        +p.toString()
                        value = p
                    }
                }
            }

            // PARAMETER
            h4 {
                +"Parameters"
            }
            ReactHTML.select {
                onChange = { event ->
                    event.target.value.let {
                        chosenParameter = Parameter.fromString(it)
                    }
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
                            chosenResponseSize = ResponseSize.fromString(it)
                        }
                    }
                    className = ClassName("form-select")
                    ReactHTML.option {
                        value = null
                        selected = true
                        +"Select a subscription size:"
                    }
                    responseSize.forEach { s ->
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
