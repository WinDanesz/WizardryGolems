package com.windanesz.wizardrygolems.entity.living;

import com.golems.main.ExtraGolems;
import com.golems.util.GolemConfigSet;
import com.windanesz.ancientspellcraft.spell.FairyRing;
import com.windanesz.wizardrygolems.integration.ASIntegration;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class EntityMushroomGolemMinion extends EntityEarthGolemMinion {

	private boolean red = false;
	protected static final DataParameter<Boolean> RED = EntityDataManager.<Boolean>createKey(EntityMushroomGolemMinion.class, DataSerializers.BOOLEAN);
	public EntityMushroomGolemMinion(World world) {
		super(world);
	}

	public EntityMushroomGolemMinion(World world, boolean isChild) {
		super(world);
		setChild(isChild);
	}

	public EntityMushroomGolemMinion(World world, boolean isChild, boolean red) {
		super(world);
		setChild(isChild);
		setRed(red);
		this.red = red;
	}

	@Override
	protected void entityInit() {
		this.getDataManager().register(RED, false);
		super.entityInit();
	}

	public void setRed(boolean red) {
		this.getDataManager().set(RED, red);
	}

	public boolean isRed() {
		return this.dataManager.get(RED);
	}

	public void notifyDataManagerChange(DataParameter<?> key) {
		super.notifyDataManagerChange(key);
		// change stats if this is a child vs. an adult golem
		GolemConfigSet cfg = getConfig(this);
		if (isRed()) {
			this.setTextureType(makeTexture(ExtraGolems.MODID, isRed() ? "golem_shroom_red" : "golem_shroom_brown"));
		}
		if (this.isChild()) {
			this.setSize(0.7F, 1.45F);
			this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(cfg.getBaseAttack() * 0.6F);
			this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(cfg.getMaxHealth() / 1.5f);
			this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3D);

		} else {
			this.setSize(1.4F, 2.9F);
			this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(cfg.getBaseAttack());
			this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(cfg.getMaxHealth());
		}
	}

	@Override
	protected ResourceLocation applyTexture() {
		return makeTexture(ExtraGolems.MODID, red ? "golem_shroom_red" : "golem_shroom_brown");
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
			SpellModifiers m = new SpellModifiers();
			m.set(WizardryItems.blast_upgrade, 0.5f, false);
			FairyRing.summonMushroomRing(world, this.getOwner() instanceof EntityPlayer ? (EntityPlayer) this.getOwner() : this, this.getPosition(), m);
		}
		super.onDeath(cause);
	}


	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setBoolean("isRed", isRed());
		return super.writeToNBT(compound);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		if (compound.hasKey("isRed")) {
			this.setRed(compound.getBoolean("isRed"));
		}
	}
}
