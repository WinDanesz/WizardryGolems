package com.windanesz.wizardrygolems.registry;

import com.windanesz.wizardrygolems.WizardryGolems;
import electroblob.wizardry.Wizardry;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.LootEntryTable;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class responsible for registering Wizardry Golem's loot tables. Also handles loot injection.
 * <p>
 * Based on Electroblob's TFSpellPack and Wizardry Loot registry classes
 *
 * @author WinDanesz
 */
@Mod.EventBusSubscriber
public class WizardryGolemsLoot {

	private WizardryGolemsLoot() {}

	/**
	 * Called from the preInit method in the main mod class to register the custom dungeon loot.
	 */
	public static void preInit() {
		// subsets
		LootTableList.register(new ResourceLocation(WizardryGolems.MODID, "subsets/uncommon_artefacts"));
		LootTableList.register(new ResourceLocation(WizardryGolems.MODID, "subsets/rare_artefacts"));
		LootTableList.register(new ResourceLocation(WizardryGolems.MODID, "subsets/epic_artefacts"));
		LootTableList.register(new ResourceLocation(WizardryGolems.MODID, "chests/dungeon_additions"));
	}

	@SubscribeEvent
	public static void onLootTableLoadEvent(LootTableLoadEvent event) {
		if (Arrays.asList(WizardryGolems.settings.lootInjectLocations).contains(event.getName())) {
			event.getTable().addPool(getAdditive(WizardryGolems.MODID + ":chests/dungeon_additions", WizardryGolems.MODID + "_wizardrygolems_dungeon_additions"));
		}

		// Injecting artefact loot into artefact tables
		if (Arrays.asList(WizardryGolems.settings.artefactInjectionLocations).contains(event.getName())) {
			if (event.getName().toString().equals(Wizardry.MODID + ":subsets/uncommon_artefacts")) {
				LootPool targetPool = event.getTable().getPool("uncommon_artefacts");

				List<LootEntry> uncommon_artefacts = new ArrayList<>();
				uncommon_artefacts.add(new LootEntryItem(WizardryGolemsItems.ring_sandstone, 1, 0, new LootFunction[] {}, new LootCondition[] {}, "ring_sandstone"));
				uncommon_artefacts.add(new LootEntryItem(WizardryGolemsItems.ring_grass, 1, 0, new LootFunction[] {}, new LootCondition[] {}, "ring_grass"));
				uncommon_artefacts.add(new LootEntryItem(WizardryGolemsItems.ring_engraved_concrete, 1, 0, new LootFunction[] {}, new LootCondition[] {}, "ring_engraved_concrete"));

				for (LootEntry entry : uncommon_artefacts) {
					targetPool.addEntry(entry);
				}

			} else if (event.getName().toString().equals(Wizardry.MODID + ":subsets/rare_artefacts")) {
				LootPool targetPool = event.getTable().getPool("rare_artefacts");
				List<LootEntry> rare_artefacts = new ArrayList<>();
				
				// Earth
				rare_artefacts.add(new LootEntryItem(WizardryGolemsItems.ring_forest_guardian, 1, 0, new LootFunction[] {}, new LootCondition[] {}, "ring_forest_guardian"));
				rare_artefacts.add(new LootEntryItem(WizardryGolemsItems.ring_obsidian, 1, 0, new LootFunction[] {}, new LootCondition[] {}, "ring_obsidian"));
				rare_artefacts.add(new LootEntryItem(WizardryGolemsItems.ring_glistering, 1, 0, new LootFunction[] {}, new LootCondition[] {}, "ring_glistering"));
				rare_artefacts.add(new LootEntryItem(WizardryGolemsItems.amulet_snare, 1, 0, new LootFunction[] {}, new LootCondition[] {}, "amulet_snare"));
				rare_artefacts.add(new LootEntryItem(WizardryGolemsItems.charm_dried_mushroom, 1, 0, new LootFunction[] {}, new LootCondition[] {}, "charm_dried_mushroom"));

				// Fire
				rare_artefacts.add(new LootEntryItem(WizardryGolemsItems.ring_charcoal, 1, 0, new LootFunction[] {}, new LootCondition[] {}, "ring_charcoal"));
				rare_artefacts.add(new LootEntryItem(WizardryGolemsItems.ring_smoldering, 1, 0, new LootFunction[] {}, new LootCondition[] {}, "ring_smoldering"));
				rare_artefacts.add(new LootEntryItem(WizardryGolemsItems.ring_flame_trail, 1, 0, new LootFunction[] {}, new LootCondition[] {}, "ring_flame_trail"));
				rare_artefacts.add(new LootEntryItem(WizardryGolemsItems.ring_fire_golem_duration, 1, 0, new LootFunction[] {}, new LootCondition[] {}, "ring_fire_golem_duration"));
				rare_artefacts.add(new LootEntryItem(WizardryGolemsItems.charm_fire_golemancy_potency, 1, 0, new LootFunction[] {}, new LootCondition[] {}, "charm_fire_golemancy_potency"));

				for (LootEntry entry : rare_artefacts) {
					targetPool.addEntry(entry);
				}

			} else if (event.getName().toString().equals(Wizardry.MODID + ":subsets/epic_artefacts")) {
				LootPool targetPool = event.getTable().getPool("epic_artefacts");
				List<LootEntry> epic_artefacts = new ArrayList<>();

				// Earth
				epic_artefacts.add(new LootEntryItem(WizardryGolemsItems.ring_ancient_emperor, 1, 0, new LootFunction[] {}, new LootCondition[] {}, "ring_ancient_emperor"));
				epic_artefacts.add(new LootEntryItem(WizardryGolemsItems.amulet_gaia, 1, 0, new LootFunction[] {}, new LootCondition[] {}, "amulet_gaia"));
				epic_artefacts.add(new LootEntryItem(WizardryGolemsItems.amulet_deathweed, 1, 0, new LootFunction[] {}, new LootCondition[] {}, "amulet_deathweed"));

				// Fire
				epic_artefacts.add(new LootEntryItem(WizardryGolemsItems.amulet_steaming_netherrack, 1, 0, new LootFunction[] {}, new LootCondition[] {}, "amulet_steaming_netherrack"));
				epic_artefacts.add(new LootEntryItem(WizardryGolemsItems.ring_flame_golem, 1, 0, new LootFunction[] {}, new LootCondition[] {}, "ring_flame_golem"));


				for (LootEntry entry : epic_artefacts) {
					targetPool.addEntry(entry);
				}
			}

		}
	}

	private static void injectEntries(LootPool sourcePool, LootPool targetPool) {
		// Accessing {@link net.minecraft.world.storage.loot.LootPool.lootEntries}
		List<LootEntry> lootEntries = ObfuscationReflectionHelper.getPrivateValue(LootPool.class, sourcePool, "field_186453_a");

		for (LootEntry entry : lootEntries) {
			targetPool.addEntry(entry);
		}
	}

	private static LootPool getAdditive(String entryName, String poolName) {
		return new LootPool(new LootEntry[] {getAdditiveEntry(entryName, 1)}, new LootCondition[0],
				new RandomValueRange(1), new RandomValueRange(0, 1), WizardryGolems.MODID + "_" + poolName);
	}

	private static LootEntryTable getAdditiveEntry(String name, int weight) {
		return new LootEntryTable(new ResourceLocation(name), weight, 0, new LootCondition[0],
				WizardryGolems.MODID + "_additive_entry");
	}

}
