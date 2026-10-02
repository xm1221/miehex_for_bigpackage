package cn.xm1221.miehex.iota;

import at.petrak.hexcasting.api.casting.iota.IotaType;
import com.mojang.serialization.MapCodec;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;

public class TypeIotaType extends IotaType<TypeIota> {

    public static final TypeIotaType INSTANCE = new TypeIotaType();
    private TypeIotaType() {}

    @Override
    public TypeIota deserialize(Tag tag, ServerLevel world) throws IllegalArgumentException {
        CompoundTag tagCompound = (CompoundTag) tag;
        return new TypeIota(tagCompound.getString("classname"));
    }

    @Override
    public Component display(Tag tag) {
        CompoundTag tagCompound = (CompoundTag) tag;
        return new TypeIota(tagCompound.getString("classname")).display();
    }

   /* @Override
    public MapCodec<TypeIota> codec() {
        return TypeIota.CODEC.fieldOf("classname");
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, TypeIota> streamCodec() {
        return StreamCodec.composite(
                ByteBufCodecs.STRING_UTF8,TypeIota::getClassname,
                TypeIota::new
        );
    }*/

    @Override
    public int color() {
        return 0X66CDAA;
    }
}
