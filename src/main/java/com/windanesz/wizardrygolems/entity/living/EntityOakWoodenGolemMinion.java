package com.windanesz.wizardrygolems.entity.living;

import com.golems.main.ExtraGolems;
import com.golems.util.GolemNames;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class EntityOakWoodenGolemMinion extends EntityEarthGolemMinion {

	public EntityOakWoodenGolemMinion(World world) {
		super(world);
		this.setCanSwim(true);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.30D);
	}

	@Override
	protected ResourceLocation applyTexture() {
		return makeTexture(ExtraGolems.MODID, GolemNames.WOODEN_GOLEM + "_oak");
	}

	@Override
	public SoundEvent getGolemSound() {
		return SoundEvents.BLOCK_WOOD_STEP;
	}
}
