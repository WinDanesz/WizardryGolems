package com.windanesz.wizardrygolems.entity.living;

import electroblob.wizardry.constants.Element;
import electroblob.wizardry.registry.WizardryItems;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public interface IHealingGolem extends IElementalGolem {

	default void onGolemUpdate(EntityGolemBaseMinion minion) {
		if (minion.isBurning()) {
			minion.extinguish();
		}
	}

	default void onSuccessFulAttackDelegate(EntityGolemBaseMinion minion, EntityLivingBase target) {

	}

	default void onDeathDelegate(EntityGolemBaseMinion minion) {
	}

	default boolean isHealingWand(ItemStack stack) {
		return stack != null && !stack.isEmpty() && (stack.getItem() == WizardryItems.novice_healing_wand || stack.getItem() == WizardryItems.apprentice_healing_wand
				|| stack.getItem() == WizardryItems.advanced_healing_wand || stack.getItem() == WizardryItems.master_healing_wand);
	}

	@Override
	default Element getElement() {
		return Element.HEALING;
	}
}
