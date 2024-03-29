import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.lapzupi.dev.currency.java-conventions")
    id("net.minecrell.plugin-yml.bukkit") version "0.6.0"
    id("com.github.johnrengelman.shadow") version "8.1.0"
}

version = "1.3.2.0"


repositories {
    mavenCentral()
    maven(
        url = "https://repo.papermc.io/repository/maven-public/"
    )
    maven(
        url = "https://oss.sonatype.org/content/groups/public/"
    )
    maven(
        url = "https://repo.aikar.co/content/groups/aikar/"
    )
    maven(
        url = "https://repo.extendedclip.com/content/repositories/placeholderapi/"
    )
    maven (
        url = "https://jitpack.io"
    )
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20.2-R0.1-SNAPSHOT")
    compileOnly("me.clip:placeholderapi:2.11.5")
    
    implementation(project(":api"))
    implementation("com.github.Lapzupi:LapzupiConfig:1.1.2")
    implementation("com.github.Lapzupi:LapzupiConnection:1.0.3")
    //libraries
    library("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.8.10")
    library("com.zaxxer:HikariCP:5.1.0")
    library("com.devskiller.friendly-id:friendly-id:1.1.0")
    library("org.spongepowered:configurate-yaml:4.1.2")
    library("org.flywaydb:flyway-core:10.0.1")
    library("org.flywaydb:flyway-mysql:10.0.1")
    
    library("com.github.ben-manes.caffeine:caffeine:3.1.8")
    
    //shaded
    implementation("co.aikar:acf-paper:0.5.1-SNAPSHOT")
}

bukkit {
    name = "LapzupiCurrency"
    main = "com.lapzupi.dev.currency.LapzupiCurrency"
    apiVersion = "1.20"
    version = project.version.toString()
    depend = listOf("PlaceholderAPI")
    authors = listOf("Lapzupi Development Team", "sarhatabaot")
}

tasks.compileKotlin {
    kotlinOptions.javaParameters = true
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