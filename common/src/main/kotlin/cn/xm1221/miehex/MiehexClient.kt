package cn.xm1221.miehex

import cn.xm1221.miehex.config.MiehexClientConfig
import me.shedaniel.autoconfig.AutoConfig
import net.minecraft.client.gui.screens.Screen

object MiehexClient {
    fun init() {
        MiehexClientConfig.init()
    }

    fun getConfigScreen(parent: Screen): Screen {
        return AutoConfig.getConfigScreen(MiehexClientConfig.GlobalConfig::class.java, parent).get()
    }
}
