package net.bivrik.fancytoasts.client.gui;

import net.bivrik.fancytoasts.Constants;
import net.bivrik.fancytoasts.client.toast.registry.ToastAnimationRegistry;
import net.bivrik.fancytoasts.client.toast.registry.ToastTextureRegistry;
import net.bivrik.fancytoasts.client.toast.texture.ToastTextureData;
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
        ToastTextureData getDisplayData(ResourceLocation id) {
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
        ToastTextureData getDisplayData(ResourceLocation id) {
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
        ToastTextureData getDisplayData(ResourceLocation id) {
            return new ToastTextureData(Component.translatable(id.toLanguageKey()), "Minecraft", Component.translatable(Constants.MOD_ID + ".sound.minecraft.description"));
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
    abstract ToastTextureData getDisplayData(ResourceLocation id);
    abstract ResourceLocation getCurrentId(ToastConfigScreen instance);
    abstract ResourceLocation[] getKeySet();

    private final String name;

    SettingType(String name) {
        this.name = name;
    }

    public static Component getDisplayName(SettingType type) {
        return Component.translatable("fancytoasts.gui.label." + type.name);
    }
}
