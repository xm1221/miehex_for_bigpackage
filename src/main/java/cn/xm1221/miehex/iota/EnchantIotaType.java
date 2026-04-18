package cn.xm1221.miehex.iota;

import at.petrak.hexcasting.api.casting.iota.IotaType;
import cn.xm1221.miehex.util.SgaUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;

public class EnchantIotaType extends IotaType<EnchantIota> {
    public static final EnchantIotaType INSTANCE = new EnchantIotaType();

    private EnchantIotaType() {}

    @Override
    public EnchantIota deserialize(Tag tag, ServerLevel world) throws IllegalArgumentException {
        CompoundTag ct = (CompoundTag) tag;
        String id = ct.getString("id");
        short lvl = ct.getShort("lvl");
        return new EnchantIota(id, lvl);
    }

    @Override
    public Component display(Tag tag) {
        CompoundTag ct = (CompoundTag) tag;
        String id = ct.getString("id").split(":")[1];
        short lvl = ct.getShort("lvl");
        String raw = (id +" level: "+ lvl).toUpperCase();
        String sga = SgaUtils.toStandardGalactic(raw);
        return Component.literal(sga).withStyle(ChatFormatting.GRAY);
    }

    @Override
    public int color() {
        return 0x88ff88;
    }
}