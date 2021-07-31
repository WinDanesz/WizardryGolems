package com.windanesz.wizardrygolems.entity.living;

import com.golems.main.ExtraGolems;
import com.golems.util.GolemNames;
import electroblob.wizardry.util.ParticleBuilder;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class EntityMelonGolemMinion extends EntityEarthGolemMinion {

	public EntityMelonGolemMinion(World world) {
		super(world);
	}

	/** The number of sparkle particles spawned when healing is cast. Defaults to 10. */
	protected float particleCount = 2;

	@Override
	protected ResourceLocation applyTexture() {
		return makeTexture(ExtraGolems.MODID, GolemNames.MELON_GOLEM);
	}

	@Override
	public SoundEvent getGolemSound() {
		return SoundEvents.BLOCK_GRAVEL_STEP;
	}

	@Override
	public void onSuccessfulAttack(EntityLivingBase target) {
		if (world.rand.nextBoolean() && this.getCaster() != null) {
			getCaster().addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 60, 0));
			this.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 60, 2));

			// FIXME: this is only called server side, should send a packet to client
			if (world.isRemote) {
				for(int i = 0; i < particleCount; i++){
					double x = getCaster().posX + world.rand.nextDouble() * 2 - 1;
					double y = getCaster().posY + getCaster().getEyeHeight() - 0.5 + world.rand.nextDouble();
					double z = getCaster().posZ + world.rand.nextDouble() * 2 - 1;
					ParticleBuilder.create(ParticleBuilder.Type.SPARKLE).pos(x, y, z).vel(0, 0.1, 0).clr(212, 21, 72).spawn(world);
				}

				ParticleBuilder.create(ParticleBuilder.Type.BUFF).entity(getCaster()).clr(212, 21, 72).spawn(world);
			}
		}
		target.addPotionEffect(new PotionEffect(MobEffects.POISON, 100, 0));
	}
}
