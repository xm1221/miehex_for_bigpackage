package cn.xm1221.miehex.api;

import at.petrak.hexcasting.api.casting.ActionRegistryEntry;
import at.petrak.hexcasting.api.casting.castables.Action;
import at.petrak.hexcasting.api.casting.math.HexDir;
import at.petrak.hexcasting.api.casting.math.HexPattern;
import at.petrak.hexcasting.common.lib.hex.HexActions;
import cn.xm1221.miehex.MieHexMod;
import cn.xm1221.miehex.MiehexRegisterer;
import net.minecraft.resources.ResourceLocation;

public class ActionRegistryHelper {

    /**
     * 注册一个图案动作。
     *
     * @param registerer 平台的注册动作，见 {@link MiehexRegisterer}
     * @param id        id
     * @param angles    笔顺角度序列，如 "qaq"
     * @param startDir  起始方向，如 HexDir.NORTH_EAST
     * @param action    动作实例
     */
    public static void register(MiehexRegisterer registerer, String id, String angles, HexDir startDir, Action action) {
        HexPattern pattern = HexPattern.fromAngles(angles, startDir);
        register(registerer, id, pattern, action);
    }

    /**
     * 注册一个图案动作。
     *
     * @param registerer 平台的注册动作
     * @param id      裸路径（不含命名空间）
     * @param pattern 已构建好的 HexPattern 对象
     * @param action  动作实例
     */
    private static void register(MiehexRegisterer registerer, String id, HexPattern pattern, Action action) {
        // 注意：过去这里用 tryParse(id) 做校验，但无命名空间的 id 会被默默解析成 "minecraft:xxx"，
        // 既拦不住错误也让日志打出了错误的命名空间。这里改为只接受裸路径并显式报错。
        if (id.indexOf(':') >= 0) {
            throw new IllegalArgumentException("动作 ID 不应包含命名空间（会自动加上 " + MieHexMod.MOD_ID + ":）: " + id);
        }
        ResourceLocation nsid = ResourceLocation.tryBuild(MieHexMod.MOD_ID, id);
        if (nsid == null) {
            throw new IllegalArgumentException("无效的动作 ID: " + id);
        }
        ActionRegistryEntry entry = new ActionRegistryEntry(pattern, action);
        // 不能直接 Registry.register(HexActions.REGISTRY, ...) —— Forge 在 mod 构造阶段已锁死注册表。
        registerer.register(HexActions.REGISTRY.key().location(), nsid, entry);
        System.out.println("Registered action: " + nsid);
    }
}