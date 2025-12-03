package net.bivrik.fancytoasts.client.config.data;

import net.bivrik.fancytoasts.client.config.ToastAnchor;
import net.bivrik.fancytoasts.client.config.ToastScreenBehavior;
import net.bivrik.fancytoasts.utility.file.Paths;
import net.bivrik.fancytoasts.platform.Services;

import java.util.Objects;

public class GeneralConfigData extends ConfigData {
    private boolean isJadeHiding;
    private boolean areSoundsEnabled;
    private float taskVolume;
    private float goalVolume;
    private float challengeVolume;
    private float loopsStrength;
    private float loopsSpeed;
    private int offsetX;
    private int offsetY;
    private ToastAnchor toastAnchor;
    private ToastScreenBehavior toastScreenBehavior;

    public GeneralConfigData(boolean isJadeHiding, boolean areSoundsEnabled, float taskVolume, float goalVolume, float challengeVolume, float loopsStrength, float loopsSpeed, int offsetX, int offsetY, ToastAnchor toastAnchor, ToastScreenBehavior toastScreenBehavior) {
        super(Paths.GENERAL_CONFIG_FILE);

        this.isJadeHiding = isJadeHiding;
        this.areSoundsEnabled = areSoundsEnabled;
        this.taskVolume = taskVolume;
        this.goalVolume = goalVolume;
        this.challengeVolume = challengeVolume;
        this.loopsStrength = loopsStrength;
        this.loopsSpeed = loopsSpeed;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.toastAnchor = toastAnchor;
        this.toastScreenBehavior = toastScreenBehavior;
    }

    public GeneralConfigData() {
        this(true, true, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0, 50, ToastAnchor.TOP, ToastScreenBehavior.TRANSPARENT);
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

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof GeneralConfigData that)) return false;
        return isJadeHiding == that.isJadeHiding && areSoundsEnabled == that.areSoundsEnabled && Float.compare(taskVolume, that.taskVolume) == 0 && Float.compare(goalVolume, that.goalVolume) == 0 && Float.compare(challengeVolume, that.challengeVolume) == 0 && Float.compare(loopsStrength, that.loopsStrength) == 0 && Float.compare(loopsSpeed, that.loopsSpeed) == 0 && offsetX == that.offsetX && offsetY == that.offsetY && toastAnchor == that.toastAnchor && toastScreenBehavior == that.toastScreenBehavior;
    }

    @Override
    public int hashCode() {
        return Objects.hash(isJadeHiding, areSoundsEnabled, taskVolume, goalVolume, challengeVolume, loopsStrength, loopsSpeed, offsetX, offsetY, toastAnchor, toastScreenBehavior);
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public GeneralConfigData copy() {
        return new GeneralConfigData(isJadeHiding, areSoundsEnabled, taskVolume, goalVolume, challengeVolume, loopsStrength, loopsSpeed, offsetX, offsetY, toastAnchor, toastScreenBehavior);
    }

    @Override
    public String toString() {
        return super.toString().replace("}", ", ") + String.format(
                "isJadeHiding='%s', areSoundsEnabled='%s', taskVolume='%s', goalVolume='%s', challengeVolume='%s', loopsStrength='%s', loopsSpeed='%s', offsetX='%s', offsetY='%s', toastAnchor='%s', toastScreenBehavior='%s'}",
                isJadeHiding, areSoundsEnabled, taskVolume, goalVolume, challengeVolume, loopsStrength, loopsSpeed, offsetX, offsetY, toastAnchor, toastScreenBehavior
        );
    }
}
