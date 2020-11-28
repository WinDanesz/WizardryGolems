package com.windanesz.wizardrygolems.entity.living;

import com.windanesz.wizardrygolems.WizardryGolems;
import electroblob.wizardry.block.BlockThorns;
import electroblob.wizardry.registry.WizardryBlocks;
import electroblob.wizardry.tileentity.TileEntityThorns;
import electroblob.wizardry.util.BlockUtils;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.SoundEvents;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

/**
 * Based on {@link com.golems.entity.EntityStrawGolem} - Author: skyjay1
 * Author: WinDanesz
 */
public class EntityStrawThornsGolemMinion extends EntityEarthGolemMinion {

	public static final String ALLOW_SPECIAL = "Allow Special: Crop Boost";
	private int range;
	private int boostFreq;
	private boolean allowed;

	public EntityStrawThornsGolemMinion(World world) {
		super(world);
		this.setCanSwim(true);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.35D);
		this.boostFreq = 40;
		this.boostFreq += this.rand.nextInt(Math.max(10, this.boostFreq / 2));
		this.range = 2;
		this.allowed = getConfig(this).getBoolean(ALLOW_SPECIAL);
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		// look for crops to boost
		if (this.allowed && this.rand.nextInt(40) == 0) {
			if (!world.isRemote)
				tryBoostCrop();
		}
	}

	private boolean tryBoostCrop() {
		final int variationY = 1;
			final int x = MathHelper.floor(this.posX);
			final int y = MathHelper.floor(this.posY);
			final int z = MathHelper.floor(this.posZ);
			final int x1 = this.rand.nextInt(this.range * 2) - this.range;
			final int y1 = this.rand.nextInt(variationY * 2) - variationY;
			final int z1 = this.rand.nextInt(this.range * 2) - this.range;
			final BlockPos pos = new BlockPos(x + x1, y + y1, z + z1);

			if(world.getBlockState(pos).getBlock() != WizardryBlocks.thorns && world.isAirBlock(pos) && BlockUtils.canBlockBeReplaced(world, pos) && BlockUtils.canBlockBeReplaced(world, pos.up())){

				((BlockThorns) WizardryBlocks.thorns).placeAt(world, pos, 3);

				TileEntity tileentity = world.getTileEntity(pos);

				if(tileentity instanceof TileEntityThorns){

					((TileEntityThorns)tileentity).setLifetime((int)(400));

					if(getCaster() != null) ((TileEntityThorns)tileentity).setCaster(getCaster());
					((TileEntityThorns)tileentity).damageMultiplier = 1;

					((TileEntityThorns)tileentity).sync();
				} else {
				}
//			}
		}
		return false;
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		this.updateDelegate();
	}

	@Override
	protected ResourceLocation applyTexture() {
		return makeTexture(WizardryGolems.MODID, "golem_strawthorns");
	}

	@Override
	public SoundEvent getGolemSound() {
		return SoundEvents.BLOCK_GRAVEL_STEP;
	}

}
