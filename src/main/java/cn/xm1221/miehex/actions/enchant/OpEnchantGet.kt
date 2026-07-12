package cn.xm1221.miehex.actions.enchant

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getItemEntity
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.ListIota
import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.utils.asInt
import at.petrak.hexcasting.common.lib.HexRegistries
import cn.xm1221.miehex.iota.EnchantIota
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.EnchantmentHelper
import kotlin.collections.plus

class OpEnchantGet() : ConstMediaAction {
    override val argc = 1

    override val mediaCost: Long
        get() = MediaConstants.DUST_UNIT

    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val entity=args.getItemEntity(0,argc)
        val stack: ItemStack = entity.item;
        var list: List<Iota> = mutableListOf<Iota>();
      val enchs = EnchantmentHelper.getEnchantments(stack)
        for (ench in enchs) {
            val id = ench.key.descriptionId.split(".")[1]+":"+ench.key.descriptionId.split(".")[2]
            val iota = EnchantIota(id,ench.value.toShort())
            list=list.plusElement(iota)
        }
        val res = listOf(ListIota(list))
            return res
    }


}