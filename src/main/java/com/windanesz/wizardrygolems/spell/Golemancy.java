package com.windanesz.wizardrygolems.spell;

import com.windanesz.wizardrygolems.entity.ai.EntitySummonAIFollowOwner;
import com.windanesz.wizardrygolems.entity.living.EntityLodestoneGolemMinion;
import com.windanesz.wizardrygolems.item.ItemPermanentGolemRing;
import com.windanesz.wizardrygolems.registry.WizardryGolemsItems;
import electroblob.wizardry.constants.Element;
import electroblob.wizardry.constants.Tier;
import electroblob.wizardry.entity.living.ISummonedCreature;
import electroblob.wizardry.item.ItemWand;
import electroblob.wizardry.spell.SpellMinion;
import electroblob.wizardry.util.BlockUtils;
import electroblob.wizardry.util.EntityUtils;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class Golemancy<T extends EntityLiving & ISummonedCreature> extends SpellMinion<T> {

	public Golemancy(String modID, String name, Function<World, T> minionFactory) {
		super(modID, name, minionFactory);
	}

	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
		int range = getProperty(SUMMON_RADIUS).intValue();
		BlockPos pos = BlockUtils.findNearbyFloorSpace(caster, range, range * 2);

		if (pos == null) { return false; }
		if (!this.spawnGolems(world, hand, caster, modifiers)) {return false;}
		this.playSound(world, caster, ticksInUse, -1, modifiers);
		return true;
	}

	@Override
	public boolean cast(World world, EntityLiving caster, EnumHand hand, int ticksInUse, EntityLivingBase target,
			SpellModifiers modifiers){
		int range = getProperty(SUMMON_RADIUS).intValue();
		BlockPos pos = BlockUtils.findNearbyFloorSpace(caster, range, range * 2);

		if (pos == null) { return false; }
		if (!this.spawnGolems(world, hand, caster, modifiers)) {return false;}
		this.playSound(world, caster, ticksInUse, -1, modifiers);
		return true;
	}

	protected abstract boolean spawnGolems(World world, EnumHand hand, EntityLivingBase caster, SpellModifiers modifiers);

	public void spawnGolem(EntityGolem golem, EntityLivingBase caster, World world, SpellModifiers modifiers) {
		int range = getProperty(SUMMON_RADIUS).intValue();
		BlockPos pos = BlockUtils.findNearbyFloorSpace(caster, range, range * 2);

		golem.setPosition(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
		((ISummonedCreature) golem).setCaster(caster);

		float artefactDurationModifier = getLifeTimeModifiers(caster);

		int lifetime = ItemPermanentGolemRing.getLifeTime(caster, golem, this, modifiers);

		// can be less than 0 if the duration is permanent
		if (lifetime > 0) {
			lifetime = (int) (lifetime * artefactDurationModifier);
		}
		((ISummonedCreature) golem).setLifetime(lifetime);
		// Modifier implementation
		// Attribute modifiers are pretty opaque, see https://minecraft.gamepedia.com/Attribute#Modifiers
		IAttributeInstance attribute = golem.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);

		if (attribute != null) {
			attribute.applyModifier( // Apparently some things don't have an attack damage
					new AttributeModifier(POTENCY_ATTRIBUTE_MODIFIER, modifiers.get(SpellModifiers.POTENCY) - 1, EntityUtils.Operations.MULTIPLY_CUMULATIVE));
		}

		// This is only used for artefacts, but it's a nice example of custom spell modifiers
		golem.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).applyModifier(
				new AttributeModifier(HEALTH_MODIFIER, modifiers.get(HEALTH_MODIFIER) - 1, EntityUtils.Operations.MULTIPLY_CUMULATIVE));
		golem.setHealth(golem.getMaxHealth()); // Need to set this because we may have just modified the value

		if (!(golem instanceof EntityLodestoneGolemMinion)) {
			EntitySummonAIFollowOwner task = new EntitySummonAIFollowOwner(golem, 1.0D, 10.0F, 2.0F);
			golem.tasks.addTask(6, task);
		}

		if (!world.isRemote) {
			world.spawnEntity(golem);
		}
	}

	public void spawnGolem(Supplier<EntityGolem> golemSupplier, EntityLivingBase caster, World world, SpellModifiers modifiers, int count) {
		for (int i = 0; i < count; i++) {
			EntityGolem golem = golemSupplier.get();
			int range = getProperty(SUMMON_RADIUS).intValue();
			BlockPos pos = BlockUtils.findNearbyFloorSpace(caster, range, range * 2);

			if (pos == null) { return; }
			golem.setPosition(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
			((ISummonedCreature) golem).setCaster(caster);

			float artefactDurationModifier = getLifeTimeModifiers(caster);

			int lifetime = ItemPermanentGolemRing.getLifeTime(caster, golem, this, modifiers);

			// can be less than 0 if the duration is permanent
			if (lifetime > 0) {
				lifetime = (int) (lifetime * artefactDurationModifier);
			}
			((ISummonedCreature) golem).setLifetime(lifetime);
			// Modifier implementation
			// Attribute modifiers are pretty opaque, see https://minecraft.gamepedia.com/Attribute#Modifiers
			IAttributeInstance attribute = golem.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);

			if (attribute != null) {
				attribute.applyModifier( // Apparently some things don't have an attack damage
						new AttributeModifier(POTENCY_ATTRIBUTE_MODIFIER, modifiers.get(SpellModifiers.POTENCY) - 1, EntityUtils.Operations.MULTIPLY_CUMULATIVE));
			}

			// This is only used for artefacts, but it's a nice example of custom spell modifiers
			golem.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).applyModifier(
					new AttributeModifier(HEALTH_MODIFIER, modifiers.get(HEALTH_MODIFIER) - 1, EntityUtils.Operations.MULTIPLY_CUMULATIVE));
			golem.setHealth(golem.getMaxHealth()); // Need to set this because we may have just modified the value

			if (!(golem instanceof EntityLodestoneGolemMinion)) {
				EntitySummonAIFollowOwner task = new EntitySummonAIFollowOwner(golem, 1.0D, 10.0F, 2.0F);
				golem.tasks.addTask(6, task);
			} else {
				((EntityLodestoneGolemMinion) golem).setPull(caster.isSneaking());
			}

			if (!world.isRemote) {
				world.spawnEntity(golem);
			}
		}
	}

	public boolean hasMasterWand(EntityPlayer player, Element element) {
		return (!player.getHeldItem(EnumHand.MAIN_HAND).isEmpty() && player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemWand
				&& ((ItemWand) player.getHeldItem(EnumHand.MAIN_HAND).getItem()).tier == Tier.MASTER
				&& ((ItemWand) player.getHeldItem(EnumHand.MAIN_HAND).getItem()).element == element) ||
				(!player.getHeldItem(EnumHand.OFF_HAND).isEmpty() && player.getHeldItem(EnumHand.OFF_HAND).getItem() instanceof ItemWand
						&& ((ItemWand) player.getHeldItem(EnumHand.OFF_HAND).getItem()).tier == Tier.MASTER
						&& ((ItemWand) player.getHeldItem(EnumHand.OFF_HAND).getItem()).element == element);
	}
	/**
	 * Called just before each minion is spawned. Calls {@link EntityLiving#onInitialSpawn(DifficultyInstance, IEntityLivingData)}
	 * by default, but subclasses can override to call extra methods on the summoned entity, for example to add
	 * special equipment. This method is only called server-side so cannot be used to spawn particles directly.
	 *
	 * @param golem     The entity being spawned.
	 * @param pos       The position at which the entity was spawned.
	 * @param caster    The caster of this spell, or null if it was cast by a dispenser.
	 * @param modifiers The modifiers this spell was cast with.
	 */
	protected void addGolemExtras(EntityGolem golem, BlockPos pos, @Nullable EntityLivingBase caster, SpellModifiers modifiers) {}

	@Override
	public boolean applicableForItem(Item item) {
		return (item == WizardryGolemsItems.golemancy_spell_book || item == WizardryGolemsItems.golemancy_scroll);
	}

	public float getLifeTimeModifiers(EntityLivingBase caster) {
		return 1f;
	}
}
