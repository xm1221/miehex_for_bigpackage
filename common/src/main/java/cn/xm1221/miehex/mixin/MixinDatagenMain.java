package cn.xm1221.miehex.mixin;

import cn.xm1221.miehex.MieHexMod;
import org.spongepowered.asm.mixin.Mixin;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;

// scuffed workaround for https://github.com/architectury/architectury-loom/issues/189
@Mixin({
    net.minecraft.data.Main.class,
    net.minecraft.server.Main.class,
})
public abstract class MixinDatagenMain {
    @WrapMethod(method = "main", remap = false)
    private static void miehex$systemExitAfterDatagenFinishes(String[] strings, Operation<Void> original) {
        try {
            original.call((Object) strings);
        } catch (Throwable throwable) {
            MieHexMod.LOGGER.error("Datagen failed!", throwable);
            System.exit(1);
        }
        MieHexMod.LOGGER.info("Datagen succeeded, terminating.");
        System.exit(0);
    }
}
