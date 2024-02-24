package com.windanesz.wizardrygolems.entity.living;

import com.windanesz.wizardrygolems.WizardryGolems;
import com.windanesz.wizardrygolems.registry.WizardryGolemsItems;
import electroblob.wizardry.Wizardry;
import electroblob.wizardry.constants.Element;
import electroblob.wizardry.entity.construct.EntityEarthquake;
import electroblob.wizardry.entity.living.ISummonedCreature;
import electroblob.wizardry.item.ItemArtefact;
import electroblob.wizardry.registry.Spells;
import electroblob.wizardry.registry.WizardryBlocks;
import electroblob.wizardry.tileentity.TileEntityPlayerSave;
import electroblob.wizardry.util.BlockUtils;
import electroblob.wizardry.util.EntityUtils;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeForest;
import net.minecraft.world.biome.BiomeProvider;

public interface IEarthGolem extends IElementalGolem {

	default void onGolemUpdate(ISummonedCreature golem) {
		if (golem instanceof EntityGolem && golem.getCaster() != null && golem.getCaster() instanceof EntityPlayer) {
			EntityGolem entityGolem = (EntityGolem) golem;
			World world = entityGolem.world;

			for (ItemArtefact artefact : ItemArtefact.getActiveArtefacts((EntityPlayer) golem.getCaster())) {
				if (artefact == WizardryGolemsItems.amulet_gaia && WizardryGolems.rand.nextFloat() < 0.15) {
					if (world.isRemote) {

						world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, ((EntityGolem) golem).posX, ((EntityGolem) golem).posY + 0.1, ((EntityGolem) golem).posZ, 0, 0, 0);

						double particleX, particleZ;

						for (int i = 0; i < 40; i++) {

							particleX = ((EntityGolem) golem).posX - 1.0d + 2 * world.rand.nextDouble();
							particleZ = ((EntityGolem) golem).posZ - 1.0d + 2 * world.rand.nextDouble();

							IBlockState block = BlockUtils.getBlockEntityIsStandingOn(golem.getCaster());
							world.spawnParticle(EnumParticleTypes.BLOCK_DUST, particleX, ((EntityGolem) golem).posY,
									particleZ, particleX - ((EntityGolem) golem).posX, 0, particleZ - ((EntityGolem) golem).posZ, Block.getStateId(block));
						}

						EntityUtils.getEntitiesWithinRadius(15, ((EntityGolem) golem).posX, ((EntityGolem) golem).posY, ((EntityGolem) golem).posZ, world, EntityPlayer.class)
								.forEach(p -> Wizardry.proxy.shakeScreen(p, 12));
					} else {
						EntityEarthquake earthquake = new EntityEarthquake(world);
						earthquake.setPosition(((EntityGolem) golem).posX, ((EntityGolem) golem).posY, ((EntityGolem) golem).posZ);
						earthquake.setCaster(golem.getCaster());
						earthquake.lifetime = (int) (3f / 0.4f);
						world.spawnEntity(earthquake);
						// Sets the various parameters
					}

				} else if (artefact == WizardryGolemsItems.amulet_snare) {
					BlockPos pos = entityGolem.getPosition();
					for(EnumFacing direction : EnumFacing.HORIZONTALS){
						BlockPos pos1 = entityGolem.getPosition().offset(direction);
						if(entityGolem.world.rand.nextBoolean() &&
								BlockUtils.canBlockBeReplaced(entityGolem.world, pos1) && BlockUtils.canPlaceBlock(entityGolem, entityGolem.world, pos1))
							entityGolem.world.setBlockState(pos1, WizardryBlocks.snare.getDefaultState());
					}

					if (BlockUtils.canBlockBeReplaced(entityGolem.world, pos.up())) {
						if (!entityGolem.world.isRemote) {
							entityGolem.world.setBlockState(entityGolem.getPosition(), WizardryBlocks.snare.getDefaultState());
							((TileEntityPlayerSave) entityGolem.world.getTileEntity(entityGolem.getPosition())).setCaster(golem.getCaster());
							((TileEntityPlayerSave) entityGolem.world.getTileEntity(entityGolem.getPosition())).sync();
						}
					}
				} else if (artefact == WizardryGolemsItems.amulet_deathweed) {
					BiomeProvider chunkManager = entityGolem.world.getBiomeProvider();
					if (chunkManager.getBiome(entityGolem.getPosition()) instanceof BiomeForest || WizardryGolems.rand.nextFloat() < 0.1) {
						Spells.forests_curse.cast(entityGolem.world, entityGolem, EnumHand.OFF_HAND, 0, null, new SpellModifiers());
					}
				}
			}
		}
	}

	@Override
	default Element getElement() {
		return Element.EARTH;
	}
}
