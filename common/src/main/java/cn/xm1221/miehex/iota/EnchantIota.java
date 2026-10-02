package cn.xm1221.miehex.iota;

import at.petrak.hexcasting.api.casting.iota.Iota;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import org.jetbrains.annotations.NotNull;

public class EnchantIota extends Iota {
    private final String id;
    private final short level;

    public EnchantIota(String id, short level) {
        super(EnchantIotaType.INSTANCE, new Object[]{id, level});
        this.id = id;
        this.level = level;
    }

    @Override
    public boolean isTruthy() {
        return true;
    }

    @Override
    protected boolean toleratesOther(Iota that) {
        if (!(that instanceof EnchantIota other)) return false;
        return this.id.equals(other.id) && this.level == other.level;
    }

    @Override
    public @NotNull Tag serialize() {
        CompoundTag tag = new CompoundTag();
        tag.putString("id", id);
        tag.putShort("lvl", level);
        return tag;
    }

    // 便捷方法，供 KubeJS 使用
    public String getId() { return id; }
    public short getLevel() { return level; }

    public  Object enchant() {return this;}
}
