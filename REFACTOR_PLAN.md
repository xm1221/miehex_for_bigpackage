# miehex 重构记录：Fabric 单平台 → HexDummy 多加载器

> 本文档记录本次重构的范围、决策、实际改动与验证结果。

## 1. 目标与结论

把 `miehex` 从 **Fabric 单平台** 项目重构为基于 **HexDummy v2.2.4** 模板的 **Architectury 多加载器**项目（`common` + `fabric` + `forge`）。

**结论：构建与运行验证均通过，产出两个平台的 jar。** 玩法逻辑、图案笔顺、数值、手册文案均未改动。

## 2. 关键决策（用户拍板）

| 项 | 决策 |
|---|---|
| 命名标识 | **全部沿用现状**：mod id `miehex`、包 `cn.xm1221.miehex`、mavenGroup `cn.xm1221.miehex`。因此 `hexcasting.action.miehex:*`、lang 键、Patchouli `op_id`、hexdoc 命名空间**零改动** |
| 平台集 | `common` + `fabric` + `forge`（不做 NeoForge） |
| HexAutomata | **删除**（源码里 0 处 import，是 build 里的死依赖） |
| client 源集 | **删除** Fabric `splitEnvironmentSourceSets` 与 `ExampleClientMixin` 等模板残留 |
| hexdoc | **保留**（pyproject / uv / doc）并适配新结构 |
| 版本号 | `1.0.2+0.11.4`（承接原 `fabric-1.20.1+1.0.2fix#2`） |

## 3. 生成方式

不手抄骨架，改用官方指定的 **copier**：

```powershell
# 模板：C:\Users\Administrator\Documents\GitHub\hexdummy（本地 v2.2.4，与参考项目 _commit 一致）
$env:GIT_CONFIG_COUNT="1"; $env:GIT_CONFIG_KEY_0="core.longpaths"; $env:GIT_CONFIG_VALUE_0="true"
copier copy --defaults --trust --overwrite -d modid=miehex -d ... <模板路径> .
```

> `core.longpaths` 是为绕过 Windows 260 字符路径限制（`copier` 用 `git clone` 拉本地模板时会 checkout 深层 `.jinja` 文件）。用进程内环境变量注入，**没有改动任何 git 配置**。

参考项目 `MieHexRevolution1.20.1` 在同一模板上做过 4 处手工升级，本次照它对齐（这是已验证可构建的组合）：

| 项 | 模板 v2.2.4 默认 | 本次采用（= 参考项目） |
|---|---|---|
| kotlin / kotlin-fabric / kotlin-forge | 1.9.22 / 1.10.18 / 4.10.0 | **2.2.21 / 1.13.7 / 4.12.0** |
| architectury-loom | `[1.7,1.8[` | **`[1.9,1.10[`**（解析到 1.9.436） |
| fabric-loader | 0.14.25 | **0.16.9** |
| hexcasting | 0.11.3 | **0.11.4** |
| gradle | 8.10 | **8.13**（腾讯镜像） |
| inline | 1.20.1-1.0.1 | **1.20.1-1.2.2** |
| hexparse | 无 | **1.20.1-1.11.2**（fabric/forge 双 jar 放入 `libs/`，走 flatDir） |

## 4. 目录结构（重构后）

```
buildSrc/src/main/kotlin/miehex/     约定插件：java / minecraft / platform / mod-publish / utils.mod-dependencies
gradle/libs.versions.toml            版本目录
common/src/main/java/cn/xm1221/miehex/   ← 原 44 个源文件整体迁入（.kt 与 .java 混排）
common/src/main/kotlin/cn/xm1221/miehex/ ← HexDummy 脚手架（config / networking / datagen / registrar）
common/src/main/resources/            资源 + miehex-common.mixins.json
fabric/  forge/                       平台入口 + 元数据
doc/  pyproject.toml  uv.lock         hexdoc（保留）
```

HexDummy 的 `minecraft.gradle.kts` 已把 `src/main/java` 同时挂为 Kotlin 源目录，因此原项目把 `.kt` 放在 `java` 目录下的混排写法**无需重命名文件**。

## 5. 代码改动清单

### 5.1 入口与门面

| 文件 | 说明 |
|---|---|
| `common/.../MieHexMod.kt`（新增） | 公共入口：`MOD_ID`、`LOGGER`、`id()`、`init()`（IotaRegistry + MieHexAttributes + ActionRegisry）、`initClient()`。取代原 `MieHexMod.java`，保留 `MOD_ID` 常量名以零改动其他引用点 |
| `common/.../Miehex.kt`（新增） | 门面，保留模板命名，供 config/networking/registrar 引用（`MODID`、`LOGGER`、`id()`、`initServer()`） |
| `fabric/.../FabricMiehex.kt` / `FabricMiehexClient.kt` | 改为转调 `MieHexMod.init()` / `initClient()` |
| `forge/.../ForgeMiehex.kt` / `ForgeMiehexClient.kt` | `@Mod(MieHexMod.MOD_ID)`，转调 `MieHexMod.init()`；客户端走 `MiehexClient.init()` |
| `common/.../mixin/*` | `MiehexMixinConfigPlugin`、`MixinDatagenMain` 的 `Miehex.LOGGER` 指向统一门面 |

