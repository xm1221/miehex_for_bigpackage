package cn.xm1221.miehex.iota;

import at.petrak.hexcasting.api.casting.iota.Iota;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class IdeaIota extends Iota {
    private final String entityTypeId;   // 生物注册名，如 "minecraft:zombie"
    private final double maxHealth;
    private final double movementSpeed;
    private final double attackDamage;
    private final double armor;

    public IdeaIota(String entityTypeId, double maxHealth, double movementSpeed,
                    double attackDamage, double armor) {
        super(IdeaIotaType.INSTANCE, new Object[]{
                entityTypeId, maxHealth, movementSpeed, attackDamage, armor
        });
        this.entityTypeId = entityTypeId;
        this.maxHealth = maxHealth;
        this.movementSpeed = movementSpeed;
        this.attackDamage = attackDamage;
        this.armor = armor;
    }

    @Override
    public boolean isTruthy() {
        return true;
    }

    @Override
    protected boolean toleratesOther(Iota that) {
        if (!(that instanceof IdeaIota other)) return false;
        return this.entityTypeId.equals(other.entityTypeId) &&
                Math.abs(this.maxHealth - other.maxHealth) < 1e-5 &&
                Math.abs(this.movementSpeed - other.movementSpeed) < 1e-5 &&
                Math.abs(this.attackDamage - other.attackDamage) < 1e-5 &&
                Math.abs(this.armor - other.armor) < 1e-5;
    }

    @Override
    public @NotNull Tag serialize() {
        CompoundTag tag = new CompoundTag();
        tag.putString("entityTypeId", entityTypeId);
        tag.putDouble("maxHealth", maxHealth);
        tag.putDouble("movementSpeed", movementSpeed);
        tag.putDouble("attackDamage", attackDamage);
        tag.putDouble("armor", armor);
        return tag;
    }

    // 供 KubeJS 直接调用的方法，返回包含所有数据的 Map
    public Map<String, Object> idea() {
        Map<String, Object> data = new HashMap<>();
        data.put("entityTypeId", entityTypeId);
        data.put("maxHealth", maxHealth);
        data.put("movementSpeed", movementSpeed);
        data.put("attackDamage", attackDamage);
        data.put("armor", armor);
        return data;
    }

    // 单个 getter，也可供 KubeJS 直接访问
    public String getEntityTypeId() { return entityTypeId; }
    public double getMaxHealth() { return maxHealth; }
    public double getMovementSpeed() { return movementSpeed; }
    public double getAttackDamage() { return attackDamage; }
    public double getArmor() { return armor; }
}
