package cn.xm1221.miehex.hook

import at.petrak.hexcasting.api.casting.eval.vm.CastingVM
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation
import at.petrak.hexcasting.api.casting.iota.Iota
import cn.xm1221.MieLib.registry.CastingVMHook
import cn.xm1221.MieLib.worker.CastingVMCall
import cn.xm1221.MieLib.worker.CastingVMWorker
import cn.xm1221.miehex.MieHexMod
import cn.xm1221.miehex.api.casting.frame.FrameCatch
import net.minecraft.server.level.ServerLevel

/**
 * 通过 MieLib 的 CastingVMHook 挂载 miehex 的 executeInner 后处理（原 CastingVMMixin）。
 * MieLib 全进程只 wrap 一次 CastingVM.executeInner，这里只注册 After worker。
 */
object MiehexCastingVMHooks {
    @JvmStatic
    fun init() {
        CastingVMHook.register(MieHexMod.id("mishap_catch"), MishapCatchWorker)
    }

    /** 原方法执行后尝试捕获事故（FrameCatch），捕获成功则替换结果。 */
    private object MishapCatchWorker : CastingVMWorker(isAfter = true, priority = 0) {
        override fun execute(
            iota: Iota,
            world: ServerLevel,
            cont: SpellContinuation,
            call: CastingVMCall,
            vm: CastingVM,
        ): CastingVMCall {
            val result = call.result ?: return call
            FrameCatch.handleMishapCatch(iota, vm, world, cont, result)?.let(call::setResult)
            return call
        }
    }
}
