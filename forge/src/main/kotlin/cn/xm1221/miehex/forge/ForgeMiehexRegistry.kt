package cn.xm1221.miehex.forge

import cn.xm1221.miehex.MiehexRegisterer
import cn.xm1221.miehex.MiehexRegistry
import at.petrak.hexcasting.common.lib.HexRegistries
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
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
 *
 * 1. `MiehexRegistry.registerAll` **全程只执行一次**（`IotaRegistry` / `ActionRegisry`
 *    内部有防重复注册守卫）。而 `RegisterEvent` 对每个注册表都会触发一次，所以做法是：
 *    把 `registerAll` 想写的所有条目先收集进 [entries]（按注册表 id 分组），
 *    再给每个需要写入的注册表挂一个监听，事件触发时只写属于它的那一组。
 *    收集过程只构造对象、不碰注册表，所以在构造阶段执行是安全的。
 *
 * 2. 注册表 key 取自 [HexRegistries] 的 `ResourceKey` 常量，**不要**去读
 *    `HexActions.REGISTRY` / `HexIotaTypes.REGISTRY` —— 那两个字段所在的类一旦初始化，
 *    会连带触发 `HexItems.<clinit>` 去 `new Item(...)`，而构造 `Item` 需要
 *    `createIntrusiveHolder`，在注册表已冻结时会抛 `Registry is already frozen`。
 */
object ForgeMiehexRegistry {

    /** 注册表 id → 该注册表下要写入的 (id, 值) 列表。 */
    private val entries = linkedMapOf<ResourceLocation, MutableList<Pair<ResourceLocation, Any>>>()

    /** 需要挂监听的注册表，顺序即写入顺序。 */
    private val wantedRegistries: List<ResourceKey<out Registry<*>>> =
        listOf(HexRegistries.IOTA_TYPE, HexRegistries.ACTION)

    @JvmStatic
    fun init() {
        collectEntries()
        for (registryKey in wantedRegistries) {
            val registryId = registryKey.location()
            val toRegister = entries[registryId].orEmpty()
            MOD_BUS.addListener { event: RegisterEvent ->
                if (event.registryKey.location() != registryId) return@addListener
                for ((id, value) in toRegister) {
                    registerInto(event, registryKey, id, value)
                }
            }
        }
    }

    /** 让 `registerAll` 把条目塞进 [entries]，不触碰任何注册表。 */
    private fun collectEntries() {
        MiehexRegistry.registerAll(MiehexRegisterer { registryId, id, value ->
            entries.getOrPut(registryId) { mutableListOf() }.add(id to value)
        })
    }

    /**
     * 把 [value] 写进 [registryKey] 的 [id] 位置。
     *
     * 这里把 key 与 value 都降到 `Registry<Any>` / `Any` 来绕开 `Registry<*>` 的星号投影：
     * `RegisterEvent.register` 本身接受 `ResourceKey<? extends Registry<T>>`，
     * 而我们的 key 就是 `ResourceKey<out Registry<*>>`，直接对齐即可。
     */
    private fun registerInto(
        event: RegisterEvent,
        registryKey: ResourceKey<out Registry<*>>,
        id: ResourceLocation,
        value: Any,
    ) {
        @Suppress("UNCHECKED_CAST")
        val key = registryKey as ResourceKey<Registry<Any>>
        event.register(key, id) { value }
    }
}
