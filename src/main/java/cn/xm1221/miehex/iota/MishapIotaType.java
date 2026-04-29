package cn.xm1221.miehex.iota;

import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.IotaType;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;

public class MishapIotaType extends IotaType<MishapIota>{
    public static final MishapIotaType INSTANCE = new  MishapIotaType();


    public MapCodec<MishapIota> codec() {
        return MishapIota.CODEC.fieldOf("value");
    }


    /*public StreamCodec<RegistryFriendlyByteBuf,MishapIota> streamCodec() {
        return StreamCodec.composite(
                ByteBufCodecs.INT,MishapIota::getKey,
                ByteBufCodecs.STRING_UTF8, MishapIota::getErrormessageCode,
                MishapIota::create
        );
    }*/

    @Override
    public int color() {
        return 0;
    }

   private MishapIotaType() {}

    @Override
    public MishapIota deserialize(Tag tag, ServerLevel world) throws IllegalArgumentException {
        int key = ((CompoundTag)tag).getInt("key");
        String messagecode = ((CompoundTag)tag).getString("message");
            return  MishapIota.create(key,messagecode);
    };

    @Override
    public Component display(Tag tag) {
            return  MishapIota.create(((CompoundTag) tag).getInt("key"),((CompoundTag) tag).getString("message")).display();
    }
}
