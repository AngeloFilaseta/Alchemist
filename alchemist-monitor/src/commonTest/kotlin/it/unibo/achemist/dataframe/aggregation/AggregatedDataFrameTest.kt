/*
 * Copyright (C) 2010-2024, Danilo Pianini and contributors
 * listed, for each module, in the respective subproject's build.gradle.kts file.
 *
 * This file is part of Alchemist, and is distributed under the terms of the
 * GNU General Public License, with a linking exception,
 * as described in the file LICENSE in the Alchemist distribution's top directory.
 */

package it.unibo.achemist.dataframe.aggregation

import io.kotest.assertions.fail
import io.kotest.core.spec.style.WordSpec
import it.unibo.alchemist.dataframe.AggregatedDataFrame
import it.unibo.alchemist.dataframe.Col
import it.unibo.alchemist.dataframe.DataFrame
import it.unibo.alchemist.dataframe.aggregation.AggregationStrategy

class AggregatedDataFrameTest : WordSpec({
    "AggregatedDataFrame" should {
        "be able to aggregate data" {

            val df0 = DataFrame.fromCols(
                listOf(
                    Col("time", listOf(0, 2, 10)),
                    Col("a", listOf(1, 4, 5)),
                    Col("b", listOf(1, 6, 5)),
                ),
            )

            val df1 = DataFrame.fromCols(
                listOf(
                    Col("time", listOf(0, 5, 20)),
                    Col("a", listOf(1, 4, 5)),
                    Col("b", listOf(1, 6, 5)),
                ),
            )

            val sumDf = AggregatedDataFrame(listOf(df0, df1), AggregationStrategy.Sum)
            val avgDf = AggregatedDataFrame(listOf(df0, df1), AggregationStrategy.Average)

            listOf(sumDf, avgDf).forEach { df ->
                df.getColumn("time") ?: fail("Time column not found")
                df.getColumn("a") ?: fail("A column not found")
                df.getColumn("b") ?: fail("B column not found")
            }
        }
    }
})
