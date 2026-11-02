package com.therighthon.rnr.common;
import com.therighthon.rnr.common.recipe.RNRMattockMode;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
public final class RNRPlayerData { private static final String KEY="rnr_mattock_mode"; public static RNRMattockMode getMode(Player p){int i=p.getPersistentData().getInt(KEY);return RNRMattockMode.values()[Math.max(0,Math.min(RNRMattockMode.values().length-1,i))];} public static void setMode(Player p,RNRMattockMode m){p.getPersistentData().putInt(KEY,m.ordinal());} public static void cycle(Player p){RNRMattockMode[] v=RNRMattockMode.values();setMode(p,v[(getMode(p).ordinal()+1)%v.length]);} private RNRPlayerData(){} }
