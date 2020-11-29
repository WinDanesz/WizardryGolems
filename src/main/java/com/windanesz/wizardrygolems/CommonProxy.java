package com.windanesz.wizardrygolems;

import com.golems.entity.EntityBoneGolem;
import com.golems.entity.EntityBookshelfGolem;
import com.golems.entity.EntityClayGolem;
import com.golems.entity.EntityCoalGolem;
import com.golems.entity.EntityConcreteGolem;
import com.golems.entity.EntityCraftingGolem;
import com.golems.entity.EntityFurnaceGolem;
import com.golems.entity.EntityGlowstoneGolem;
import com.golems.entity.EntityGoldGolem;
import com.golems.entity.EntityHardenedClayGolem;
import com.golems.entity.EntityIceGolem;
import com.golems.entity.EntityLeafGolem;
import com.golems.entity.EntityMagmaGolem;
import com.golems.entity.EntityMelonGolem;
import com.golems.entity.EntityMushroomGolem;
import com.golems.entity.EntityObsidianGolem;
import com.golems.entity.EntityPrismarineGolem;
import com.golems.entity.EntitySandstoneGolem;
import com.golems.entity.EntityStrawGolem;
import com.golems.entity.EntityTNTGolem;
import com.golems.entity.EntityWoodenGolem;
import com.golems.util.GolemLookup;
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

public class CommonProxy {

	/**
	 * Called from init() in the main mod class to initialise the particle factories.
	 */
	public void registerParticles() {}

	/**
	 * Called from preInit() in the main mod class to initialise the renderers.
	 */
	public void registerRenderers() {}

	public static void registerGolems() {

		/// Golem Block placeholder

		/// Config linking
		GolemLookup.addConfig(EntityBoneGolemMinion.class, GolemLookup.getConfig(EntityBoneGolem.class));
		GolemLookup.addConfig(EntityBookshelfGolemMinion.class, GolemLookup.getConfig(EntityBookshelfGolem.class));
		GolemLookup.addConfig(EntityClayGolemMinion.class, GolemLookup.getConfig(EntityClayGolem.class));
		GolemLookup.addConfig(EntityCoalGolemMinion.class, GolemLookup.getConfig(EntityCoalGolem.class));
		GolemLookup.addConfig(EntityConcreteGolemMinion.class, GolemLookup.getConfig(EntityConcreteGolem.class));
		GolemLookup.addConfig(EntityCraftingGolemMinion.class, GolemLookup.getConfig(EntityCraftingGolem.class));
		GolemLookup.addConfig(EntityFurnaceGolemMinion.class, GolemLookup.getConfig(EntityFurnaceGolem.class));
		GolemLookup.addConfig(EntityGlowstoneGolemMinion.class, GolemLookup.getConfig(EntityGlowstoneGolem.class));
		GolemLookup.addConfig(EntityGoldGolemMinion.class, GolemLookup.getConfig(EntityGoldGolem.class));
		GolemLookup.addConfig(EntityIceGolemMinion.class, GolemLookup.getConfig(EntityIceGolem.class));
		GolemLookup.addConfig(EntityLeafGolemMinion.class, GolemLookup.getConfig(EntityLeafGolem.class));
		GolemLookup.addConfig(EntityMagmaGolemMinion.class, GolemLookup.getConfig(EntityMagmaGolem.class));
		GolemLookup.addConfig(EntityMelonGolemMinion.class, GolemLookup.getConfig(EntityMelonGolem.class));
		GolemLookup.addConfig(EntityMushroomGolemMinion.class, GolemLookup.getConfig(EntityMushroomGolem.class));
		GolemLookup.addConfig(EntityOakWoodenGolemMinion.class, GolemLookup.getConfig(EntityWoodenGolem.class));
		GolemLookup.addConfig(EntityObsidianGolemMinion.class, GolemLookup.getConfig(EntityObsidianGolem.class));
		GolemLookup.addConfig(EntityPrismarineGolemMinion.class, GolemLookup.getConfig(EntityPrismarineGolem.class));
		GolemLookup.addConfig(EntitySandstoneGolemMinion.class, GolemLookup.getConfig(EntitySandstoneGolem.class));
		GolemLookup.addConfig(EntityStrawThornsGolemMinion.class, GolemLookup.getConfig(EntityStrawGolem.class));
		GolemLookup.addConfig(EntityTNTGolemMinion.class, GolemLookup.getConfig(EntityTNTGolem.class));
		GolemLookup.addConfig(EntityTerracottaGolemMinion.class, GolemLookup.getConfig(EntityHardenedClayGolem.class));
	}
}
