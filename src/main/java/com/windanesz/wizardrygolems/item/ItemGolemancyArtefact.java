package com.windanesz.wizardrygolems.item;

import electroblob.wizardry.constants.Element;
import electroblob.wizardry.item.ItemArtefact;
import net.minecraft.item.EnumRarity;

public class ItemGolemancyArtefact extends ItemArtefact {

	Element element;

	public ItemGolemancyArtefact(EnumRarity rarity, Type type, Element element) {
		super(rarity, type);
		this.element = element;
	}
}

