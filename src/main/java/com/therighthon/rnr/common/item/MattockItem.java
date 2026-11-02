package com.therighthon.rnr.common.item;
import com.therighthon.rnr.common.RNRTags; import net.minecraft.world.item.DiggerItem; import net.minecraft.world.item.Tier;
public class MattockItem extends DiggerItem { public MattockItem(Tier tier,float attackDamage,float attackSpeed,Properties props){super(attackDamage,attackSpeed,tier,RNRTags.Blocks.MINEABLE_WITH_MATTOCK,props);} }
