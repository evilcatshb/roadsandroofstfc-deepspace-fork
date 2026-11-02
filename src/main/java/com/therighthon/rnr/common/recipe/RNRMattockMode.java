package com.therighthon.rnr.common.recipe;
import net.minecraft.util.StringRepresentable; import java.util.Locale;
public enum RNRMattockMode implements StringRepresentable { SMOOTH, SLAB, STAIR; public String getSerializedName(){return name().toLowerCase(Locale.ROOT);} }
