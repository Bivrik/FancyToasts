package net.bivrik.fancytoasts.client.gui;

import net.bivrik.fancytoasts.core.Constants;
import net.bivrik.fancytoasts.client.gui.screen.ToastConfigScreen;
import net.bivrik.fancytoasts.client.registry.AnimationRegistry;
import net.bivrik.fancytoasts.client.registry.TextureRegistry;
import net.bivrik.fancytoasts.client.toast.DisplayData;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public enum SettingType {
    TEXTURES("textures") {
        @Override
        public void apply(ToastConfigScreen instance, ResourceLocation id) {
            instance.getConfigData().setTextureId(id);
        }

        @Override
        public DisplayData getDisplayData(ResourceLocation id) {
            return TextureRegistry.getData(id);
        }

        @Override
        public ResourceLocation getCurrentId(ToastConfigScreen instance) {
            return instance.getConfigData().getTextureId();
        }

        @Override
        public ResourceLocation[] getKeySet() {
            return TextureRegistry.getIds().toArray(new ResourceLocation[0]);
        }
    },
    ANIMATIONS("animations") {
        @Override
        public void apply(ToastConfigScreen instance, ResourceLocation id) {
            instance.getConfigData().setAnimationId(id);
        }

        @Override
        public DisplayData getDisplayData(ResourceLocation id) {
            return AnimationRegistry.getData(id);
        }

        @Override
        public ResourceLocation getCurrentId(ToastConfigScreen instance) {
            return instance.getConfigData().getAnimationId();
        }

        @Override
        public ResourceLocation[] getKeySet() {
            return AnimationRegistry.getIds().toArray(new ResourceLocation[0]);
        }
    },
    SOUNDS("sounds") {
        @Override
        public void apply(ToastConfigScreen instance, ResourceLocation id) {
            instance.getConfigData().putSoundIdForType(id, instance.getAdvancementType());
        }

        @Override
        public DisplayData getDisplayData(ResourceLocation id) {
            DisplayData data;

            if (id.toLanguageKey().contains("minecraft")) {
                data = new DisplayData(id.toLanguageKey(), "Minecraft", Constants.MOD_ID + ".sound.minecraft.description", false);
            }
            else if (BuiltInRegistries.SOUND_EVENT.containsKey(id)) {
                data = new DisplayData(id.toLanguageKey(), id.getNamespace(), Constants.MOD_ID + ".sound.mod.description", false);
            }
            else {
                data = new DisplayData(id.toLanguageKey(), id.getNamespace(), Constants.MOD_ID + ".sound.resource_pack.description", false);
            }

            return data;
        }

        @Override
        public ResourceLocation getCurrentId(ToastConfigScreen instance) {
            return instance.getConfigData().getSoundIdByType(instance.getAdvancementType());
        }

        @Override
        public ResourceLocation[] getKeySet() {
            return Minecraft.getInstance().getSoundManager().getAvailableSounds().toArray(new ResourceLocation[0]);
        }
    };

    public abstract void apply(ToastConfigScreen instance, ResourceLocation entry);
    public abstract DisplayData getDisplayData(ResourceLocation id);
    public abstract ResourceLocation getCurrentId(ToastConfigScreen instance);
    public abstract ResourceLocation[] getKeySet();

    private final String name;

    SettingType(String name) {
        this.name = name;
    }

    public Component getDisplayName() {
        return Component.translatable("fancytoasts.gui.label." + name);
    }

    public String getName() {
        return name;
    }
}
