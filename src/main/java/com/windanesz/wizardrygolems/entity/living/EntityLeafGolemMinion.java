package com.windanesz.wizardrygolems.entity.living;

import com.golems.entity.GolemBase;
import com.golems.main.ExtraGolems;
import com.golems.util.GolemConfigSet;
import com.golems.util.GolemNames;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

/**
 * Based on {@link com.golems.entity.EntityLeafGolem} - Author: skyjay1
 * Author: WinDanesz
 */
public class EntityLeafGolemMinion extends EntityColorizedGolemMinion implements IEarthGolem {

	public static final String ALLOW_SPECIAL = "Allow Special: Regeneration";

	private static final ResourceLocation TEXTURE_BASE = GolemBase.makeTexture(ExtraGolems.MODID, GolemNames.LEAF_GOLEM);
	private static final ResourceLocation TEXTURE_OVERLAY = GolemBase.makeTexture(ExtraGolems.MODID,
			GolemNames.LEAF_GOLEM + "_grayscale");

	public EntityLeafGolemMinion(World world) {
		super(world, 0x5F904A, TEXTURE_BASE, TEXTURE_OVERLAY);
		this.setCanSwim(true);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.31D);
	}

	public EntityLeafGolemMinion(World world, boolean isChild) {
		super(world);
		setChild(isChild);
		this.setCanSwim(true);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.28D);
		this.setSize(1.4F, 2.9F);
	}

	/**
	 * Called frequently so the entity can update its state every tick as required.
	 * For example, zombies and skeletons use this to react to sunlight and start to
	 * burn.
	 */
	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		GolemConfigSet cfg = getConfig(this);
		if (cfg.getBoolean(ALLOW_SPECIAL) && this.getActivePotionEffect(MobEffects.REGENERATION) == null
				&& rand.nextInt(40) == 0) {
			this.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 200 + 20 * (1 + rand.nextInt(8)), 1));
		}

		if (this.ticksExisted % 10 == 2 && this.world.isRemote) {
			Biome biome = this.world.getBiome(this.getPosition());
			long color = biome.getFoliageColorAtPos(this.getPosition());
			this.setColor(color);
		}

		// slow falling for this entity
		if (this.motionY < -0.1D) {
			this.motionY *= 4.0D / 5.0D;
		}
	}

	@Override
	public void onUpdate(){
		super.onUpdate();
		this.updateDelegate();
	}

	@Override
	public SoundEvent getGolemSound() {
		return SoundEvents.BLOCK_GRASS_STEP;
	}


	@Override
	public void onDeath(DamageSource cause) {
		onDeathDelegate(this);
		super.onDeath(cause);
	}

}
