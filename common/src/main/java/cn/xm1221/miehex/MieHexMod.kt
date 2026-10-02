package cn.xm1221.miehex

import cn.xm1221.miehex.registry.ActionRegisry
import cn.xm1221.miehex.registry.IotaRegistry
import cn.xm1221.miehex.registry.MieHexAttributes
import net.minecraft.resources.ResourceLocation
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

/**
 * miehex 的公共初始化入口，由各平台入口（FabricMiehex / ForgeMiehex）调用。
 *
 * 这个 object 取代了原先的 MieHexMod 类，保留 `MOD_ID` 常量名以免影响其他引用点；
 * 额外提供类型化 LOGGER 与 `id()` 辅助函数。
 */
object MieHexMod {
    const val MOD_ID: String = "miehex"

    @JvmField
    val LOGGER: Logger = LogManager.getLogger(MOD_ID)

    @JvmStatic
    fun id(path: String): ResourceLocation = ResourceLocation(MOD_ID, path)

    @JvmStatic
    fun init() {
        IotaRegistry.init()
        MieHexAttributes.register()
        ActionRegisry.init()
    }

    /** 仅客户端调用；由各平台入口的客户端初始化钩子转发。 */
    @JvmStatic
    fun initClient() {
        MiehexClient.init()
    }
}
