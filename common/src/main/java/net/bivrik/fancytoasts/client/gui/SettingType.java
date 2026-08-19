package net.bivrik.fancytoasts.client.gui;

import net.bivrik.fancytoasts.client.gui.screen.ToastConfigScreen;
import net.bivrik.fancytoasts.client.registry.AnimationRegistry;
import net.bivrik.fancytoasts.client.registry.TextureRegistry;
import net.bivrik.fancytoasts.client.toast.DisplayData;
import net.bivrik.fancytoasts.core.Constants;
import net.bivrik.fancytoasts.platform.utility.Components;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public enum SettingType {
    TEXTURES("textures") {
        @Override
        public void apply(ToastConfigScreen instance, Identifier id) {
            instance.getConfigData().setTextureId(id);
        }

        @Override
        public DisplayData getDisplayData(Identifier id) {
            return TextureRegistry.getData(id);
        }

        @Override
        public Identifier getCurrentId(ToastConfigScreen instance) {
            return instance.getConfigData().getTextureId();
        }

        @Override
        public Identifier[] getKeySet() {
            return TextureRegistry.getIds().toArray(new Identifier[0]);
        }
    },
    ANIMATIONS("animations") {
        @Override
        public void apply(ToastConfigScreen instance, Identifier id) {
            instance.getConfigData().setAnimationId(id);
        }

        @Override
        public DisplayData getDisplayData(Identifier id) {
            return AnimationRegistry.getData(id);
        }

        @Override
        public Identifier getCurrentId(ToastConfigScreen instance) {
            return instance.getConfigData().getAnimationId();
        }

        @Override
        public Identifier[] getKeySet() {
            return AnimationRegistry.getIds().toArray(new Identifier[0]);
        }
    },
    SOUNDS("sounds") {
        @Override
        public void apply(ToastConfigScreen instance, Identifier id) {
            instance.getConfigData().putSoundIdForType(id, instance.getAdvancementType());
        }

        @Override
        public DisplayData getDisplayData(Identifier id) {
            DisplayData data;

            String name = id.toLanguageKey();
            if (id.getNamespace().equals(Constants.Compatibilities.MINECRAFT_ID)) {
                data = new DisplayData(name, "Minecraft", Components.stringOf("toast.sound.minecraft.description"), false);
            } else if (BuiltInRegistries.SOUND_EVENT.containsKey(id)) {
                data = new DisplayData(name, id.getNamespace(), Components.stringOf("toast.sound.mod.description"), false);
            } else {
                data = new DisplayData(name, id.getNamespace(), Components.stringOf("toast.sound.resourcepack.description"), false);
            }

            return data;
        }

        @Override
        public Identifier getCurrentId(ToastConfigScreen instance) {
            return instance.getConfigData().getSoundIdByType(instance.getAdvancementType());
        }

        @Override
        public Identifier[] getKeySet() {
            return Minecraft.getInstance().getSoundManager().getAvailableSounds().toArray(new Identifier[0]);
        }
    };

    public abstract void apply(ToastConfigScreen instance, Identifier entry);
    public abstract DisplayData getDisplayData(Identifier id);
    public abstract Identifier getCurrentId(ToastConfigScreen instance);
    public abstract Identifier[] getKeySet();

    private final String name;

    SettingType(String name) {
        this.name = name;
    }

    public Component getDisplayName() {
        return Components.of("gui." + name);
    }

    public String getName() {
        return name;
    }
}
