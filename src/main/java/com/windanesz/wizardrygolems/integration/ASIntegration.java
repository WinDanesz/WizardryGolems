package com.windanesz.wizardrygolems.integration;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.Loader;

public final class ASIntegration {

	public static final String AS_MOD_ID = "ancientspellcraft";

	private static boolean loaded;

	public static void init() {
		loaded = Loader.isModLoaded(AS_MOD_ID);
	}

	public static void setLightningTileProperties(TileEntity tile, EntityLivingBase owner, int duration) {
		if (loaded) LightingBlockHandler.setTileProperties(tile,owner,duration);
	}

	public static IBlockState getLightningBlockState() {
	     return LightingBlockHandler.getLightningBlock();
	}
	public static boolean isLoaded() {return loaded;}
}
