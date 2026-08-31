# Changelog

## 1.5.1
### Bug fixes
* Fixed key bindings not saving properly, now it's 100% robust
### Adjustments
* Sounds from completing advancements are no longer shown in subtitles since it's UI
* Categories in credits screen now has fallback to visual appealing string, if there is no translation
> How did I mess up key bindings?! (30.08.26)

## 1.5.0
### New features
* Finer FTB Quests control (implemented by Furglitch)
* Better handling of title/description/announcement and icons
* Added ability to choose sounds for each type (only through `toast.json`)
* Added ability to ignore quest types (only through `toast_filtering.json`)
* Questlog support (suggested by FlowStudios)
* All quests handled like `TASK` since there are no difference in quests
* Sounds play as intended upon completing quest
### Bug fixes
* NeoForge crashes under mysterious occasions because of keybinds
* Text alpha flickering on toasts sometimes
* Render type Behind flickering when any screen is opened with Gnetum (found by Fyoncle)
* Wrong sound played during fade phase of every animation (UI_TOAST_IN -> UI_TOAST_OUT)
### Adjustments
* Changed managers system to be more robust and use less memory
* Animations now tick based
* Updated logo
* If description of toast display is missing, it now always shows announcement/title
* Removed filtering of vanilla toasts
* Updated Credits screen
* Added image link buttons in Fancy Toasts screen
* Clearer config logging
* Tweaked `Vanilla`, `Neon`, `Steamy`, and `OG` texture types
* Tweaked `Quirky` and `Vanilla-Like` animation types
* Changed archive name to `mod_id-mod_version-loader-mc_version` pattern
> Just a lot of changes after a while (25.08.26)

### 1.4.7
### New features
* Ability to change type of announcement for toasts instead of just hard-codded ones (title/description/announcement)
* Aether overridden sounds compatibility (suggested by TwoBluDogs)
* The Dawn Era advancement icons compatibility (suggested by SundGGs)
### Bug fixes
* Credits not being loaded due to daily cap (found by Fyoncle)
* Dots of wrapped text is now the same color as the text
### Adjustments
* Changed standard SFX for tasks (Note Block Chime sound instead of Allay)
* Now credits are being cached on client-side! Even without WI-FI connection they can be shown
* Settings rearrange so they are more comfortable to use
* Settings now use actual lists, so there is a slider
* Texture type `Vanilla-Like` was slightly tweaked to match vanilla feeling better
* Animation type `Standard` was slightly tweaked to match vanilla feeling better
* Slightly better optimization and new easing system
> QoL changes which I should've done much faster, oops. And also new GitHub page :D (25.04.26)

### 1.4.6
### New features
* Texture type `Neon`
* Setting to hide boss bar health during an advancement (suggested by capaMEC)
* Setting to tweak pitch difference for sfx (suggested by shbashi)
* Setting to multiply animation's speed
### Bug fixes
* Crash on Linux and MacOS with custom textures (found by Furglitch & Fyoncle)
* Issue with texture packs that add unicode emojis (found by Fyoncle)
* More stable animation timings
### Adjustments
* Texture type `Landspaper` got a bit darker so it's easier to read
* Configs versioning system tweaked (?)
> A Jack of all trades is master of none. Got carried away and scraped 1.5.0 update for future, duh (04.02.26)

### 1.4.5.1
### Bug fixes
* Memory leak due to lack of unsubscribing from events (sorry)
* FTBQuests compatibility:
  * Quest not being rendered if there are multiple icons
  * Custom textures causes just empty icon frame (now by default there is a fallback as a `Quest Book` for icon)
### Adjustments
* Updated Simplified Chinese language by Gao Xinyang (`zh_cn`)
* FTBQuests compatibility:
  * There is now 3-minute window between repeatable quests, so they don't spam the screen
* Default positions for different anchors (suggested by Redls07)
* Inverted `y` position (`50` is now `-50`), because it just makes more sense
* New config version system (for easier migrating in the future, so configs don't reset to default when updated version)

### v1.4.5
### New features
* Advancements filtering! Can be changed types of advancements, toasts and even specific advancement or whole categories to enable/disable
* FTBQuests support! Now quests from FTBQuests can be seen on the screen in new fancy way!
  * Announcement text -> Quest's announcement
  * Title text -> Quest's title
  * Description text -> Quest's subtitle (fallback: toast's announcement)
* Texture type `Landspaper`
* Toast advanced positioning. It supports anchor system with offsets relative to this anchor
* Customizable loops. Now strength and speed of them (sin/cos waves) can be changed
### Bug fixes
* Key bindings don't register sometimes
* Credits' vignette offset
* 1.21.1 and lower: tooltips not being rendered properly under toast with `transparent` setting
* 1.21.1 and lower: double click sound when opening a screen with sliders
* EMI compatibility issue
* Badoptimizations compatibility issue
* Too dark credits screen list
### Adjustments
* Total refactor. More robust loadings, easier maintain, and less future compat issues (hopefully)
* Pitch difference for Advancement Toast sounds
* Toasts sound timing tweaked
* Custom textures are now loaded until the last toast using it is shown. Prevents missing textures and memory leaks
* Splashes
* Settings screens aren't closed after saving. It's for easier tweaking of toasts' position and etc.
* Better visual settings UI
* Better translations (in term of keys, and maybe slightly improved English version)
* Toast Control won't disable Fancy Toasts anymore, but they still conflict
> I'm gonna need a rest after this... huh, a rest? (30.11.25)

