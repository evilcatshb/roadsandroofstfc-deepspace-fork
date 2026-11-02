package com.therighthon.rnr.common.fluid;
import com.therighthon.rnr.RoadsAndRoofs;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.DeferredRegister; import net.minecraftforge.registries.ForgeRegistries;
public final class RNRFluids { public static final DeferredRegister<Fluid> FLUIDS=DeferredRegister.create(ForgeRegistries.FLUIDS,RoadsAndRoofs.MOD_ID); private RNRFluids(){} }
