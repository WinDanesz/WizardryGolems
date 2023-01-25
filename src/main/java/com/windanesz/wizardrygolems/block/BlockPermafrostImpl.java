package com.windanesz.wizardrygolems.block;

import com.windanesz.wizardrygolems.entity.living.EntityPermafrostGolemMinion;
import electroblob.wizardry.block.BlockPermafrost;
import electroblob.wizardry.registry.Spells;
import electroblob.wizardry.registry.WizardryPotions;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.EntityUtils;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockPermafrostImpl extends BlockPermafrost {

	@Override
	public void onEntityCollision(World world, BlockPos pos, IBlockState state, Entity entity) {

		if (EntityUtils.isLiving(entity) && entity.ticksExisted % 30 == 0 && !(entity instanceof EntityPermafrostGolemMinion)) {
			// Can't make it player damage unless we make this block a tile entity, but there will be too many for that
			entity.attackEntityFrom(DamageSource.MAGIC, Spells.permafrost.getProperty(Spell.DAMAGE).floatValue());
			int duration = Spells.permafrost.getProperty(Spell.EFFECT_DURATION).intValue();
			int amplifier = Spells.permafrost.getProperty(Spell.EFFECT_STRENGTH).intValue();
			((EntityLivingBase) entity).addPotionEffect(new PotionEffect(WizardryPotions.frost, duration, amplifier));
		}

		// EntityLivingBase's slipperiness code doesn't get the block below it properly so slipperiness only works for
		// full blocks...
		if (entity.onGround) {

			// Not brilliant but it's about the best I can do
			entity.motionX *= 1.12 - entity.motionX * entity.motionX;
			entity.motionZ *= 1.12 - entity.motionZ * entity.motionZ;

		}

	}

	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing face) {
		return face == EnumFacing.DOWN ? BlockFaceShape.SOLID : face == EnumFacing.UP ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
	}

	@Override
	public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
		return true;
	}
}