//package com.windanesz.wizardrygolems.spell;
//
//import com.windanesz.wizardrygolems.entity.living.EntityClayGolemMinion;
//import com.windanesz.wizardrygolems.entity.living.EntityGolemBaseMinion;
//import com.windanesz.wizardrygolems.entity.living.EntityTerracottaGolemMinion;
//import com.windanesz.wizardrygolems.registry.WizardryGolemsItems;
//import electroblob.wizardry.entity.living.ISummonedCreature;
//import electroblob.wizardry.item.ItemArtefact;
//import electroblob.wizardry.registry.WizardryItems;
//import electroblob.wizardry.spell.SpellMinion;
//import electroblob.wizardry.util.BlockUtils;
//import electroblob.wizardry.util.EntityUtils;
//import electroblob.wizardry.util.SpellModifiers;
//import net.minecraft.entity.EntityLiving;
//import net.minecraft.entity.EntityLivingBase;
//import net.minecraft.entity.SharedMonsterAttributes;
//import net.minecraft.entity.ai.attributes.AttributeModifier;
//import net.minecraft.entity.ai.attributes.IAttributeInstance;
//import net.minecraft.entity.player.EntityPlayer;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.world.World;
//
//import javax.annotation.Nullable;
//import java.util.function.Function;
//
//import static electroblob.wizardry.item.ItemArtefact.getActiveArtefacts;
//
//public class UnholyGolemancy<T extends EntityLiving & ISummonedCreature> extends SpellMinion<T> {
//
//	int golemCount = 3;
//
//	public UnholyGolemancy(String modID, String name, Function<World, T> minionFactory) {
//		super(modID, name, minionFactory);
//	}
//
//	/**
//	 * Actually spawns the minions. By default, this spawns the number of minions specified by the
//	 * {@link SpellMinion#MINION_COUNT} property within a number of blocks of the caster specified by the property
//	 * {@link SpellMinion#SUMMON_RADIUS}, returning false if there is no space to spawn the minions. Override to do
//	 * something special, like spawning minions in a specific position.
//	 *
//	 * @param world     The world in which to spawn the minions.
//	 * @param caster    The entity that cast this spell, or null if it was cast by a dispenser.
//	 * @param modifiers The spell modifiers this spell was cast with.
//	 * @return False to cause the spell to fail, true to allow it to continue.
//	 * @see SpellMinion#addMinionExtras(EntityLiving, BlockPos, EntityLivingBase, SpellModifiers, int)
//	 */
//	// Protected since someone might want to extend this class and change the behaviour of this method.
//	protected boolean spawnMinions(World world, EntityLivingBase caster, SpellModifiers modifiers) {
//
//		if (!world.isRemote && caster instanceof EntityPlayer) {
//
//			EntityPlayer player = (EntityPlayer) caster;
//
//			// artefact check to make counts work ....
//
//			for (int i = 0; i < golemCount; i++) {
//
//				int range = getProperty(SUMMON_RADIUS).intValue();
//
//				// Try and find a nearby floor space
//				BlockPos pos = BlockUtils.findNearbyFloorSpace(caster, range, range * 2);
//
//				// If there was no floor around and the entity isn't a flying one, the spell fails.
//				// As per the javadoc for findNearbyFloorSpace, there's no point trying the rest of the minions.
//				if (pos == null)
//					return false;
//
//				// artefact check
//				EntityGolemBaseMinion golem = getGolem(world, player);
//
//				golem.setPosition(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
//				golem.setCaster(caster);
//				// Modifier implementation
//				// Attribute modifiers are pretty opaque, see https://minecraft.gamepedia.com/Attribute#Modifiers
//				golem.setLifetime((int) (getProperty(MINION_LIFETIME).floatValue() * modifiers.get(WizardryItems.duration_upgrade)));
//				IAttributeInstance attribute = golem.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
//				float dmg = modifiers.get(SpellModifiers.POTENCY) - 1;
//				float health = modifiers.get(SpellModifiers.POTENCY) - 1;
//				if (attribute != null)
//					attribute.applyModifier( // Apparently some things don't have an attack damage
//							new AttributeModifier(POTENCY_ATTRIBUTE_MODIFIER, modifiers.get(SpellModifiers.POTENCY) - 1, EntityUtils.Operations.MULTIPLY_CUMULATIVE));
//				// This is only used for artefacts, but it's a nice example of custom spell modifiers
//				golem.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).applyModifier(
//						new AttributeModifier(HEALTH_MODIFIER, modifiers.get(HEALTH_MODIFIER) - 1, EntityUtils.Operations.MULTIPLY_CUMULATIVE));
//				golem.setHealth(golem.getMaxHealth()); // Need to set this because we may have just modified the value
//
//				this.addMinionExtras(golem, pos, caster, modifiers, i);
//
//				world.spawnEntity(golem);
//			}
//		}
//
//		return true;
//	}
//
//	protected void addMinionExtras(EntityGolemBaseMinion minion, BlockPos pos,
//			@Nullable EntityLivingBase caster, SpellModifiers modifiers, int alreadySpawned) {
//		minion.onInitialSpawn(minion.world.getDifficultyForLocation(pos), null);
//	}
//
//	protected EntityGolemBaseMinion getGolem(World world, EntityPlayer player) {
//		for (ItemArtefact artefact : getActiveArtefacts(player)) {
//			if (artefact == WizardryGolemsItems.ring_ancient_emperor) {
//				this.golemCount = 4;
//				return new EntityTerracottaGolemMinion(world);
//			}
//		}
//		return new EntityClayGolemMinion(world);
//	}
//
//}
