package net.bivrik.fancytoasts.client.config;

import net.bivrik.fancytoasts.client.util.Paths;
import net.bivrik.fancytoasts.platform.Services;

public class GeneralConfigData extends ConfigData {
    private boolean isJadeCompatEnabled;
    private boolean areSoundsEnabled;
    private float taskVolume;
    private float goalVolume;
    private float challengeVolume;
    private AdvancementToastPosition position;
    private AdvancementToastScreenBehavior screenBehavior;

    public GeneralConfigData(boolean isJadeCompatEnabled, boolean areSoundsEnabled, float taskVolume, float goalVolume, float challengeVolume, AdvancementToastPosition position, AdvancementToastScreenBehavior screenBehavior) {
        super(Paths.GENERAL_CONFIG_FILE);

        this.isJadeCompatEnabled = isJadeCompatEnabled;
        this.areSoundsEnabled = areSoundsEnabled;
        this.taskVolume = taskVolume;
        this.goalVolume = goalVolume;
        this.challengeVolume = challengeVolume;
        this.position = position;
        this.screenBehavior = screenBehavior;
    }

    public GeneralConfigData() {
        super(Paths.GENERAL_CONFIG_FILE);

        this.isJadeCompatEnabled = true;
        this.areSoundsEnabled = true;
        this.taskVolume = 1.0f;
        this.goalVolume = 1.0f;
        this.challengeVolume = 1.0f;
        this.position = AdvancementToastPosition.CENTER;
        this.screenBehavior = AdvancementToastScreenBehavior.TOP;
    }

    public boolean isJadeCompatEnabled() {
        return isJadeCompatEnabled;
    }
    public void setJadeCompatEnabled(boolean isJadeCompatibility) {
        this.isJadeCompatEnabled = isJadeCompatibility;

        if (!this.isJadeCompatEnabled) {
            Services.PLATFORM.tryEnableJade();
        }
        else {
            Services.PLATFORM.tryDisableJade();
        }
    }

    public boolean areSoundsEnabled() {
        return areSoundsEnabled;
    }
    public void setSoundsEnabled(boolean areSoundsEnabled) {
        this.areSoundsEnabled = areSoundsEnabled;
    }

    public float getTaskVolume() {
        return taskVolume;
    }
    public void setTaskVolume(float taskVolume) {
        this.taskVolume = taskVolume;
    }

    public float getGoalVolume() {
        return goalVolume;
    }
    public void setGoalVolume(float goalVolume) {
        this.goalVolume = goalVolume;
    }

    public float getChallengeVolume() {
        return challengeVolume;
    }
    public void setChallengeVolume(float challengeVolume) {
        this.challengeVolume = challengeVolume;
    }

    public AdvancementToastPosition getPosition() {
        return position;
    }
    public void setPosition(AdvancementToastPosition position) {
        this.position = position;
    }

    public AdvancementToastScreenBehavior getScreenBehavior() {
        return screenBehavior;
    }
    public void setScreenBehavior(AdvancementToastScreenBehavior screenBehavior) {
        this.screenBehavior = screenBehavior;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public GeneralConfigData get() {
        return new GeneralConfigData(isJadeCompatEnabled, areSoundsEnabled, taskVolume, goalVolume, challengeVolume, position, screenBehavior);
    }

    @Override
    public String toString() {
        return super.toString().replace("}", ", ") + String.format("isJadeCompatibility='%s', areSoundsEnabled='%s', taskVolume='%s', goalVolume='%s', challengeVolume='%s', advancementToastPosition='%s', advancementToastScreenBehavior='%s'}", isJadeCompatEnabled, areSoundsEnabled, taskVolume, goalVolume, challengeVolume, position, screenBehavior);
    }
}