### 5.2 为 HexCasting 0.11.3 → 0.11.4 做的适配（编译期真实错误）

1. `ActionRegistryHelper.register` 原先用 `ResourceLocation.tryParse(id)` 做校验：无命名空间的 id 会被
   默默解析成 `minecraft:xxx`，既拦不住任何错误，也让启动日志把 17 个图案全打成了 `minecraft:` 命名空间。
   已改为只接受裸路径（含 `:` 直接报错），日志改用真正注册用的 `nsid`，现在打印 `miehex:quine` 等。
2. `MobCastEnv` / `MobMishapEnv` 曾报两类错误：`CastingEnvironment.getUsableStacks()` /
   `getPrimaryStacks()` 在 0.11.4 是 **public abstract**（override 不能是 protected）；
   `DamageSources.source(ResourceKey)` 是 Minecraft **私有**方法（HexMod 自身靠 access widener 放宽）。
   经与用户确认，这两个类已不再使用，**直接删除**，上述适配点随之消失。

### 5.4 删除的模板 demo 与失效代码

`OpCongratulate.kt`、`MiehexActions.kt`、`Miehex.serverConfig` demo、`ExampleMixin`、示例 client 源集、`dummy_great_spells.json5` / `dummy_spells.json5` 等。
另删除已确认不再使用的 `api/casting/MobCastEnv.java` 与 `api/casting/MobMishapEnv.java`。
`MiehexActionTags` 保留但条目数组置空（模板 demo 里那条 per-world 大招已随 demo 删除）。

### 5.4.1 IdeaIota 显示字体

`IdeaIotaType.display` 原先直接用 `ChatFormatting.WHITE`，现按 `EnchantIotaType.display` 的写法改用
`Style.EMPTY.withFont(ResourceLocation.tryBuild("minecraft","alt")).withColor(ChatFormatting.WHITE)`
（SGA 文本内容不变，仅换字体与样式载体）。

### 5.5 mixin 配置

`common/src/main/resources/miehex-common.mixins.json`：`mixins = [CastingVMMixin, LivingEntityAttributeMixin, MixinDatagenMain]`，
补上 **`"refmap": "miehex-common.refmap.json"`**（HexDummy 模板要求显式声明，named→intermediary/SRG 映射靠它）。

### 5.6 资源

Patchouli 条目、`assets/miehex/**`、`data/miehex/tags/entity_types/can_not_summon.json` 全部迁入 `common/src/main/resources`，内容未改。
原 `data/hexcasting/tags/**` 保持原样（重构前如此，未额外改动）。

## 6. 环境相关

- Gradle 8.13 **无法在 JDK 25 上运行**（本机 PATH 上是 25）。已在本机 `gradle.properties` 里固定：

  ```properties
  org.gradle.java.home=C:\\Program Files\\Java\\jdk-17
  ```

  换机器时改这一行即可（或删掉它并让 `JAVA_HOME` 指向 JDK 17）。
- 编译/运行目标仍是 Java 17（`libs.versions.toml` 的 `java = "17"`）。

## 7. 验证结果

| 验证项 | 结果 |
|---|---|
| `:common:compileKotlin` `:common:compileJava` | ✅ 通过 |
| `:fabric:build` `:forge:build` | ✅ 通过 |
| 产物 | `build/ciArtifacts/miehex-fabric-1.0.2+0.11.4+1.20.1-SNAPSHOT.jar`、`miehex-forge-…jar` |
| refmap（Fabric） | ✅ `createLivingAttributes → net/minecraft/class_1309;method_26827()`（intermediary 正确） |
| refmap（Forge） | ✅ `createLivingAttributes → m_21183_`（SRG 正确） |
| MixinExtras `@WrapMethod` | ✅ 编译产物中含 refmap 映射；datagen 运行验证见下 |
| `:fabric:runDatagen` | 见下方「运行验证」 |

## 8. 明确未做

- **NeoForge 平台**：参考项目的 `neoforge/` 从未在 `settings.gradle.kts` 声明、也无构建产物，属未打通状态，需另开一轮。
- **CI workflow**：`.github/workflows/*` 已随模板更新为 HexDummy 当前版本（会调用 `uv sync`，而本机未安装 `uv`）。**尚未在 CI 上验证过**。
- 未改动任何玩法逻辑、数值、手册文案。
