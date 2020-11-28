package com.windanesz.wizardrygolems.entity.living;

import com.golems.entity.GolemBase;
import electroblob.wizardry.Wizardry;
import electroblob.wizardry.entity.living.ISummonedCreature;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

import java.util.UUID;

public abstract class EntityGolemBaseMinion extends GolemBase implements ISummonedCreature {

	// Minion field implementations
	private int lifetime = -1;
	private UUID casterUUID;

	public EntityGolemBaseMinion(World world) {
		super(world);
	}

	/// Minion related methods ///

	// Setter + getter implementations
	@Override
	public int getLifetime() { return lifetime; }

	@Override
	public void setLifetime(int lifetime) { this.lifetime = lifetime; }

	@Override
	public UUID getOwnerId() { return casterUUID; }

	@Override
	public void setOwnerId(UUID uuid) { this.casterUUID = uuid; }

	// Recommended overrides
	@Override
	protected int getExperiencePoints(EntityPlayer player) { return 0; }

	@Override
	protected boolean canDropLoot() { return false; }

	@Override
	protected Item getDropItem() { return null; }

	@Override
	protected ResourceLocation getLootTable() { return null; }

	@Override
	public boolean canPickUpLoot() { return false; }

	@Override
	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		super.writeEntityToNBT(nbttagcompound);
		this.writeNBTDelegate(nbttagcompound);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		super.readEntityFromNBT(nbttagcompound);
		this.readNBTDelegate(nbttagcompound);
	}

	@Override
	public boolean getCanSpawnHere() {
		return this.world.getDifficulty() != EnumDifficulty.PEACEFUL;
	}

	@Override
	public ITextComponent getDisplayName() {
		if (getCaster() != null) {
			return new TextComponentTranslation(NAMEPLATE_TRANSLATION_KEY, getCaster().getName(),
					new TextComponentTranslation("entity." + this.getEntityString() + ".name"));
		} else {
			return super.getDisplayName();
		}
	}

	@Override
	public boolean hasCustomName() {
		// If this returns true, the renderer will show the nameplate when looking directly at the entity
		return Wizardry.settings.summonedCreatureNames && getCaster() != null;
	}

	@Override
	public void onSpawn() {
		this.spawnParticleEffect();
	}

	@Override
	public void onDespawn() {

	}

	private void spawnParticleEffect() {

	}

	/// ExtraGolems methods ///

	@Override
	public SoundEvent getGolemSound() {
		return SoundEvents.BLOCK_STONE_STEP;
	}

	@Override
	protected ResourceLocation applyTexture() {
		return null;
	}

	@Override
	public boolean hasParticleEffect() {
		return false;
	}

}
