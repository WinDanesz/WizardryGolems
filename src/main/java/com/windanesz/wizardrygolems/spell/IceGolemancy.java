package com.windanesz.wizardrygolems.spell;

import com.windanesz.wizardrygolems.entity.living.EntityIceGolemMinion;
import com.windanesz.wizardrygolems.entity.living.EntityPermafrostGolemMinion;
import com.windanesz.wizardrygolems.entity.living.EntitySnowGolemMinion;
import com.windanesz.wizardrygolems.entity.living.EntityWinterGolemMinion;
import com.windanesz.wizardrygolems.registry.WizardryGolemsItems;
import com.windanesz.wizardryutils.item.ItemNewArtefact;
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
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import java.util.function.Function;

import static electroblob.wizardry.item.ItemArtefact.getActiveArtefacts;

public class IceGolemancy<T extends EntityLiving & ISummonedCreature> extends Golemancy<T> {

	public IceGolemancy(String modID, String name, Function<World, T> minionFactory) {
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

					if (artefact == WizardryGolemsItems.ring_snow_golem) {
						// Snow Golem
						int count = 5;
						for (int i = 0; i < count; i++) {
							EntityGolem golemi = new EntitySnowGolemMinion(world);
							spawnGolem(golemi, player, world, modifiers);
						}
						hasArtefact = true;
						break;
					} else if (artefact == WizardryGolemsItems.ring_winter_golem) {
						if (!player.getHeldItem(hand).isEmpty() && (player.getHeldItem(hand).getItem() instanceof ItemWand && ((ItemWand) player.getHeldItem(hand).getItem()).tier == Tier.MASTER
								&& ((ItemWand) player.getHeldItem(hand).getItem()).element == Element.ICE)) {
							// Winter Golem
							EntityGolem golemi = new EntityWinterGolemMinion(world);
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

				for (ItemNewArtefact artefact : ItemNewArtefact.getActiveArtefacts(player)) {

					if (artefact == WizardryGolemsItems.head_permafrost_crown) {
						// Permafrost Golems
						int count = 2;
						for (int i = 0; i < count; i++) {
							EntityGolem golemi = new EntityPermafrostGolemMinion(world);
							spawnGolem(golemi, player, world, modifiers);
						}
						hasArtefact = true;
						break;
					}
				}
			}
		}

		spawnDefaultParticles(world, caster);
		if (!hasArtefact) {
			// Default - 2x Ice Golems
			int count = 2;
			for (int i = 0; i < count; i++) {
				EntityGolem golemi = new EntityIceGolemMinion(world);
				spawnGolem(golemi, caster, world, modifiers);
			}
		}

		return true;
	}

	public void spawnDefaultParticles(World world, EntityLivingBase caster) {
		if (world.isRemote) {
			for (int i = 0; i < 20; i++) {
				float r = world.rand.nextFloat();
				double speed = 0.02 / r * (1 + world.rand.nextDouble());
				ParticleBuilder.create(ParticleBuilder.Type.CLOUD)
						.pos(caster.posX, caster.posY + 1, caster.posZ)
						.clr(250, 255, 255)
						.spin(r * (5 - 1) + 0.5, speed)
						.time(20)
						.spawn(world);
			}
			for (int i = 0; i < 30; i++) {
				float r = world.rand.nextFloat();
				double speed = 0.02 / r * (1 + world.rand.nextDouble());
				ParticleBuilder.create(ParticleBuilder.Type.SNOW)
						.pos(caster.posX, caster.posY + 1, caster.posZ)
						.clr(250, 255, 255)
						.spin(r * (5 - 1) + 0.5, speed)
						.time((int) (20 + 20 * world.rand.nextFloat()))
						.spawn(world);
			}
		}
	}
}
