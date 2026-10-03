package cn.xm1221.miehex

import cn.xm1221.miehex.registry.ActionRegisry
import cn.xm1221.miehex.registry.IotaRegistry
import net.minecraft.resources.ResourceLocation

/**
 * 平台无关的注册动作：把 [value] 放到注册表 [registryId] 的 [id] 位置。
 *
 * 用「注册表的 ResourceLocation」而不是 `Registry` / `ResourceKey` 来寻址，
 * 是为了让两个平台各自干净地解析：Forge 的 `RegisterEvent` 接受资源位置并自行取注册表，
 * Fabric 则回退到原版根注册表去查。
 */
fun interface MiehexRegisterer {
    fun register(registryId: ResourceLocation, id: ResourceLocation, value: Any)
}

/**
 * 模组所有注册表写入的统一入口。
 *
 * 为什么要绕这一层：**Forge 会在 mod 构造阶段就锁死注册表**，此时直接
 * `Registry.register(...)` 会抛
 * `IllegalStateException: Can not register to a locked registry. Modder should use Forge Register methods.`
 * 所以注册必须发生在平台的注册事件里（Forge 的 `RegisterEvent`）；Fabric 没有这个事件，
 * 只能在 `ModInitializer` 里直接写。两边时机不同，靠传入不同的 [MiehexRegisterer] 抹平。
 *
 * [registerAll] **只能被调用一次**，且必须晚于 HexCasting 建好它自己的自定义注册表
 * （Forge 上由 `NewRegistryEvent` 完成，到 `RegisterEvent` 阶段已就绪）。
 */
object MiehexRegistry {
    @JvmStatic
    fun registerAll(registerer: MiehexRegisterer) {
        IotaRegistry.init(registerer)
        ActionRegisry.init(registerer)
    }
}
