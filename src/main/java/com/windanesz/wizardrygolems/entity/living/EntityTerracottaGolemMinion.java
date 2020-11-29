package com.windanesz.wizardrygolems.entity.living;

import com.golems.main.ExtraGolems;
import com.golems.util.GolemNames;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class EntityTerracottaGolemMinion extends EntityEarthGolemMinion {

	public EntityTerracottaGolemMinion(final World world) {
		super(world);
	}

	public EntityTerracottaGolemMinion(World world, boolean isChild) {
		super(world);
		setChild(isChild);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.28D);
		this.setSize(1.4F, 2.9F);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1.5);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(5);
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		this.updateDelegate();
	}

	protected ResourceLocation applyTexture() {
		return makeTexture(ExtraGolems.MODID, GolemNames.TERRACOTTA_GOLEM);
	}

	@Override
	public SoundEvent getGolemSound() {
		return SoundEvents.BLOCK_STONE_STEP;
	}

}