package cn.xm1221.miehex.iota;

import at.petrak.hexcasting.api.casting.iota.IotaType;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
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
        var style = Style.EMPTY.withFont(ResourceLocation.tryBuild("minecraft","alt")).withColor(ChatFormatting.GRAY);
        return Component.literal(id+" ").withStyle(style).append(Component.translatable("enchantment.level."+lvl));
    }

    @Override
    public int color() {
        return 0x88ff88;
    }
}