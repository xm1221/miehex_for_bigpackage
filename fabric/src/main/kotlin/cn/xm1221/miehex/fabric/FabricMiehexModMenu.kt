package cn.xm1221.miehex.fabric

import com.terraformersmc.modmenu.api.ConfigScreenFactory
import com.terraformersmc.modmenu.api.ModMenuApi
import cn.xm1221.miehex.MiehexClient

object FabricMiehexModMenu : ModMenuApi {
    override fun getModConfigScreenFactory() = ConfigScreenFactory(MiehexClient::getConfigScreen)
}
