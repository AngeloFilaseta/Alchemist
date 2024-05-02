/*
 * Copyright (C) 2010-2022, Danilo Pianini and contributors
 * listed, for each module, in the respective subproject's build.gradle.kts file.
 *
 * This file is part of Alchemist, and is distributed under the terms of the
 * GNU General Public License, with a linking exception,
 * as described in the file LICENSE in the Alchemist distribution's top directory.
 */

import Libs.alchemist

plugins {
    alias(libs.plugins.kotest.multiplatform)
}

kotlin {
    jvm {
        withJava()
    }
    js(IR) {
        browser()
        binaries.executable()
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                compileOnly(libs.spotbugs.annotations)
                implementation(alchemist("graphql"))
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.0-RC.2")
                implementation(libs.apollo.runtime)
                implementation(libs.kotlin.stdlib)
                implementation(libs.ktor.client.core)
                implementation(libs.redux.kotlin.threadsafe)
                implementation(libs.lets.plot)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(libs.kotest.assertions.core)
                implementation(libs.kotest.framework.engine)
                implementation(libs.kotest.framework.datatest)
                implementation(libs.kotlin.test.common)
                implementation(libs.kotlin.test.annotations)
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(rootProject)
                implementation(alchemist("graphql-surrogates"))
                implementation(libs.bundles.ktor.server)
                implementation(libs.logback)
                implementation(libs.resourceloader)
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(libs.kotest.assertions.core)
                implementation(libs.kotest.runner)
                implementation(libs.ktor.server.test.host)
            }
        }
        val jsMain by getting {
            dependencies {
                implementation(libs.bundles.ktor.client)
                implementation(libs.bundles.kotlin.react)
            }
        }
    }

    targets.all {
        compilations.configureEach {
            // Workaround for w: duplicate library name: org.jetbrains.kotlin:kotlinx-atomicfu-runtime
            if (defaultSourceSet.name != "jsTest") {
                kotlinOptions {
                    allWarningsAsErrors = true
                }
            }
        }
    }
}

tasks.build {
    dependsOn(webpackTask)
    dependsOn(copyWebpackOutput)
}

/**
 * Webpack task that generates the JS artifacts.
 */
val webpackTask = tasks.named("jsBrowserDevelopmentWebpack")

webpackTask.configure {
    finalizedBy(copyWebpackOutput)
}

val copyWebpackOutput by tasks.registering(Copy::class) {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    dependsOn(tasks.named("jvmProcessResources"))
    dependsOn(webpackTask)
    from(
        webpackTask.map { out ->
            out.outputs.files
        },
    )
    into("src/commonMain/resources")
    shouldRunAfter(webpackTask)
}

publishing.publications {
    withType<MavenPublication> {
        pom {
            contributors {
                contributor {
                    name.set("Angelo Filaseta")
                    email.set("angelo.filaseta@studio.unibo.it")
                }
            }
        }
    }
}
