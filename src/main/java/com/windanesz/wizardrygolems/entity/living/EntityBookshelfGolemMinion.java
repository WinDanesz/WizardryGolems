package com.windanesz.wizardrygolems.entity.living;

import com.golems.main.ExtraGolems;
import com.golems.util.GolemConfigSet;
import com.golems.util.GolemNames;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class EntityBookshelfGolemMinion extends EntityGolemBaseMinion {

	public static final String ALLOW_SPECIAL = "Allow Special: Potion Effects";
	private static final Potion[] goodEffects = {MobEffects.FIRE_RESISTANCE, MobEffects.REGENERATION,
			MobEffects.STRENGTH, MobEffects.ABSORPTION, MobEffects.LUCK, MobEffects.INSTANT_HEALTH, MobEffects.RESISTANCE,
			MobEffects.INVISIBILITY, MobEffects.SPEED, MobEffects.JUMP_BOOST};

	public EntityBookshelfGolemMinion(World world) {
		super(world);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.29D);

	}

	protected ResourceLocation applyTexture() {
		return makeTexture(ExtraGolems.MODID, GolemNames.BOOKSHELF_GOLEM);
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		GolemConfigSet cfg = getConfig(this);
		if (cfg.getBoolean(ALLOW_SPECIAL) && this.getActivePotionEffects().isEmpty() && rand.nextInt(40) == 0) {
			final Potion potion = goodEffects[rand.nextInt(goodEffects.length)];
			final int len = potion.isInstant() ? 1 : 200 + 100 * (1 + rand.nextInt(5));
			this.addPotionEffect(new PotionEffect(potion, len, rand.nextInt(2)));
		}
	}

	@Override
	public SoundEvent getGolemSound() {
		return SoundEvents.BLOCK_WOOD_STEP;
	}
}