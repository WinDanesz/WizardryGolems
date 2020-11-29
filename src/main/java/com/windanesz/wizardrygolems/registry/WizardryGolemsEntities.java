package com.windanesz.wizardrygolems.registry;

import com.golems.util.GolemLookup;
import com.windanesz.wizardrygolems.WizardryGolems;
import com.windanesz.wizardrygolems.entity.living.EntityBoneGolemMinion;
import com.windanesz.wizardrygolems.entity.living.EntityBookshelfGolemMinion;
import com.windanesz.wizardrygolems.entity.living.EntityClayGolemMinion;
import com.windanesz.wizardrygolems.entity.living.EntityCoalGolemMinion;
import com.windanesz.wizardrygolems.entity.living.EntityConcreteGolemMinion;
import com.windanesz.wizardrygolems.entity.living.EntityCraftingGolemMinion;
import com.windanesz.wizardrygolems.entity.living.EntityFurnaceGolemMinion;
import com.windanesz.wizardrygolems.entity.living.EntityGlowstoneGolemMinion;
import com.windanesz.wizardrygolems.entity.living.EntityGoldGolemMinion;
import com.windanesz.wizardrygolems.entity.living.EntityIceGolemMinion;
import com.windanesz.wizardrygolems.entity.living.EntityLeafGolemMinion;
import com.windanesz.wizardrygolems.entity.living.EntityMagmaGolemMinion;
import com.windanesz.wizardrygolems.entity.living.EntityMelonGolemMinion;
import com.windanesz.wizardrygolems.entity.living.EntityMushroomGolemMinion;
import com.windanesz.wizardrygolems.entity.living.EntityOakWoodenGolemMinion;
import com.windanesz.wizardrygolems.entity.living.EntityObsidianGolemMinion;
import com.windanesz.wizardrygolems.entity.living.EntityPrismarineGolemMinion;
import com.windanesz.wizardrygolems.entity.living.EntitySandstoneGolemMinion;
import com.windanesz.wizardrygolems.entity.living.EntityStrawThornsGolemMinion;
import com.windanesz.wizardrygolems.entity.living.EntityTNTGolemMinion;
import com.windanesz.wizardrygolems.entity.living.EntityTerracottaGolemMinion;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber
public class WizardryGolemsEntities {

	private WizardryGolemsEntities() {} // No instances!

	/**
	 * Most entity trackers fall into one of a few categories, so they are defined here for convenience. This
	 * generally follows the values used in vanilla for each entity type.
	 */
	enum TrackingType {

		LIVING(80, 3, true),
		PROJECTILE(64, 1, true),
		CONSTRUCT(160, 10, false);

		int range;
		int interval;
		boolean trackVelocity;

		TrackingType(int range, int interval, boolean trackVelocity) {
			this.range = range;
			this.interval = interval;
			this.trackVelocity = trackVelocity;
		}
	}

	/**
	 * Incrementing index for the mod-specific entity network ID.
	 */
	private static int id = 0;

