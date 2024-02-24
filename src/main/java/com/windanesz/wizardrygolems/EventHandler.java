package com.windanesz.wizardrygolems;

import electroblob.wizardry.spell.LightningBolt;
import electroblob.wizardry.util.AllyDesignationSystem;
import electroblob.wizardry.util.EntityUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class EventHandler {

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onEntityStruckByLightningEvent(EntityStruckByLightningEvent event) {
		if (event.getEntity() instanceof EntityLivingBase && event.getLightning().getEntityData().hasUniqueId(LightningBolt.SUMMONER_NBT_KEY)) {
			Entity summoner = EntityUtils.getEntityByUUID(event.getLightning().world,
					event.getLightning().getEntityData().getUniqueId(LightningBolt.SUMMONER_NBT_KEY));

			if (summoner instanceof EntityLivingBase && summoner == event.getEntity() || AllyDesignationSystem.isAllied((EntityLivingBase) summoner, (EntityLivingBase) event.getEntity())) {
				event.getEntity().getEntityData().setBoolean(LightningBolt.IMMUNE_TO_LIGHTNING_NBT_KEY, true);
				event.setCanceled(true);
			}
		}
	}

}
