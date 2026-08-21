package net.bivrik.fancytoasts.platform.utility;

import net.bivrik.fancytoasts.utility.DefaultUVs;
import net.bivrik.fancytoasts.utility.TypeBasedUVs;

public enum AdvancementType {
    TASK(DefaultUVs.TASK),
    GOAL(DefaultUVs.GOAL),
    CHALLENGE(DefaultUVs.CHALLENGE);

    private final TypeBasedUVs uvs;

    AdvancementType(TypeBasedUVs uvs) {
        this.uvs = uvs;
    }

    public TypeBasedUVs getUvs() {
        return uvs;
    }
}