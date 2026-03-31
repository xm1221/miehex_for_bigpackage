package cn.xm1221.miehex.api;


import at.petrak.hexcasting.api.casting.iota.Iota;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.nbt.CompoundTag;
import at.petrak.hexcasting.api.casting.iota.IotaType;
import org.jetbrains.annotations.NotNull;

public class KubeJSIotaNBTHelper {


    private final Iota deserialize;
    private final @NotNull Tag serialize;

    public KubeJSIotaNBTHelper(Iota iota,CompoundTag tag, ServerLevel level) {
        this.deserialize = IotaType.deserialize(tag,level);
        this.serialize = IotaType.serialize(iota);
    }


    public Iota getDeserialize() {
        return deserialize;
    }

    public @NotNull Tag getSerialize() {
        return serialize;
    }
}