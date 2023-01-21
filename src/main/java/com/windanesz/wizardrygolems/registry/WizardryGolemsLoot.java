package com.windanesz.wizardrygolems.registry;

import com.windanesz.wizardrygolems.WizardryGolems;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootEntryTable;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

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
		LootTableList.register(new ResourceLocation(WizardryGolems.MODID, "chests/dungeon_additions"));
	}

	@SubscribeEvent
	public static void onLootTableLoadEvent(LootTableLoadEvent event) {
		if (Arrays.asList(WizardryGolems.settings.lootInjectLocations).contains(event.getName())) {
			event.getTable().addPool(getAdditive(WizardryGolems.MODID + ":chests/dungeon_additions", WizardryGolems.MODID + "_wizardrygolems_dungeon_additions"));
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