	@SubscribeEvent
	public static void register(RegistryEvent.Register<EntityEntry> event) {

		IForgeRegistry<EntityEntry> registry = event.getRegistry();

		// mobs
		registry.register(createEntry(EntityMagmaGolemMinion.class, "golem_magma_minion", TrackingType.LIVING).build());
		registry.register(createEntry(EntityBoneGolemMinion.class, "golem_bone_minion", TrackingType.LIVING).build());
		registry.register(createEntry(EntityBookshelfGolemMinion.class, "golem_bookshelf_minion", TrackingType.LIVING).build());
		registry.register(createEntry(EntityClayGolemMinion.class, "golem_clay_minion", TrackingType.LIVING).build());
		registry.register(createEntry(EntityCoalGolemMinion.class, "golem_coal_minion", TrackingType.LIVING).build());
		registry.register(createEntry(EntityConcreteGolemMinion.class, "golem_concrete_minion", TrackingType.LIVING).build());
		registry.register(createEntry(EntityCraftingGolemMinion.class, "golem_crafting_minion", TrackingType.LIVING).build());
		registry.register(createEntry(EntityFurnaceGolemMinion.class, "golem_furnace_minion", TrackingType.LIVING).build());
		registry.register(createEntry(EntityGlowstoneGolemMinion.class, "golem_glowstone_minion", TrackingType.LIVING).build());
		registry.register(createEntry(EntityGoldGolemMinion.class, "golem_gold_minion", TrackingType.LIVING).build());
		registry.register(createEntry(EntityIceGolemMinion.class, "golem_ice_minion", TrackingType.LIVING).build());
		registry.register(createEntry(EntityLeafGolemMinion.class, "golem_leaf_minion", TrackingType.LIVING).build());
		registry.register(createEntry(EntityMelonGolemMinion.class, "golem_melon_minion", TrackingType.LIVING).build());
		registry.register(createEntry(EntityMushroomGolemMinion.class, "golem_mushroom_minion", TrackingType.LIVING).build());
		registry.register(createEntry(EntityOakWoodenGolemMinion.class, "golem_wooden_oak_minion", TrackingType.LIVING).build());
		registry.register(createEntry(EntityObsidianGolemMinion.class, "golem_obsidian_minion", TrackingType.LIVING).build());
		registry.register(createEntry(EntityPrismarineGolemMinion.class, "golem_prismarine_minion", TrackingType.LIVING).build());
		registry.register(createEntry(EntitySandstoneGolemMinion.class, "golem_sandstone_minion", TrackingType.LIVING).build());
		registry.register(createEntry(EntityStrawThornsGolemMinion.class, "golem_straw_minion", TrackingType.LIVING).build());
		registry.register(createEntry(EntityTNTGolemMinion.class, "golem_tnt_minion", TrackingType.LIVING).build());
		registry.register(createEntry(EntityTerracottaGolemMinion.class, "golem_terracotta_minion", TrackingType.LIVING).build());


		GolemLookup.addGolem(EntityBoneGolemMinion.class, Blocks.BEDROCK);
		GolemLookup.addGolem(EntityBookshelfGolemMinion.class, Blocks.BEDROCK);
		GolemLookup.addGolem(EntityClayGolemMinion.class, Blocks.BEDROCK);
		GolemLookup.addGolem(EntityCoalGolemMinion.class, Blocks.BEDROCK);
		GolemLookup.addGolem(EntityConcreteGolemMinion.class, Blocks.BEDROCK);
		GolemLookup.addGolem(EntityCraftingGolemMinion.class, Blocks.BEDROCK);
		GolemLookup.addGolem(EntityFurnaceGolemMinion.class, Blocks.BEDROCK);
		GolemLookup.addGolem(EntityGlowstoneGolemMinion.class, Blocks.BEDROCK);
		GolemLookup.addGolem(EntityGoldGolemMinion.class, Blocks.BEDROCK);
		GolemLookup.addGolem(EntityIceGolemMinion.class, Blocks.BEDROCK);
		GolemLookup.addGolem(EntityLeafGolemMinion.class, Blocks.BEDROCK);
		GolemLookup.addGolem(EntityMagmaGolemMinion.class, Blocks.BEDROCK);
		GolemLookup.addGolem(EntityMelonGolemMinion.class, Blocks.BEDROCK);
		GolemLookup.addGolem(EntityMushroomGolemMinion.class, Blocks.BEDROCK);
		GolemLookup.addGolem(EntityOakWoodenGolemMinion.class, Blocks.BEDROCK);
		GolemLookup.addGolem(EntityObsidianGolemMinion.class, Blocks.BEDROCK);
		GolemLookup.addGolem(EntityPrismarineGolemMinion.class, Blocks.BEDROCK);
		GolemLookup.addGolem(EntitySandstoneGolemMinion.class, Blocks.BEDROCK);
		GolemLookup.addGolem(EntityStrawThornsGolemMinion.class, Blocks.BEDROCK);
		GolemLookup.addGolem(EntityTNTGolemMinion.class, Blocks.BEDROCK);
		GolemLookup.addGolem(EntityTerracottaGolemMinion.class, Blocks.BEDROCK);

		//		registry.register(createEntry(EntityImbuementTriggerer.class, 		"entity_imbuement_triggerer", 		TrackingType.CONSTRUCT).build());

	}

	/**
	 * Private helper method that simplifies the parts of an {@link EntityEntry} that are common to all entities.
	 * This automatically assigns a network id, and accepts a {@link TrackingType} for automatic tracker assignment.
	 *
	 * @param entityClass The entity class to use.
	 * @param name        The name of the entity. This will form the path of a {@code ResourceLocation} with domain
	 *                    {@code wizardrygolems}, which in turn will be used as both the registry name and the 'command' name.
	 * @param tracking    The {@link TrackingType} to use for this entity.
	 * @param <T>         The type of entity.
	 * @return The (part-built) builder instance, allowing other builder methods to be added as necessary.
	 */
	private static <T extends Entity> EntityEntryBuilder<T> createEntry(Class<T> entityClass, String name, TrackingType tracking) {
		return createEntry(entityClass, name).tracker(tracking.range, tracking.interval, tracking.trackVelocity);
	}

	/**
	 * Private helper method that simplifies the parts of an {@link EntityEntry} that are common to all entities.
	 * This automatically assigns a network id.
	 *
	 * @param entityClass The entity class to use.
	 * @param name        The name of the entity. This will form the path of a {@code ResourceLocation} with domain
	 *                    {@code wizardrygolems}, which in turn will be used as both the registry name and the 'command' name.
	 * @param <T>         The type of entity.
	 * @return The (part-built) builder instance, allowing other builder methods to be added as necessary.
	 */
	private static <T extends Entity> EntityEntryBuilder<T> createEntry(Class<T> entityClass, String name) {
		ResourceLocation registryName = new ResourceLocation(WizardryGolems.MODID, name);
		return EntityEntryBuilder.<T>create().entity(entityClass).id(registryName, id++).name(registryName.toString());
	}
}
