import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("java")
    id("maven-publish")
    kotlin("jvm") version "1.8.10"
    id("net.minecrell.plugin-yml.bukkit") version "0.5.3"
    id("com.github.johnrengelman.shadow") version "8.1.0"
}

group = "com.lapzupi.dev"
version = "1.0.0"

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
    compileOnly("io.papermc.paper:paper-api:1.19.3-R0.1-SNAPSHOT")
    compileOnly("me.clip:placeholderapi:2.11.2")
    
    implementation("com.github.Lapzupi:LapzupiConfig:1.0.2")
    //libraries
    library(kotlin("stdlib-jdk8"))
    library("com.zaxxer:HikariCP:5.0.1")
    library("com.devskiller.friendly-id:friendly-id:1.1.0")
    library("org.spongepowered:configurate-yaml:4.1.2")
    library("org.flywaydb:flyway-core:9.15.1")
    library("org.flywaydb:flyway-mysql:9.15.1")
    
    library("com.github.ben-manes.caffeine:caffeine:3.1.5")
    
    //shaded
    implementation("co.aikar:acf-paper:0.5.1-SNAPSHOT")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

bukkit {
    name = "LapzupiCurrency"
    main = "com.lapzupi.dev.currency.LapzupiCurrency"
    apiVersion = "1.19"
    version = project.version.toString()
    depend = listOf("PlaceholderAPI")
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

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "17"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "17"
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = groupId
            artifactId = artifactId
            version = version
            
            from(components["java"])
        }
    }
}