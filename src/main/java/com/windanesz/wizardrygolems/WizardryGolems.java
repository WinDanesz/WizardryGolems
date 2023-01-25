package com.windanesz.wizardrygolems;

import com.windanesz.wizardrygolems.integration.ASIntegration;
import com.windanesz.wizardrygolems.registry.BookshelfItems;
import com.windanesz.wizardrygolems.registry.WizardryGolemsBlocks;
import com.windanesz.wizardrygolems.registry.WizardryGolemsLoot;
import com.windanesz.wizardrygolems.worldgen.WorldGenLivingSnow;
import com.windanesz.wizardryutils.registry.ItemModelRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.Logger;

import java.util.Random;

@Mod(modid = WizardryGolems.MODID, name = WizardryGolems.NAME, version = WizardryGolems.VERSION, acceptedMinecraftVersions = WizardryGolems.MC_VERSION, dependencies = "required-after:ebwizardry@[@WIZARDRY_VERSION@,4.4);required-after:wizardryutils@[@WIZARDRY_UTILS_VERSION@,);required-after:golems@[@GOLEMS_VERSION@,)")
public class WizardryGolems {

	public static final String MODID = "wizardrygolems";
	public static final String NAME = "Wizardry Golems by Dan";
	public static final String VERSION = "@VERSION@";
	public static final String MC_VERSION = "[1.12.2]";

	public static final Random rand = new Random();

	/**
	 * Static instance of the {@link Settings} object for WizardryGolems.
	 */
	public static Settings settings = new Settings();

	public static Logger logger;

	// The instance of wizardry that Forge uses.
	@Mod.Instance(WizardryGolems.MODID)
	public static WizardryGolems instance;

	// Location of the proxy code, used by Forge.
	@SidedProxy(clientSide = "com.windanesz.wizardrygolems.client.ClientProxy", serverSide = "com.windanesz.wizardrygolems.CommonProxy")
	public static CommonProxy proxy;
	/**
	 * Static instance of the {@link electroblob.wizardry.Settings} object for Wizardry.
	 */

	//	public static final Logger LOG = LogManager.getLogger(MODID);
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();
		settings = new Settings();
		WizardryGolemsLoot.preInit();
		proxy.registerRenderers();
		ItemModelRegistry.registerModForAutoItemModelRegistry(MODID);

		BookshelfItems.PreInitRegisterBookShelfModelTextures();
		ASIntegration.init();
		WizardryGolemsBlocks.registerTileEntities();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {

		MinecraftForge.EVENT_BUS.register(instance); // Since there's already an instance we might as well use it

		CommonProxy.registerGolems();

		BookshelfItems.InitBookshelfItems();
		GameRegistry.registerWorldGenerator(new WorldGenLivingSnow(), 50);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {}

}
