package cn.xm1221.miehex.item;

import at.petrak.hexcasting.api.item.IotaHolderItem;
import at.petrak.hexcasting.api.item.VariantItem;
import at.petrak.hexcasting.api.item.PigmentItem;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.IotaType;
import at.petrak.hexcasting.api.pigment.ColorProvider;
import at.petrak.hexcasting.api.utils.HexUtils;
import at.petrak.hexcasting.api.utils.NBTHelper;
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class CustomIotaHolderItem extends Item implements IotaHolderItem, VariantItem, PigmentItem {
    private final int numVariants;

    public CustomIotaHolderItem(Properties properties, int numVariants) {
        super(properties);
        this.numVariants = numVariants;
    }

    // ========== IotaHolderItem 接口 ==========
    @Override
    public @Nullable CompoundTag readIotaTag(ItemStack stack) {
        return NBTHelper.getCompound(stack, "data");
    }

    @Override
    public @Nullable Iota readIota(ItemStack stack, ServerLevel world) {
        CompoundTag tag = readIotaTag(stack);
        if (tag != null) {
            return IotaType.deserialize(tag, world);
        }
        return null;
    }

    @Override
    public @Nullable Iota emptyIota(ItemStack stack) {
        return null;
    }

    @Override
    public int getColor(ItemStack stack) {
        // 视觉覆盖
        if (NBTHelper.hasString(stack, IotaHolderItem.TAG_OVERRIDE_VISUALLY)) {
            String override = NBTHelper.getString(stack, IotaHolderItem.TAG_OVERRIDE_VISUALLY);
            if (override != null && ResourceLocation.isValidResourceLocation(override)) {
                ResourceLocation key = new ResourceLocation(override);
                if (HexIotaTypes.REGISTRY.containsKey(key)) {
                    IotaType<?> type = HexIotaTypes.REGISTRY.get(key);
                    if (type != null) return type.color();
                }
            }
            // 无有效覆盖，返回一个默认颜色（避免客户端类）
            return 0xFFAACCFF;
        }
        CompoundTag tag = readIotaTag(stack);
        if (tag == null) {
            return HexUtils.ERROR_COLOR;
        }
        return IotaType.getColor(tag);
    }

    @Override
    public boolean writeable(ItemStack stack) {
        return true;
    }

    @Override
    public boolean canWrite(ItemStack stack, @Nullable Iota iota) {
        return true;
    }

    @Override
    public void writeDatum(ItemStack stack, @Nullable Iota iota) {
        if (iota == null) {
            stack.removeTagKey("data");
        } else {
            NBTHelper.put(stack, "data", IotaType.serialize(iota));
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        IotaHolderItem.appendHoverText(this, stack, components, flag);
    }

    // ========== VariantItem 接口 ==========
    @Override
    public int numVariants() {
        return numVariants;
    }

    @Override
    public int getVariant(ItemStack stack) {
        return NBTHelper.getInt(stack, VariantItem.TAG_VARIANT, 0);
    }

    @Override
    public void setVariant(ItemStack stack, int variant) {
        NBTHelper.putInt(stack, VariantItem.TAG_VARIANT, clampVariant(variant));
    }

    // ========== PigmentItem 接口 ==========
    @Override
    public ColorProvider provideColor(ItemStack stack, UUID owner) {
        int variant = getVariant(stack);
        float hue = (variant % numVariants) / (float) numVariants;
        int baseColor = 0xFF000000 | Mth.hsvToRgb(hue, 0.8f, 1.0f);
        // 正确实现：重写 getRawColor 方法
        return new ColorProvider() {
            @Override
            protected int getRawColor(float time, Vec3 position) {
                return baseColor;
            }
        };
    }
}