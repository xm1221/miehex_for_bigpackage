package cn.xm1221.miehex.registry;

import at.petrak.hexcasting.api.casting.math.HexDir;
import cn.xm1221.miehex.MiehexRegisterer;
import cn.xm1221.miehex.actions.enchant.OpAddEnchant;
import cn.xm1221.miehex.actions.enchant.OpEnchant;
import cn.xm1221.miehex.actions.enchant.OpEnchantGet;
import cn.xm1221.miehex.actions.idea.OpIdeaGet;
import cn.xm1221.miehex.actions.idea.OpIdeaModify;
import cn.xm1221.miehex.actions.idea.OpIdeaSummon;
import cn.xm1221.miehex.actions.meta.OpBraveEval;
import cn.xm1221.miehex.actions.meta.OpCatch;
import cn.xm1221.miehex.actions.meta.OpEvolution;
import cn.xm1221.miehex.actions.meta.OpThrow;
import cn.xm1221.miehex.actions.stack.OpPush;
import cn.xm1221.miehex.actions.stack.OpThrust;
import cn.xm1221.miehex.actions.stack.mishapiota.OpMishapArgs;
import cn.xm1221.miehex.actions.stack.type.OpTypes;
import cn.xm1221.miehex.api.ActionRegistryHelper;
import cn.xm1221.miehex.util.PushUtils;

/**
 * 本模组的全部图案动作。
 *
 * 注册经 [MiehexRegisterer] 转交平台（Forge 必须发生在 RegisterEvent 里），
 * 因此不能在静态初始化阶段直接写注册表。
 */
public class ActionRegisry {
    private static boolean registered = false;

    public static void init(MiehexRegisterer registerer) {
        if (registered) {
            throw new IllegalStateException("ActionRegisry has already been initialized!");
        }
        registered = true;

        ActionRegistryHelper.register(registerer, "quine","qqqqqeawqwqwqwqwqwwded", HexDir.EAST, new OpPush(PushUtils.QUNIE,0));
        ActionRegistryHelper.register(registerer, "get_enchant","awaeqwawq",HexDir.NORTH_EAST,new OpEnchantGet());
        ActionRegistryHelper.register(registerer, "enchant_add","qawwwwaqeeeaqwwqaee",HexDir.EAST, new OpAddEnchant());
        ActionRegistryHelper.register(registerer, "enchant","dwdqewdwe", HexDir.NORTH_WEST,new OpEnchant());
        ActionRegistryHelper.register(registerer, "new_idea","qwqwqwqwqwq",HexDir.EAST,new OpPush(PushUtils.EMPTY_IDEA,0));
        ActionRegistryHelper.register(registerer, "idea_get","qwwwdwewdwwwqwqwwwdwewdwwwqqqwe",HexDir.EAST,new OpIdeaGet());
        ActionRegistryHelper.register(registerer, "idea_modify","wewedwaqdeeaqqwqw",HexDir.NORTH_WEST,new OpIdeaModify());
        ActionRegistryHelper.register(registerer, "summon_idea_entity","wqwqawdeaqqdeewew",HexDir.NORTH_EAST,new OpIdeaSummon());
        ActionRegistryHelper.register(registerer, "brave_eval","deaqqw",HexDir.SOUTH_EAST,new OpBraveEval());
        ActionRegistryHelper.register(registerer, "easy_thrust","wawaqw", HexDir.SOUTH_EAST, new OpThrust(false));
        ActionRegistryHelper.register(registerer, "easy_extract","wedwdw", HexDir.SOUTH_WEST, new OpThrust(true));
        ActionRegistryHelper.register(registerer, "evolution","dadawaaw",HexDir.NORTH_EAST,new OpEvolution());
        ActionRegistryHelper.register(registerer, "catch","deaqw", HexDir.SOUTH_EAST, new OpCatch());
        ActionRegistryHelper.register(registerer, "throw","edqa",HexDir.SOUTH_EAST,new OpThrow());
        ActionRegistryHelper.register(registerer, "type_iota","wqawdew",HexDir.NORTH_EAST,new OpTypes().getIotatype());
        ActionRegistryHelper.register(registerer, "mishap_type","waqedw",HexDir.NORTH_EAST,new OpTypes().getMishaptype());
        ActionRegistryHelper.register(registerer, "mishap_args","deeeew",HexDir.NORTH_EAST,new OpMishapArgs().getInvaldiota());
    }
}
