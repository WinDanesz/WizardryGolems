package com.windanesz.wizardrygolems.entity.living;

import com.windanesz.wizardrygolems.WizardryGolems;
import electroblob.wizardry.entity.living.EntityAIAttackSpell;
import electroblob.wizardry.entity.living.ISpellCaster;
import electroblob.wizardry.registry.Spells;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.registry.WizardryPotions;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.ParticleBuilder;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EntitySparkGolemMinion extends EntityGolemBaseMinion implements ILightingGolem, ISpellCaster {
	private double AISpeed = 0.5;
	private Spell continuousSpell;
	private int spellCounter;

	private static final List<Spell> attack = Collections.unmodifiableList(new ArrayList<Spell>() {{
		add(Spells.lightning_arrow);
	}});

	private EntityAIAttackSpell<EntitySparkGolemMinion> spellAttackAI = new EntityAIAttackSpell<>(this, AISpeed, 15f, 30, 140);
	private EntityAIAttackMelee meleeAI = new EntityAIAttackMelee(this, 1.0D, false);

	public EntitySparkGolemMinion(World world) {
		super(world);
		// For some reason this can't be in initEntityAI
		this.tasks.addTask(1, this.spellAttackAI);
		this.setCanTakeFallDamage(true);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.26D);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30);
		this.setSize(1.4F, 2.9F);
	}

	@Override
	protected void initEntityAI() {
		super.initEntityAI();
		removeTask(EntityAIAttackMelee.class);
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
	public void setSpellCounter(int count) {
		spellCounter = count;
	}

	@Override
	public int getSpellCounter() {
		return spellCounter;
	}

	/**
	 * Called frequently so the entity can update its state every tick as required.
	 * For example, zombies and skeletons use this to react to sunlight and start to
	 * burn.
	 */
	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		if (world.isRemote) {
			for (int i = 0; i < 5; i++) {
				ParticleBuilder.create(ParticleBuilder.Type.SPARK, this).spawn(world);
			}
		}

		if (this.ticksExisted % 5 == 0 && getAttackTarget() != null) {
			if (this.getDistance(this.getAttackTarget()) > 3) {
				this.tasks.addTask(1, spellAttackAI);
				this.tasks.removeTask(meleeAI);
			} else {
				this.tasks.removeTask(spellAttackAI);
				this.tasks.addTask(1, meleeAI);
			}
		}

		if (!this.isPotionActive(WizardryPotions.static_aura)) {
			this.addPotionEffect(new PotionEffect(WizardryPotions.static_aura, 60));
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

	@Override
	public boolean attackEntityAsMob(Entity target) {
		float currentAttack = (float) this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
		float damage = currentAttack + (float) (this.rand.nextDouble() - 0.5) * 0.75F * currentAttack;
		if (this.rand.nextInt(100) < this.criticalChance) {
			damage *= this.criticalModifier;
		}

		ReflectionHelper.setPrivateValue(EntityIronGolem.class, this, 10, new String[] {"field_70855_f", "attackTimer"});
		this.world.setEntityState(this, (byte) 4);
		boolean flag = target.attackEntityFrom(DamageSource.MAGIC, damage);
		if (flag) {
			target.motionY += this.knockbackY;
			this.applyEnchantments(this, target);
		}

		this.playSound(this.getThrowSound(), 1.0F, 0.9F + this.rand.nextFloat() * 0.2F);

		SpellModifiers modifiers = new SpellModifiers();
		modifiers.set(SpellModifiers.POTENCY, 0.1f, false);
		modifiers.set(WizardryItems.blast_upgrade, 0.1f, true);
		castSpellOnTarget(target, Spells.chain_lightning, modifiers);
		return true;
	}

}
