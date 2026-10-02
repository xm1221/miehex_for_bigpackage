package cn.xm1221.miehex.iota;

import at.petrak.hexcasting.api.casting.eval.CastResult;
import at.petrak.hexcasting.api.casting.eval.ResolvedPatternType;
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage;
import at.petrak.hexcasting.api.casting.eval.vm.CastingVM;
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.ListIota;
import at.petrak.hexcasting.api.casting.iota.IotaType;
import at.petrak.hexcasting.common.lib.hex.HexEvalSounds;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FunctionIota extends Iota {
    private final Iota id;
    private final ListIota code;

    private final Iota result;

    public FunctionIota(Iota id, ListIota code, Iota result) {
        super(FunctionIotaType.INSTANCE, new Object[]{id, code});
        this.id = id;
        this.code = code;
        this.result = result;
    }

    public Iota getId() {
        return id;
    }

    public ListIota getCode() {
        return code;
    }

    public Iota getResult() {
        return result;
    }

    @Override
    public boolean isTruthy() {
        return true;
    }

    @Override
    protected boolean toleratesOther(Iota that) {
        if (!(that instanceof FunctionIota other)) return false;
        return Iota.tolerates(this.code, other.code);
    }

    @Override
    public @NotNull Tag serialize() {
        CompoundTag tag = new CompoundTag();
        tag.put("id", IotaType.serialize(id));
        tag.put("code", IotaType.serialize(code));
        tag.put("result", IotaType.serialize(result));
        return tag;
    }

    @Override
    public boolean executable(){return true;}

    @Override
    public @NotNull CastResult execute(CastingVM vm, ServerLevel world, SpellContinuation continuation) {
        CastingImage image = vm.getImage();
        List<Iota> stack = image.getStack();
        stack.add(this);
        return new CastResult(this, continuation, null, List.of(), ResolvedPatternType.EVALUATED, HexEvalSounds.NOTHING);
    }
}