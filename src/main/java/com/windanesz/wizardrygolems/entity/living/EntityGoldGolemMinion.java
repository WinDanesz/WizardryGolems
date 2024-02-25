package com.windanesz.wizardrygolems.entity.living;

import com.golems.main.ExtraGolems;
import com.golems.util.GolemNames;
import electroblob.wizardry.entity.living.ISpellCaster;
import electroblob.wizardry.event.SpellCastEvent;
import electroblob.wizardry.registry.Spells;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.MagicDamage;
import electroblob.wizardry.util.ParticleBuilder;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

public class EntityGoldGolemMinion extends EntityGolemBaseMinion implements ISpellCaster, IHealingGolem {

	public EntityGoldGolemMinion(World world) {
		super(world);

		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
		this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(0.9D);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(50);
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		updateDelegate();
		onGolemUpdate(this);
		if (this.rand.nextInt(100) == 0) {
			SpellModifiers modifiers = new SpellModifiers();
			if (MinecraftForge.EVENT_BUS.post(new SpellCastEvent.Pre(SpellCastEvent.Source.NPC, getSpells().get(0), this, modifiers))) {
				if (getSpells().get(0).cast(this.world, this, EnumHand.MAIN_HAND, 0, this, modifiers)) {

					MinecraftForge.EVENT_BUS.post(new SpellCastEvent.Post(SpellCastEvent.Source.NPC, getSpells().get(0), this, modifiers));

					// For now, the cooldown is just added to the co nstant base cooldown. I think this
					// is a reasonable way of doing things; it's certainly better than before.
				}
			}
		}

		if (world.isRemote) {
			//ParticleBuilder.create(ParticleBuilder.Type.SPARKLE, rand, posX, posY, posZ, 0.03, true).clr(1, 1, 0.65f).fade(0.7f, 0, 1)
		//			.time(20 + rand.nextInt(10)).spawn(world);

			if(this.ticksExisted > 1){ // Don't spawn particles behind where it started!
				double x = posX + rand.nextFloat() * (rand.nextBoolean() ? -1 : 1);
				double y = posY;
				double z = posZ + rand.nextFloat() * (rand.nextBoolean() ? -1 : 1);
				float brightness = 0.5f + (world.rand.nextFloat() * 0.5f);
				ParticleBuilder.create(ParticleBuilder.Type.SPARKLE, rand, x , y + getEyeHeight() /2 , z,
								0.07, true).clr(1.0f, 1.0f, brightness).fade(0.9f, 0.9f, 0.9f)
						.time(20 + rand.nextInt(10)).spawn(world);
			}

			for (int i = 0; i < 1; i++) {
				float brightness = 0.5f + (world.rand.nextFloat() * 0.5f);
				ParticleBuilder.create(ParticleBuilder.Type.FLASH, this).scale(1.8f).time(30).clr(1.0f, 1.0f, brightness).fade(0.9f, 0.9f, 0.9f).spawn(world);
			}


		}
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
		target.attackEntityFrom( MagicDamage.causeDirectMagicDamage(this, MagicDamage.DamageType.RADIANT), damage);
		return true;
	}

	@Override
	public void onDeath(DamageSource cause) {
		onDeathDelegate(this);
		super.onDeath(cause);
	}

	@Override
	public void onSuccessfulAttack(EntityLivingBase target) {
		onSuccessFulAttackDelegate(this, target);
	}

	@Override
	public boolean isBurning() {
		return super.isBurning();
	}

	@Override
	public boolean hasParticleEffect() {
		return super.hasParticleEffect();
	}

	@Override
	protected ResourceLocation applyTexture() {
		return makeTexture(ExtraGolems.MODID, GolemNames.GOLD_GOLEM);
	}

	@Override
	public SoundEvent getGolemSound() {
		return SoundEvents.BLOCK_STONE_STEP;
	}

	@Nonnull
	@Override
	public List<Spell> getSpells() {
		return Collections.singletonList(Spells.group_heal);
	}

	@Override
	public boolean isPotionApplicable(PotionEffect potioneffectIn) {
		return potioneffectIn.getPotion() != MobEffects.WITHER && potioneffectIn.getPotion() != MobEffects.HUNGER && potioneffectIn.getPotion() != MobEffects.WEAKNESS && super.isPotionApplicable(potioneffectIn
		);
	}
}
