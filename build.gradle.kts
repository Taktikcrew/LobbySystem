import net.minecrell.pluginyml.paper.PaperPlugin
import net.minecrell.pluginyml.paper.PaperPluginDescription

plugins {
    id("java")
    alias(libs.plugins.shadow)
    alias(libs.plugins.plugindescription)
    alias(libs.plugins.paperweight)
}

group = "de.taktikcrew"
version = project.version

repositories {
    mavenCentral()
    mavenLocal()

    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    paperweight.paperDevBundle(libs.versions.paper)

    compileOnly(libs.paper)

    compileOnly(libs.core)

    compileOnly(libs.luckperms)

    annotationProcessor(libs.lombok)
    compileOnly(libs.lombok)
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    sourceCompatibility = JavaVersion.VERSION_23.toString()
    targetCompatibility = JavaVersion.VERSION_23.toString()
}

tasks.shadowJar {
    archiveFileName.set("LobbySystem-${project.version}.jar")
    manifest {
        attributes["paperweight-mappings-namespace"] = "spigot"
    }
}

tasks.assemble {
    dependsOn(tasks.reobfJar)
}

paper {
    name = "LobbySystem"
    version = project.version.toString()
    authors = listOf("Smoofy", "Taktikcrew")
    apiVersion = "1.21"
    main = "de.taktikcrew.lobbysystem.Lobby"
    serverDependencies {
        register("Core") {
            load = PaperPluginDescription.RelativeLoadOrder.BEFORE
        }
    }
}