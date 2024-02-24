package com.windanesz.wizardrygolems.entity.living;

import com.windanesz.wizardrygolems.WizardryGolems;
import electroblob.wizardry.Wizardry;
import electroblob.wizardry.entity.EntityLevitatingBlock;
import electroblob.wizardry.entity.living.EntityAIAttackSpell;
import electroblob.wizardry.entity.living.ISpellCaster;
import electroblob.wizardry.item.ItemArtefact;
import electroblob.wizardry.registry.Spells;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.registry.WizardrySounds;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.AllyDesignationSystem;
import electroblob.wizardry.util.EntityUtils;
import electroblob.wizardry.util.GeometryUtils;
import electroblob.wizardry.util.MagicDamage;
import electroblob.wizardry.util.ParticleBuilder;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class EntityLodestoneGolemMinion extends EntityGolemBaseMinion implements ILightingGolem, ISpellCaster {
	private double AISpeed = 0.5;
	private Spell continuousSpell;
	private int spellCounter;

	private static final List<Spell> attack = Collections.unmodifiableList(new ArrayList<Spell>() {{
		add(Spells.lightning_arrow);
	}});

	protected static final DataParameter<Boolean> PULL = EntityDataManager.<Boolean>createKey(EntityLodestoneGolemMinion.class, DataSerializers.BOOLEAN);

	// Can attack for 7 seconds, then must cool down for 3.
	private EntityAIAttackSpell<EntityLodestoneGolemMinion> spellAttackAI = new EntityAIAttackSpell<>(this, AISpeed, 15f, 3, 140);

	public EntityLodestoneGolemMinion(World world) {
		super(world);
		// For some reason this can't be in initEntityAI
		this.tasks.addTask(1, this.spellAttackAI);
		this.setCanTakeFallDamage(true);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.26D);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3);
		this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(2);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(40);
		this.setSize(1.4F, 2.9F);
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		this.dataManager.register(PULL, false);
	}

	public boolean isPulling() {
		return dataManager.get(PULL);
	}

	public void setPull(boolean isPulling) {
		dataManager.set(PULL, isPulling);
	}

	protected ResourceLocation applyTexture() {
		return makeTexture(WizardryGolems.MODID, "golem_lodestone");
	}

	@Override
	public List<Spell> getSpells() {
		return attack;
	}

	@Override
	public SpellModifiers getModifiers() {
		return new SpellModifiers();
	}

	@Override
	public Spell getContinuousSpell() {
		return continuousSpell;
	}

	@Override
	public void setContinuousSpell(Spell spell) {
		continuousSpell = spell;
	}

	@Override
	public void setSpellCounter(int count) {
		spellCounter = count;
	}

	@Override
	public int getSpellCounter() {
		return spellCounter;
	}

	@Override
	protected void initEntityAI() {
		this.tasks.taskEntries.clear();
		this.targetTasks.taskEntries.clear();
		this.tasks.addTask(0, new EntityAIWatchClosest(this, EntityLivingBase.class, 0));
		this.tasks.addTask(3, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityLivingBase.class,
				0, false, true, this.getTargetSelector()));
		this.setAIMoveSpeed((float) AISpeed);
	}

	/**
	 * Called frequently so the entity can update its state every tick as required.
	 * For example, zombies and skeletons use this to react to sunlight and start to
	 * burn.
	 */
	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		if (world.isRemote && this.ticksExisted % 3 == 0) {
			ParticleBuilder.create(ParticleBuilder.Type.SPARK, this).spawn(world);
			if (getAttackTarget() == null || getAttackTarget().isDead) {
				spellAttackAI.resetTask();
			}
			ParticleBuilder.create(ParticleBuilder.Type.SPARKLE, this).clr(isPulling() ? 0x4e95d4 : 0xedf9fa).spin(4, 0.02).time(60).spawn(world);
		}
		if (this.ticksExisted % 2 == 0) {

			if (isPulling()) {

				float radius = 12f;

				List<Entity> targets = EntityUtils.getEntitiesWithinRadius(radius, this.posX, this.posY,
						this.posZ, this.world, Entity.class);

				targets.removeIf(t -> !(t instanceof EntityLivingBase) || t instanceof EntityLodestoneGolemMinion);

				for (Entity target : targets) {

					if ((this.isValidTarget(target) || !AllyDesignationSystem.isAllied(this, (EntityLivingBase) target)) && this.getDistance(target) > 3) {
						double SUCTION_STRENGTH = 0.2;
						// If the target can't be moved, it isn't sucked in but is still damaged if it gets too close
						if (!(target instanceof EntityPlayer && ((getCaster() instanceof EntityPlayer && !Wizardry.settings.playersMoveEachOther)
								|| ItemArtefact.isArtefactActive((EntityPlayer) target, WizardryItems.amulet_anchoring)))) {

							EntityUtils.undoGravity(target);
							if (target instanceof EntityLevitatingBlock) {((EntityLevitatingBlock) target).suspend();}

							// Sucks the target in
							if (this.posX > target.posX && target.motionX < 1) {
								target.motionX += SUCTION_STRENGTH;
							} else if (this.posX < target.posX && target.motionX > -1) {
								target.motionX -= SUCTION_STRENGTH;
							}

							if (this.posY > target.posY && target.motionY < 1) {
								target.motionY += SUCTION_STRENGTH;
							} else if (this.posY + 2 < target.posY && target.motionY > -1) {
								target.motionY -= SUCTION_STRENGTH;
							}

							if (this.posZ > target.posZ && target.motionZ < 1) {
								target.motionZ += SUCTION_STRENGTH;
							} else if (this.posZ < target.posZ && target.motionZ > -1) {
								target.motionZ -= SUCTION_STRENGTH;
							}

							if (world.isRemote && target.ticksExisted % 3 == 0) {
								ParticleBuilder.create(ParticleBuilder.Type.DUST, target).spawn(world);
							}

							// Player motion is handled on that player's client so needs packets
							if (target instanceof EntityPlayerMP) {
								((EntityPlayerMP) target).connection.sendPacket(new SPacketEntityVelocity(target));
							}
						}

						if (this.getDistance(target) <= 2) {
							// Damages the target if it is close enough, or destroys it if it's a block
							if (target instanceof EntityFallingBlock) {
								target.playSound(WizardrySounds.ENTITY_BLACK_HOLE_BREAK_BLOCK, 0.5f,
										(rand.nextFloat() - rand.nextFloat()) * 0.2f + 1);
								IBlockState state = ((EntityFallingBlock) target).getBlock();
								if (state != null) {world.playEvent(2001, new BlockPos(target), Block.getStateId(state));}
								target.setDead();

							} else {
								if (this.getCaster() != null) {

								} else {

								}
							}
						}
					}
				}
			} else {

				float radius = 3;
				float BOUNCINESS = 0.2f;
				List<Entity> targets = EntityUtils.getEntitiesWithinRadius(radius, this.posX, this.posY,
						this.posZ, this.world, Entity.class);

				targets.removeIf(t -> !(t instanceof EntityLivingBase) || t instanceof EntityLodestoneGolemMinion);
				for (Entity target : targets) {

					if (this.isValidTarget(target)) {

						Vec3d currentPos = Arrays.stream(GeometryUtils.getVertices(target.getEntityBoundingBox()))
								.min(Comparator.comparingDouble(v -> v.distanceTo(this.getPositionVector())))
								.orElse(target.getPositionVector()); // This will never happen, it's just here to make the compiler happy

						double currentDistance = target.getDistance(this);

						// Estimate the target's position next tick
						// We have to assume the same vertex is closest or the velocity will be wrong
						Vec3d nextTickPos = currentPos.add(target.motionX, target.motionY, target.motionZ);
						double nextTickDistance = nextTickPos.distanceTo(this.getPositionVector());

						boolean flag;

						if (EntityUtils.isLiving(target)) {
							// Non-allied living entities shouldn't be inside at all
							flag = nextTickDistance <= radius;
						} else {
							// Non-living entities will bounce off if they hit the forcefield within the next tick...
							flag = (currentDistance > radius && nextTickDistance <= radius) // ...from the outside...
									|| (currentDistance < radius && nextTickDistance >= radius); // ...or from the inside
						}

						if (flag) {

							// Ring of interdiction
							if (getCaster() instanceof EntityPlayer && ItemArtefact.isArtefactActive((EntityPlayer) getCaster(),
									WizardryItems.ring_interdiction) && EntityUtils.isLiving(target)) {
								target.attackEntityFrom(MagicDamage.causeIndirectMagicDamage(this, getCaster(),
										MagicDamage.DamageType.MAGIC), 1);
							}

							Vec3d targetRelativePos = currentPos.subtract(this.getPositionVector());

							double nudgeVelocity =  0.1;
							if (EntityUtils.isLiving(target)) {nudgeVelocity = 0.25;}
							Vec3d extraVelocity = targetRelativePos.normalize().scale(nudgeVelocity);

							// ...make it bounce off!
							target.motionX = target.motionX * -BOUNCINESS + extraVelocity.x;
							target.motionY = target.motionY * -BOUNCINESS + extraVelocity.y;
							target.motionZ = target.motionZ * -BOUNCINESS + extraVelocity.z;

							// Prevents the forcefield bouncing things into the floor
							if (target.onGround && target.motionY < 0) {target.motionY = 0.1;}

							// How far the target needs to move towards the centre (negative means away from the centre)
							double distanceTowardsCentre = -(targetRelativePos.length() - radius) - (radius - nextTickDistance);
							Vec3d targetNewPos = target.getPositionVector().add(targetRelativePos.normalize().scale(distanceTowardsCentre));
							target.setPosition(targetNewPos.x, targetNewPos.y, targetNewPos.z);

							world.playSound(target.posX, target.posY, target.posZ, WizardrySounds.ENTITY_FORCEFIELD_DEFLECT,
									WizardrySounds.SPELLS, 0.3f, 1.3f, false);

							if (!world.isRemote) {
								// Player motion is handled on that player's client so needs packets
								if (target instanceof EntityPlayerMP) {
									((EntityPlayerMP) target).connection.sendPacket(new SPacketEntityVelocity(target));
								}

							} else {

								Vec3d relativeImpactPos = targetRelativePos.normalize().scale(radius);

								float yaw = (float) Math.atan2(relativeImpactPos.x, -relativeImpactPos.z);
								float pitch = (float) Math.asin(relativeImpactPos.y / radius);

								ParticleBuilder.create(ParticleBuilder.Type.FLASH).pos(this.getPositionVector().add(relativeImpactPos))
										.time(6).face((float) (yaw * 180 / Math.PI), (float) (pitch * 180 / Math.PI))
										.clr(0.9f, 0.95f, 1).spawn(world);

								for (int i = 0; i < 12; i++) {

									float yaw1 = yaw + 0.3f * (rand.nextFloat() - 0.5f) - (float) Math.PI / 2;
									float pitch1 = pitch + 0.3f * (rand.nextFloat() - 0.5f);

									float brightness = rand.nextFloat();

									double r = radius + 0.05;
									double x = this.posX + r * MathHelper.cos(yaw1) * MathHelper.cos(pitch1);
									double y = this.posY + r * MathHelper.sin(pitch1);
									double z = this.posZ + r * MathHelper.sin(yaw1) * MathHelper.cos(pitch1);

									ParticleBuilder.create(ParticleBuilder.Type.DUST).pos(x, y, z).time(6 + rand.nextInt(6))
											.face((float) (yaw1 * 180 / Math.PI) + 90, (float) (pitch1 * 180 / Math.PI)).scale(1.5f)
											.clr(0.7f + 0.3f * brightness, 0.85f + 0.15f * brightness, 1).spawn(world);
								}
							}
						}
					}
				}
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

	@Override
	public void setCanSwim(boolean canSwim) {

	}
}
