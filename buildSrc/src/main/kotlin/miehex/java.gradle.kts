// A convention plugin that should be applied to all build scripts.

package miehex

import libs

plugins {
    java
    kotlin("jvm")
    id("architectury-plugin")
}

val mavenGroup: String by project
val modVersion: String by project
val javaVersion = libs.versions.java.get().toInt()
val minecraftVersion = libs.versions.minecraft.get()
val release = System.getenv("RELEASE") == "true"

group = mavenGroup

version = "$modVersion+$minecraftVersion"
if (!release) {
    version = "$version-SNAPSHOT"
}

repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
    maven { url = uri("https://maven.blamejared.com") }
    maven { url = uri("https://maven.fabricmc.net/") }
    maven { url = uri("https://maven.hexxy.media") }
    maven { url = uri("https://maven.ladysnake.org/releases") } // Cardinal Components
    maven { url = uri("https://maven.minecraftforge.net/") }
    maven { url = uri("https://maven.parchmentmc.org") }
    maven { url = uri("https://maven.shedaniel.me") }
    maven { url = uri("https://maven.terraformersmc.com/releases") }
    maven { url = uri("https://maven.theillusivec4.top") } // Caelus
    maven { url = uri("https://thedarkcolour.github.io/KotlinForForge") }
    exclusiveContent {
        filter {
            includeGroup("maven.modrinth")
        }
        forRepository {
            maven { url = uri("https://api.modrinth.com/maven") }
        }
    }
    exclusiveContent {
        filter {
            includeGroup("libs")
        }
        forRepository {
            flatDir { dir(rootProject.file("libs")) }
        }
    }
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(javaVersion)
    withSourcesJar()
    withJavadocJar()
}

kotlin {
    jvmToolchain(javaVersion)
}

tasks {
    compileJava {
        options.apply {
            encoding = "UTF-8"
            release = javaVersion
        }
    }

    jar {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }

    withType<GenerateModuleMetadata>().configureEach {
        enabled = false
    }

    javadoc {
        options {
            this as StandardJavadocDocletOptions
            // 必须显式指定编码：javadoc 默认按平台编码读源码（本机 GBK），
            // 而源码是 UTF-8 且带中文注释/Unicode 字面量，会报"编码 GBK 的不可映射字符"。
            // compileJava 已经设了 encoding，javadoc 需要另设。
            encoding = "UTF-8"
            charSet = "UTF-8"
            addStringOption("Xdoclint:none", "-quiet")
        }
    }

    processResources {
        exclude(".cache")
    }

    processTestResources {
        exclude(".cache")
    }

    // Datagen 往 src/generated/resources 写文件，而 sourcesJar 会读该目录（数据产物留在源集里）。
    // 不声明依赖的话，Gradle 8 会在同一次调用里同时跑 build 与 runAllDatagen 时报
    // "uses this output of task ... without declaring an explicit or implicit dependency"。
    tasks.matching { it.name == "sourcesJar" }.configureEach {
        dependsOn(tasks.matching { it.name == "runDatagen" })
    }
}
