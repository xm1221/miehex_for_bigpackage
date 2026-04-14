// cn/xm1221/miehex/api/BrainsweepKJSHelper.java
package cn.xm1221.miehex.api;

import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.IotaType;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.castables.SpellAction;
import io.yukkuric.hexautomata.action_patch.brainsweep.BrainsweepCallback;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.Nullable;

public class BrainsweepKJSHelper {

    /**
     * 注册一个自定义剥离意识回调。
     *
     * @param key      唯一标识符（如 "mypack:heal_from_gold"）
     * @param priority 优先级，数字越小越先执行
     * @param entityType   用于提取实体类型的示例实体（可为 null）
     * @param iota     用于提取 iota 类型的示例 iota（可为 null）
     * @param callback 自定义回调逻辑
     */
    public static void register(String key, int priority,
                                @Nullable EntityType<Entity> entityType,
                                @Nullable Iota iota,
                                BrainsweepFunction callback) {


        // 2. 从 iota 实例提取 IotaType
        IotaType<?> iotaType = null;
        if (iota != null) {
            iotaType = iota.getType();
            if (iotaType == null) {
                throw new IllegalArgumentException("无法从 iota 获取 IotaType: " + iota);
            }
        }

        // 3. 创建 BrainsweepCallback 实例（泛型强制转换）
        @SuppressWarnings("unchecked")
        BrainsweepCallback<Entity, Iota> callbackInstance = new BrainsweepCallback<Entity, Iota>(
                priority,
                (EntityType<Entity>) entityType,
                (IotaType<Iota>) iotaType
        ) {
            @Override
            public SpellAction.Result call(Entity entity, Iota iota, CastingEnvironment env) {
                return callback.apply(entity, iota, env);
            }
        };

        // 4. 注册到 HexAutomata 的全局回调表（注意 Kotlin 伴生对象的访问方式）
        BrainsweepCallback.Companion.set(key, callbackInstance);
        System.out.println("Registered Brainsweep callback: " + key);
    }
}