rootProject.name = "currency"

include("api")
include("plugin")

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        maven("https://repo.papermc.io/repository/maven-public/")
        maven("https://oss.sonatype.org/content/groups/public/")
        maven("https://repo.aikar.co/content/groups/aikar/")
        maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
        maven("https://jitpack.io")
    }

    versionCatalogs {
        create("libs") {
            library("paper-api", "io.papermc.paper:paper-api:1.21.1-R0.1-SNAPSHOT")
            library("placeholder-api", "me.clip:placeholderapi:2.11.6")

            bundle("lapzupi-core", listOf("lapzupi-config", "lapzupi-connection", "lapzupi-files"))
            library("lapzupi-config", "com.github.Lapzupi:LapzupiConfig:1.2.1")
            library("lapzupi-connection", "com.github.Lapzupi:LapzupiConnection:1.1.1")
            library("lapzupi-files", "com.github.Lapzupi:LapzupiConfig:1.1.0")

            library("friendly-id","com.devskiller.friendly-id:friendly-id:1.1.0")
            library("caffeine","com.github.ben-manes.caffeine:caffeine:3.1.8")

            bundle("flyway", listOf("flyway-core", "flyway-mysql"))
            version("flyway", "11.0.0")
            library("flyway-core","org.flywaydb","flyway-core").versionRef("flyway")
            library("flyway-mysql", "org.flywaydb","flyway-mysql").versionRef("flyway")
            library("hikari-cp", "com.zaxxer:HikariCP:6.2.1")

            library("kotlin-stdlib", "org.jetbrains.kotlin:kotlin-stdlib:2.1.0")

            library("acf-commands", "co.aikar:acf-paper:0.5.1-SNAPSHOT")

            plugin("shadow", "com.gradleup.shadow").version("8.3.5")
            plugin("bukkit-yml", "net.minecrell.plugin-yml.bukkit").version("0.6.0")
        }
    }
}