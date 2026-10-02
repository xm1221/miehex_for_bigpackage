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
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public class CustomIotaHolderItem extends Item implements IotaHolderItem, VariantItem, PigmentItem {
    private final int numVariants;
    private final boolean writable;      // 是否允许写入
    private final boolean pageable;      // 是否支持多页

    // NBT 标签常量（与 Spellbook 完全一致）
    public static final String TAG_SELECTED_PAGE = "page_idx";
    public static final String TAG_PAGES = "pages";
    public static final String TAG_PAGE_NAMES = "page_names";
    public static final String TAG_SEALED = "sealed_pages";
    public static final String TAG_VARIANT = "variant";
    public static final int MAX_PAGES = 64;

    // 单页模式专用标签
    public static final String TAG_DATA = "data";

    public CustomIotaHolderItem(Properties properties, int numVariants, boolean writable, boolean pageable) {
        super(properties);
        this.numVariants = numVariants;
        this.writable = writable;
        this.pageable = pageable;
    }

    // ==================== 内部辅助方法（多页模式） ====================

    private static boolean arePagesEmpty(ItemStack stack) {
        CompoundTag pages = NBTHelper.getCompound(stack, TAG_PAGES);
        return pages == null || pages.isEmpty();
    }

    private static int getPage(ItemStack stack, int ifEmpty) {
        if (arePagesEmpty(stack)) {
            return ifEmpty;
        } else if (NBTHelper.hasNumber(stack, TAG_SELECTED_PAGE)) {
            int index = NBTHelper.getInt(stack, TAG_SELECTED_PAGE);
            if (index == 0) index = 1;
            return index;
        } else {
            return 1;
        }
    }

    private static int highestPage(ItemStack stack) {
        CompoundTag pages = NBTHelper.getCompound(stack, TAG_PAGES);
        if (pages == null) return 0;
        return pages.getAllKeys().stream()
                .flatMap(s -> { try { return Stream.of(Integer.parseInt(s)); } catch (NumberFormatException e) { return Stream.empty(); } })
                .max(Integer::compare).orElse(0);
    }

    private static boolean isPageSealed(ItemStack stack, int page) {
        CompoundTag sealed = NBTHelper.getCompound(stack, TAG_SEALED);
        return NBTHelper.getBoolean(sealed, String.valueOf(page));
    }

    private static void setPageSealed(ItemStack stack, int page, boolean sealed) {
        CompoundTag sealedTag = NBTHelper.getOrCreateCompound(stack, TAG_SEALED);
        String key = String.valueOf(page);
        if (sealed) {
            sealedTag.putBoolean(key, true);
        } else {
            sealedTag.remove(key);
            if (sealedTag.isEmpty()) stack.removeTagKey(TAG_SEALED);
        }
    }

    private static boolean isSealed(ItemStack stack) {
        int idx = getPage(stack, 1);
        return isPageSealed(stack, idx);
    }

    private static void setSealed(ItemStack stack, boolean sealed) {
        int idx = getPage(stack, 1);
        setPageSealed(stack, idx, sealed);
    }

    private static void setPageData(ItemStack stack, int page, @Nullable CompoundTag data) {
        if (data == null) {
            CompoundTag pages = NBTHelper.getCompound(stack, TAG_PAGES);
            if (pages != null) {
                pages.remove(String.valueOf(page));
                if (pages.isEmpty()) stack.removeTagKey(TAG_PAGES);
            }
        } else {
            CompoundTag pages = NBTHelper.getOrCreateCompound(stack, TAG_PAGES);
            pages.put(String.valueOf(page), data);
        }
    }

    private static CompoundTag getPageData(ItemStack stack, int page) {
        CompoundTag pages = NBTHelper.getCompound(stack, TAG_PAGES);
        if (pages == null) return null;
        Tag tag = pages.get(String.valueOf(page));
        return tag instanceof CompoundTag ? (CompoundTag) tag : null;
    }

    // ==================== 公开 API（与 Spellbook 对齐） ====================

    public static int GetCurrentPage(ItemStack stack) {
        return getPage(stack, 1);
    }
    public boolean isPageable() {
        return pageable;
    }

    public static void SetCurrentPage(ItemStack stack, int page) {
        int clamped = Mth.clamp(page, 1, MAX_PAGES);
        NBTHelper.putInt(stack, TAG_SELECTED_PAGE, clamped);
        // 更新自定义名称（如果有）
        int shifted = Math.max(1, clamped);
        String nameKey = String.valueOf(shifted);
        CompoundTag names = NBTHelper.getCompound(stack, TAG_PAGE_NAMES);
        if (names != null && names.contains(nameKey)) {
            String nameJson = names.getString(nameKey);
            Component name = Component.Serializer.fromJson(nameJson);
            if (name != null) stack.setHoverName(name);
        } else {
            stack.resetHoverName();
        }
    }

    public static void RotatePage(ItemStack stack, boolean increase) {
        int idx = getPage(stack, 0);
        if (idx != 0) {
            idx += increase ? 1 : -1;
            idx = Math.max(1, idx);
        }
        idx = Mth.clamp(idx, 0, MAX_PAGES);
        NBTHelper.putInt(stack, TAG_SELECTED_PAGE, idx);

        // 更新自定义名称
        int shifted = Math.max(1, idx);
        String nameKey = String.valueOf(shifted);
        CompoundTag names = NBTHelper.getCompound(stack, TAG_PAGE_NAMES);
        if (names != null && names.contains(nameKey)) {
            String nameJson = names.getString(nameKey);
            Component name = Component.Serializer.fromJson(nameJson);
            if (name != null) stack.setHoverName(name);
        } else {
            stack.resetHoverName();
        }
    }

    public static int GetHighestPage(ItemStack stack) {
        return highestPage(stack);
    }

    public static boolean IsPageSealed(ItemStack stack, int page) {
        return isPageSealed(stack, page);
    }

    public static void SetPageSealed(ItemStack stack, int page, boolean sealed) {
        setPageSealed(stack, page, sealed);
    }

    public static boolean IsCurrentPageSealed(ItemStack stack) {
        return isSealed(stack);
    }

    public static void SetCurrentPageSealed(ItemStack stack, boolean sealed) {
        setSealed(stack, sealed);
    }

    // ==================== IotaHolderItem 实现 ====================

    @Override
    public @Nullable CompoundTag readIotaTag(ItemStack stack) {
        if (!pageable) {
            return NBTHelper.getCompound(stack, TAG_DATA);
        } else {
            int page = getPage(stack, 1);
            return getPageData(stack, page);
        }
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
    public boolean writeable(ItemStack stack) {
        if (!writable) return false;
        if (pageable) {
            return !isSealed(stack);
        } else {
            return true;
        }
    }

    @Override
    public boolean canWrite(ItemStack stack, @Nullable Iota iota) {
        if (!writable) return false;
        if (pageable) {
            return iota != null && !isSealed(stack);
        } else {
            return iota != null;
        }
    }

    @Override
    public void writeDatum(ItemStack stack, @Nullable Iota iota) {
        if (!writable) return;
        if (pageable) {
            if (isSealed(stack)) return;
            int page = getPage(stack, 1);
            if (iota == null) {
                setPageData(stack, page, null);
                // 清除该页的密封标记
                setPageSealed(stack, page, false);
            } else {
                CompoundTag data = IotaType.serialize(iota);
                setPageData(stack, page, data);
            }
        } else {
            if (iota == null) {
                stack.removeTagKey(TAG_DATA);
            } else {
                NBTHelper.put(stack, TAG_DATA, IotaType.serialize(iota));
            }
        }
    }

    @Override
    public int getColor(ItemStack stack) {
        if (NBTHelper.hasString(stack, IotaHolderItem.TAG_OVERRIDE_VISUALLY)) {
            String override = NBTHelper.getString(stack, IotaHolderItem.TAG_OVERRIDE_VISUALLY);
            if (override != null && ResourceLocation.isValidResourceLocation(override)) {
                ResourceLocation key = new ResourceLocation(override);
                if (HexIotaTypes.REGISTRY.containsKey(key)) {
                    IotaType<?> type = HexIotaTypes.REGISTRY.get(key);
                    if (type != null) return type.color();
                }
            }
            return 0xFFAACCFF;
        }
        CompoundTag tag = readIotaTag(stack);
        if (tag == null) return HexUtils.ERROR_COLOR;
        return IotaType.getColor(tag);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        if (pageable) {
            boolean sealed = isSealed(stack);
            boolean empty = false;
            if (NBTHelper.hasNumber(stack, TAG_SELECTED_PAGE)) {
                int pageIdx = NBTHelper.getInt(stack, TAG_SELECTED_PAGE);
                int highest = highestPage(stack);
                if (highest != 0) {
                    if (sealed) {
                        components.add(Component.translatable("hexcasting.tooltip.spellbook.page.sealed",
                                        Component.literal(String.valueOf(pageIdx)).withStyle(ChatFormatting.WHITE),
                                        Component.literal(String.valueOf(highest)).withStyle(ChatFormatting.WHITE),
                                        Component.translatable("hexcasting.tooltip.spellbook.sealed").withStyle(ChatFormatting.GOLD))
                                .withStyle(ChatFormatting.GRAY));
                    } else {
                        components.add(Component.translatable("hexcasting.tooltip.spellbook.page",
                                        Component.literal(String.valueOf(pageIdx)).withStyle(ChatFormatting.WHITE),
                                        Component.literal(String.valueOf(highest)).withStyle(ChatFormatting.WHITE))
                                .withStyle(ChatFormatting.GRAY));
                    }
                } else {
                    empty = true;
                }
            } else {
                empty = true;
            }

            if (empty) {
                boolean overridden = NBTHelper.hasString(stack, IotaHolderItem.TAG_OVERRIDE_VISUALLY);
                if (sealed) {
                    if (overridden) {
                        components.add(Component.translatable("hexcasting.tooltip.spellbook.sealed").withStyle(ChatFormatting.GOLD));
                    } else {
                        components.add(Component.translatable("hexcasting.tooltip.spellbook.empty.sealed",
                                        Component.translatable("hexcasting.tooltip.spellbook.sealed").withStyle(ChatFormatting.GOLD))
                                .withStyle(ChatFormatting.GRAY));
                    }
                } else if (!overridden) {
                    components.add(Component.translatable("hexcasting.tooltip.spellbook.empty").withStyle(ChatFormatting.GRAY));
                }
            }
        } else {
            // 单页模式无额外提示
        }
        IotaHolderItem.appendHoverText(this, stack, components, flag);
        super.appendHoverText(stack, level, components, flag);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (pageable) {
            // 与 Spellbook 相同：更新选中页索引，并恢复页面名称
            int idx = getPage(stack, 0);
            NBTHelper.putInt(stack, TAG_SELECTED_PAGE, idx);
            int shifted = Math.max(1, idx);
            String nameKey = String.valueOf(shifted);
            CompoundTag names = NBTHelper.getOrCreateCompound(stack, TAG_PAGE_NAMES);
            if (stack.hasCustomHoverName()) {
                names.putString(nameKey, Component.Serializer.toJson(stack.getHoverName()));
            } else {
                names.remove(nameKey);
                if (names.isEmpty()) stack.removeTagKey(TAG_PAGE_NAMES);
            }
        }
    }

    // ==================== VariantItem 实现 ====================
    @Override
    public int numVariants() {
        return numVariants;
    }

    @Override
    public int getVariant(ItemStack stack) {
        return NBTHelper.getInt(stack, TAG_VARIANT, 0);
    }

    @Override
    public void setVariant(ItemStack stack, int variant) {
        if (pageable && isSealed(stack)) return; // 模仿 Spellbook：密封后不可更改变种
        NBTHelper.putInt(stack, TAG_VARIANT, clampVariant(variant));
    }

    // ==================== PigmentItem 实现 ====================
    @Override
    public ColorProvider provideColor(ItemStack stack, UUID owner) {
        int variant = getVariant(stack);
        float hue = (variant % numVariants) / (float) numVariants;
        int baseColor = 0xFF000000 | Mth.hsvToRgb(hue, 0.8f, 1.0f);
        return new ColorProvider() {
            @Override
            protected int getRawColor(float time, Vec3 position) {
                return baseColor;
            }
        };
    }
}