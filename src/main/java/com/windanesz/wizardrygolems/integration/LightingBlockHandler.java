package com.windanesz.wizardrygolems.integration;

import com.windanesz.ancientspellcraft.registry.ASBlocks;
import com.windanesz.ancientspellcraft.tileentity.TileEntityLightningBlock;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.Loader;

public class LightingBlockHandler {

	public static boolean loaded = Loader.isModLoaded("ancientspellcraft");

	private LightingBlockHandler() {}

	public static void setTileProperties(TileEntity tile, EntityLivingBase owner, int duration) {
		if (loaded && tile instanceof TileEntityLightningBlock) {
			((TileEntityLightningBlock) tile).setCaster(owner);
			((TileEntityLightningBlock) tile).setLifetime(duration);

		}
	}

	public static IBlockState getLightningBlock() {
		return ASBlocks.lightning_block.getDefaultState();
	}
}
