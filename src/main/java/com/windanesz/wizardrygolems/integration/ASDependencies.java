package com.windanesz.wizardrygolems.integration;

import com.windanesz.ancientspellcraft.entity.projectile.EntitySafeIceShard;
import com.windanesz.ancientspellcraft.registry.ASBlocks;
import com.windanesz.ancientspellcraft.spell.FairyRing;
import com.windanesz.ancientspellcraft.tileentity.TileEntityLightningBlock;
import electroblob.wizardry.entity.projectile.EntityIceShard;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;

public class ASDependencies {

	public static boolean loaded = Loader.isModLoaded("ancientspellcraft");

	private ASDependencies() {}

	public static void setTileProperties(TileEntity tile, EntityLivingBase owner, int duration) {
		if (loaded && tile instanceof TileEntityLightningBlock) {
			((TileEntityLightningBlock) tile).setCaster(owner);
			((TileEntityLightningBlock) tile).setLifetime(duration);

		}
	}

	public static IBlockState getLightningBlock() {
		return ASBlocks.lightning_block.getDefaultState();
	}

	public static EntityIceShard getIceShard(World world) {
		return new EntitySafeIceShard(world);
	}

	public static void summonFairyRing(World world, EntityLivingBase owner, BlockPos pos, SpellModifiers modifiers) {
		FairyRing.summonMushroomRing(world, owner, pos, modifiers);
	}

}
