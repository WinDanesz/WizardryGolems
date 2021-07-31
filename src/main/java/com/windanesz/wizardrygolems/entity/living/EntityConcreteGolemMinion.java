package com.windanesz.wizardrygolems.entity.living;

import com.windanesz.wizardrygolems.WizardryGolems;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class EntityConcreteGolemMinion extends EntityEarthGolemMinion {

	public EntityConcreteGolemMinion(World world) {
		super(world);
	}

	@Override
	protected ResourceLocation applyTexture() {
		return makeTexture(WizardryGolems.MODID, "golem_concrete");
	}

	@Override
	public SoundEvent getGolemSound() {
		return SoundEvents.BLOCK_GRAVEL_STEP;
	}
}
