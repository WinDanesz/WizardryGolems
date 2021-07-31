package com.windanesz.wizardrygolems.client.model;

import com.windanesz.wizardrygolems.registry.WizardryGolemsItems;
import electroblob.wizardry.item.IMultiTexturedItem;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(Side.CLIENT)
public final class WizardryGolemsModels {
	private WizardryGolemsModels() { // no instances
	}

	@SubscribeEvent
	public static void register(ModelRegistryEvent event) {

		// -------------------- Other --------------------
		registerItemModel(WizardryGolemsItems.golemancy_spell_book);
		registerItemModel(WizardryGolemsItems.golemancy_scroll);

		// -------------------- Ring --------------------
		// Earth
		registerItemModel(WizardryGolemsItems.ring_ancient_emperor);
		registerItemModel(WizardryGolemsItems.ring_engraved_concrete);
		registerItemModel(WizardryGolemsItems.ring_obsidian);
		registerItemModel(WizardryGolemsItems.ring_sandstone);
		registerItemModel(WizardryGolemsItems.ring_forest_guardian);
		registerItemModel(WizardryGolemsItems.ring_grass);
		registerItemModel(WizardryGolemsItems.ring_glistering);

		// Fire
		registerItemModel(WizardryGolemsItems.ring_charcoal);
		registerItemModel(WizardryGolemsItems.ring_smoldering);
		registerItemModel(WizardryGolemsItems.ring_flame_trail);
		registerItemModel(WizardryGolemsItems.ring_fire_golem_duration);
		registerItemModel(WizardryGolemsItems.ring_flame_golem);

		// -------------------- Amulet --------------------
		// Earth
		registerItemModel(WizardryGolemsItems.amulet_gaia);
		registerItemModel(WizardryGolemsItems.amulet_deathweed);
		registerItemModel(WizardryGolemsItems.amulet_snare);

		// Fire
		registerItemModel(WizardryGolemsItems.amulet_steaming_netherrack);

		// -------------------- Charm --------------------
		// Earth
		registerItemModel(WizardryGolemsItems.charm_dried_mushroom);

		// Fire
		registerItemModel(WizardryGolemsItems.charm_fire_golemancy_potency);
		registerItemModel(WizardryGolemsItems.charm_ifrit_bottle);

	}

	// below registry methods are courtesy of EB
	private static void registerItemModel(Item item) {
		ModelBakery.registerItemVariants(item, new ModelResourceLocation(item.getRegistryName(), "inventory"));
		ModelLoader.setCustomMeshDefinition(item, s -> new ModelResourceLocation(item.getRegistryName(), "inventory"));
	}

	private static <T extends Item & IMultiTexturedItem> void registerMultiTexturedModel(T item) {

		if (item.getHasSubtypes()) {
			NonNullList<ItemStack> items = NonNullList.create();
			item.getSubItems(item.getCreativeTab(), items);
			for (ItemStack stack : items) {
				ModelLoader.setCustomModelResourceLocation(item, stack.getMetadata(),
						new ModelResourceLocation(item.getModelName(stack), "inventory"));
			}
		}
	}

	private static void registerItemModel(Item item, int metadata, String variant) {
		ModelLoader.setCustomModelResourceLocation(item, metadata,
				new ModelResourceLocation(item.getRegistryName(), variant));
	}

}

