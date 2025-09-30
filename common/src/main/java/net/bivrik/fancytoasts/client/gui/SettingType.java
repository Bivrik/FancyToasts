package net.bivrik.fancytoasts.client.gui;

import net.bivrik.fancytoasts.Constants;
import net.bivrik.fancytoasts.client.toast.ToastAnimationRegistry;
import net.bivrik.fancytoasts.client.toast.ToastTextureRegistry;
import net.bivrik.fancytoasts.client.toast.texture.DisplayData;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public enum SettingType {
    TEXTURES("textures") {
        @Override
        void apply(ToastConfigScreen instance, ResourceLocation id) {
            instance.getConfigData().setTextureId(id);
        }

        @Override
        DisplayData getDisplayData(ResourceLocation id) {
            return ToastTextureRegistry.getData(id);
        }

        @Override
        ResourceLocation getCurrentId(ToastConfigScreen instance) {
            return instance.getConfigData().getTextureId();
        }

        @Override
        ResourceLocation[] getKeySet() {
            return ToastTextureRegistry.getIds().toArray(new ResourceLocation[0]);
        }
    },
    ANIMATIONS("animations") {
        @Override
        void apply(ToastConfigScreen instance, ResourceLocation id) {
            instance.getConfigData().setAnimationId(id);
        }

        @Override
        DisplayData getDisplayData(ResourceLocation id) {
            return ToastAnimationRegistry.getData(id);
        }

        @Override
        ResourceLocation getCurrentId(ToastConfigScreen instance) {
            return instance.getConfigData().getAnimationId();
        }

        @Override
        ResourceLocation[] getKeySet() {
            return ToastAnimationRegistry.getIds().toArray(new ResourceLocation[0]);
        }
    },
    SOUNDS("sounds") {
        @Override
        void apply(ToastConfigScreen instance, ResourceLocation id) {
            instance.getConfigData().putSound(instance.getAdvancementType(), id);
        }

        @Override
        DisplayData getDisplayData(ResourceLocation id) {
            return new DisplayData(Component.translatable(id.toLanguageKey()), "Minecraft", Component.translatable(Constants.MOD_ID + ".sound.minecraft.description"));
        }

        @Override
        ResourceLocation getCurrentId(ToastConfigScreen instance) {
            return instance.getConfigData().getSoundId(instance.getAdvancementType());
        }

        @Override
        ResourceLocation[] getKeySet() {
            return BuiltInRegistries.SOUND_EVENT.keySet().toArray(new ResourceLocation[0]);
        }
    };

    abstract void apply(ToastConfigScreen instance, ResourceLocation entry);
    abstract DisplayData getDisplayData(ResourceLocation id);
    abstract ResourceLocation getCurrentId(ToastConfigScreen instance);
    abstract ResourceLocation[] getKeySet();

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
