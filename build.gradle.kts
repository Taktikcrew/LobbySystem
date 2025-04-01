import net.minecrell.pluginyml.paper.PaperPluginDescription

plugins {
    id("java")
    id("java-library")
    id("xyz.jpenilla.run-paper") version "2.3.1"

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
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots")

    maven {
        url = uri("https://maven.pkg.github.com/smoofy19/SmoofyCore")
        credentials {
            username = project.findProperty("gpr.user").toString()
            password = project.findProperty("gpr.token").toString()
        }
    }
}

dependencies {
    paperweight.paperDevBundle(libs.versions.paper)

    compileOnly(libs.paper)

    compileOnly(libs.core)

    compileOnly(libs.luckperms)

    implementation(libs.sadu.mariadb)
    implementation(libs.sadu.data.source)
    implementation(libs.sadu.queries)
    implementation(libs.sadu.mappings)
    implementation(libs.sadu.updater)

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

tasks.runServer {
    // Configure the Minecraft version for our task.
    // This is the only required configuration besides applying the plugin.
    // Your plugin's jar (or shadowJar if present) will be used automatically.
    minecraftVersion("1.21.4")
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