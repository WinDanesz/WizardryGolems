package com.windanesz.wizardrygolems.entity.living;

import com.windanesz.wizardrygolems.integration.ASIntegration;
import com.windanesz.wizardrygolems.registry.WizardryGolemsItems;
import com.windanesz.wizardryutils.item.ItemNewArtefact;
import electroblob.wizardry.Wizardry;
import electroblob.wizardry.constants.Element;
import electroblob.wizardry.entity.construct.EntityIceSpike;
import electroblob.wizardry.item.ItemArtefact;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.registry.WizardryPotions;
import electroblob.wizardry.spell.LightningBolt;
import electroblob.wizardry.util.BlockUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ILightingGolem extends IElementalGolem {

	default void onGolemUpdate(EntityGolemBaseMinion minion) {
		if (minion != null) {

			if (minion.world.getTotalWorldTime() % 10L == 0) {

				EntityLivingBase caster = minion.getCaster();

				if (caster instanceof EntityPlayer && !minion.world.isRemote) {
					EntityPlayer player = (EntityPlayer) caster;

					for (ItemArtefact artefact : ItemArtefact.getActiveArtefacts(player)) {
						if (artefact == WizardryGolemsItems.charm_static_aura) {
							if (minion.getDistance(player) < 16) {
								minion.addPotionEffect(new PotionEffect(WizardryPotions.static_aura, 40, 0, true, true));
								if (isLightningWand(player.getHeldItemMainhand()) || isLightningWand(player.getHeldItemOffhand())) {
									player.addPotionEffect(new PotionEffect(WizardryPotions.static_aura, 40, 0, true, true));
								}
							}
						}
						if (artefact == WizardryGolemsItems.ring_electric_scatter && ASIntegration.isLoaded()) {
							//minion.addPotionEffect(new PotionEffect(MobEffects.SPEED, 40, 0));
							if (minion.world.isAirBlock(minion.getPosition())) {
								minion.world.setBlockState(minion.getPosition(), ASIntegration.getLightningBlockState());
								TileEntity tile = minion.world.getTileEntity(minion.getPosition());
								ASIntegration.setLightningTileProperties(tile, minion.getOwner() == null ? (EntityLivingBase) this : (EntityLivingBase) minion.getOwner(), 160);
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
				if (artefact == WizardryGolemsItems.amulet_raging_skies) {
					if (minion.world.canBlockSeeSky(minion.getPosition().up())) {
						if (!minion.world.isRemote) {
							// Temporarily disable the fire tick gamerule if player block damage is disabled
							// Bit of a hack but it works fine!
							boolean doFireTick = minion.world.getGameRules().getBoolean("doFireTick");
							if (doFireTick && !Wizardry.settings.playerBlockDamage) {minion.world.getGameRules().setOrCreateGameRule("doFireTick", "false");}

							EntityLightningBolt lightning = new EntityLightningBolt(minion.world, minion.posX, minion.posY, minion.posZ, false);
							if (minion.getCaster() != null) {
								lightning.getEntityData().setUniqueId(LightningBolt.SUMMONER_NBT_KEY, minion.getCaster().getUniqueID());
							} else {
								lightning.getEntityData().setUniqueId(LightningBolt.SUMMONER_NBT_KEY, minion.getUniqueID());
							}

							lightning.getEntityData().setFloat(LightningBolt.DAMAGE_MODIFIER_NBT_KEY, 1.0f);
							minion.world.addWeatherEffect(lightning);

							// Reset doFireTick to true if it was true before
							if (doFireTick && !Wizardry.settings.playerBlockDamage) minion.world.getGameRules().setOrCreateGameRule("doFireTick", "true");
						}

					}
				}
			}
		}
	}

	default boolean isLightningWand(ItemStack stack) {
		return stack != null && !stack.isEmpty() && (stack.getItem() == WizardryItems.novice_lightning_wand || stack.getItem() == WizardryItems.apprentice_lightning_wand
				|| stack.getItem() == WizardryItems.advanced_lightning_wand || stack.getItem() == WizardryItems.master_lightning_wand);
	}

	@Override
	default Element getElement() {
		return Element.LIGHTNING;
	}
}
