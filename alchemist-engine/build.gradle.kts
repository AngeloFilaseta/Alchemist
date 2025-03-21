/*
 * Copyright (C) 2010-2022, Danilo Pianini and contributors
 * listed, for each module, in the respective subproject's build.gradle.kts file.
 *
 * This file is part of Alchemist, and is distributed under the terms of the
 * GNU General Public License, with a linking exception,
 * as described in the file LICENSE in the Alchemist distribution's top directory.
 */
import Libs.alchemist
import Libs.incarnation

plugins {
    id("kotlin-jvm-convention")
}

dependencies {
    api(alchemist("api"))

    implementation(alchemist("maintenance-tooling"))
    implementation(libs.jgrapht.core)
    implementation(libs.guava)
    implementation(libs.trove4j)
    implementation(libs.kotlin.coroutines.core)

    testImplementation(alchemist("euclidean-geometry"))
    testImplementation(alchemist("implementationbase"))
    testImplementation(alchemist("loading"))
    testImplementation(incarnation("biochemistry"))
}

publishing.publications {
    withType<MavenPublication> {
        pom {
            contributors {
                contributor {
                    name.set("Martina Baiardi")
                    email.set("m.baiardi@unibo.it")
                }
                contributor {
                    name.set("Andrea Placuzzi")
                    email.set("andrea.placuzzi@studio.unibo.it")
                }
                contributor {
                    name.set("Franco Pradelli")
                    email.set("franco.pradelli@studio.unibo.it")
                }
                contributor {
                    name.set("Giacomo Scaparrotti")
                    email.set("giacomo.scaparrotti@studio.unibo.it")
                    url.set("https://www.linkedin.com/in/giacomo-scaparrotti-0aa77569")
                }
            }
        }
    }
}
