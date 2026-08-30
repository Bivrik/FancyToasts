package net.bivrik.fancytoasts.client.sound;

import net.minecraft.client.resources.sounds.AbstractSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;

public class UISoundInstance extends AbstractSoundInstance {
    private UISoundInstance(Identifier location, SoundSource source, RandomSource random, float volume, float pitch) {
        super(location, source, random);
        this.volume = volume;
        this.pitch = pitch;
        this.attenuation = Attenuation.NONE;
    }

    public static UISoundInstance create(SoundEvent sound, float volume, float pitch) {
        return new UISoundInstance(sound.location(), SoundSource.MASTER, SoundInstance.createUnseededRandom(), volume, pitch);
    }
}
