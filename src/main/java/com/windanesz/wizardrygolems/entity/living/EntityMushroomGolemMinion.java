package com.windanesz.wizardrygolems.entity.living;

import com.golems.main.ExtraGolems;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class EntityMushroomGolemMinion extends EntityEarthGolemMinion {

	public EntityMushroomGolemMinion(World world) {
		super(world);
	}

	@Override
	protected ResourceLocation applyTexture() {
		return makeTexture(ExtraGolems.MODID, "golem_shroom_red");
	}

	@Override
	public SoundEvent getGolemSound() {
		return SoundEvents.BLOCK_GRAVEL_STEP;
	}

	@Override
	public void onSuccessfulAttack(EntityLivingBase target) {
		target.addPotionEffect(new PotionEffect(MobEffects.POISON, 100, 0));
	}
}
