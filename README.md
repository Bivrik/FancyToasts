> This is 1.21.10 Fabric/Forge/NeoForge version 1.4.4

# Fancy Toasts 🎉
There is a reason why you are here. It might be curiosity. But I think you are here because of there old-fashion advancement toasts! This is a Minecraft Mod that changes plain, boring, and annoying advancement toasts to something new and interesting!

<img src="https://cdn.modrinth.com/data/eUziWqPC/images/4906580b5c9307549e5f6ff80306f36b49f28a8f.gif" alt="Showcase of Standard Animation" width="400"/> <img src="https://cdn.modrinth.com/data/eUziWqPC/images/a33770e45d281106e9534ce83cb68de86d528f59.gif" alt="Showcase of Playful Animation" width="400"/>

### Download
Try it out on [CurseForge](https://www.curseforge.com/minecraft/mc-mods/fancy-toasts) or [Modrinth](https://modrinth.com/mod/fancy-toasts). Other sources are not mine.

# CUSTOMIZATION 🤪

### Custom Textures
This mods supports custom textures! Yes, you can add textures from friends, modpcaks, or create your own! It's as easy as just drawing a texture. To do so, you can follow [this guide](https://github.com/Bivrik/FancyToasts/wiki/Custom-Texture-Guide). With this, possibilities of customization are endless! If you just want to add existing texture, check out this [page](https://github.com/Bivrik/FancyToasts/wiki/Custom-Texture-Guide#using).

### Custom Config Screen
It's not only about visuals, it's also about conveniences. It adds new screens for configuration. You can change textures, animations and sounds easily, along side with more tehnical settings, such as volumes, compatibilities and etc!

# Description 🏗️
Adds new advancement toast system into Minecraft. Currently mod has 24 standard toast variants, because of 4 animation styles and 6 texture types. Supports config screen on Fabric (Mod Menu)/Forge/NeoForge

### Animations
* Standard
* Playful
* Quirky
* Old-Like

### Textures
* Vanilla-Like
* Nature
* OG
* Modern
* Terracraft
* Steamy

# Other 🔎

### Support 🧡
If you like my mod, you play it, and you want to see future development, hear my thoughts, get early access, or even see devlogs, then visit my [Boosty](https://boosty.to/bivrik) and support me, whether it's a one-time donation or a subscription. I will be very thankful! 🤗

### Languages
| Language              | Key     | Made by      |
|-----------------------|---------|--------------|
| English               | `en_us` |              |
| Russian               | `ru_ru` |              |
| Spanish               | `es_es` | `translator` |
| German                | `de_de` | `translator` |
| Portuguese            | `pt_br` | `translator` |
| Chinese (Simplified)  | `zh_cn` | Gao Xinyang  |
| Chinese (Traditional) | `zh_tw` | ChaTian      |
| Japanese              | `ja_jp` | PExPE3       |

### Plans
Big goals/plans/ideas for future updates. Some of them are abstract ideas, and some are more specific 

* More languages
* More animations (goal: 4/5)
* More textures (goal: 6/10)
* Wide version support
  * **Currently maintaining**

    | Minecraft version | Mod version       |
    |-------------------|-------------------|
    | Latest (1.21.10)  | 1.4.3             |
    | 1.21.1            | 1.4.3             |
    | 1.20.1            | 1.4.3             |
  * **Planned with priorities**

    | Minecraft version | Port priority |
    |-------------------|---------------|
    | 1.19.4            | High          |
    | 1.16.5            | Medium        |
    | 1.18.2            | Low           |
    | 1.17.1            | Low           |
* Rework of animations to make them easier to make?
* Personalized textures for specific mods/datapacks?
* Custom sounds? (v1.5.x?)
* FTBQuests compatibility? (v1.4.x?)
#### Completed
* Config GUI rework
* Wider support for sounds (mods, resourcepacks)
* Ability to change sounds, position, render type
* Custom textures
* Jade compatibility

# Changelog 📝

## v1.4.4
* New features:
  * Community "Credits" screen! For everyone who supported or contributed!
  * Filtering! Easier way to find sounds, textures and animations
  * Config button inside key-bindings! No actual need to install extra mods to access config screen (got an idea because of pupcakie)
  * Animation type `Old-Like` (suggested by Redls07)
  * Traditional Chinese language by ChaTian (`zh_tw`)
  * Portuguese language by translator (`pt_br`)
* Bug fixes:
  * UI highlight is being rendered beyond selection list
  * More robust render checking, because of crash with some other mods (found by LiterallyLink)
  * Filtering not working properly
* Adjustments:
  * Compatibility buttons (with Jade) is now hidden, when Jade is not installed
  * Better UI indicator for easier understanding which texture/animation/sound is selected, and not (suggested by ttylmatt2)
  * Splashes!
  * Support for sounds from mods and resourcepacks instead of just Minecraft ones (suggested by F0rsakenPhant0M)
> I need to refactor it tho... Whatever, press UPDATE! (13.11.25)

## v1.4.3
* New features:
  * UI highlights. Now it's more visual appealing when selecting animation/texture/sound
  * `Modern` texture redraw!
  * Simplified Chinese language by Gao Xinyang (`zh_cn` instead of `zh_tw`)
* Bug fixes:
  * Toast becomes transparent/darken when opened chat or chat in bed
  * Custom texture loses its texture upon opening texture selection screen
  * Crash when there is no any custom texture in the folder
  * Some text not being rendered under `transparent` toast due to depth (1.21.1 and lower)
* Adjustments:
  * A lot of small refactor changes
  * More helper/utility classes for easier porting
  * ???
> More technical update for easier maintaining (09.10.25)

## v1.4.2
* New features:
  * Volume slider now changes by 1% instead of 10% (suggested by VaporeonScripts)
  * Ability to change toast's general position! Top left, top center or top right
  * Ability to change toast's render type when any screen is opened (chests, invetory, etc). Top, behind or transparent (suggested by guguz)
* Bug fixes:
  * Doesn't show toast when installed BadOptimizations mod
* Adjustments:
  * Toast system code overhaul, which is slightly more optimizied (I hope)
  * Slight optimization when rendering toasts
> I wanted to call it Update Nobody Asked For, but there are a lot of community suggestions, actually (28.09.25)

## v1.4.1
* New features:
  * Complete config screen overhaul! Now there are 3 screens, which makes it more user-friendly. Also much more options, descriptions, authors, searching... just check it out!
  * Ability to change sounds! Just as quite a lot of people asked for. Now you can choose from any Minecraft built-in sounds
  * Ability to change Jade compatibilty. As a few people suggested, were added a button to disable Jade hiding upon advancement toast showing
  * Splashes. It isn't about why. It's about why not
* Bug fixes:
  * Crashes when title/description of advancement is empty. No idea why it happens... but it fixed now
  * Doesn't load custom textures
  * Sometimes text doesn't appear when toast shows
* Adjustments:
  * Added Boosty button in the main config screen
  * Changed config system
> User-friendly gui screen update, baby (13.09.25)

## v1.4.0
* New features:
  * Support for custom textures in configs! Create your own textures using simple template and json file!
  * Texture type `Steamy`
  * Texture type `Terracraft`
  * Animation type `Quirky`
  * Jade compatibility! Now jade display hides, when advancement is shown
  * Japanese language by PExPE3 (`ja_jp`)
* Bug fixes:
  * Sometimes delyaed sound
* Adjustments:
  * Updated configs (not the screen)
  * Changed registration system and half of the code in general
> Yet biggest update with custom texture system (26.08.25)

## v1.3.3
* New features:
  * Texture type `Modern`
* Bug fixes:
  * NeoForge crashes when open config screen
  * Forge looses mixins after build
* Adjustments:
  * Changed slightly code for easier version changes and multiloader support
> Port to Fabric/Forge/NeoForge (18.08.25)

## v1.3.2
* Bug fixes:
  * Wrong audio delaying due to pause menu
* Adjustments:
  * Changed links
> Bug fixes (14.08.25)

## v1.3.1
> Initial release on Fabric (13.08.25)
