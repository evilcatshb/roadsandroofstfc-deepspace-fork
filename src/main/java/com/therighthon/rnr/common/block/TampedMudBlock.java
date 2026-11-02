package com.therighthon.rnr.common.block;


public class TampedMudBlock extends TampedSoilBlock
{
    public TampedMudBlock(Properties pProperties)
    {
        super(pProperties.speedFactor(0.8f));
    }
}
