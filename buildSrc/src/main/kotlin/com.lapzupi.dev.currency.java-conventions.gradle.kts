import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("java")
    id("maven-publish")
    kotlin("jvm")
}

group = "com.lapzupi.dev.currency"

val compileKotlin: KotlinCompile by tasks
compileKotlin.compilerOptions {
    jvmTarget.set(JvmTarget.JVM_21)
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.compilerOptions {
    jvmTarget.set(JvmTarget.JVM_21)
}


java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
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