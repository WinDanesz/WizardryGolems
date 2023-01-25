package com.windanesz.wizardrygolems.block;

import com.windanesz.wizardrygolems.tile.TileEntityLivingSnow;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockLivingSnow extends BlockSnow implements ITileEntityProvider {

	public BlockLivingSnow() {
	}

	@Nullable
	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityLivingSnow();
	}

}
