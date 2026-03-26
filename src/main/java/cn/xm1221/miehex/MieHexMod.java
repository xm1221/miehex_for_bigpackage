package cn.xm1221.miehex;

import cn.xm1221.miehex.registry.IotaRegistry;
import net.fabricmc.api.ModInitializer;

public class MieHexMod implements ModInitializer {
	public static final String MOD_ID = "miehex";

	@Override
	public void onInitialize() {
		IotaRegistry.init();
	}
}