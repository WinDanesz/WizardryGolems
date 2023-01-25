package com.windanesz.wizardrygolems.entity.living;

import com.windanesz.wizardrygolems.registry.WizardryGolemsItems;
import com.windanesz.wizardryutils.item.ItemNewArtefact;
import electroblob.wizardry.entity.construct.EntityFrostSigil;
import electroblob.wizardry.entity.construct.EntityIceSpike;
import electroblob.wizardry.entity.projectile.EntityIceShard;
import electroblob.wizardry.item.ItemArtefact;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.registry.WizardryPotions;
import electroblob.wizardry.util.BlockUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IIceGolem {

	default void onGolemUpdate(EntityGolemBaseMinion minion) {
		if (minion != null) {

			if (minion.world.getTotalWorldTime() % 10L == 0) {

				EntityLivingBase caster = minion.getCaster();

				if (caster instanceof EntityPlayer && !minion.world.isRemote) {
					EntityPlayer player = (EntityPlayer) caster;

					for (ItemArtefact artefact : ItemArtefact.getActiveArtefacts(player)) {
						if (artefact == WizardryGolemsItems.charm_frost_cloak) {
							if (minion.getDistance(player) < 16) {
								minion.addPotionEffect(new PotionEffect(WizardryPotions.ice_shroud, 40, 0, true, true));
								if (isIceWand(player.getHeldItemMainhand()) || isIceWand(player.getHeldItemOffhand())) {
									player.addPotionEffect(new PotionEffect(WizardryPotions.ice_shroud, 40, 0, true, true));
								}
							}
						}
					}
				}
			}
		}
	}

	default void onSuccessFulAttackDelegate(EntityGolemBaseMinion minion, EntityLivingBase target) {
		if (!target.world.isRemote && target.world.rand.nextFloat() < 0.15f && minion.getCaster() instanceof EntityPlayer
				&& ItemArtefact.isArtefactActive((EntityPlayer) minion.getCaster(), WizardryGolemsItems.amulet_jagged_sapphire)) {
			for (int i = 0; i < 5; i++) {
				EntityIceSpike iceSpike = new EntityIceSpike(target.world);
				double x = target.posX + 2 - target.world.rand.nextFloat() * 4;
				double z = target.posZ + 2 - target.world.rand.nextFloat() * 4;
				Integer y = BlockUtils.getNearestSurface(target.world, new BlockPos(x, target.posY, z), EnumFacing.UP, 2, true,
						BlockUtils.SurfaceCriteria.basedOn(World::isBlockFullCube));
				if (y == null) {return;}
				iceSpike.setFacing(EnumFacing.UP);
				iceSpike.setCaster(minion.getCaster());
				iceSpike.lifetime = 90;
				iceSpike.setPosition(x, y, z);
				target.world.spawnEntity(iceSpike);
			}
		}

		if (!target.world.isRemote && minion.getCaster() instanceof EntityPlayer && minion.getCaster().isPotionActive(WizardryPotions.ice_shroud)
				&& ItemNewArtefact.isArtefactActive((EntityPlayer) minion.getCaster(), WizardryGolemsItems.belt_coldlink)) {
			minion.getCaster().heal(0.5f);
		}
	}

	default void onDeathDelegate(EntityGolemBaseMinion minion) {
		if (minion != null && minion.getCaster() != null && minion.getCaster() instanceof EntityPlayer) {

			EntityPlayer player = (EntityPlayer) minion.getCaster();
			for (ItemArtefact artefact : ItemArtefact.getActiveArtefacts(player)) {
				if (artefact == WizardryGolemsItems.charm_frozen_mark) {
					if (!minion.world.isRemote) {
						for (EnumFacing direction : EnumFacing.HORIZONTALS) {
							BlockPos pos = minion.getPosition().offset(direction, 2);
							Integer y = BlockUtils.getNearestFloor(minion.world, pos, 2);
							if (y == null) {continue;}
							EntityFrostSigil sigil = new EntityFrostSigil(minion.world);
							sigil.setCaster(minion.getCaster());
							sigil.setPosition(pos.getX() + 0.5, y, pos.getZ() + 0.5);
							minion.world.spawnEntity(sigil);
						}
					}
				}
				if (artefact == WizardryGolemsItems.amulet_broken_ice) {

					if (!player.world.isRemote && player.world.rand.nextFloat() < 0.5f) {
						for (int i = 0; i < 8; i++) {
							double dx = minion.world.rand.nextDouble() - 0.5;
							double dy = minion.world.rand.nextDouble() - 0.5;
							double dz = minion.world.rand.nextDouble() - 0.5;
							EntityIceShard iceshard = new EntityIceShard(minion.world);
							iceshard.setPosition(minion.posX + dx + Math.signum(dx) * minion.width,
									minion.posY + minion.height / 2 + dy,
									minion.posZ + dz + Math.signum(dz) * minion.width);
							iceshard.motionX = dx * 1.5;
							iceshard.motionY = dy * 1.5;
							iceshard.motionZ = dz * 1.5;
							iceshard.setCaster(player);
							minion.world.spawnEntity(iceshard);
						}
					}

				}

			}
		}
	}

	default boolean isIceWand(ItemStack stack) {
		return stack != null && !stack.isEmpty() && (stack.getItem() == WizardryItems.novice_ice_wand || stack.getItem() == WizardryItems.apprentice_ice_wand
				|| stack.getItem() == WizardryItems.advanced_ice_wand || stack.getItem() == WizardryItems.master_ice_wand);
	}
}
