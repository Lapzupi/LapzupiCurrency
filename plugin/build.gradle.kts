import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.lapzupi.dev.currency.java-conventions")
    alias(libs.plugins.bukkit.yml)
    alias(libs.plugins.shadow)
}

version = "1.3.2.2"

dependencies {
    compileOnly(libs.paper.api)
    compileOnly(libs.placeholder.api)
    
    implementation(project(":api"))
    implementation(libs.bundles.lapzupi.core)
    //libraries
    library(libs.kotlin.stdlib)
    library(libs.hikari.cp)
    library(libs.friendly.id)
//    library("org.spongepowered:configurate-yaml:4.1.2")
    library(libs.bundles.flyway)
    
    library(libs.caffeine)
    
    //shaded
    implementation(libs.acf.commands)
}

bukkit {
    name = "LapzupiCurrency"
    main = "com.lapzupi.dev.currency.LapzupiCurrency"
    apiVersion = "1.21"
    version = project.version.toString()
    depend = listOf("PlaceholderAPI")
    authors = listOf("Lapzupi Development Team", "sarhatabaot")
}

tasks.compileKotlin {
    compilerOptions.javaParameters = true
}

tasks {
    build {
        dependsOn(shadowJar)
    }
    
    shadowJar {
        minimize()
        archiveFileName.set("LapzupiCurrency-${project.version}.jar")
        archiveClassifier.set("shadow")
    
        relocate ("co.aikar.commands", "com.lapzupi.dev.acf")
        relocate ("co.aikar.locales", "com.lapzupi.dev.locales")
    }
}