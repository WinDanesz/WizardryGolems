package com.windanesz.wizardrygolems.entity.living;

import com.golems.main.ExtraGolems;
import com.golems.util.GolemConfigSet;
import com.golems.util.GolemNames;
import com.windanesz.wizardrygolems.registry.WizardryGolemsSpells;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static electroblob.wizardry.spell.SpellMinion.MINION_LIFETIME;

/**
 * Based on {@link com.golems.entity.EntityMagmaGolem} - Author: skyjay1
 * Author: WinDanesz
 */
public class EntityMagmaGolemMinion extends EntityFireGolemMinion {

	public static final String ALLOW_FIRE_SPECIAL = "Allow Special: Burn Enemies";
	public static final String ALLOW_LAVA_SPECIAL = "Allow Special: Melt Cobblestone";
	public static final String ALLOW_SPLITTING = "Allow Special: Split";
	public static final String ALLOW_WATER_DAMAGE = "Enable Water Damage";
	public static final String MELT_DELAY = "Melting Delay";

	/**
	 * Golem should stand in one spot for number of ticks before affecting the block
	 * below it.
	 */
	private int ticksStandingStill;
	/**
	 * Helpers for "Standing Still" code
	 */
	private int stillX, stillZ;
	/**
	 * Whether this golem is hurt by water
	 */
	private boolean isHurtByWater = true;

	private boolean allowMelting;
	private int meltDelay;

	private static final String TEXTURE_LOC = ExtraGolems.MODID + ":textures/entity/magma/" + GolemNames.MAGMA_GOLEM;
	private static final ResourceLocation[] TEXTURES = new ResourceLocation[] {
			new ResourceLocation(TEXTURE_LOC + "_0.png"), new ResourceLocation(TEXTURE_LOC + "_1.png"),
			new ResourceLocation(TEXTURE_LOC + "_2.png"), new ResourceLocation(TEXTURE_LOC + "_3.png"),
			new ResourceLocation(TEXTURE_LOC + "_2.png"), new ResourceLocation(TEXTURE_LOC + "_1.png")};

	public EntityMagmaGolemMinion(World world) {
		super(world);
		this.isHurtByWater = true;
		this.allowMelting = true;
		this.meltDelay = 240;
		this.ticksStandingStill = 0;
		this.setImmuneToFire(true);
		this.setCanSwim(!this.isHurtByWater);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.28D);
		this.setSize(1.4F, 2.9F);
	}

	public EntityMagmaGolemMinion(World world, boolean isChild) {
		super(world);
		setChild(isChild);
		this.isHurtByWater = true;
		this.allowMelting = true;
		this.meltDelay = 240;
		this.ticksStandingStill = 0;
		this.setImmuneToFire(true);
		this.setCanSwim(!this.isHurtByWater);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.28D);
		this.setSize(1.4F, 2.9F);
	}

	@Override
	public void notifyDataManagerChange(DataParameter<?> key) {
		super.notifyDataManagerChange(key);
		// change stats if this is a child vs. an adult golem
		GolemConfigSet cfg = getConfig(this);
		if (this.isChild()) {
			this.setSize(0.7F, 1.45F);
			this.allowMelting = false;
			this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(cfg.getBaseAttack() * 0.6F);
			this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(cfg.getMaxHealth() / 3);
		} else {
			this.setSize(1.4F, 2.9F);
			this.allowMelting = true;
			this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(cfg.getBaseAttack());
			this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(cfg.getMaxHealth());
		}
	}

	@Override
	protected ResourceLocation applyTexture() {
		return TEXTURES[0];
	}

	@Override
	public ResourceLocation getTextureType() {
		final int changeInterval = 5;
		int textureNum = ((this.ticksExisted + this.getEntityId()) / changeInterval) % TEXTURES.length;
		return TEXTURES[textureNum];
	}

	@Override
	public void onSuccessfulAttack(EntityLivingBase target) {
		if (getConfig(this).getBoolean(ALLOW_FIRE_SPECIAL)) {
			target.setFire(2 + rand.nextInt(5));
		}
	}

	@Override
	public void setDead() {
		// spawn baby golems here if possible
		if (!this.world.isRemote && !this.isChild()) {

			for (int i = 0; i < 2; i++) {

				EntityMagmaGolemMinion childGolem = new EntityMagmaGolemMinion(this.world, true);

				// copy attack target info
				if (this.getAttackTarget() != null) {
					childGolem.setAttackTarget(this.getAttackTarget());
				}

				// set lifetime and caster
				childGolem.setLifetime((int)(WizardryGolemsSpells.fire_golemancy.getProperty(MINION_LIFETIME).floatValue()));
				childGolem.setCaster(this.getCaster());

				// set location
				childGolem.setLocationAndAngles(this.posX + rand.nextDouble() - 0.5D, this.posY, this.posZ + rand.nextDouble() - 0.5D,
						this.rotationYaw + rand.nextInt(20) - 10, 0);

				// spawn the entities
				this.getEntityWorld().spawnEntity(childGolem);
			}

		}

		super.setDead();
	}

	/**
	 * Called frequently so the entity can update its state every tick as required.
	 * For example, zombies and skeletons use this to react to sunlight and start to
	 * burn.
	 */
//	@Override
//	public void onLivingUpdate() {
//		super.onLivingUpdate();
//		// take damage from water/rain
//		if (this.isHurtByWater && this.isWet()) {
//			this.attackEntityFrom(DamageSource.DROWN, 0.5F);
//		}
//		// check the cobblestone-melting math
//		if (this.allowMelting && !this.isChild()) {
//			final int x = MathHelper.floor(this.posX);
//			final int y = MathHelper.floor(this.posY - 0.20000000298023224D);
//			final int z = MathHelper.floor(this.posZ);
//			final BlockPos below = new BlockPos(x, y, z);
//			final Block b1 = this.world.getBlockState(below).getBlock();
//
//			if (x == this.stillX && z == this.stillZ) {
//				// check if it's been holding still long enough AND on top of cobblestone
//				if (++this.ticksStandingStill >= this.meltDelay && b1 == Blocks.COBBLESTONE && rand.nextInt(16) == 0) {
//					IBlockState replace = Blocks.MAGMA.getDefaultState();
//					this.world.setBlockState(below, replace, 3);
//					this.ticksStandingStill = 0;
//				}
//			} else {
//				this.ticksStandingStill = 0;
//				this.stillX = x;
//				this.stillZ = z;
//			}
//		}
//	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getBrightnessForRender() {
		return 15728880;
	}

	@Override
	public SoundEvent getGolemSound() {
		return SoundEvents.BLOCK_STONE_STEP;
	}

	@Override
	public boolean hasParticleEffect() {
		return false;
	}

	/**
	 * Returns the colour of this creature's appear/disappear animation.
	 */
	@Override
	public int getAnimationColour(float animationProgress) {
		return 0x923807;
	}
}
