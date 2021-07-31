package com.windanesz.wizardrygolems.entity.living;

import com.golems.main.ExtraGolems;
import com.golems.util.GolemNames;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class EntityObsidianGolemMinion extends EntityEarthGolemMinion {

	public EntityObsidianGolemMinion(World world) {
		super(world);
		this.setImmuneToFire(true);
	}

	@Override
	protected ResourceLocation applyTexture() {
		return makeTexture(ExtraGolems.MODID, GolemNames.OBSIDIAN_GOLEM);
	}

	@Override
	public SoundEvent getGolemSound() {
		return SoundEvents.BLOCK_GRAVEL_STEP;
	}
}
