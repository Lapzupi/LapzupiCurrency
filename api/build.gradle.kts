import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.lapzupi.dev.currency.java-conventions")
}

version = "1.2.0"

dependencies {
    compileOnly(kotlin("stdlib-jdk8"))
}
repositories {
    mavenCentral()
}
