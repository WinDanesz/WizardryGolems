package com.windanesz.wizardrygolems;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static electroblob.wizardry.Settings.toResourceLocations;

@Config(modid = WizardryGolems.MODID, name = "WizardryGolems") // No fancy configs here so we can use the annotation, hurrah!
public class Settings {

	public ResourceLocation[] artefactInjectionLocations = toResourceLocations(generalSettings.ARTEFACT_INJECTION_LOCATIONS);
	public ResourceLocation[] lootInjectLocations = toResourceLocations(generalSettings.LOOT_INJECTION_LOCATIONS);

	@SuppressWarnings("unused")
	@Mod.EventBusSubscriber(modid = WizardryGolems.MODID)
	private static class EventHandler {
		/**
		 * Inject the new values and save to the config file when the config has been changed from the GUI.
		 *
		 * @param event The event
		 */
		@SubscribeEvent
		public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
			if (event.getModID().equals(WizardryGolems.MODID)) {
				ConfigManager.sync(WizardryGolems.MODID, Config.Type.INSTANCE);
			}
		}
	}

	@Config.Name("General Settings")
	public static GeneralSettings generalSettings = new GeneralSettings();

	public static class GeneralSettings {

		@Config.Name("Artefact Inject locations")
		@Config.Comment("List of loot tables to inject Wizardry Golems artefacts (as specified in loot_tables/subsets/...) into.")
		private String[] ARTEFACT_INJECTION_LOCATIONS = {
				"ebwizardry:subsets/uncommon_artefacts",
				"ebwizardry:subsets/rare_artefacts",
				"ebwizardry:subsets/epic_artefacts"};

		@Config.Name("Loot Inject locations")
		@Config.Comment("List of loot tables to inject wizardrygolems loot (as specified in loot_tables/chests/dungeon_additions.json) into.")
		private String[] LOOT_INJECTION_LOCATIONS = {"minecraft:chests/simple_dungeon",
				"minecraft:chests/abandoned_mineshaft", "minecraft:chests/desert_pyramid", "minecraft:chests/jungle_temple",
				"minecraft:chests/stronghold_corridor", "minecraft:chests/stronghold_crossing",
				"minecraft:chests/stronghold_library", "minecraft:chests/igloo_chest", "minecraft:chests/woodland_mansion",
				"minecraft:chests/end_city_treasure"};
	}
}