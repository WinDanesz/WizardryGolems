package com.windanesz.wizardrygolems.entity.living;

import com.golems.blocks.BlockUtilityGlow;
import com.golems.entity.ai.EntityAIUtilityBlock;
import com.golems.main.ExtraGolems;
import com.golems.main.GolemItems;
import com.golems.util.GolemConfigSet;
import com.golems.util.GolemLookup;
import com.golems.util.GolemNames;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityGlowstoneGolemMinion extends EntityGolemBaseMinion {

	public static final String ALLOW_SPECIAL = "Allow Special: Emit Light";
	public static final String FREQUENCY = "Light Frequency";

	/**
	 * Float value between 0.0F and 1.0F that determines light level
	 **/
	private final float brightness;

	public EntityGlowstoneGolemMinion(final World world) {
		// dangerous ... too expensive to check for non-null in constructor call :(
		this(world, 1.0F, GolemLookup.getConfig(com.golems.entity.EntityGlowstoneGolem.class).getInt(FREQUENCY),
				GolemLookup.getConfig(com.golems.entity.EntityGlowstoneGolem.class).getBoolean(ALLOW_SPECIAL));
		this.isImmuneToFire = true;
		this.setCanTakeFallDamage(true);
		this.setCanSwim(true);
		this.setLootTableLoc(GolemNames.GLOWSTONE_GOLEM);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.26D);
	}

	/**
	 * Flexible constructor to allow child classes to customize
	 **/
	public EntityGlowstoneGolemMinion(final World world, final float lightLevel, final int freq, final boolean allowed) {
		super(world);
		int lightInt = (int) (lightLevel * 15.0F);
		this.brightness = lightLevel;
		final IBlockState state = GolemItems.blockLightSource.getDefaultState().withProperty(BlockUtilityGlow.LIGHT_LEVEL,
				lightInt);
		this.tasks.addTask(9, new EntityAIUtilityBlock(this, state, freq, allowed));

		GolemConfigSet cfg = getConfig(this);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(cfg.getBaseAttack());
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(cfg.getMaxHealth());
	}

	@Override
	protected ResourceLocation applyTexture() {
		return makeTexture(ExtraGolems.MODID, GolemNames.GLOWSTONE_GOLEM);
	}

	@Override
	public SoundEvent getGolemSound() {
		return SoundEvents.BLOCK_GLASS_STEP;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.BLOCK_GLASS_BREAK;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getBrightnessForRender() {
		return (int) (15728880F * this.brightness);
	}

	@Override
	public float getBrightness() {
		return this.brightness;
	}

	@Override
	public boolean isProvidingLight() {
		return true;
	}
}
