package com.windanesz.wizardrygolems.spell;

import com.windanesz.wizardrygolems.entity.ai.EntitySummonAIFollowOwner;
import com.windanesz.wizardrygolems.entity.living.EntityFlameGolemMinion;
import com.windanesz.wizardrygolems.entity.living.EntityFurnaceGolemMinion;
import com.windanesz.wizardrygolems.entity.living.EntityMagmaGolemMinion;
import com.windanesz.wizardrygolems.entity.living.EntityNetherBrickGolemMinion;
import com.windanesz.wizardrygolems.registry.WizardryGolemsItems;
import electroblob.wizardry.constants.Element;
import electroblob.wizardry.constants.Tier;
import electroblob.wizardry.entity.living.ISummonedCreature;
import electroblob.wizardry.item.ItemArtefact;
import electroblob.wizardry.item.ItemWand;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.spell.SpellMinion;
import electroblob.wizardry.util.BlockUtils;
import electroblob.wizardry.util.EntityUtils;
import electroblob.wizardry.util.ParticleBuilder;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import java.util.function.Function;

import static electroblob.wizardry.item.ItemArtefact.getActiveArtefacts;

public class FireGolemancy<T extends EntityLiving & ISummonedCreature> extends Golemancy<T> {

	public FireGolemancy(String modID, String name, Function<World, T> minionFactory) {
		super(modID, name, minionFactory);
	}

	/**
	 * Actually spawns the minions. By default, this spawns the number of minions specified by the
	 * {@link SpellMinion#MINION_COUNT} property within a number of blocks of the caster specified by the property
	 * {@link SpellMinion#SUMMON_RADIUS}, returning false if there is no space to spawn the minions. Override to do
	 * something special, like spawning minions in a specific position.
	 *
	 * @param world     The world in which to spawn the minions.
	 * @param caster    The entity that cast this spell, or null if it was cast by a dispenser.
	 * @param modifiers The spell modifiers this spell was cast with.
	 * @return False to cause the spell to fail, true to allow it to continue.
	 * @see SpellMinion#addMinionExtras(EntityLiving, BlockPos, EntityLivingBase, SpellModifiers, int)
	 */
	// Protected since someone might want to extend this class and change the behaviour of this method.
	@SuppressWarnings("Duplicates")
	protected boolean spawnGolems(World world, EnumHand hand, EntityLivingBase caster, SpellModifiers modifiers) {
		boolean hasArtefact = false;

		if (caster instanceof EntityPlayer) {

			EntityPlayer player = (EntityPlayer) caster;

			if (!world.isRemote) {

				for (ItemArtefact artefact : getActiveArtefacts(player)) {

					if (artefact == WizardryGolemsItems.ring_smoldering) {
						// Magma Golem
						int count = 1;
						for (int i = 0; i < count; i++) {
							EntityGolem golemi = new EntityMagmaGolemMinion(world, false);
							spawnGolem(golemi, player, world, modifiers);
						}
						hasArtefact = true;
						break;

					} else if (artefact == WizardryGolemsItems.ring_charcoal) {
						// Furnace Golem
						int count = 2;
						for (int i = 0; i < count; i++) {
							EntityGolem golemi = new EntityFurnaceGolemMinion(world);
							spawnGolem(golemi, player, world, modifiers);
						}
						hasArtefact = true;
						break;
					} else if (artefact == WizardryGolemsItems.ring_flame_golem) {
						if (!player.getHeldItem(hand).isEmpty() && (player.getHeldItem(hand).getItem() instanceof ItemWand && ((ItemWand) player.getHeldItem(hand).getItem()).tier == Tier.MASTER
								&& ((ItemWand) player.getHeldItem(hand).getItem()).element == Element.FIRE)) {
							// Flame Golem
							EntityGolem golemi = new EntityFlameGolemMinion(world);
							spawnGolem(golemi, player, world, modifiers);
							hasArtefact = true;
							break;
						} else {
							if (!world.isRemote) {
								player.sendStatusMessage(new TextComponentTranslation("spell." + this.getUnlocalisedName() + ".low_wand_tier"), true);
							}
						}
					}
				}

			}
		}

		spawnDefaultParticles(world, caster);
		if (!hasArtefact) {
			// Default - 2x Nether Brick Golems
			int count = 2;
			for (int i = 0; i < count; i++) {
				EntityGolem golemi = new EntityNetherBrickGolemMinion(world);
				spawnGolem(golemi, caster, world, modifiers);
			}
		}

		return true;
	}

