# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/).

## 1.0.2.1+0.11.4

### Fixed

- 修复 Forge 端启动崩溃：注册表写入改由 `RegisterEvent` 承担（Forge 在 mod 构造阶段
  就已冻结注册表，直接 `Registry.register` 会抛 `Can not register to a locked registry`）。
- 修正 `mods.toml` 里 `cloth_config` 的版本范围（原为 `[null,)`，现随版本目录正确展开）。
- 移除 `mods.toml` 中的 `mixinextras` 依赖声明 —— 它是随 jar 内嵌的库、不注册 modid，
  Forge 的 ModSorter 必然报 `[MISSING]`。

### Changed

- 删除失效的 `LivingEntityAttributeMixin` 与 `MieHexAttributes`：其 `mob_media` /
  `mob_ambit_radius` 两个属性除该 mixin 外无任何引用，成品动作读取的是原版属性。
- 删除未使用的 KubeJS 运行时物品注册入口（Forge 无法在脚本运行期注册物品）。
- `IdeaIota` 显示改用 `minecraft:alt` 字体。

## 1.0.2+0.11.4

### Added

- Initial version.
