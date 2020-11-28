package com.windanesz.wizardrygolems.entity.living;

import com.golems.entity.GolemBase;
import com.golems.main.ExtraGolems;
import com.golems.util.GolemConfigSet;
import com.golems.util.GolemLookup;
import com.golems.util.GolemNames;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class EntityCoalGolemMinion extends EntityGolemBaseMinion {

	public static final String ALLOW_SPECIAL = "Allow Special: Blindness";

	public EntityCoalGolemMinion(World world) {
		super(world);

		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.28D);
	}

	@Override
	public void onSuccessfulAttack(EntityLivingBase target) {
		final int BLIND_CHANCE = 4;
		GolemConfigSet cfg = getConfig(this);
		if (cfg.getBoolean(ALLOW_SPECIAL) && target instanceof EntityLivingBase && this.rand.nextInt(BLIND_CHANCE) == 0) {
			target.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 20 * (3 + rand.nextInt(5)), 0));
		}
	}

	@Override
	protected ResourceLocation applyTexture() {
		return makeTexture(ExtraGolems.MODID, GolemNames.COAL_GOLEM);
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		// if burning, the fire never goes out on its own
		if (this.isBurning()) {
			this.setFire(2);
		}
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		this.updateDelegate();
	}

	@Override
	public SoundEvent getGolemSound() {
		return SoundEvents.BLOCK_STONE_STEP;
	}

	/** @return The Blocks used to build this golem, or null if there is none **/
	@Nullable
	public static Block[] getBuildingBlocks(GolemBase golem) {
		return GolemLookup.getBuildingBlocks(golem.getClass());
	}
}
