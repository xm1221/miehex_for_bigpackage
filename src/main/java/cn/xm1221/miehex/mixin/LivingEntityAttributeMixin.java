package cn.xm1221.miehex.mixin;

import at.petrak.hexcasting.common.lib.HexAttributes;
import cn.xm1221.miehex.registry.MieHexAttributes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityAttributeMixin extends Entity {

    @Shadow public abstract AttributeMap getAttributes();

    public LivingEntityAttributeMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }


    @Inject(at = @At("RETURN"), method = "createLivingAttributes")
    private static void hex$addAttributes(CallbackInfoReturnable<AttributeSupplier.Builder> cir) {
        var out = cir.getReturnValue();
        out.add(MieHexAttributes.MOB_MEDIA);
        out.add(MieHexAttributes.MOB_AMBIT_RADIUS);
        out.add(HexAttributes.MEDIA_CONSUMPTION_MODIFIER);

    }


}