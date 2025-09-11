package net.bivrik.fancytoasts.client.config;

import net.bivrik.fancytoasts.client.util.Paths;

public class GeneralConfigData extends ConfigData {
    private boolean isJadeCompatEnabled;
    private boolean areSoundsEnabled;
    private float taskVolume;
    private float goalVolume;
    private float challengeVolume;

    public GeneralConfigData(boolean isJadeCompatibility, boolean areSoundsEnabled, float taskVolume, float goalVolume, float challengeVolume) {
        super(Paths.GENERAL_CONFIG_FILE);

        this.isJadeCompatEnabled = isJadeCompatibility;
        this.areSoundsEnabled = areSoundsEnabled;
        this.taskVolume = taskVolume;
        this.goalVolume = goalVolume;
        this.challengeVolume = challengeVolume;
    }

    public GeneralConfigData() {
        super(Paths.GENERAL_CONFIG_FILE);

        this.isJadeCompatEnabled = true;
        this.areSoundsEnabled = true;
        this.taskVolume = 1.0f;
        this.goalVolume = 1.0f;
        this.challengeVolume = 1.0f;
    }

    public boolean isJadeCompatEnabled() {
        return isJadeCompatEnabled;
    }
    public void setJadeCompatEnabled(boolean isJadeCompatibility) {
        this.isJadeCompatEnabled = isJadeCompatibility;
    }

    public boolean areSoundsEnabled() {
        return areSoundsEnabled;
    }
    public void setAreSoundsEnabled(boolean areSoundsEnabled) {
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

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public GeneralConfigData get() {
        return new GeneralConfigData(isJadeCompatEnabled, areSoundsEnabled, taskVolume, goalVolume, challengeVolume);
    }

    @Override
    public String toString() {
        return super.toString().replace("}", ", ") + String.format("isJadeCompatibility='%s', areSoundsEnabled='%s', taskVolume='%s', goalVolume='%s', challengeVolume='%s'}", isJadeCompatEnabled, areSoundsEnabled, taskVolume, goalVolume, challengeVolume);
    }
}
