package com.windanesz.wizardrygolems.entity.living;

import com.windanesz.wizardrygolems.WizardryGolems;
import electroblob.wizardry.entity.construct.EntityStormcloud;
import electroblob.wizardry.entity.living.EntityAIAttackSpell;
import electroblob.wizardry.entity.living.ISpellCaster;
import electroblob.wizardry.registry.Spells;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.ParticleBuilder;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EntityMaelstromGolemMinion extends EntityGolemBaseMinion implements ILightingGolem, ISpellCaster {
	private static final List<Spell> attack = Collections.unmodifiableList(new ArrayList<Spell>() {{
		add(Spells.lightning_arrow);
		add(Spells.lightning_ray);
	}});
	private double AISpeed = 0.5;
	private Spell continuousSpell;
	private int spellCounter;
	private EntityStormcloud cloud;
	private EntityAIAttackSpell<EntityMaelstromGolemMinion> spellAttackAI = new EntityAIAttackSpell<>(this, AISpeed, 15f, 5, 140);

	public EntityMaelstromGolemMinion(World world) {
		super(world);
		// For some reason this can't be in initEntityAI
		this.tasks.addTask(1, this.spellAttackAI);
		this.setCanTakeFallDamage(true);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.26D);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3);
		this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1f);
		this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(2);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(80);

		this.setSize(1.4F, 2.9F);
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

	protected ResourceLocation applyTexture() {
		return makeTexture(WizardryGolems.MODID, "golem_spark");
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
	public int getSpellCounter() {
		return spellCounter;
	}

	@Override
	public void setSpellCounter(int count) {
		spellCounter = count;
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
		}

		if (!this.world.isRemote && this.ticksExisted == 40) {
			EntityStormcloud cloud = new EntityStormcloud(this.world);
			if (this.getLifetime() != -1) {
				cloud.lifetime = (this.getLifetime());
			}
			cloud.setCaster(this.getCaster() == null ? this : this.getCaster());
			cloud.setSizeMultiplier(3);
			cloud.setPosition(this.posX - 10, this.posY + 10, this.posZ - 10);
			cloud.lifetime = 600;
			world.spawnEntity(cloud);
			this.cloud = cloud;
			cloud.motionX = 0;
			cloud.motionZ = 0;
		}

		if (!world.isRemote && cloud != null && this.ticksExisted % 10 == 0) {
			Vec3d vel = this.getPositionVector().subtract(cloud.getPositionVector()).normalize().scale(2);
			cloud.motionX = vel.x;
			cloud.motionZ = vel.z;
		}

		if (world.isRemote) {
			if (ticksExisted < 60) {
				for (int i = 0; i < 3; i++) {
					double speed = (rand.nextBoolean() ? 1 : -1) * (0.1 + 0.01 * rand.nextDouble());
					ParticleBuilder.create(ParticleBuilder.Type.CLOUD).entity(this).vel(0, 0.4, 0)
							.clr(0.3f, 0.3f, 0.3f).shaded(true).time(30).scale(1).spin(rand.nextDouble() * (1 - 0.5) + 0.5, speed).shaded(true).spawn(world);
				}
			}

			for (int i = 0; i < 2; i++) {
				double speed = (rand.nextBoolean() ? 1 : -1) * (0.05 + 0.02 * rand.nextDouble());
				ParticleBuilder.create(ParticleBuilder.Type.CLOUD).entity(this).pos(0, rand.nextDouble() * height * 0.8f, 0)
						.clr(0.3f, 0.3f, 0.3f).shaded(false).spin(rand.nextDouble() * +0.5, speed).spawn(world);
			}

			// clouds circling around the golem
			//	ParticleBuilder.create(ParticleBuilder.Type.CLOUD, this).scale(0.7f).clr(0.3f, 0.3f, 0.3f).spin(4 * Math.max(0.7f, rand.nextFloat()), 0.02).time(60).spawn(world);

			double speed = (rand.nextBoolean() ? 1 : -1) * (0.1 + 0.05 * rand.nextDouble());
			ParticleBuilder.create(ParticleBuilder.Type.SPARK).entity(this).pos(0, rand.nextDouble() * height, 0).vel(0, 0, 0)
					.time(30).scale(2).spin(rand.nextDouble() * (1 - 0.5) + 0.5, speed).shaded(true).spawn(world);
			speed = (rand.nextBoolean() ? 1 : -1) * (0.1 + 0.05 * rand.nextDouble());
			// purple spark
			ParticleBuilder.create(ParticleBuilder.Type.SPARK).entity(this).clr(0xb369d1).pos(0, rand.nextDouble() * height, 0).vel(0, 0, 0)
					.time(10).scale(1.5f).spin(rand.nextDouble() * (1 - 0.5) + 0.5, speed).shaded(true).spawn(world);

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
	public boolean attackEntityFrom(DamageSource source, float amount) {
		if (!source.isMagicDamage()) {
			amount = amount * 0.25f;
		}
		return super.attackEntityFrom(source, amount);
	}

	@Override
	protected float applyPotionDamageCalculations(DamageSource source, float damage) {
		damage = super.applyPotionDamageCalculations(source, damage);
		if (source.isMagicDamage()) {
			damage *= 1.3f; // Remnants are 30% more vulnerable to magic damage
		}
		return damage;
	}
}
