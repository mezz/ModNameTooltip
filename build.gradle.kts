plugins {
    id("java-library")
    id("eclipse")
    id("idea")
    id("maven-publish")
    // https://projects.neoforged.net/neoforged/neogradle
    id("net.neoforged.gradle.userdev") version("7.0.142")
}

// gradle.properties
val mod_version: String by extra
val mod_group_id: String by extra
val minecraft_version: String by extra
val mod_id: String by extra
val neo_version: String by extra
val neo_version_range: String by extra
val loader_version_range: String by extra
val minecraft_version_range: String by extra
val mod_authors: String by extra
val mod_description: String by extra
val mod_name: String by extra
val mod_license: String by extra
val mod_java_version: String by extra

version = mod_version
group = mod_group_id

repositories {
    mavenLocal()
}

val baseArchivesName = "${mod_id}-${minecraft_version}-neoforge"
base {
    archivesName.set(baseArchivesName)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(mod_java_version))
    }
}

minecraft {
    // Copies the files from 'processResources' to the IDE's resource output directories
    // TODO
//    copyIdeResources = true
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
    implementation("net.neoforged:neoforge:${neo_version}")
}

tasks.withType<ProcessResources>().configureEach {
    filesMatching(listOf("META-INF/neoforge.mods.toml")) {
        expand(mapOf(
            "neo_version" to neo_version,
            "neo_version_range" to neo_version_range,
            "loader_version_range" to loader_version_range,
            "minecraft_version" to minecraft_version,
            "minecraft_version_range" to minecraft_version_range,
            "mod_authors" to mod_authors,
            "mod_description" to mod_description,
            "mod_id" to mod_id,
            "mod_name" to mod_name,
            "mod_license" to mod_license,
            "mod_version" to mod_version,
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
            languageVersion.set(JavaLanguageVersion.of(mod_java_version))
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
