package com.windanesz.wizardrygolems.tile;

import com.windanesz.wizardrygolems.block.BlockLivingSnow;
import com.windanesz.wizardrygolems.entity.living.EntitySnowGolemMinion;
import electroblob.wizardry.entity.living.ISummonedCreature;
import electroblob.wizardry.tileentity.TileEntityPlayerSave;
import electroblob.wizardry.util.AllyDesignationSystem;
import electroblob.wizardry.util.EntityUtils;
import electroblob.wizardry.util.ParticleBuilder;
import net.minecraft.block.BlockSnow;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class TileEntityLivingSnow extends TileEntityPlayerSave implements ITickable {

	float radius = 8;
	public static final int cooldownMax = 400;
	int cooldown = 20;
	int timeToSpawn = -1;

	public TileEntityLivingSnow() {
	}

	@Override
	public void update() {

		if (cooldown > 0) {
			cooldown--;
		}

		if (!getWorld().isDaytime() && !this.world.isRemote && cooldown <= 0 && world.getTotalWorldTime() % 41 == 0) {

			List<EntityLivingBase> nearby = EntityUtils.getLivingWithinRadius(radius, this.pos.getX(), this.pos.getY(), this.pos.getZ(), world);
			nearby.removeIf(e -> !isValidTarget(e));
			if (!nearby.isEmpty()) {
				timeToSpawn = 40;
				this.sync();
			}
		}

		if (timeToSpawn >= 0) {
			spawnParticle();
			timeToSpawn--;
			if (timeToSpawn == 0 && !world.isRemote) {

				EntitySnowGolemMinion golem = new EntitySnowGolemMinion(world);
				BlockPos pos = this.pos;
				golem.setPosition(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
				int lifetime = -1;

				if (this.getCaster() != null) {
					golem.setCaster(this.getCaster());
					lifetime = 400;
				}

				if (this.getCaster() == null) {
					golem.setCaster(null);
				}

				((ISummonedCreature) golem).setLifetime(lifetime);
				// Modifier implementation
				// Attribute modifiers are pretty opaque, see https://minecraft.gamepedia.com/Attribute#Modifiers
				golem.setHealth(golem.getMaxHealth()); // Need to set this because we may have just modified the value
				world.spawnEntity(golem);
				this.cooldown = cooldownMax;
				this.timeToSpawn = -1;

				// worldgen ones are single use
				if (getCaster() == null) {
					world.setBlockState(this.pos, Blocks.SNOW_LAYER.getDefaultState().withProperty(BlockSnow.LAYERS, world.getBlockState(this.pos).getValue(BlockLivingSnow.LAYERS)));
				}
			}
		}
	}

	private void spawnParticle() {
		if (world.isRemote) {
			for (int i = 0; i < 6; i++) {
				float radius = 1;
				double speed = (world.rand.nextBoolean() ? 1 : -1) * (0.1 + 0.05 * world.rand.nextDouble());
				ParticleBuilder.create(ParticleBuilder.Type.SNOW).pos(this.pos.getX() + 0.5, this.pos.getY() + 0.1, this.pos.getZ() + 0.5).vel(0, 0.01, 0)
						.time(20).scale(1).spin((world.rand.nextDouble() * (radius - 0.5) + 0.5) - (timeToSpawn * 0.01), speed).shaded(true).spawn(world);
			}
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
		tagCompound.setInteger("cooldown", cooldown);
		tagCompound.setInteger("timeToSpawn", timeToSpawn);
		return super.writeToNBT(tagCompound);
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		this.cooldown = tagCompound.getInteger("cooldown");
		this.timeToSpawn = tagCompound.getInteger("timeToSpawn");
		super.readFromNBT(tagCompound);
	}

	public boolean isValidTarget(Entity target) {
		return AllyDesignationSystem.isValidTarget(this.getCaster(), target);
	}
}
