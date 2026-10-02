// A convention plugin that should be applied to all Minecraft-related subprojects, including common.

@file:Suppress("UnstableApiUsage")

package miehex

import kotlin.io.path.div
import libs

plugins {
    id("miehex.java")

    `maven-publish`
    id("dev.architectury.loom")
    id("at.petra-k.pkpcpbp.PKJson5Plugin")
}

val modId: String by project
val platform: String by project

base.archivesName = "${modId}-$platform"

loom {
    silentMojangMappingsLicense()
    accessWidenerPath = project(":common").file("src/main/resources/miehex.accesswidener")

    mixin {
        // the default name includes both archivesName and the subproject, resulting in the platform showing up twice
        // default: miehex-common-common-refmap.json
        // fixed:   miehex-common.refmap.json
        defaultRefmapName = "${base.archivesName.get()}.refmap.json"

        // 必须走 legacy Mixin AP：新版 AP 拿不到 loom 的混淆映射参数，Forge 侧不会生成 SRG refmap，
        // 结果是 mixin 里的类/方法名被留成 Fabric intermediary 名（如 net/minecraft/class_1309），
        // Forge 运行时直接 InvalidInjectionException。
        useLegacyMixinAp = true
    }
}

pkJson5 {
    autoProcessJson5 = true
    autoProcessJson5Flattening = true
}

dependencies {
    minecraft(libs.minecraft)

    mappings(loom.layered {
        officialMojangMappings()
        parchment(libs.parchment)
    })

    annotationProcessor(libs.bundles.asm)
}

sourceSets {
    main {
        kotlin {
            srcDir(file("src/main/java"))
        }
        resources {
            srcDir(file("src/generated/resources"))
        }
    }
}
