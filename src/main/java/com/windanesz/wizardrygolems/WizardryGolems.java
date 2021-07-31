package com.windanesz.wizardrygolems;

import com.windanesz.wizardrygolems.registry.BookshelfItems;
import com.windanesz.wizardrygolems.registry.WizardryGolemsLoot;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

import java.util.Random;

@Mod(modid = WizardryGolems.MODID, name = WizardryGolems.NAME, version = WizardryGolems.VERSION, acceptedMinecraftVersions = WizardryGolems.MC_VERSION, dependencies = "required-after:ebwizardry@[4.3,4.4);required-after:golems@[7.1.9,)")
public class WizardryGolems {

	public static final String MODID = "wizardrygolems";
	public static final String NAME = "Wizardry Golems by Dan";
	public static final String VERSION = "1.2.0";
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

		BookshelfItems.PreInitRegisterBookShelfModelTextures();

	}

	@EventHandler
	public void init(FMLInitializationEvent event) {

		MinecraftForge.EVENT_BUS.register(instance); // Since there's already an instance we might as well use it

		CommonProxy.registerGolems();

		BookshelfItems.InitBookshelfItems();
	}

}
