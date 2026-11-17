package net.bivrik.fancytoasts.client.sound;

import net.minecraft.client.resources.sounds.AbstractSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;

public class UiSoundInstance extends AbstractSoundInstance {
    private static final SoundSource UI_SOUND_SOURCE = SoundSource.MASTER;

    private UiSoundInstance(ResourceLocation location, float volume, float pitch) {
        super(location, UI_SOUND_SOURCE, SoundInstance.createUnseededRandom());
        this.volume = volume;
        this.pitch = pitch;
        this.looping = false;
        this.relative = true;
        this.attenuation = Attenuation.NONE;
    }

    public static UiSoundInstance create(SoundEvent sound, float volume, float pitch) {
        return new UiSoundInstance(sound.getLocation(), volume, pitch);
    }

    public static UiSoundInstance create(SoundEvent soundEvent, float volume) {
        return new UiSoundInstance(soundEvent.getLocation(), volume, 1.0f);
    }
}
