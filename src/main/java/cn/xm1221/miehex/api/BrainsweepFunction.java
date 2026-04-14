package cn.xm1221.miehex.api;

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.castables.SpellAction;
import net.minecraft.world.entity.Entity;

@FunctionalInterface
public interface BrainsweepFunction {
    SpellAction.Result apply(Entity entity, Iota iota, CastingEnvironment env);
}