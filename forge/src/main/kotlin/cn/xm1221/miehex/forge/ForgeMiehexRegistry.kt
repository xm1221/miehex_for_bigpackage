package cn.xm1221.miehex.forge

import cn.xm1221.miehex.MiehexRegisterer
import cn.xm1221.miehex.MiehexRegistry
import at.petrak.hexcasting.common.lib.hex.HexActions
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraftforge.registries.RegisterEvent
import thedarkcolour.kotlinforforge.forge.MOD_BUS

/**
 * Forge 侧的注册入口。
 *
 * Forge 在 mod 构造阶段就已锁死注册表，必须等 `RegisterEvent` 才能写，所以这里把注册
 * 挂到 mod 事件总线上，而不是在 `ForgeMiehex` 的 init 里直接写。
 *
 * 两个要点：
 * 1. `RegisterEvent` **对每一个注册表各触发一次**。若在事件里反复调 `registerAll`，
 *    第二次就会撞上 `ActionRegisry` / `IotaRegistry` 的防重复注册守卫。所以每个注册表
 *    只挂**一个**监听，且该监听自己只触发一次 `registerAll`，再按注册表 id 分发。
 * 2. 用 `HexActions.REGISTRY.key()` 这种「直接从强类型注册表对象取 key」的方式寻址，
 *    不要把它塞进 `Map<.., ResourceKey<out Registry<*>>>` —— 星号投影会被捕获成
 *    无法跨函数传递的类型，`ResourceKey.create` 也就匹配不上。
 */
object ForgeMiehexRegistry {

    @JvmStatic
    fun init() {
        listenFor(BuiltInRegistries.ATTRIBUTE.key().location(), BuiltInRegistries.ATTRIBUTE)
        listenFor(HexIotaTypes.REGISTRY.key().location(), HexIotaTypes.REGISTRY)
        listenFor(HexActions.REGISTRY.key().location(), HexActions.REGISTRY)
    }

    /**
     * 为 [registry] 挂一个只处理它的 `RegisterEvent` 监听。
     *
     * 事件真正触发时才构造 [MiehexRegisterer] 并执行一次 `registerAll`。
     */
    private fun <T : Any> listenFor(registryId: ResourceLocation, registry: Registry<T>) {
        MOD_BUS.addListener { event: RegisterEvent ->
            if (event.registryKey.location() != registryId) return@addListener
            MiehexRegistry.registerAll(MiehexRegisterer { targetId, id, value ->
                if (targetId != registryId) return@MiehexRegisterer
                @Suppress("UNCHECKED_CAST")
                event.register(registry.key(), id) { value as T }
            })
        }
    }
}
