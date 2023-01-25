package com.windanesz.wizardrygolems.registry;

import com.windanesz.wizardrygolems.WizardryGolems;
import com.windanesz.wizardrygolems.block.BlockLivingSnow;
import com.windanesz.wizardrygolems.block.BlockPermafrostImpl;
import com.windanesz.wizardrygolems.tile.TileEntityLivingSnow;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nonnull;

@GameRegistry.ObjectHolder(WizardryGolems.MODID)
@Mod.EventBusSubscriber
public class WizardryGolemsBlocks {

	private WizardryGolemsBlocks() {} // no instances

	public static final Block permafrost_custom = placeholder();
	public static final Block living_snow = placeholder();

	@Nonnull
	@SuppressWarnings("ConstantConditions")
	private static <T> T placeholder() { return null; }

	public static void registerBlock(IForgeRegistry<Block> registry, String name, Block block) {
		block.setRegistryName(WizardryGolems.MODID, name);
		block.setTranslationKey(block.getRegistryName().toString());
		registry.register(block);
	}

	@SubscribeEvent
	public static void register(RegistryEvent.Register<Block> event) {

		IForgeRegistry<Block> registry = event.getRegistry();
		registerBlock(registry, "permafrost_custom",  new BlockPermafrostImpl());
		registerBlock(registry, "living_snow",  new BlockLivingSnow());
	}

	/** Called from the preInit method in the main mod class to register all the tile entities. */
	public static void registerTileEntities() {
		// Nope, these still don't have their own registry...
		GameRegistry.registerTileEntity(TileEntityLivingSnow.class, new ResourceLocation(WizardryGolems.MODID, "living_snow"));
	}
}
