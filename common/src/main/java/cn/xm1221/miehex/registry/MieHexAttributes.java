package cn.xm1221.miehex.registry;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.core.registries.BuiltInRegistries;
import cn.xm1221.miehex.MieHexMod;
import cn.xm1221.miehex.MiehexRegisterer;

public class MieHexAttributes {
    public static final Attribute MOB_MEDIA = new RangedAttribute(
            MieHexMod.MOD_ID + ".attributes.mob_media",
            0.0, 0.0, Double.MAX_VALUE
    ).setSyncable(true);

    public static final Attribute MOB_AMBIT_RADIUS = new RangedAttribute(
            MieHexMod.MOD_ID + ".attributes.mob_ambit_radius",
            16.0, 0.0, 64.0
    ).setSyncable(true);

    /**
     * 注册到原版属性注册表。
     *
     * 注意：这里**不能**再用 `Registry.register(BuiltInRegistries.ATTRIBUTE, ...)` 直接写 ——
     * Forge 在 mod 构造阶段已锁死注册表，必须交给平台的注册事件。
     */
    public static void register(MiehexRegisterer registerer) {
        registerer.register(BuiltInRegistries.ATTRIBUTE.key().location(), MieHexMod.id("mob_media"), MOB_MEDIA);
        registerer.register(BuiltInRegistries.ATTRIBUTE.key().location(), MieHexMod.id("mob_ambit_radius"), MOB_AMBIT_RADIUS);
    }

    public static AttributeSupplier.Builder createEntityAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(MOB_MEDIA, 100000.0)
                .add(MOB_AMBIT_RADIUS, 16);
    }
}
