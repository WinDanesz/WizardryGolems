package com.windanesz.wizardrygolems.registry;

import com.windanesz.wizardrygolems.WizardryGolems;
import com.windanesz.wizardrygolems.entity.living.EntityClayGolemMinion;
import com.windanesz.wizardrygolems.entity.living.EntityIceGolemMinion;
import com.windanesz.wizardrygolems.entity.living.EntityNetherBrickGolemMinion;
import com.windanesz.wizardrygolems.spell.EarthGolemancy;
import com.windanesz.wizardrygolems.spell.FireGolemancy;
import com.windanesz.wizardrygolems.spell.IceGolemancy;
import electroblob.wizardry.spell.Spell;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nonnull;

@ObjectHolder(WizardryGolems.MODID)
@EventBusSubscriber
public final class WizardryGolemsSpells {

	private WizardryGolemsSpells() {} // no instances

	public static final Spell earth_golemancy = placeholder();
	public static final Spell fire_golemancy = placeholder();


	@Nonnull
	@SuppressWarnings("ConstantConditions")
	private static <T> T placeholder() { return null; }

//	public static final Spell antimagic_field = placeholder();

	@SubscribeEvent
	public static void register(RegistryEvent.Register<Spell> event) {

		IForgeRegistry<Spell> registry = event.getRegistry();

		Item[] GolemancySpellItems = {WizardryGolemsItems.golemancy_spell_book, WizardryGolemsItems.golemancy_scroll};

		// 1.0.0 Spells
		registry.register(new EarthGolemancy<>(WizardryGolems.MODID, "earth_golemancy", EntityClayGolemMinion::new));
		registry.register(new FireGolemancy<>(WizardryGolems.MODID, "fire_golemancy", EntityNetherBrickGolemMinion::new));
		registry.register(new IceGolemancy<>(WizardryGolems.MODID, "ice_golemancy", EntityIceGolemMinion::new));

	}
}
