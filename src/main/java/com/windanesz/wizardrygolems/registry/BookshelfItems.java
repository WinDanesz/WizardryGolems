package com.windanesz.wizardrygolems.registry;

import com.windanesz.wizardrygolems.WizardryGolems;
import electroblob.wizardry.inventory.ContainerBookshelf;
import net.minecraft.util.ResourceLocation;

import static electroblob.wizardry.block.BlockBookshelf.registerBookModelTexture;

public class BookshelfItems {

	public static void PreInitRegisterBookShelfModelTextures() {
		registerBookModelTexture(() -> WizardryGolemsItems.golemancy_spell_book, new ResourceLocation(WizardryGolems.MODID, "blocks/books_golemancy"));
		registerBookModelTexture(() -> WizardryGolemsItems.golemancy_scroll, new ResourceLocation(WizardryGolems.MODID, "blocks/scrolls_golemancy"));

	}

	public static void InitBookshelfItems() {

		ContainerBookshelf.registerBookItem(WizardryGolemsItems.golemancy_spell_book);
		ContainerBookshelf.registerBookItem(WizardryGolemsItems.golemancy_scroll);
	}
}
