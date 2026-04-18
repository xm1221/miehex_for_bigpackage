package cn.xm1221.miehex;

import cn.xm1221.miehex.registry.IotaRegistry;
import cn.xm1221.miehex.registry.MieHexAttributes;
import net.fabricmc.api.ModInitializer;
import net.minecraft.world.entity.EntityType;

public class MieHexMod implements ModInitializer {
	public static final String MOD_ID = "miehex";

	@Override
	public void onInitialize() {
		IotaRegistry.init();
		MieHexAttributes.register();

	}

}