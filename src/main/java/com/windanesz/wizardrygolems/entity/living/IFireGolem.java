package com.windanesz.wizardrygolems.entity.living;

import com.windanesz.wizardrygolems.registry.WizardryGolemsItems;
import electroblob.wizardry.constants.Element;
import electroblob.wizardry.item.ItemArtefact;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.registry.WizardryPotions;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.SoundCategory;

public interface IFireGolem extends IElementalGolem {

	default void onGolemUpdate(EntityGolemBaseMinion minion) {
		if (minion != null) {

			if (minion.world.getTotalWorldTime() % 10L == 0) {

				EntityLivingBase caster = minion.getCaster();

				if (caster instanceof EntityPlayer) {
					EntityPlayer player = (EntityPlayer) caster;

					for (ItemArtefact artefact : ItemArtefact.getActiveArtefacts(player)) {
						if (artefact == WizardryGolemsItems.ring_flame_trail) {
							if (!minion.world.isRemote && minion.onGround && minion.world.isAirBlock(minion.getPosition())) {
								if (minion.world.isAirBlock(minion.getPosition())) {
									minion.world.playSound(null, minion.getPosition(), SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, minion.world.rand.nextFloat() * 0.4F + 0.8F);
									minion.world.setBlockState(minion.getPosition(), Blocks.FIRE.getDefaultState(), 11);
								}
							}
						} else if (artefact == WizardryGolemsItems.charm_fire_golemancy_potency) {
							if (minion.getDistance(player) < 16) {
								if (isFireWand(player.getHeldItemMainhand()) || isFireWand(player.getHeldItemOffhand())) {
									player.addPotionEffect(new PotionEffect(WizardryPotions.empowerment, 40, 0, true, true));
								}
							}
						} else if (artefact == WizardryGolemsItems.charm_ifrit_bottle) {
							if (minion.getDistance(player) < 16) {
								minion.addPotionEffect(new PotionEffect(WizardryPotions.fireskin, 40, 0, true, true));
								if (isFireWand(player.getHeldItemMainhand()) || isFireWand(player.getHeldItemOffhand())) {
									player.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 40, 0, true, true));
								}
							}
						}

					}
				}
			}
		}

	}

	default void onDeathDelegate(EntityGolemBaseMinion minion) {
		if (minion != null && minion.getCaster() != null && minion.getCaster() instanceof EntityPlayer) {

			EntityPlayer player = (EntityPlayer) minion.getCaster();
			for (ItemArtefact artefact : ItemArtefact.getActiveArtefacts(player)) {
				if (artefact == WizardryGolemsItems.amulet_steaming_netherrack && !minion.world.isRemote) {
					minion.world.newExplosion(null, minion.posX, minion.posY + 1, minion.posZ, minion instanceof EntityFurnaceGolemMinion ? 3 : 1, false, false);
				}
			}
		}
	}

	default boolean isFireWand(ItemStack stack) {
		return stack != null && !stack.isEmpty() && (stack.getItem() == WizardryItems.novice_fire_wand || stack.getItem() == WizardryItems.apprentice_fire_wand
				|| stack.getItem() == WizardryItems.advanced_fire_wand || stack.getItem() == WizardryItems.master_fire_wand);
	}

	@Override
	default Element getElement() {
		return Element.FIRE;
	}
}
