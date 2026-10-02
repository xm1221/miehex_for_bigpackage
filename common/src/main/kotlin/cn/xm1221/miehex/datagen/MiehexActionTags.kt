package cn.xm1221.miehex.datagen

import at.petrak.hexcasting.api.casting.ActionRegistryEntry
import at.petrak.hexcasting.api.mod.HexTags
import at.petrak.hexcasting.common.lib.HexRegistries
import cn.xm1221.miehex.registry.RegistrarEntry
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.TagsProvider
import java.util.concurrent.CompletableFuture

// see also: https://github.com/FallingColors/HexMod/blob/871f9387a3e1ccf0231a3e90c31e5d8472d46fde/Common/src/main/java/at/petrak/hexcasting/datagen/tag/HexActionTagProvider.java#L18
// (ignore the "ersatzActionTag" part, it doesn't seem to be necessary anymore)
class MiehexActionTags(
    output: PackOutput,
    provider: CompletableFuture<HolderLookup.Provider>,
) : TagsProvider<ActionRegistryEntry>(output, HexRegistries.ACTION, provider) {
    override fun addTags(provider: HolderLookup.Provider) {
        // per-world great spells / 需要启明的图案。
        // 重构前本模组用 data/hexcasting/tags/**（1.20.1 旧路径）手工提供这三张表；
        // HexDummy 的 datagen 会生成 1.20.1 新路径，故此处保留生成器但暂不列条目，
        // 需要时把对应的 RegistrarEntry<ActionRegistryEntry> 放进下面的数组即可。
        for (entry in arrayOf<RegistrarEntry<ActionRegistryEntry>>()) {
            tag(HexTags.Actions.CAN_START_ENLIGHTEN).add(entry.key)
            tag(HexTags.Actions.PER_WORLD_PATTERN).add(entry.key)
            tag(HexTags.Actions.REQUIRES_ENLIGHTENMENT).add(entry.key)
        }
    }
}
