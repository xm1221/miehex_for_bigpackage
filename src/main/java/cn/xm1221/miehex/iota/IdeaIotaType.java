package cn.xm1221.miehex.iota;

import at.petrak.hexcasting.api.casting.iota.IotaType;
import cn.xm1221.miehex.util.SgaUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;

public class IdeaIotaType extends IotaType<IdeaIota> {
    public static final IdeaIotaType INSTANCE = new IdeaIotaType();

    private IdeaIotaType() {}

    @Override
    public IdeaIota deserialize(Tag tag, ServerLevel world) throws IllegalArgumentException {
        CompoundTag ct = (CompoundTag) tag;
        String entityTypeId = ct.getString("entityTypeId");
        double maxHealth = ct.getDouble("maxHealth");
        double movementSpeed = ct.getDouble("movementSpeed");
        double attackDamage = ct.getDouble("attackDamage");
        double armor = ct.getDouble("armor");
        return new IdeaIota(entityTypeId, maxHealth, movementSpeed, attackDamage, armor);
    }

    @Override
    public Component display(Tag tag) {
        CompoundTag ct = (CompoundTag) tag;
        String entityTypeId = ct.getString("entityTypeId");
        double maxHealth = ct.getDouble("maxHealth");
        double movementSpeed = ct.getDouble("movementSpeed");
        double attackDamage = ct.getDouble("attackDamage");
        double armor = ct.getDouble("armor");

        // 显示简洁信息，转换为大写 SGA
        String raw = String.format("IDEA || {TYPE:%s HP:%.0f SPD:%.1f DMG:%.1f ARM:%.1f}",
                entityTypeId,maxHealth,movementSpeed,attackDamage,armor);
                String sga = SgaUtils.toStandardGalactic(raw);
        return Component.literal(sga).withStyle(ChatFormatting.WHITE);
    }

    @Override
    public int color() {
        return 0xC0C0C0; // 灰白色
    }
}
