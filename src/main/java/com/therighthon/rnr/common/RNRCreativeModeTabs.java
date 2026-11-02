package com.therighthon.rnr.common;
import com.therighthon.rnr.RoadsAndRoofs;
import com.therighthon.rnr.common.item.RNRItems;
import com.therighthon.rnr.common.block.RNRBlocks;
import com.therighthon.rnr.common.material.RNRMaterials.Metal;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public final class RNRCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS=DeferredRegister.create(Registries.CREATIVE_MODE_TAB,RoadsAndRoofs.MOD_ID);
    public static final RegistryObject<CreativeModeTab> RNR_TAB=CREATIVE_TABS.register("rnr.creative_tab.roads_and_roofs",()->CreativeModeTab.builder()
        .title(Component.translatable("roads_and_roofs"))
        .icon(()->new ItemStack(RNRItems.MATTOCKS.get(Metal.COPPER).get()))
        .displayItems((p,o)->{
            RNRItems.ITEMS.getEntries().forEach(x -> o.accept(x.get()));
        })
        .build());
    private RNRCreativeModeTabs(){}
}
