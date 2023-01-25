package com.windanesz.wizardrygolems.registry;

import com.windanesz.wizardrygolems.WizardryGolems;
import com.windanesz.wizardrygolems.item.ItemGolemancyArtefact;
import com.windanesz.wizardrygolems.item.ItemLivingSnowBottle;
import com.windanesz.wizardrygolems.item.ItemPermanentGolemRing;
import com.windanesz.wizardryutils.item.ItemNewArtefact;
import com.windanesz.wizardryutils.registry.ItemRegistry;
import electroblob.wizardry.constants.Element;
import electroblob.wizardry.item.ItemArtefact;
import electroblob.wizardry.item.ItemScroll;
import electroblob.wizardry.item.ItemSpellBook;
import electroblob.wizardry.registry.WizardryTabs;
import net.minecraft.block.Block;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nonnull;

@ObjectHolder(WizardryGolems.MODID)
@Mod.EventBusSubscriber
public final class WizardryGolemsItems {

	private WizardryGolemsItems() {} // No instances!

	@Nonnull
	@SuppressWarnings("ConstantConditions")
	private static <T> T placeholder() { return null; }

	// -------------------- Other --------------------
	public static final Item golemancy_spell_book = placeholder();
	public static final Item golemancy_scroll = placeholder();

	// -------------------- Ring --------------------
	// Earth
	public static final Item ring_ancient_emperor = placeholder();
	public static final Item ring_engraved_concrete = placeholder();
	public static final Item ring_obsidian = placeholder();
	public static final Item ring_sandstone = placeholder();
	public static final Item ring_forest_guardian = placeholder();
	public static final Item ring_grass = placeholder();
	public static final Item ring_glistering = placeholder();

	// Generic
	public static final Item ring_permanent_golem = placeholder();

	// Fire
	public static final Item ring_charcoal = placeholder();
	public static final Item ring_smoldering = placeholder();
	public static final Item ring_flame_trail = placeholder();
	public static final Item ring_fire_golem_duration = placeholder();
	public static final Item ring_flame_golem = placeholder();

	// -------------------- Amulet --------------------
	// Earth
	public static final Item amulet_gaia = placeholder();
	public static final Item amulet_deathweed = placeholder();
	public static final Item amulet_snare = placeholder();

	// Fire
	public static final Item amulet_steaming_netherrack = placeholder();

	// -------------------- Charm --------------------

	// Earth
	public static final Item charm_dried_mushroom = placeholder();

	// Fire
	public static final Item charm_fire_golemancy_potency = placeholder();
	public static final Item charm_ifrit_bottle = placeholder();

	// Ice
	public static final Item head_permafrost_crown = placeholder();
	public static final Item ring_snow_golem = placeholder();
	public static final Item ring_frostbite = placeholder();
	public static final Item ring_winter_golem = placeholder();
	public static final Item amulet_jagged_sapphire = placeholder();
	public static final Item amulet_broken_ice = placeholder();
	public static final Item charm_frost_cloak = placeholder();
	public static final Item charm_frozen_mark = placeholder();
	public static final Item belt_coldlink = placeholder();

	// below registry methods are courtesy of EB
	public static void registerItem(IForgeRegistry<Item> registry, String name, Item item) {
		registerItem(registry, name, item, false);
	}

	public static void registerItem(IForgeRegistry<Item> registry, String name, Item item, boolean setTabIcon) {
		item.setRegistryName(WizardryGolems.MODID, name);
		item.setTranslationKey(item.getRegistryName().toString());
		registry.register(item);

		if (setTabIcon && item.getCreativeTab() instanceof WizardryTabs.CreativeTabSorted) {
			((WizardryTabs.CreativeTabSorted) item.getCreativeTab()).setIconItem(new ItemStack(item));
		}

		if (item.getCreativeTab() instanceof WizardryTabs.CreativeTabListed) {
			((WizardryTabs.CreativeTabListed) item.getCreativeTab()).order.add(item);
		}
	}

