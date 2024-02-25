package com.windanesz.wizardrygolems.integration;

import electroblob.wizardry.entity.projectile.EntityIceShard;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;

public final class ASIntegration {

	public static final String AS_MOD_ID = "ancientspellcraft";

	private static boolean loaded;

	public static void init() {
		loaded = Loader.isModLoaded(AS_MOD_ID);
	}

	public static void setLightningTileProperties(TileEntity tile, EntityLivingBase owner, int duration) {
		if (loaded) {ASDependencies.setTileProperties(tile, owner, duration);}
	}

	public static IBlockState getLightningBlockState() {
		return ASDependencies.getLightningBlock();
	}

	public static boolean isLoaded() {return loaded;}

	public static EntityIceShard getIceShard(World world) {
		return loaded ? ASDependencies.getIceShard(world) : new EntityIceShard(world);
	}

	public static void summonFairyRing(World world, EntityLivingBase owner, BlockPos pos, SpellModifiers modifiers) {
		if (loaded) {
			ASDependencies.summonFairyRing(world, owner, pos, modifiers);
		}
	}
}
