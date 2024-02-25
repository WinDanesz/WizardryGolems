package com.windanesz.wizardrygolems.spell;

import com.windanesz.wizardrygolems.entity.living.EntityGoldGolemMinion;
import com.windanesz.wizardrygolems.entity.living.EntityLodestoneGolemMinion;
import com.windanesz.wizardrygolems.entity.living.EntityMaelstromGolemMinion;
import com.windanesz.wizardrygolems.entity.living.EntitySparkGolemMinion;
import com.windanesz.wizardrygolems.registry.WizardryGolemsItems;
import electroblob.wizardry.constants.Element;
import electroblob.wizardry.entity.living.ISummonedCreature;
import electroblob.wizardry.item.ItemArtefact;
import electroblob.wizardry.spell.SpellMinion;
import electroblob.wizardry.util.ParticleBuilder;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import java.util.function.Function;

import static electroblob.wizardry.item.ItemArtefact.getActiveArtefacts;

public class HealingGolemancy<T extends EntityLiving & ISummonedCreature> extends Golemancy<T> {

	public HealingGolemancy(String modID, String name, Function<World, T> minionFactory) {
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
		boolean hasScatterRing = false;
		if (caster instanceof EntityPlayer) {

			EntityPlayer player = (EntityPlayer) caster;

			if (!world.isRemote) {

				for (ItemArtefact artefact : getActiveArtefacts(player)) {

					if (artefact == WizardryGolemsItems.ring_lodestone_golem) {
						spawnGolem(() -> new EntityLodestoneGolemMinion(world), player, world, modifiers, 1);
						hasArtefact = true;
						break;
					}

					if (artefact == WizardryGolemsItems.ring_spark_golem) {
						spawnGolem(() -> new EntitySparkGolemMinion(world), player, world, modifiers, 1);
						hasArtefact = true;
						break;
					}

					if (artefact == WizardryGolemsItems.head_maelstrom_golem) {
						if (hasMasterWand(player, Element.LIGHTNING)) {
							spawnGolem(() -> new EntityMaelstromGolemMinion(world), player, world, modifiers, 1);
							hasArtefact = true;
							break;
						} else {
							player.sendStatusMessage(new TextComponentTranslation("spell." + this.getUnlocalisedName() + ".low_wand_tier"), true);
						}
					}
					if (artefact == WizardryGolemsItems.ring_electric_scatter) {
						hasScatterRing = true;
					}
				}
			}
		}

		if (!hasArtefact) {
			spawnGolem(() -> new EntityGoldGolemMinion(world), caster, world, modifiers, 1);
		}
		spawnDefaultParticles(world, caster);

		return true;
	}

	public void spawnDefaultParticles(World world, EntityLivingBase caster) {
		if (world.isRemote) {

			//	ParticleBuilder.create(ParticleBuilder.Type.BEAM).pos(caster.posX, caster.posY + 4, caster.posZ).target(caster.posX, caster.posY, caster.posZ).scale(20)
			//			.clr(0xffbf00).time(10).spawn(world);
			//
			float brightness = 0.5f + (world.rand.nextFloat() * 0.5f);

			ParticleBuilder.create(ParticleBuilder.Type.FLASH)
					.clr(1.0f, 1.0f, brightness).fade(0.9f, 0.9f, 0.9f)
					.pos(caster.posX, caster.posY + 0.2f, caster.posZ)
					.spin(0, 0.03)
					.time(60)
					.scale(3.5f)
					.face(EnumFacing.UP)
					.spawn(world);

			for (int i = 0; i < 20; i++) {
				float r = world.rand.nextFloat();
				double speed = 0.02 / r * (1 + world.rand.nextDouble());
				ParticleBuilder.create(ParticleBuilder.Type.FLASH)
						.pos(caster.posX, caster.posY + 1, caster.posZ)
						.clr(0xffbf00)
						.spin(r * (5 - 1) + 0.5, speed)
						.time(20)
						.spawn(world);
			}
			for (int i = 0; i < 30; i++) {
				float r = world.rand.nextFloat();
				double speed = 0.02 / r * (1 + world.rand.nextDouble());
				ParticleBuilder.create(ParticleBuilder.Type.FLASH)
						.pos(caster.posX, caster.posY + 1, caster.posZ)
						.clr(0xfff098)
						.spin(r * (5 - 1) + 0.5, speed)
						.time((int) (20 + 20 * world.rand.nextFloat()))
						.spawn(world);
			}

			for (int i = 0; i < 30; i++) {
				brightness = 0.5f + (world.rand.nextFloat() * 0.5f);
				float r = world.rand.nextFloat();
				double speed = 0.02 / r * (1 + world.rand.nextDouble());
				ParticleBuilder.create(ParticleBuilder.Type.SPARKLE)
						.pos(caster.posX, caster.posY + 1, caster.posZ)
						.spin(r * (5 - 1) + 0.5, speed)
						.clr(1.0f, 1.0f, brightness)
						.time((int) (20 + 20 * world.rand.nextFloat()))
						.spawn(world);
			}
		}
	}

}