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
import com.golems.entity.EntityNetherBrickGolem;
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
import com.windanesz.wizardrygolems.entity.living.EntityFlameGolemMinion;
import com.windanesz.wizardrygolems.entity.living.EntityFurnaceGolemMinion;
import com.windanesz.wizardrygolems.entity.living.EntityGlowstoneGolemMinion;
import com.windanesz.wizardrygolems.entity.living.EntityGoldGolemMinion;
import com.windanesz.wizardrygolems.entity.living.EntityIceGolemMinion;
import com.windanesz.wizardrygolems.entity.living.EntityLeafGolemMinion;
import com.windanesz.wizardrygolems.entity.living.EntityLodestoneGolemMinion;
import com.windanesz.wizardrygolems.entity.living.EntityMagmaGolemMinion;
import com.windanesz.wizardrygolems.entity.living.EntityMelonGolemMinion;
import com.windanesz.wizardrygolems.entity.living.EntityMushroomGolemMinion;
import com.windanesz.wizardrygolems.entity.living.EntityNetherBrickGolemMinion;
import com.windanesz.wizardrygolems.entity.living.EntityOakWoodenGolemMinion;
import com.windanesz.wizardrygolems.entity.living.EntityObsidianGolemMinion;
import com.windanesz.wizardrygolems.entity.living.EntityPackedIceGolemMinion;
import com.windanesz.wizardrygolems.entity.living.EntityPermafrostGolemMinion;
import com.windanesz.wizardrygolems.entity.living.EntityPrismarineGolemMinion;
import com.windanesz.wizardrygolems.entity.living.EntitySandstoneGolemMinion;
import com.windanesz.wizardrygolems.entity.living.EntitySnowGolemMinion;
import com.windanesz.wizardrygolems.entity.living.EntityStrawThornsGolemMinion;
import com.windanesz.wizardrygolems.entity.living.EntityTNTGolemMinion;
import com.windanesz.wizardrygolems.entity.living.EntityTerracottaGolemMinion;
import com.windanesz.wizardrygolems.entity.living.EntityThunderstoneGolemMinion;
import com.windanesz.wizardrygolems.entity.living.EntityWinterGolemMinion;
import electroblob.wizardry.util.MagicDamage;

public class CommonProxy {

	/**
	 * Called from preInit() in the main mod class to initialise the renderers.
	 */
	public void registerRenderers() {}

	public static void registerGolems() {

		// -------------------- Golem Configs --------------------

		// Earth
		GolemLookup.addConfig(EntityClayGolemMinion.class, GolemLookup.getConfig(EntityClayGolem.class));
		GolemLookup.addConfig(EntityConcreteGolemMinion.class, GolemLookup.getConfig(EntityConcreteGolem.class));
		GolemLookup.addConfig(EntityLeafGolemMinion.class, GolemLookup.getConfig(EntityLeafGolem.class));
		GolemLookup.addConfig(EntityMelonGolemMinion.class, GolemLookup.getConfig(EntityMelonGolem.class));
		GolemLookup.addConfig(EntityMushroomGolemMinion.class, GolemLookup.getConfig(EntityMushroomGolem.class));
		GolemLookup.addConfig(EntityOakWoodenGolemMinion.class, GolemLookup.getConfig(EntityWoodenGolem.class));
		GolemLookup.addConfig(EntityObsidianGolemMinion.class, GolemLookup.getConfig(EntityObsidianGolem.class));
		GolemLookup.addConfig(EntitySandstoneGolemMinion.class, GolemLookup.getConfig(EntitySandstoneGolem.class));
		GolemLookup.addConfig(EntityStrawThornsGolemMinion.class, GolemLookup.getConfig(EntityStrawGolem.class));
		GolemLookup.addConfig(EntityTerracottaGolemMinion.class, GolemLookup.getConfig(EntityHardenedClayGolem.class));

		// Fire
		GolemLookup.addConfig(EntityFurnaceGolemMinion.class, GolemLookup.getConfig(EntityFurnaceGolem.class));
		GolemLookup.addConfig(EntityMagmaGolemMinion.class, GolemLookup.getConfig(EntityMagmaGolem.class));
		GolemLookup.addConfig(EntityNetherBrickGolemMinion.class, GolemLookup.getConfig(EntityNetherBrickGolem.class));
		GolemLookup.addConfig(EntityFlameGolemMinion.class, GolemLookup.getConfig(EntityObsidianGolem.class));

		// TODO
		GolemLookup.addConfig(EntityBoneGolemMinion.class, GolemLookup.getConfig(EntityBoneGolem.class));
		GolemLookup.addConfig(EntityBookshelfGolemMinion.class, GolemLookup.getConfig(EntityBookshelfGolem.class));
		GolemLookup.addConfig(EntityCoalGolemMinion.class, GolemLookup.getConfig(EntityCoalGolem.class));
		GolemLookup.addConfig(EntityCraftingGolemMinion.class, GolemLookup.getConfig(EntityCraftingGolem.class));
		GolemLookup.addConfig(EntityGlowstoneGolemMinion.class, GolemLookup.getConfig(EntityGlowstoneGolem.class));
		GolemLookup.addConfig(EntityGoldGolemMinion.class, GolemLookup.getConfig(EntityGoldGolem.class));
		GolemLookup.addConfig(EntityIceGolemMinion.class, GolemLookup.getConfig(EntityIceGolem.class));
		GolemLookup.addConfig(EntityPrismarineGolemMinion.class, GolemLookup.getConfig(EntityPrismarineGolem.class));
		GolemLookup.addConfig(EntityTNTGolemMinion.class, GolemLookup.getConfig(EntityTNTGolem.class));
		GolemLookup.addConfig(EntityThunderstoneGolemMinion.class, GolemLookup.getConfig(EntityIceGolem.class));
		GolemLookup.addConfig(EntityLodestoneGolemMinion.class, GolemLookup.getConfig(EntityIceGolem.class));
	}