	public void spawnDefaultParticles(World world, EntityLivingBase caster) {
		if (world.isRemote) {
			for (int i = 0; i < 10; i++) {
				float r = world.rand.nextFloat();
				double speed = 0.02 / r * (1 + world.rand.nextDouble());
				ParticleBuilder.create(ParticleBuilder.Type.CLOUD)
						.pos(caster.posX, caster.posY + 1, caster.posZ)
						.clr(235, 150, 52)
						.spin(r * (5 - 1) + 0.5, speed)
						.time(20)
						.spawn(world);
			}
			for (int i = 0; i < 30; i++) {
				float r = world.rand.nextFloat();
				double speed = 0.02 / r * (1 + world.rand.nextDouble());
				ParticleBuilder.create(ParticleBuilder.Type.MAGIC_FIRE)
						.pos(caster.posX, caster.posY + 1, caster.posZ)
						.spin(r * (5 - 1) + 0.5, speed)
						.time((int) (20 + 20 * world.rand.nextFloat()))
						.spawn(world);
			}
		}
	}

	@SuppressWarnings("Duplicates")
	public void spawnGolem(EntityGolem golem, EntityLivingBase caster, World world, SpellModifiers modifiers) {
		int range = getProperty(SUMMON_RADIUS).intValue();
		BlockPos pos = BlockUtils.findNearbyFloorSpace(caster, range, range * 2);

		golem.setPosition(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
		((ISummonedCreature) golem).setCaster(caster);

		float artefactDurationModifier = 1.0f;
		if (caster instanceof EntityPlayer && ItemArtefact.isArtefactActive((EntityPlayer) caster, WizardryGolemsItems.ring_fire_golem_duration)) {
			artefactDurationModifier = 2.0f;
		}

		// Modifier implementation
		// Attribute modifiers are pretty opaque, see https://minecraft.gamepedia.com/Attribute#Modifiers
		((ISummonedCreature) golem).setLifetime((int) (artefactDurationModifier * getProperty(MINION_LIFETIME).floatValue() * modifiers.get(WizardryItems.duration_upgrade)));
		IAttributeInstance attribute = golem.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);

		if (attribute != null) {
			attribute.applyModifier( // Apparently some things don't have an attack damage
					new AttributeModifier(POTENCY_ATTRIBUTE_MODIFIER, modifiers.get(SpellModifiers.POTENCY) - 1, EntityUtils.Operations.MULTIPLY_CUMULATIVE));
		}

		// This is only used for artefacts, but it's a nice example of custom spell modifiers
		golem.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).applyModifier(
				new AttributeModifier(HEALTH_MODIFIER, modifiers.get(HEALTH_MODIFIER) - 1, EntityUtils.Operations.MULTIPLY_CUMULATIVE));
		golem.setHealth(golem.getMaxHealth()); // Need to set this because we may have just modified the value

		EntitySummonAIFollowOwner task = new EntitySummonAIFollowOwner(golem, 1.0D, 10.0F, 2.0F);
		golem.tasks.addTask(6, task);

		if (!world.isRemote) {
			world.spawnEntity(golem);
		}
	}

	@Override
	public float getLifeTimeModifiers(EntityLivingBase caster) {
		if (caster instanceof EntityPlayer && ItemArtefact.isArtefactActive((EntityPlayer) caster, WizardryGolemsItems.ring_fire_golem_duration)) {
			return 2.0f;
		}
		return super.getLifeTimeModifiers(caster);
	}
}
