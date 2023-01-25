package com.windanesz.wizardrygolems.entity.living;

import com.golems.events.IceGolemFreezeEvent;
import com.windanesz.wizardrygolems.WizardryGolems;
import com.windanesz.wizardrygolems.registry.WizardryGolemsBlocks;
import com.windanesz.wizardrygolems.registry.WizardryGolemsItems;
import electroblob.wizardry.item.ItemArtefact;
import electroblob.wizardry.registry.WizardryPotions;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;

import java.util.List;
import java.util.function.Function;

public class EntityPermafrostGolemMinion extends EntityGolemBaseMinion implements IIceGolem {

	public static final String ALLOW_SPECIAL = "Allow Special: Freeze Blocks";
	public static final String AOE = "Area of Effect";

	public EntityPermafrostGolemMinion(World world) {
		super(world);
		this.setCanTakeFallDamage(true);

		this.setCanSwim(true); // just in case
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.26D);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20F);
	}

	@Override
	public boolean isPotionApplicable(PotionEffect potioneffectIn) {
		return potioneffectIn.getPotion() != WizardryPotions.frost && super.isPotionApplicable(potioneffectIn);
	}

	@Override
	protected ResourceLocation applyTexture() {
		return makeTexture(WizardryGolems.MODID, "golem_permafrost");
	}

	/**
	 * Called frequently so the entity can update its state every tick as required.
	 * For example, zombies and skeletons use this to react to sunlight and start to
	 * burn.
	 */
	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();

		if (!this.world.isRemote && this.onGround && this.world.isAirBlock(this.getPosition())) {
			if (this.world.isAirBlock(this.getPosition())) {
				//this.world.playSound(null, this.getPosition(), SoundEvents.ICE, SoundCategory.BLOCKS, 1.0F, this.world.rand.nextFloat() * 0.4F + 0.8F);
				this.world.setBlockState(this.getPosition(), WizardryGolemsBlocks.permafrost_custom.getDefaultState(), 11);
			}
		}
		
		// calling every other tick reduces lag by 50%
		if (this.ticksExisted % 5 == 0) {
			final int x = MathHelper.floor(this.posX);
			final int y = MathHelper.floor(this.posY - 0.20000000298023224D);
			final int z = MathHelper.floor(this.posZ);
			final BlockPos below = new BlockPos(x, y, z);

			if (this.world.getBiome(below).getTemperature(below) > 1.0F) {
				this.attackEntityFrom(DamageSource.ON_FIRE, 1.0F);
			}

			final IceGolemFreezeEvent event = new IceGolemFreezeEvent(this, below, 1);
			if (!MinecraftForge.EVENT_BUS.post(event) && event.getResult() != Event.Result.DENY) {
				this.freezeBlocks(event.getAffectedPositions(), event.getFunction(), event.updateFlag);
			}
		}
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		onGolemUpdate(this);
	}

	@Override
	public void onSuccessfulAttack(EntityLivingBase target) {
		onSuccessFulAttackDelegate(this, target);

		if (!this.world.isRemote && target != null && this.world.rand.nextBoolean() && getCaster() instanceof EntityPlayer
				&& ItemArtefact.isArtefactActive((EntityPlayer) getCaster(), WizardryGolemsItems.ring_frostbite)) {
			target.addPotionEffect(new PotionEffect(WizardryPotions.frost, 30, 1));
		}
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.BLOCK_GLASS_BREAK;
	}

	@Override
	public SoundEvent getGolemSound() {
		return SoundEvents.BLOCK_GLASS_STEP;
	}

	@Override
	public void onDeath(DamageSource cause) {
		onDeathDelegate(this);
		super.onDeath(cause);
	}

	/**
	 * Usually called after creating and firing a {@link IceGolemFreezeEvent}.
	 * Iterates through the list of positions and calls
	 * {@code apply(IBlockState input)} on the passed
	 * {@code Function<IBlockState, IBlockState>} .
	 *
	 * @return whether all setBlockState calls were successful.
	 **/
	public boolean freezeBlocks(final List<BlockPos> positions, final Function<IBlockState, IBlockState> function,
			final int updateFlag) {
		boolean flag = false;
		for (int i = 0, len = positions.size(); i < len; i++) {
			final BlockPos pos = positions.get(i);
			final IBlockState currentState = this.world.getBlockState(pos);
			final IBlockState toSet = function.apply(currentState);
			if (toSet != null && toSet != currentState) {
				flag &= this.world.setBlockState(pos, toSet, updateFlag);
			}
		}
		return flag;
	}
}
