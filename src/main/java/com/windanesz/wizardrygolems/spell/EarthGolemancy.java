package com.windanesz.wizardrygolems.spell;

import com.windanesz.wizardrygolems.entity.living.EntityClayGolemMinion;
import com.windanesz.wizardrygolems.entity.living.EntityConcreteGolemMinion;
import com.windanesz.wizardrygolems.entity.living.EntityLeafGolemMinion;
import com.windanesz.wizardrygolems.entity.living.EntityMelonGolemMinion;
import com.windanesz.wizardrygolems.entity.living.EntityMushroomGolemMinion;
import com.windanesz.wizardrygolems.entity.living.EntityOakWoodenGolemMinion;
import com.windanesz.wizardrygolems.entity.living.EntityObsidianGolemMinion;
import com.windanesz.wizardrygolems.entity.living.EntitySandstoneGolemMinion;
import com.windanesz.wizardrygolems.entity.living.EntityStrawThornsGolemMinion;
import com.windanesz.wizardrygolems.entity.living.EntityTerracottaGolemMinion;
import com.windanesz.wizardrygolems.registry.WizardryGolemsItems;
import electroblob.wizardry.client.DrawingUtils;
import electroblob.wizardry.constants.Element;
import electroblob.wizardry.constants.Tier;
import electroblob.wizardry.entity.living.ISummonedCreature;
import electroblob.wizardry.item.ItemArtefact;
import electroblob.wizardry.item.ItemWand;
import electroblob.wizardry.spell.SpellMinion;
import electroblob.wizardry.util.ParticleBuilder;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.function.Function;

import static electroblob.wizardry.item.ItemArtefact.getActiveArtefacts;

public class EarthGolemancy<T extends EntityLiving & ISummonedCreature> extends Golemancy<T> {

	public EarthGolemancy(String modID, String name, Function<World, T> minionFactory) {
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

		if (caster instanceof EntityPlayer) {

			EntityPlayer player = (EntityPlayer) caster;

			if (!world.isRemote) {
				boolean hasArtefact = false;

				for (ItemArtefact artefact : getActiveArtefacts(player)) {
					if (artefact == WizardryGolemsItems.ring_ancient_emperor) {
						int count = 12;
						for (int i = 0; i < count; i++) {
							EntityGolem golemi = new EntityTerracottaGolemMinion(world, true);
							spawnGolem(golemi, player, world, modifiers);
						}
						hasArtefact = true;
						break;

						// ConcreteGolem
					} else if (artefact == WizardryGolemsItems.ring_engraved_concrete) {
						int count = 2;
						for (int i = 0; i < count; i++) {
							EntityGolem golemi = new EntityConcreteGolemMinion(world);
							spawnGolem(golemi, player, world, modifiers);
						}
						hasArtefact = true;
						break;

						// SandstoneGolem
					} else if (artefact == WizardryGolemsItems.ring_sandstone) {
						int count = 5;
						for (int i = 0; i < count; i++) {
							EntityGolem golemi = new EntitySandstoneGolemMinion(world);
							spawnGolem(golemi, player, world, modifiers);
						}
						hasArtefact = true;
						break;

						// ObsidianGolem
					} else if (artefact == WizardryGolemsItems.ring_obsidian) {
						if (!player.getHeldItem(hand).isEmpty() && (player.getHeldItem(hand).getItem() instanceof ItemWand && ((ItemWand) player.getHeldItem(hand).getItem()).tier == Tier.MASTER
								&& ((ItemWand) player.getHeldItem(hand).getItem()).element == Element.EARTH)) {
							int count = 1;
							for (int i = 0; i < count; i++) {
								EntityGolem golemi = new EntityObsidianGolemMinion(world);
								spawnGolem(golemi, player, world, modifiers);
							}
							hasArtefact = true;
						} else {
							if (!world.isRemote) {
								player.sendStatusMessage(new TextComponentTranslation("spell." + this.getUnlocalisedName() + ".low_wand_tier"), true);
							}
						}
						break;

						// WoodenGolem and LeafGolem
					} else if (artefact == WizardryGolemsItems.ring_forest_guardian) {
						int count = 2;
						for (int i = 0; i < count; i++) {
							EntityGolem golemi = new EntityOakWoodenGolemMinion(world);
							spawnGolem(golemi, player, world, modifiers);
						}
						int leafGolemCount = 3;
						for (int i = 0; i < leafGolemCount; i++) {
							EntityGolem golemi = new EntityLeafGolemMinion(world, true);
							spawnGolem(golemi, player, world, modifiers);
						}
						hasArtefact = true;
						break;

					} else if (artefact == WizardryGolemsItems.ring_grass) {
						int count = 4;
						for (int i = 0; i < count; i++) {
							EntityGolem golemi = new EntityStrawThornsGolemMinion(world);
							spawnGolem(golemi, player, world, modifiers);
						}
						hasArtefact = true;
						break;

					} else if (artefact == WizardryGolemsItems.charm_dried_mushroom) {
						int count = 3;
						for (int i = 0; i < count; i++) {
							EntityGolem golemi = new EntityMushroomGolemMinion(world);
							spawnGolem(golemi, player, world, modifiers);
						}
						hasArtefact = true;
						break;
					}
				}

				for (ItemArtefact artefact : getActiveArtefacts(player)) {
					if (artefact == WizardryGolemsItems.ring_glistering) {
						EntityGolem golemi = new EntityMelonGolemMinion(world);
						spawnGolem(golemi, player, world, modifiers);
					}
				}

				// Default - 3x Clay Golems
				if (!hasArtefact) {
					int count = 3;
					for (int i = 0; i < count; i++) {
						EntityGolem golemi = new EntityClayGolemMinion(world);
						spawnGolem(golemi, player, world, modifiers);
					}
				}
			}

			if (ItemArtefact.isArtefactActive(player, WizardryGolemsItems.ring_ancient_emperor)) {
				spawnDesertParticles(world, player);

			} else {
				spawnEarthParticles(world, player);
			}
		}

		return true;
	}

