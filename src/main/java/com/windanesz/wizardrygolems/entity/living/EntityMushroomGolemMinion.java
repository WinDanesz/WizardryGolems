package com.windanesz.wizardrygolems.entity.living;

import com.golems.main.ExtraGolems;
import com.windanesz.wizardrygolems.integration.ASIntegration;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class EntityMushroomGolemMinion extends EntityEarthGolemMinion {

	public EntityMushroomGolemMinion(World world) {
		super(world);
	}

	@Override
	protected ResourceLocation applyTexture() {
		return makeTexture(ExtraGolems.MODID, "golem_shroom_red");
	}

	@Override
	public SoundEvent getGolemSound() {
		return SoundEvents.BLOCK_GRAVEL_STEP;
	}

	@Override
	public void onSuccessfulAttack(EntityLivingBase target) {
		target.addPotionEffect(new PotionEffect(MobEffects.POISON, 100, 0));
	}

	@Override
	public void onDeath(DamageSource cause) {
		if (rand.nextBoolean() && ASIntegration.isLoaded()) {
			Spell spell = Spell.get("ancientspellcraft:fairy_ring");
			if (spell != null) {
				spell.cast(world, this, EnumHand.MAIN_HAND, 0, getCaster(), new SpellModifiers());
			}
		}
		super.onDeath(cause);
	}
}