	public static void setGolemImmunities() {
		// Earth
		MagicDamage.setEntityImmunities(EntityClayGolemMinion.class, MagicDamage.DamageType.POISON);
		MagicDamage.setEntityImmunities(EntityConcreteGolemMinion.class, MagicDamage.DamageType.POISON);
		MagicDamage.setEntityImmunities(EntityLeafGolemMinion.class, MagicDamage.DamageType.POISON);
		MagicDamage.setEntityImmunities(EntityMelonGolemMinion.class, MagicDamage.DamageType.POISON);
		MagicDamage.setEntityImmunities(EntityMushroomGolemMinion.class, MagicDamage.DamageType.POISON);
		MagicDamage.setEntityImmunities(EntityOakWoodenGolemMinion.class, MagicDamage.DamageType.POISON);
		MagicDamage.setEntityImmunities(EntityObsidianGolemMinion.class, MagicDamage.DamageType.POISON);
		MagicDamage.setEntityImmunities(EntitySandstoneGolemMinion.class, MagicDamage.DamageType.POISON);
		MagicDamage.setEntityImmunities(EntityStrawThornsGolemMinion.class, MagicDamage.DamageType.POISON);
		MagicDamage.setEntityImmunities(EntityTerracottaGolemMinion.class, MagicDamage.DamageType.POISON);
		// -------------------- Fire --------------------
		MagicDamage.setEntityImmunities(EntityFurnaceGolemMinion.class, MagicDamage.DamageType.FIRE);
		MagicDamage.setEntityImmunities(EntityMagmaGolemMinion.class, MagicDamage.DamageType.FIRE);
		MagicDamage.setEntityImmunities(EntityNetherBrickGolemMinion.class, MagicDamage.DamageType.FIRE);
		MagicDamage.setEntityImmunities(EntityFlameGolemMinion.class, MagicDamage.DamageType.FIRE);

		// -------------------- Ice --------------------
		MagicDamage.setEntityImmunities(EntityIceGolemMinion.class,MagicDamage.DamageType.FROST);
		MagicDamage.setEntityImmunities(EntityPackedIceGolemMinion.class,MagicDamage.DamageType.FROST);
		MagicDamage.setEntityImmunities(EntityPermafrostGolemMinion.class,MagicDamage.DamageType.FROST);
		MagicDamage.setEntityImmunities(EntitySnowGolemMinion.class,MagicDamage.DamageType.FROST);
		MagicDamage.setEntityImmunities(EntityWinterGolemMinion.class,MagicDamage.DamageType.FROST);

		// -------------------- Lightning --------------------
		MagicDamage.setEntityImmunities(EntityThunderstoneGolemMinion.class, MagicDamage.DamageType.SHOCK);
		MagicDamage.setEntityImmunities(EntityLodestoneGolemMinion.class, MagicDamage.DamageType.SHOCK);
	}
}
