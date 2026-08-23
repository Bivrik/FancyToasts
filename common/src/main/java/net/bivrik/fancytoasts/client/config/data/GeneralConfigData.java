package net.bivrik.fancytoasts.client.config.data;

import net.bivrik.fancytoasts.client.config.DisplayTextType;
import net.bivrik.fancytoasts.client.config.ToastAnchor;
import net.bivrik.fancytoasts.client.config.ToastScreenBehavior;
import net.bivrik.fancytoasts.core.Constants;
import net.bivrik.fancytoasts.platform.Services;
import net.bivrik.fancytoasts.utility.file.Paths;

import java.util.Objects;

public class GeneralConfigData extends ConfigData {
    private static final int VERSION = Constants.ConfigVersions.GENERAL;

    private boolean isJadeHiding;
    private boolean isBossBarHiding;
    private boolean isAetherEnabled;
    private boolean areSoundsEnabled;
    private float taskVolume;
    private float goalVolume;
    private float challengeVolume;
    private float loopsStrength;
    private float loopsSpeed;
    private float pitchRandomness;
    private float animationSpeed;
    private int offsetX;
    private int offsetY;
    private ToastAnchor toastAnchor;
    private ToastScreenBehavior toastScreenBehavior;
    private DisplayTextType titleDisplayTextType;
    private DisplayTextType descriptionDisplayTextType;

    private GeneralConfigData(boolean isJadeHiding, boolean isBossBarHiding, boolean isAetherEnabled, boolean areSoundsEnabled,
                              float taskVolume, float goalVolume, float challengeVolume,
                              float loopsStrength, float loopsSpeed, float pitchRandomness,
                              float animationSpeed, int offsetX, int offsetY,
                              ToastAnchor toastAnchor, ToastScreenBehavior toastScreenBehavior,
                              DisplayTextType titleDisplayTextType, DisplayTextType descriptionDisplayTextType) {
        super(Paths.GENERAL_CONFIG_FILE);

        this.isJadeHiding = isJadeHiding;
        this.isBossBarHiding = isBossBarHiding;
        this.isAetherEnabled = isAetherEnabled;
        this.areSoundsEnabled = areSoundsEnabled;
        this.taskVolume = taskVolume;
        this.goalVolume = goalVolume;
        this.challengeVolume = challengeVolume;
        this.loopsStrength = loopsStrength;
        this.loopsSpeed = loopsSpeed;
        this.pitchRandomness = pitchRandomness;
        this.animationSpeed = animationSpeed;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.toastAnchor = toastAnchor;
        this.toastScreenBehavior = toastScreenBehavior;
        this.titleDisplayTextType = titleDisplayTextType;
        this.descriptionDisplayTextType = descriptionDisplayTextType;
    }

    public GeneralConfigData() {
        this(true, true, true, true,
                1.0f, 1.0f, 1.0f,
                1.0f, 1.0f, 0.05f, 1.0f,
                ToastAnchor.TOP.getBaseOffsetX(), ToastAnchor.TOP.getBaseOffsetY(), ToastAnchor.TOP,
                ToastScreenBehavior.TRANSPARENT, DisplayTextType.DEFAULT, DisplayTextType.DEFAULT);
    }

    public boolean isJadeHiding() {
        return isJadeHiding;
    }

    public void setJadeHiding(boolean isJadeHiding) {
        this.isJadeHiding = isJadeHiding;

        if (!isJadeHiding) {
            Services.JADE.tryEnable();
        }
    }

    public boolean isBossBarHiding() {
        return isBossBarHiding;
    }

    public void setBossBarHiding(boolean isBossBarHiding) {
        this.isBossBarHiding = isBossBarHiding;
    }

    public boolean isAetherEnabled() {
        return isAetherEnabled;
    }

