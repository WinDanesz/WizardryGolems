package com.windanesz.wizardrygolems.client;

import com.golems.entity.GolemBase;
import com.golems.entity.GolemColorized;
import com.windanesz.wizardrygolems.CommonProxy;
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
import com.windanesz.wizardrygolems.entity.living.EntityWinterGolemMinion;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

import static com.golems.proxies.ClientProxy.FACTORY_COLORED_GOLEM;
import static com.golems.proxies.ClientProxy.FACTORY_TEXTURED_GOLEM;

public class ClientProxy extends CommonProxy {

	public void registerRenderers() {

		// CREATURES

		// Earth Golemancy
		registerEntityRender(EntityLeafGolemMinion.class);
		registerEntityRender(EntityClayGolemMinion.class);
		registerEntityRender(EntityMushroomGolemMinion.class);
		registerEntityRender(EntityMelonGolemMinion.class);
		registerEntityRender(EntityObsidianGolemMinion.class);
		registerEntityRender(EntitySandstoneGolemMinion.class);
		registerEntityRender(EntityConcreteGolemMinion.class);
		registerEntityRender(EntityTerracottaGolemMinion.class);
		registerEntityRender(EntityStrawThornsGolemMinion.class);

		registerEntityRender(EntityOakWoodenGolemMinion.class);

		// Fire Golemancy
		registerEntityRender(EntityNetherBrickGolemMinion.class);
		registerEntityRender(EntityMagmaGolemMinion.class);
		registerEntityRender(EntityFurnaceGolemMinion.class);
		registerEntityRender(EntityFlameGolemMinion.class);

		// Ice Golemancy
		registerEntityRender(EntityIceGolemMinion.class);
		registerEntityRender(EntityPackedIceGolemMinion.class);
		registerEntityRender(EntityPermafrostGolemMinion.class);
		registerEntityRender(EntitySnowGolemMinion.class);
		registerEntityRender(EntityWinterGolemMinion.class);

		// TODO
		registerEntityRender(EntityCoalGolemMinion.class);
		registerEntityRender(EntityPrismarineGolemMinion.class);
		registerEntityRender(EntityBoneGolemMinion.class);
		registerEntityRender(EntityCraftingGolemMinion.class);
		registerEntityRender(EntityGlowstoneGolemMinion.class);
		registerEntityRender(EntityTNTGolemMinion.class);
		registerEntityRender(EntityGoldGolemMinion.class);
		registerEntityRender(EntityBookshelfGolemMinion.class);

	}

	/**
	 * Helper function for entity rendering registration. If the class inherits from
	 * {@code GolemColorized.class}, then it will be register using
	 * {@link #registerColorized}. Otherwise, the class will be registered using
	 * {@link #registerTextured(Class)} by default.
	 */
	public static void registerEntityRender(final Class<? extends GolemBase> clazz) {
		if (GolemColorized.class.isAssignableFrom(clazz)) {
			registerColorized((Class<? extends GolemColorized>) clazz);
		} else {
			registerTextured(clazz);
		}
	}

	/**
	 * Registers an entity with the RenderGolem rendering class.
	 */
	public static void registerTextured(final Class<? extends GolemBase> golem) {
		RenderingRegistry.registerEntityRenderingHandler(golem, FACTORY_TEXTURED_GOLEM);
	}

	public static void registerColorized(final Class<? extends GolemColorized> golem) {
		RenderingRegistry.registerEntityRenderingHandler(golem, FACTORY_COLORED_GOLEM);
	}

	private static void registerRender(final Item i, final String name, int... meta) {
		if (meta.length < 1) {
			meta = new int[] {0};
		}
		final ModelResourceLocation mrl = new ModelResourceLocation(name, "inventory");
		for (final int m : meta) {
			ModelLoader.setCustomModelResourceLocation(i, m, mrl);
		}
	}

	private static void registerRender(final Item i, final int... meta) {
		registerRender(i, i.getRegistryName().toString(), meta);
	}
}

