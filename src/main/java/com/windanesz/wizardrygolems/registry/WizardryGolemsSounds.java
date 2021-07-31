package com.windanesz.wizardrygolems.registry;

import com.windanesz.wizardrygolems.WizardryGolems;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;


@GameRegistry.ObjectHolder(WizardryGolems.MODID)
@Mod.EventBusSubscriber(modid = WizardryGolems.MODID)
public class WizardryGolemsSounds {

	private WizardryGolemsSounds() {} // no instances!

	public static final SoundEvent FLAME_GOLEM_SOUND = createSound("flame_golem_sound");

	public static SoundEvent createSound(String name) {
		return createSound(WizardryGolems.MODID, name);
	}

	/**
	 * Creates a sound with the given name, to be read from {@code assets/[modID]/sounds.json}.
	 */
	public static SoundEvent createSound(String modID, String name) {
		// All the setRegistryName methods delegate to this one, it doesn't matter which you use.
		return new SoundEvent(new ResourceLocation(modID, name)).setRegistryName(name);
	}

	@SubscribeEvent
	public static void register(RegistryEvent.Register<SoundEvent> event) {
		event.getRegistry().register(FLAME_GOLEM_SOUND);
	}
}