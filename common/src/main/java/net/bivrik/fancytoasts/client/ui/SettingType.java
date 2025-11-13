package net.bivrik.fancytoasts.client.ui;

import net.bivrik.fancytoasts.client.gui.ToastConfigScreen;
import net.bivrik.fancytoasts.client.toast.ToastAnimationRegistry;
import net.bivrik.fancytoasts.client.toast.ToastTextureRegistry;
import net.bivrik.fancytoasts.client.toast.texture.DisplayData;
import net.bivrik.fancytoasts.platform.utility.Components;
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
            return ToastTextureRegistry.getData(id);
        }

        @Override
        public ResourceLocation getCurrentId(ToastConfigScreen instance) {
            return instance.getConfigData().getTextureId();
        }

        @Override
        public ResourceLocation[] getKeySet() {
            return ToastTextureRegistry.getIds().toArray(new ResourceLocation[0]);
        }
    },
    ANIMATIONS("animations") {
        @Override
        public void apply(ToastConfigScreen instance, ResourceLocation id) {
            instance.getConfigData().setAnimationId(id);
        }

        @Override
        public DisplayData getDisplayData(ResourceLocation id) {
            return ToastAnimationRegistry.getData(id);
        }

        @Override
        public ResourceLocation getCurrentId(ToastConfigScreen instance) {
            return instance.getConfigData().getAnimationId();
        }

        @Override
        public ResourceLocation[] getKeySet() {
            return ToastAnimationRegistry.getIds().toArray(new ResourceLocation[0]);
        }
    },
    SOUNDS("sounds") {
        @Override
        public void apply(ToastConfigScreen instance, ResourceLocation id) {
            instance.getConfigData().putSound(instance.getAdvancementType(), id);
        }

        @Override
        public DisplayData getDisplayData(ResourceLocation id) {
            DisplayData data;

            if (id.toLanguageKey().contains("minecraft")) {
                data = new DisplayData(Component.literal(id.toLanguageKey()), "Minecraft", Components.of("sound.minecraft.description"));
            }
            else if (BuiltInRegistries.SOUND_EVENT.containsKey(id)) {
                data = new DisplayData(Component.literal(id.toLanguageKey()), getAuthor(id.getNamespace()), Components.of("sound.mod.description"));
            }
            else {
                data = new DisplayData(Component.literal(id.toLanguageKey()), getAuthor(id.getNamespace()), Components.of("sound.resource_pack.description"));
            }

            return data;
        }

        private String getAuthor(String namespace) {
            var end = namespace.substring(1);
            var start = namespace.substring(0, 1).toUpperCase();
            return start + end;
        }

        @Override
        public ResourceLocation getCurrentId(ToastConfigScreen instance) {
            return instance.getConfigData().getSoundId(instance.getAdvancementType());
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