    public void setAetherEnabled(boolean isAetherEnabled) {
        this.isAetherEnabled = isAetherEnabled;
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

    public float getLoopsStrength() {
        return loopsStrength;
    }

    public void setLoopsStrength(float loopsStrength) {
        this.loopsStrength = loopsStrength;
    }

    public float getLoopsSpeed() {
        return loopsSpeed;
    }

    public void setLoopsSpeed(float loopsSpeed) {
        this.loopsSpeed = loopsSpeed;
    }

    public float getPitchRandomness() {
        return pitchRandomness;
    }

    public void setPitchRandomness(float pitchRandomness) {
        this.pitchRandomness = pitchRandomness;
    }

    public float getAnimationSpeed() {
        return animationSpeed;
    }

    public void setAnimationSpeed(float animationSpeed) {
        this.animationSpeed = animationSpeed;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(int offsetX) {
        this.offsetX = offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(int offsetY) {
        this.offsetY = offsetY;
    }

    public ToastAnchor getToastAnchor() {
        return toastAnchor;
    }

    public void setToastAnchor(ToastAnchor toastAnchor) {
        this.toastAnchor = toastAnchor;
    }

    public ToastScreenBehavior getToastScreenBehavior() {
        return toastScreenBehavior;
    }

    public void setToastScreenBehavior(ToastScreenBehavior toastScreenBehavior) {
        this.toastScreenBehavior = toastScreenBehavior;
    }

    public DisplayTextType getTitleDisplayTextType() {
        return titleDisplayTextType;
    }

    public void setTitleDisplayTextType(DisplayTextType titleDisplayTextType) {
        this.titleDisplayTextType = titleDisplayTextType;
    }

    public DisplayTextType getDescriptionDisplayTextType() {
        return descriptionDisplayTextType;
    }

    public void setDescriptionDisplayTextType(DisplayTextType descriptionDisplayTextType) {
        this.descriptionDisplayTextType = descriptionDisplayTextType;
    }

    @Override
    public int getLatestVersion() {
        return VERSION;
    }

    @Override
    public GeneralConfigData copy() {
        return new GeneralConfigData(isJadeHiding, isBossBarHiding, isAetherEnabled, areSoundsEnabled,
                taskVolume, goalVolume, challengeVolume, loopsStrength, loopsSpeed,
                pitchRandomness, animationSpeed, offsetX, offsetY, toastAnchor,
                toastScreenBehavior, titleDisplayTextType, descriptionDisplayTextType).withLatestVersion();
    }

    @Override
    public int hashCode() {
        return Objects.hash(isJadeHiding, isBossBarHiding, isAetherEnabled, areSoundsEnabled,
                taskVolume, goalVolume, challengeVolume, loopsStrength, loopsSpeed,
                pitchRandomness, animationSpeed, offsetX, offsetY, toastAnchor,
                toastScreenBehavior, titleDisplayTextType, descriptionDisplayTextType);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof GeneralConfigData that)) return false;
        return isJadeHiding == that.isJadeHiding
                && isBossBarHiding == that.isBossBarHiding
                && isAetherEnabled == that.isAetherEnabled
                && areSoundsEnabled == that.areSoundsEnabled
                && Float.compare(taskVolume, that.taskVolume) == 0
                && Float.compare(goalVolume, that.goalVolume) == 0
                && Float.compare(challengeVolume, that.challengeVolume) == 0
                && Float.compare(loopsStrength, that.loopsStrength) == 0
                && Float.compare(loopsSpeed, that.loopsSpeed) == 0
                && Float.compare(pitchRandomness, that.pitchRandomness) == 0
                && Float.compare(animationSpeed, that.animationSpeed) == 0
                && offsetX == that.offsetX
                && offsetY == that.offsetY
                && toastAnchor == that.toastAnchor
                && toastScreenBehavior == that.toastScreenBehavior
                && titleDisplayTextType == that.titleDisplayTextType
                && descriptionDisplayTextType == that.descriptionDisplayTextType;
    }

    @Override
    public String toString() {
        return getBaseToStringBuilder()
                .append("isJadeHiding", isJadeHiding)
                .append("isBossBarHiding", isBossBarHiding)
                .append("isAetherEnabled", isAetherEnabled)
                .append("areSoundsEnabled", areSoundsEnabled)
                .append("taskVolume", taskVolume)
                .append("goalVolume", goalVolume)
                .append("challengeVolume", challengeVolume)
                .append("loopsStrength", loopsStrength)
                .append("loopsSpeed", loopsSpeed)
                .append("pitchRandomness", pitchRandomness)
                .append("animationSpeed", animationSpeed)
                .append("offsetX", offsetX)
                .append("offsetY", offsetY)
                .append("toastAnchor", toastAnchor)
                .append("toastScreenBehavior", toastScreenBehavior)
                .append("titleDisplayTextType", titleDisplayTextType)
                .append("descriptionDisplayTextType", descriptionDisplayTextType)
                .toString();
    }
}
