
package cn.xm1221.miehex.registry;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import cn.xm1221.miehex.MieHexMod;

public class MieHexAttributes {
    public static final Attribute MOB_MEDIA = new RangedAttribute(
            MieHexMod.MOD_ID + ".attributes.mob_media",
            0.0, 0.0, Double.MAX_VALUE
    ).setSyncable(true);

    public static final Attribute MOB_AMBIT_RADIUS = new RangedAttribute(
            MieHexMod.MOD_ID + ".attributes.mob_ambit_radius",
            16.0, 0.0, 64.0
    ).setSyncable(true);

    public static void register() {
        Registry.register(BuiltInRegistries.ATTRIBUTE, new ResourceLocation(MieHexMod.MOD_ID, "mob_media"), MOB_MEDIA);
        Registry.register(BuiltInRegistries.ATTRIBUTE, new ResourceLocation(MieHexMod.MOD_ID, "mob_ambit_radius"), MOB_AMBIT_RADIUS);
    }

    public static AttributeSupplier.Builder createEntityAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(MOB_MEDIA, 100000.0)
                .add(MOB_AMBIT_RADIUS,16);
    }
}