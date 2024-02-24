package com.windanesz.wizardrygolems.entity.living;

import com.windanesz.wizardrygolems.WizardryGolems;
import com.windanesz.wizardrygolems.registry.WizardryGolemsItems;
import electroblob.wizardry.item.ItemArtefact;
import electroblob.wizardry.registry.WizardryPotions;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import javax.annotation.Nullable;

public class EntitySnowGolemMinion extends EntityGolemBaseMinion implements IIceGolem {

	public static final ResourceLocation LOOT_TABLE = new ResourceLocation(WizardryGolems.MODID, "entities/snow_golem");

	public EntitySnowGolemMinion(World world) {
		super(world);
		setChild(true);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.28D);
		//	this.setSize(1.4F, 2.9F);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(2.5);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10);
	}

	@Override
	public void setCaster(@Nullable EntityLivingBase caster) {
		super.setCaster(caster);

		if (this.getCaster() == null) {
			this.targetTasks.taskEntries.clear();
			this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
		}
	}

	protected void initEntityAI() {
		super.initEntityAI();

	}

	@Override
	protected boolean canDropLoot() {
		if (getCaster() == null) {
			return true;
		}
		return super.canDropLoot();
	}

	// Recommended overrides
	@Override
	protected int getExperiencePoints(EntityPlayer player) {
		if (getCaster() == null) {
			return 4;
		}
		return super.getExperiencePoints(player);
	}

	@Override
	protected ResourceLocation getLootTable() {
		if (getCaster() == null) {
			return LOOT_TABLE;
		}
		return super.getLootTable();
	}

	@Override
	protected ResourceLocation applyTexture() {
		return makeTexture(WizardryGolems.MODID, "golem_snow");
	}

	/**
	 * Called frequently so the entity can update its state every tick as required.
	 * For example, zombies and skeletons use this to react to sunlight and start to
	 * burn.
	 */
	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		if (this.ticksExisted % 20 == 0) {
			final int x = MathHelper.floor(this.posX);
			final int y = MathHelper.floor(this.posY - 0.20000000298023224D);
			final int z = MathHelper.floor(this.posZ);
			final BlockPos below = new BlockPos(x, y, z);
			Biome biome = this.world.getBiome(below);
			if (biome.getTemperature(below) > 1.0F) {
				this.attackEntityFrom(DamageSource.ON_FIRE, 0.5f);
			} else if (biome.getTemperature(below) <=  0.1f) {
				this.heal(0.3f);
			}
		}
	}

	@Override
	public void onSuccessfulAttack(EntityLivingBase target) {
		onSuccessFulAttackDelegate(this, target);

		if (!this.world.isRemote && target != null && this.world.rand.nextBoolean() && getCaster() instanceof EntityPlayer
				&& ItemArtefact.isArtefactActive((EntityPlayer) getCaster(), WizardryGolemsItems.ring_frostbite)) {
			target.addPotionEffect(new PotionEffect(WizardryPotions.frost, 30, 1));
		}
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		onGolemUpdate(this);
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_SNOWMAN_DEATH;
	}

	@Override
	public SoundEvent getGolemSound() {return SoundEvents.BLOCK_SNOW_STEP;}

	@Override
	public void onDeath(DamageSource cause) {
		onDeathDelegate(this);
		super.onDeath(cause);
	}
}
