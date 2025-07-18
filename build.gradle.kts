plugins {
    id("java-library")
    id("eclipse")
    id("idea")
    id("maven-publish")
    // https://projects.neoforged.net/neoforged/neogradle
    id("net.neoforged.gradle.userdev") version("7.0.190")
}

// gradle.properties
val loaderVersionRange: String by extra
val minecraftVersion: String by extra
val minecraftVersionRange: String by extra
val modAuthors: String by extra
val modDescription: String by extra
val modGroupId: String by extra
val modId: String by extra
val modJavaVersion: String by extra
val modLicense: String by extra
val modName: String by extra
val modVersion: String by extra
val neoVersion: String by extra
val neoVersionRange: String by extra
val parchmentMappingsMinecraftVersion: String by extra
val parchmentMappingsVersion: String by extra

version = modVersion
group = modGroupId

repositories {
    mavenLocal()
}

val baseArchivesName = "${modId}-${minecraftVersion}-neoforge"
base {
    archivesName.set(baseArchivesName)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(modJavaVersion))
    }
}

// Default run configurations.
// These can be tweaked, removed, or duplicated as needed.
runs {
    // applies to all the run configs below
    configureEach {
        // Recommended logging data for a userdev environment
        // The markers can be added/remove as needed separated by commas.
        // "SCAN": For mods scan.
        // "REGISTRIES": For firing of registry events.
        // "REGISTRYDUMP": For getting the contents of all registries.
        systemProperty("forge.logging.markers", "REGISTRIES")

        // Recommended logging level for the console
        // You can set various levels here.
        // Please read: https://stackoverflow.com/questions/2031163/when-to-use-the-different-log-levels
        systemProperty("forge.logging.console.level", "debug")

        modSources.add(project.name, sourceSets.main.get())
    }
}

sourceSets {
    main {
        resources {
            // Include resources generated by data generators.
            srcDir("src/generated/resources")
        }
    }
}

dependencies {
    implementation("net.neoforged:neoforge:${neoVersion}")
}

tasks.withType<ProcessResources>().configureEach {
    filesMatching(listOf("META-INF/neoforge.mods.toml")) {
        expand(mapOf(
            "neoVersion" to neoVersion,
            "neoVersionRange" to neoVersionRange,
            "loaderVersionRange" to loaderVersionRange,
            "minecraftVersion" to minecraftVersion,
            "minecraftVersionRange" to minecraftVersionRange,
            "modAuthors" to modAuthors,
            "modDescription" to modDescription,
            "modId" to modId,
            "modName" to modName,
            "modLicense" to modLicense,
            "modVersion" to modVersion,
        ))
    }
}

// Example configuration to allow publishing using the maven-publish plugin
publishing {
    publications {
        register<MavenPublication>("mavenJava") {
            artifact(tasks.jar.get())
        }
    }
    repositories {
        maven {
            setUrl("file://${project.projectDir}/repo")
        }
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    javaToolchains {
        compilerFor {
            languageVersion.set(JavaLanguageVersion.of(modJavaVersion))
        }
    }
}

// IDEA no longer automatically downloads sources/javadoc jars for dependencies, so we need to explicitly enable the behavior.
idea {
    module {
        isDownloadSources = true
        isDownloadJavadoc = true
    }
}

subsystems {
    parchment {
        // The Minecraft version for which the Parchment mappings were created.
        // This does not necessarily need to match the Minecraft version your mod targets
        // Defaults to the value of Gradle property neogradle.subsystems.parchment.minecraftVersion
        minecraftVersion = parchmentMappingsMinecraftVersion

        // The version of Parchment mappings to apply.
        // See https://parchmentmc.org/docs/getting-started for a list.
        // Defaults to the value of Gradle property neogradle.subsystems.parchment.mappingsVersion
        mappingsVersion = parchmentMappingsVersion

        // Overrides the full Maven coordinate of the Parchment artifact to use
        // This is computed from the minecraftVersion and mappingsVersion properties by default.
        // If you set this property explicitly, minecraftVersion and mappingsVersion will be ignored.
        // The built-in default value can also be overriden using the Gradle property neogradle.subsystems.parchment.parchmentArtifact
        // parchmentArtifact = "org.parchmentmc.data:parchment-$minecraftVersion:$mappingsVersion:checked@zip"

        // Set this to false if you don't want the https://maven.parchmentmc.org/ repository to be added automatically when
        // applying Parchment mappings is enabled
        // The built-in default value can also be overriden using the Gradle property neogradle.subsystems.parchment.addRepository
        // addRepository = true

        // Can be used to explicitly disable this subsystem. By default, it will be enabled automatically as soon
        // as parchmentArtifact or minecraftVersion and mappingsVersion are set.
        // The built-in default value can also be overriden using the Gradle property neogradle.subsystems.parchment.enabled
        // enabled = true
    }
}