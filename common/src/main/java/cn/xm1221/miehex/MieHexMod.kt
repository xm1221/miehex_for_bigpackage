package cn.xm1221.miehex

import net.minecraft.resources.ResourceLocation
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

/**
 * miehex 的公共初始化入口，由各平台入口（FabricMiehex / ForgeMiehex）调用。
 *
 * 这个 object 取代了原先的 MieHexMod 类，保留 `MOD_ID` 常量名以免影响其他引用点；
 * 额外提供类型化 LOGGER 与 `id()` 辅助函数。
 *
 * **注册表写入不在这里**：Forge 在 mod 构造阶段就锁死了注册表，注册必须发生在平台的
 * 注册事件里，见 [MiehexRegistry.registerAll]。
 */
object MieHexMod {
    const val MOD_ID: String = "miehex"

    @JvmField
    val LOGGER: Logger = LogManager.getLogger(MOD_ID)

    @JvmStatic
    fun id(path: String): ResourceLocation = ResourceLocation(MOD_ID, path)

    /** 仅客户端调用；由各平台入口的客户端初始化钩子转发。 */
    @JvmStatic
    fun initClient() {
        MiehexClient.init()
    }
}
