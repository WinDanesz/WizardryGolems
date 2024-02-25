package com.windanesz.wizardrygolems.entity.living;

import com.windanesz.wizardrygolems.WizardryGolems;
import com.windanesz.wizardrygolems.registry.WizardryGolemsSounds;
import electroblob.wizardry.entity.living.EntityAIAttackSpell;
import electroblob.wizardry.entity.living.ISpellCaster;
import electroblob.wizardry.event.SpellCastEvent;
import electroblob.wizardry.packet.PacketCastSpell;
import electroblob.wizardry.packet.WizardryPacketHandler;
import electroblob.wizardry.registry.Spells;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EntityFlameGolemMinion extends EntityFireGolemMinion implements ISpellCaster {

	private double AISpeed = 0.5;

	// Can attack for 7 seconds, then must cool down for 3.
	private EntityAIAttackSpell<EntityFlameGolemMinion> spellAttackAI = new EntityAIAttackSpell<>(this, AISpeed, 15f, 5, 140);

	private Spell continuousSpell;
	private int spellCounter;

	private static final List<Spell> RANGED = Collections.unmodifiableList(
			new ArrayList<Spell>() {{
				add(Spells.fire_breath);
				add(Spells.fireskin);
				add(Spells.firebolt);
				add(Spells.disintegration);
			}}
	);
	private static final List<Spell> RANGED_AND_MELEE = Collections.unmodifiableList(
			new ArrayList<Spell>() {{
				add(Spells.fire_breath);
				add(Spells.fireskin);
				add(Spells.firebolt);
				add(Spells.disintegration);
				add(Spells.ring_of_fire);
			}}
	);

	public EntityFlameGolemMinion(World world) {
		super(world);
		// For some reason this can't be in initEntityAI
		this.tasks.addTask(1, this.spellAttackAI);
		this.setImmuneToFire(false);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(80);
	}

	@Override
	protected void initEntityAI() {

		this.tasks.addTask(0, new EntityAIWatchClosest(this, EntityLivingBase.class, 0));
		this.tasks.addTask(3, new EntityAILookIdle(this));
		// this.targetTasks.addTask(0, new EntityAIMoveTowardsTarget(this, 1, 10));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
		this.tasks.addTask(7, new EntityAIWander(this, 0.8D));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityLivingBase.class,
				0, false, true, this.getTargetSelector()));

		this.setAIMoveSpeed((float) AISpeed);
	}

	@Override
	protected ResourceLocation applyTexture() {
		return makeTexture(WizardryGolems.MODID, "golem_flame");
	}

	@Override
	public SoundEvent getGolemSound() { return WizardryGolemsSounds.FLAME_GOLEM_SOUND; }

	@Override
	public void onUpdate() {
		super.onUpdate();
		addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 20));

		setFire(3);

		if (!world.isRemote && world.getTotalWorldTime() % 20 == 0 && world.rand.nextInt(10) == 0) {

			if (getAttackTarget() == null || getAttackTarget().isDead) {
				spellAttackAI.resetTask();
			}

			if (getCaster() instanceof EntityPlayer) {
				EntityPlayer caster = (EntityPlayer) getCaster();
				SpellModifiers modifiers = new SpellModifiers();

				Spell spell = Spells.flaming_weapon;
				// If anything stops the spell working at this point, nothing else happens.
				if(MinecraftForge.EVENT_BUS.post(new SpellCastEvent.Pre(SpellCastEvent.Source.OTHER, spell, getCaster(), new SpellModifiers()))){
					return;
				}

				if(spell.cast(caster.world, caster, EnumHand.MAIN_HAND, 0, modifiers)){

					MinecraftForge.EVENT_BUS.post(new SpellCastEvent.Post(SpellCastEvent.Source.OTHER, spell, caster, modifiers));

					if(spell.requiresPacket()){
						// Sends a packet to all players in dimension to tell them to spawn particles.
						// Only sent if the spell succeeded, because if the spell failed, you wouldn't
						// need to spawn any particles!
						IMessage msg = new PacketCastSpell.Message(caster.getEntityId(), null, spell, modifiers);
						WizardryPacketHandler.net.sendToDimension(msg, caster.world.provider.getDimension());
					}
				}
			}
		}

	}

	@Override
	public List<Spell> getSpells(){
		if (this.getAttackTarget() != null && getDistance(getAttackTarget()) > 3) {
			return RANGED;
		}
		return RANGED_AND_MELEE;
	}

	@Override
	public SpellModifiers getModifiers(){
		return new SpellModifiers();
	}

	@Override
	public Spell getContinuousSpell(){
		return continuousSpell;
	}

	@Override
	public void setContinuousSpell(Spell spell){
		continuousSpell = spell;
	}

	@Override
	public void setSpellCounter(int count){
		spellCounter = count;
	}

	@Override
	public int getSpellCounter(){
		return spellCounter;
	}

	@Override
	public float getBrightness(){
		return 0.8F;
	}
	@Override
	@SideOnly(Side.CLIENT)
	public int getBrightnessForRender(){
		return 15728880;
	}
	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();

		if (world.isRemote) {
			for(int i = 0; i < 2; i++){
				this.world.spawnParticle(EnumParticleTypes.FLAME,
						this.posX + (this.rand.nextDouble() - 0.5D) * (double)this.width,
						this.posY + this.height / 2 + this.rand.nextDouble() * (double)this.height / 2,
						this.posZ + (this.rand.nextDouble() - 0.5D) * (double)this.width, 0.0D, -0.1D, 0.0D);
			}
		}

	}
}