### v1.4.4.1
### Bug fixes
* Key bindings can't be changed on Forge/NeoForge
* Render type `behind` quirks a bit

### v1.4.4
### New features
* Community "Credits" screen! For everyone who supported or contributed!
* Filtering! Easier way to find sounds, textures and animations
* Config button inside key-bindings! No actual need to install extra mods to access config screen (got an idea because of pupcakie)
* Animation type `Old-Like` (suggested by Redls07)
* Traditional Chinese language by ChaTian (`zh_tw`)
* Portuguese language by translator (`pt_br`)
### Bug fixes
* UI highlight is being rendered beyond selection list
* More robust render checking, because of crash with some other mods (found by LiterallyLink)
* Filtering not working properly
### Adjustments
* Compatibility buttons (with Jade) is now hidden, when Jade is not installed
* Better UI indicator for easier understanding which texture/animation/sound is selected, and not (suggested by ttylmatt2)
* Splashes connected to ARG!
* Support for sounds from mods and resourcepacks instead of just Minecraft ones (suggested by F0rsakenPhant0M)
* By default Render Type is set to `Transparent` instead of `Top`
> I need to refactor it tho... Whatever, press UPDATE! (13.11.25)

### v1.4.3.1
### Bug fixes
* Fabric version not working
* Crash with Paladin's Furniture
### Adjustments
* More robust rendering system

### v1.4.3
### New features
* UI highlights. Now it's more visual appealing when selecting animation/texture/sound
* `Modern` texture redraw!
* Simplified Chinese language by Gao Xinyang (`zh_cn` instead of `zh_tw`)
### Bug fixes
* Toast becomes transparent/darken when opened chat or chat in bed
* Custom texture loses its texture upon opening texture selection screen
* Crash when there is no any custom texture in the folder
* Some text not being rendered under `transparent` toast due to depth (1.21.1 and lower)
### Adjustments
* A lot of small refactor changes
* More helper/utility classes for easier porting
* ???
> More technical update for easier maintaining (09.10.25)

### v1.4.2
### New features
* Volume slider now changes by 1% instead of 10% (suggested by VaporeonScripts)
* Ability to change toast's general position! Top left, top center or top right
* Ability to change toast's render type when any screen is opened (chests, invetory, etc). Top, behind or transparent (suggested by guguz)
### Bug fixes
* Doesn't show toast when installed BadOptimizations mod
### Adjustments
* Toast system code overhaul, which is slightly more optimizied (I hope)
* Slight optimization when rendering toasts
> I wanted to call it Update Nobody Asked For, but there are a lot of community suggestions, actually (28.09.25)

### v1.4.1
### New features
* Complete config screen overhaul! Now there are 3 screens, which makes it more user-friendly. Also much more options, descriptions, authors, searching... just check it out!
* Ability to change sounds! Just as quite a lot of people asked for. Now you can choose from any Minecraft built-in sounds
* Ability to change Jade compatibilty. As a few people suggested, were added a button to disable Jade hiding upon advancement toast showing
* Splashes. It isn't about why. It's about why not
### Bug fixes
* Crashes when title/description of advancement is empty. No idea why it happens... but it fixed now
* Doesn't load custom textures
* Sometimes text doesn't appear when toast shows
### Adjustments
* Added Boosty button in the main config screen
* Changed config system
> User-friendly gui screen update, baby (13.09.25)

### v1.4.0
### New features
* Support for custom textures in configs! Create your own textures using simple template and json file!
* Texture type `Steamy`
* Texture type `Terracraft`
* Animation type `Quirky`
* Jade compatibility! Now jade display hides, when advancement is shown
* Japanese language by PExPE3 (`ja_jp`)
### Bug fixes
* Sometimes delyaed sound
### Adjustments
* Updated configs (not the screen)
* Changed registration system and half of the code in general
> Yet biggest update with custom texture system (26.08.25)

### v1.3.3
### New features
* Texture type `Modern`
### Bug fixes
* NeoForge crashes when open config screen
* Forge looses mixins after build
### Adjustments
* Changed slightly code for easier version changes and multiloader support
> Port to Fabric/Forge/NeoForge (18.08.25)

### v1.3.2
### Bug fixes
* Wrong audio delaying due to pause menu
### Adjustments
* Changed links
> Bug fixes (14.08.25)

### v1.3.1
> Initial release on Fabric (13.08.25)