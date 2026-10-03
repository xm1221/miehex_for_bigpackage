package cn.xm1221.miehex.iota;

import at.petrak.hexcasting.api.casting.iota.IotaType;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
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
        /*double maxHealth = ct.getDouble("maxHealth");
        double movementSpeed = ct.getDouble("movementSpeed");
        double attackDamage = ct.getDouble("attackDamage");
        double armor = ct.getDouble("armor");*/

        // 显示简洁信息；用 minecraft:alt 字体呈现银河字母效果，不再自行做字符映射
        String raw = "IDEA:"+entityTypeId+" GOD BLESS HEXCASTERS";
        var style = Style.EMPTY.withFont(ResourceLocation.tryBuild("minecraft","alt")).withColor(ChatFormatting.WHITE);
        return Component.literal(raw).withStyle(style);
    }

    @Override
    public int color() {
        return 0xC0C0C0; // 灰白色
    }
}
