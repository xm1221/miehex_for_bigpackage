@file:JvmName("MiehexAbstractions")

package cn.xm1221.miehex

import dev.architectury.injectables.annotations.ExpectPlatform
import cn.xm1221.miehex.registry.MiehexRegistrar

fun initRegistries(vararg registries: MiehexRegistrar<*>) {
    for (registry in registries) {
        initRegistry(registry)
    }
}

@ExpectPlatform
fun <T : Any> initRegistry(registrar: MiehexRegistrar<T>) {
    throw AssertionError()
}
