package cn.xm1221.miehex.registry;

import at.petrak.hexcasting.common.lib.HexRegistries;
import cn.xm1221.miehex.MieHexMod;
import cn.xm1221.miehex.MiehexRegisterer;
import cn.xm1221.miehex.iota.*;
import net.minecraft.resources.ResourceLocation;

/**
 * 注册本模组的自定义 Iota 类型。
 *
 * 注意：这里**不能**再用 `Registry.register(...)` 直接写 —— Forge 在 mod 构造阶段已锁死注册表，
 * 必须通过 [MiehexRegisterer] 把写入动作交给平台的注册事件。
 */
public class IotaRegistry {
    private static boolean registered = false;

    public static void init(MiehexRegisterer registerer) {
        if (registered) {
            throw new IllegalStateException("IotaRegistry has already been initialized!");
        }
        registered = true;

        register(registerer, "enchant", EnchantIotaType.INSTANCE);
        register(registerer, "idea", IdeaIotaType.INSTANCE);
        register(registerer, "function", FunctionIotaType.INSTANCE);
        register(registerer, "mishap", MishapIotaType.INSTANCE);
        register(registerer, "type", TypeIotaType.INSTANCE);
    }

    private static void register(MiehexRegisterer registerer, String path, Object type) {
        registerer.register(
                HexRegistries.IOTA_TYPE.location(),
                new ResourceLocation(MieHexMod.MOD_ID, path),
                type
        );
    }
}
