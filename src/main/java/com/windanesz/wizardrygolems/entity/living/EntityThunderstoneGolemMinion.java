package com.windanesz.wizardrygolems.entity.living;

import com.windanesz.wizardrygolems.WizardryGolems;
import electroblob.wizardry.util.AllyDesignationSystem;
import electroblob.wizardry.util.EntityUtils;
import electroblob.wizardry.util.GeometryUtils;
import electroblob.wizardry.util.MagicDamage;
import electroblob.wizardry.util.ParticleBuilder;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.List;

public class EntityThunderstoneGolemMinion extends EntityGolemBaseMinion implements ILightingGolem {

	public EntityThunderstoneGolemMinion(World world) {
		super(world);
		this.setCanTakeFallDamage(true);
		this.setCanSwim(true); // just in case
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.26D);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3);
		this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(2);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(25);
		this.setSize(1.4F, 2.9F);
	}

	public EntityThunderstoneGolemMinion(World world, boolean isChild) {
		super(world);
		setChild(isChild);
		this.setCanTakeFallDamage(true);
		this.setCanSwim(true); // just in case
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.26D);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3);
		this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(2);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(25);
		this.setSize(0.7F, 1.45F);
	}

	@Override
	public void notifyDataManagerChange(DataParameter<?> key) {
		// change stats if this is a child vs. an adult golem
		if (this.isChild()) {
			this.setSize(0.7F, 1.45F);
			this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(2);
			this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(12);
		} else {
			this.setSize(1.4F, 2.9F);
			this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3);
			this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(25);
		}
	}
	protected ResourceLocation applyTexture() {
		return makeTexture(WizardryGolems.MODID, "golem_thunderstone");
	}

	/**
	 * Called frequently so the entity can update its state every tick as required.
	 * For example, zombies and skeletons use this to react to sunlight and start to
	 * burn.
	 */
	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		if(world.isRemote && this.ticksExisted % 10 == 0){
			ParticleBuilder.create(ParticleBuilder.Type.SPARK, this).spawn(world);
		}


		if(this.ticksExisted % 40 == 0){
			float radius = isChild() ? 2f : 1f;


				List<EntityLivingBase> targets = EntityUtils.getLivingWithinRadius(
						radius,
						this.posX, this.posY, this.posZ, world);

				for (EntityLivingBase target : targets) {
					if (AllyDesignationSystem.isValidTarget(this, target)) {
						// Base damage is 4 hearts no matter where the target is.
						target.attackEntityFrom(MagicDamage.causeDirectMagicDamage(this, MagicDamage.DamageType.SHOCK), 4);

						if (!world.isRemote) {

							double dx = target.posX - this.posX;
							double dz = target.posZ - this.posZ;
							// Normalises the velocity.
							double vectorLength = MathHelper.sqrt(dx * dx + dz * dz);
							dx /= vectorLength;
							dz /= vectorLength;

							if (getAttackTarget() != target) {
								target.motionX = 1.5 * dx;
								target.motionY = 0;
								target.motionZ = 1.5 * dz;
							}


							// Player motion is handled on that player's client so needs packets
							if (target instanceof EntityPlayerMP) {
								((EntityPlayerMP) target).connection.sendPacket(new SPacketEntityVelocity(target));
							}
						}
					}
				}

				if (world.isRemote) {
					ParticleBuilder.create(ParticleBuilder.Type.LIGHTNING_PULSE).pos(this.posX, this.posY
									+ GeometryUtils.ANTI_Z_FIGHTING_OFFSET, this.posZ)
							.scale(1).spawn(world);
				}
		}
	}

	@Override
	public void onSuccessfulAttack(EntityLivingBase target) {
		onSuccessFulAttackDelegate(this, target);
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		onGolemUpdate(this);
	}

	@Override
	public void onDeath(DamageSource cause) {
		onDeathDelegate(this);
		super.onDeath(cause);
	}


	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.BLOCK_GLASS_BREAK;
	}

	@Override
	public SoundEvent getGolemSound() {
		return SoundEvents.BLOCK_GLASS_STEP;
	}


}
