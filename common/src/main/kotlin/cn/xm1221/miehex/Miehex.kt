package cn.xm1221.miehex

import cn.xm1221.miehex.config.MiehexServerConfig
import net.minecraft.resources.ResourceLocation

/**
 * 模组门面：保留 HexDummy 模板的 `Miehex` 命名，供配置 / 网络 / 注册器等脚手架代码引用。
 *
 * 真正的初始化逻辑在 MieHexMod.init / MieHexMod.initClient，平台入口调用那里。
 */
object Miehex {
    const val MODID: String = MieHexMod.MOD_ID

    @JvmField
    val LOGGER = MieHexMod.LOGGER

    @JvmStatic
    fun id(path: String): ResourceLocation = MieHexMod.id(path)

    /** 仅服务端调用；由各平台入口的专用服务端初始化钩子转发。 */
    @JvmStatic
    fun initServer() {
        MiehexServerConfig.initServer()
    }
}