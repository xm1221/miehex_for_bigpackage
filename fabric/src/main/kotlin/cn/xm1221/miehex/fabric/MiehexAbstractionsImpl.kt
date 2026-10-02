@file:JvmName("MiehexAbstractionsImpl")

package cn.xm1221.miehex.fabric

import cn.xm1221.miehex.registry.MiehexRegistrar
import net.minecraft.core.Registry

fun <T : Any> initRegistry(registrar: MiehexRegistrar<T>) {
    val registry = registrar.registry
    registrar.init { id, value -> Registry.register(registry, id, value) }
}
