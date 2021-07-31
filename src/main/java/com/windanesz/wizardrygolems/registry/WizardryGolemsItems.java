package com.windanesz.wizardrygolems.registry;

import com.windanesz.wizardrygolems.WizardryGolems;
import com.windanesz.wizardrygolems.item.ItemGolemancyArtefact;
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
		registerItem(registry, "ring_sandstone", new ItemGolemancyArtefact(EnumRarity.UNCOMMON, ItemArtefact.Type.RING, Element.EARTH));
		registerItem(registry, "ring_grass", new ItemGolemancyArtefact(EnumRarity.UNCOMMON, ItemArtefact.Type.RING, Element.EARTH));
		registerItem(registry, "ring_engraved_concrete", new ItemGolemancyArtefact(EnumRarity.UNCOMMON, ItemArtefact.Type.RING, Element.EARTH));
		registerItem(registry, "ring_forest_guardian", new ItemGolemancyArtefact(EnumRarity.RARE, ItemArtefact.Type.RING, Element.EARTH));
		registerItem(registry, "ring_obsidian", new ItemGolemancyArtefact(EnumRarity.RARE, ItemArtefact.Type.RING, Element.EARTH));
		registerItem(registry, "ring_glistering", new ItemGolemancyArtefact(EnumRarity.RARE, ItemArtefact.Type.RING, Element.EARTH));
		registerItem(registry, "ring_ancient_emperor", new ItemGolemancyArtefact(EnumRarity.EPIC, ItemArtefact.Type.RING, Element.EARTH));

		registerItem(registry, "amulet_snare", new ItemGolemancyArtefact(EnumRarity.RARE, ItemArtefact.Type.AMULET, Element.EARTH));
		registerItem(registry, "amulet_gaia", new ItemGolemancyArtefact(EnumRarity.EPIC, ItemArtefact.Type.AMULET, Element.EARTH));
		registerItem(registry, "amulet_deathweed", new ItemGolemancyArtefact(EnumRarity.EPIC, ItemArtefact.Type.AMULET, Element.EARTH));

		registerItem(registry, "charm_dried_mushroom", new ItemGolemancyArtefact(EnumRarity.RARE, ItemArtefact.Type.AMULET, Element.EARTH));

		// Fire
		registerItem(registry, "ring_charcoal", new ItemGolemancyArtefact(EnumRarity.RARE, ItemArtefact.Type.RING, Element.FIRE));
		registerItem(registry, "ring_smoldering", new ItemGolemancyArtefact(EnumRarity.RARE, ItemArtefact.Type.RING, Element.FIRE));
		registerItem(registry, "ring_flame_trail", new ItemGolemancyArtefact(EnumRarity.RARE, ItemArtefact.Type.RING, Element.FIRE));
		registerItem(registry, "ring_fire_golem_duration", new ItemGolemancyArtefact(EnumRarity.RARE, ItemArtefact.Type.RING, Element.FIRE));
		registerItem(registry, "ring_flame_golem", new ItemGolemancyArtefact(EnumRarity.EPIC, ItemArtefact.Type.RING, Element.FIRE));

		registerItem(registry, "amulet_steaming_netherrack", new ItemGolemancyArtefact(EnumRarity.EPIC, ItemArtefact.Type.AMULET, Element.FIRE));

		registerItem(registry, "charm_fire_golemancy_potency", new ItemGolemancyArtefact(EnumRarity.RARE, ItemArtefact.Type.CHARM, Element.FIRE));
		registerItem(registry, "charm_ifrit_bottle", new ItemGolemancyArtefact(EnumRarity.EPIC, ItemArtefact.Type.CHARM, Element.FIRE));

	}

	public static void registerDispenseBehaviours() {
		//		BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(golemancy_scroll, new BehaviourSpellDispense());
	}
}