	private static void registerItemBlock(IForgeRegistry<Item> registry, Block block) {
		Item itemblock = new ItemBlock(block).setRegistryName(block.getRegistryName());
		registry.register(itemblock);
	}

	private static void registerItemBlock(IForgeRegistry<Item> registry, Block block, Item itemblock) {
		itemblock.setRegistryName(block.getRegistryName());
		registry.register(itemblock);
	}

	@SubscribeEvent
	public static void register(RegistryEvent.Register<Item> event) {

		IForgeRegistry<Item> registry = event.getRegistry();

		registerItem(registry, "golemancy_spell_book", new ItemSpellBook());
		registerItem(registry, "golemancy_scroll", new ItemScroll());

		// Earth
		ItemRegistry.registerItemArtefact(registry, "ring_sandstone", WizardryGolems.MODID, new ItemGolemancyArtefact(EnumRarity.UNCOMMON, ItemArtefact.Type.RING, Element.EARTH));
		ItemRegistry.registerItemArtefact(registry, "ring_grass", WizardryGolems.MODID, new ItemGolemancyArtefact(EnumRarity.UNCOMMON, ItemArtefact.Type.RING, Element.EARTH));
		ItemRegistry.registerItemArtefact(registry, "ring_engraved_concrete", WizardryGolems.MODID, new ItemGolemancyArtefact(EnumRarity.UNCOMMON, ItemArtefact.Type.RING, Element.EARTH));
		ItemRegistry.registerItemArtefact(registry, "ring_forest_guardian", WizardryGolems.MODID, new ItemGolemancyArtefact(EnumRarity.RARE, ItemArtefact.Type.RING, Element.EARTH));
		ItemRegistry.registerItemArtefact(registry, "ring_obsidian", WizardryGolems.MODID, new ItemGolemancyArtefact(EnumRarity.RARE, ItemArtefact.Type.RING, Element.EARTH));
		ItemRegistry.registerItemArtefact(registry, "ring_glistering", WizardryGolems.MODID, new ItemGolemancyArtefact(EnumRarity.RARE, ItemArtefact.Type.RING, Element.EARTH));
		ItemRegistry.registerItemArtefact(registry, "ring_ancient_emperor", WizardryGolems.MODID, new ItemGolemancyArtefact(EnumRarity.EPIC, ItemArtefact.Type.RING, Element.EARTH));

		ItemRegistry.registerItemArtefact(registry, "amulet_snare", WizardryGolems.MODID, new ItemGolemancyArtefact(EnumRarity.RARE, ItemArtefact.Type.AMULET, Element.EARTH));
		ItemRegistry.registerItemArtefact(registry, "amulet_gaia", WizardryGolems.MODID, new ItemGolemancyArtefact(EnumRarity.EPIC, ItemArtefact.Type.AMULET, Element.EARTH));
		ItemRegistry.registerItemArtefact(registry, "amulet_deathweed", WizardryGolems.MODID, new ItemGolemancyArtefact(EnumRarity.EPIC, ItemArtefact.Type.AMULET, Element.EARTH));
		ItemRegistry.registerItemArtefact(registry, "charm_dried_mushroom", WizardryGolems.MODID, new ItemGolemancyArtefact(EnumRarity.RARE, ItemArtefact.Type.AMULET, Element.EARTH));
		// Fire
		ItemRegistry.registerItemArtefact(registry, "ring_charcoal", WizardryGolems.MODID, new ItemGolemancyArtefact(EnumRarity.RARE, ItemArtefact.Type.RING, Element.FIRE));
		ItemRegistry.registerItemArtefact(registry, "ring_smoldering", WizardryGolems.MODID, new ItemGolemancyArtefact(EnumRarity.RARE, ItemArtefact.Type.RING, Element.FIRE));
		ItemRegistry.registerItemArtefact(registry, "ring_flame_trail", WizardryGolems.MODID, new ItemGolemancyArtefact(EnumRarity.RARE, ItemArtefact.Type.RING, Element.FIRE));
		ItemRegistry.registerItemArtefact(registry, "ring_fire_golem_duration", WizardryGolems.MODID, new ItemGolemancyArtefact(EnumRarity.RARE, ItemArtefact.Type.RING, Element.FIRE));
		ItemRegistry.registerItemArtefact(registry, "ring_flame_golem", WizardryGolems.MODID, new ItemGolemancyArtefact(EnumRarity.EPIC, ItemArtefact.Type.RING, Element.FIRE));

		ItemRegistry.registerItemArtefact(registry, "amulet_steaming_netherrack", WizardryGolems.MODID, new ItemGolemancyArtefact(EnumRarity.EPIC, ItemArtefact.Type.AMULET, Element.FIRE));

		ItemRegistry.registerItemArtefact(registry, "charm_fire_golemancy_potency", WizardryGolems.MODID, new ItemGolemancyArtefact(EnumRarity.RARE, ItemArtefact.Type.CHARM, Element.FIRE));
		ItemRegistry.registerItemArtefact(registry, "charm_ifrit_bottle", WizardryGolems.MODID, new ItemGolemancyArtefact(EnumRarity.EPIC, ItemArtefact.Type.CHARM, Element.FIRE));

		// Generic
		ItemRegistry.registerItemArtefact(registry, "ring_permanent_golem", WizardryGolems.MODID, new ItemPermanentGolemRing(EnumRarity.EPIC, ItemArtefact.Type.RING, Element.MAGIC));

		// Ice
		ItemRegistry.registerItemArtefact(registry, "ring_frostbite", WizardryGolems.MODID, new ItemGolemancyArtefact(EnumRarity.UNCOMMON, ItemArtefact.Type.RING, Element.ICE));
		ItemRegistry.registerItemArtefact(registry, "ring_snow_golem", WizardryGolems.MODID, new ItemGolemancyArtefact(EnumRarity.UNCOMMON, ItemArtefact.Type.RING, Element.ICE));
		ItemRegistry.registerItemArtefact(registry, "ring_winter_golem", WizardryGolems.MODID, new ItemGolemancyArtefact(EnumRarity.EPIC, ItemArtefact.Type.RING, Element.ICE));
		ItemRegistry.registerItemArtefact(registry, "amulet_jagged_sapphire", WizardryGolems.MODID, new ItemGolemancyArtefact(EnumRarity.UNCOMMON, ItemArtefact.Type.AMULET, Element.ICE));
		ItemRegistry.registerItemArtefact(registry, "amulet_broken_ice", WizardryGolems.MODID, new ItemGolemancyArtefact(EnumRarity.UNCOMMON, ItemArtefact.Type.AMULET, Element.ICE));
		ItemRegistry.registerItemArtefact(registry, "charm_frozen_mark", WizardryGolems.MODID, new ItemGolemancyArtefact(EnumRarity.UNCOMMON, ItemArtefact.Type.CHARM, Element.ICE));
		ItemRegistry.registerItemArtefact(registry, "charm_frost_cloak", WizardryGolems.MODID, new ItemGolemancyArtefact(EnumRarity.EPIC, ItemArtefact.Type.CHARM, Element.ICE));
		ItemRegistry.registerItemArtefact(registry, "charm_living_snow", WizardryGolems.MODID, new ItemLivingSnowBottle(EnumRarity.EPIC, ItemArtefact.Type.CHARM, Element.ICE));
		ItemRegistry.registerItemArtefact(registry, "head_permafrost_crown", WizardryGolems.MODID, new ItemNewArtefact(EnumRarity.RARE, ItemNewArtefact.Type.HEAD));
		ItemRegistry.registerItemArtefact(registry, "belt_coldlink", WizardryGolems.MODID, new ItemNewArtefact(EnumRarity.RARE, ItemNewArtefact.Type.BELT));

	}
}