	public void spawnDesertParticles(World world, EntityPlayer caster) {
		if (world.isRemote) {
			for (int i = 0; i < 60; i++) {
				float r = world.rand.nextFloat();
				double speed = 0.02 / r * (1 + world.rand.nextDouble());
				ParticleBuilder.create(ParticleBuilder.Type.CLOUD)
						.pos(caster.posX, caster.posY + 1, caster.posZ)
						.clr(DrawingUtils.mix(DrawingUtils.mix(0xe3ca81, 0xe3ca81, r / 0.6f), 0x857b5e, (r - 0.6f) / 0.4f))
						.spin(r * (5 - 1) + 0.5, speed)
						.time(20)
						.spawn(world);
			}
		}
	}

	public void spawnEarthParticles(World world, EntityPlayer caster) {
		if (world.isRemote) {
			for (int i = 0; i < 20; i++) {
				float r = world.rand.nextFloat();
				double speed = 0.04 / r * (1 + world.rand.nextDouble());
				ParticleBuilder.create(ParticleBuilder.Type.LEAF)
						.pos(caster.posX, caster.posY + 1, caster.posZ)
						.clr(DrawingUtils.mix(DrawingUtils.mix(0x026e31, 0x07a835, r / 0.6f), 0x07a835, (r - 0.6f) / 0.4f))
						.spin(r * (3), speed)
						.time(40)
						.spawn(world);
			}
		}
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
	@Override
	protected void addGolemExtras(EntityGolem golem, BlockPos pos, @Nullable EntityLivingBase caster, SpellModifiers modifiers) {
		// golem.onInitialSpawn(golem.world.getDifficultyForLocation(pos), null);
		if (golem instanceof EntityObsidianGolemMinion) {
			golem.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 1000, 0));
		}
	}
}
