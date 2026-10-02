// cn.xm1221.miehex.api.casting.MobMishapEnv.java
package cn.xm1221.miehex.api.casting;

import at.petrak.hexcasting.api.casting.eval.MishapEnvironment;
import at.petrak.hexcasting.api.casting.mishaps.Mishap;
import at.petrak.hexcasting.common.lib.HexDamageTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class MobMishapEnv extends MishapEnvironment {
    /**
     * 实际的生物施法者。
     * <p>
     * 注意：父类的 {@code caster} 字段是 {@code @Nullable ServerPlayer}，本类构造时传入 null，
     * 因此所有"对施法者本身生效"的逻辑都必须走 mobCaster，不能用 caster。
     */
    protected final LivingEntity mobCaster;

    public MobMishapEnv(LivingEntity caster) {
        super((ServerLevel) caster.level(), null); // 生物不是玩家，父类 caster 传 null
        this.mobCaster = caster;
    }

    @Override
    public void yeetHeldItemsTowards(Vec3 targetPos) {
        var pos = mobCaster.position();
        var delta = targetPos.subtract(pos).normalize().scale(0.5);
        for (var hand : InteractionHand.values()) {
            var stack = mobCaster.getItemInHand(hand);
            mobCaster.setItemInHand(hand, ItemStack.EMPTY);
            yeetItem(stack, pos, delta);
        }
    }

    @Override
    public void dropHeldItems() {
        var delta = mobCaster.getLookAngle();
        yeetHeldItemsTowards(mobCaster.position().add(delta));
    }

    @Override
    public void damage(float healthProportion) {
        // 注意：DamageSources#source(ResourceKey) 是 Minecraft 的私有方法，
        // HexCasting 自己靠 access widener 放宽访问；这里改走公共路径自行构造 DamageSource。
        var overcastType = mobCaster.level().registryAccess()
                .registryOrThrow(Registries.DAMAGE_TYPE)
                .getHolderOrThrow(HexDamageTypes.OVERCAST);
        Mishap.trulyHurt(
                mobCaster,
                new DamageSource(overcastType),
                mobCaster.getMaxHealth() * healthProportion
        );
    }

    @Override
    public void drown() {
        if (mobCaster.getAirSupply() < 200) {
            mobCaster.hurt(mobCaster.damageSources().drown(), 2f);
        }
        mobCaster.setAirSupply(0);
    }

    @Override
    public void removeXp(int amount) {
        // 生物无经验值，忽略
    }

    @Override
    public void blind(int ticks) {
        // 可添加失明效果，但生物不需要
    }
}
