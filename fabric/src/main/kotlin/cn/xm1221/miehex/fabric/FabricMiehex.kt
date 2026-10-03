package cn.xm1221.miehex.fabric

import cn.xm1221.miehex.MiehexRegisterer
import cn.xm1221.miehex.MiehexRegistry
import net.fabricmc.api.ModInitializer
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries

object FabricMiehex : ModInitializer {
    override fun onInitialize() {
        // Fabric 没有注册事件，且 mod 初始化早于注册表冻结，直接写即可。
        MiehexRegistry.registerAll(MiehexRegisterer(::fabricRegister))
    }

    /**
     * 从原版根注册表按资源位置取出目标注册表再写入。
     * hexcasting 的自定义注册表也挂在根注册表上，所以在 Fabric 上同样能查到。
     */
    private fun fabricRegister(
        registryId: net.minecraft.resources.ResourceLocation,
        id: net.minecraft.resources.ResourceLocation,
        value: Any,
    ) {
        val registry = BuiltInRegistries.REGISTRY.get(registryId)
            ?: error("Registry not found: $registryId")
        @Suppress("UNCHECKED_CAST")
        Registry.register(registry as Registry<Any>, id, value)
    }
}
