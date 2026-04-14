package cn.xm1221.miehex.iota;

import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.IotaType;
import at.petrak.hexcasting.api.casting.iota.ListIota;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class FunctionIotaType extends IotaType<FunctionIota> {
    public static final FunctionIotaType INSTANCE = new FunctionIotaType();

    private FunctionIotaType() {}

    @Nullable
    @Override
    public FunctionIota deserialize(Tag tag, ServerLevel world) throws IllegalArgumentException {
        CompoundTag ct = (CompoundTag) tag;
        Tag idTag = ct.get("id");
        Tag codeTag = ct.get("code");
        Tag resultTag = ct.get("result");
        if (idTag == null || codeTag == null) return null;

        Iota id = IotaType.deserialize((CompoundTag) idTag, world);
        Iota codeIota = IotaType.deserialize((CompoundTag) codeTag, world);
        Iota resultIota = IotaType.deserialize((CompoundTag) resultTag, world);
        if (!(codeIota instanceof ListIota codeList)) {
            List list = new ArrayList();
            ListIota emptylist = new ListIota(list);
           return  new FunctionIota(id,emptylist,resultIota);
        }
        return new FunctionIota(id, codeList,resultIota);
    }

    @Override
    public Component display(Tag tag) {
        CompoundTag ct = (CompoundTag) tag;
        Tag idTag = ct.get("id");
        Tag codeTag = ct.get("code");
        Tag resultTag = ct.get("result");
        if (idTag == null || codeTag == null||resultTag == null)  {
            return Component.literal("Function(?)");
        }
        Component idDisplay = getDisplay((CompoundTag) idTag);
        Component resultDisplay = getDisplay((CompoundTag) resultTag);
        return Component.literal("Function: ").withStyle(ChatFormatting.GOLD).append("(").append(idDisplay).append(")").append(" → ").append(resultDisplay);
    }

    @Override
    public int color() {
        return 0xFFD700; // 金色
    }
}
