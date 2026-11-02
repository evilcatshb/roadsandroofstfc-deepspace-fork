package com.therighthon.rnr.common.block;


public class TampedSoilBlock extends TampedHeightBlock
{
    public TampedSoilBlock(Properties pProperties)
    {
        super(pProperties.speedFactor(0.9f));
    }

}
