package com.windanesz.wizardrygolems.entity.living;

import com.windanesz.wizardrygolems.WizardryGolems;
import electroblob.wizardry.entity.living.EntityAIAttackSpell;
import electroblob.wizardry.entity.living.ISpellCaster;
import electroblob.wizardry.event.SpellCastEvent;
import electroblob.wizardry.packet.PacketCastSpell;
import electroblob.wizardry.packet.WizardryPacketHandler;
import electroblob.wizardry.registry.Spells;
import electroblob.wizardry.registry.WizardryPotions;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.ParticleBuilder;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EntityWinterGolemMinion extends EntityGolemBaseMinion implements ISpellCaster, IIceGolem {

	private double AISpeed = 0.5;

	private EntityAIAttackSpell<EntityWinterGolemMinion> spellAttackAI = new EntityAIAttackSpell<>(this, AISpeed, 15f, 30, 140);

	private Spell continuousSpell;
	private int spellCounter;

	private static final List<Spell> attack = Collections.unmodifiableList(
			new ArrayList<Spell>() {{
				add(Spells.ice_lance);
				add(Spells.ice_charge);
				add(Spells.ice_spikes);
				if (Loader.isModLoaded("mospells")) {
					add(Spell.get("mospells:frost_breath"));
					add(Spell.get("mospells:winds_of_winter"));
				} else {
					add(Spells.frost_ray);
				}
				add(Spells.blizzard);
			}}
	);

	public EntityWinterGolemMinion(World world) {
		super(world);
		this.tasks.addTask(1, this.spellAttackAI);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(80);
		this.setImmuneToFire(true);
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
		return makeTexture(WizardryGolems.MODID, "golem_winter");
	}

	/**
	 * Called frequently so the entity can update its state every tick as required.
	 * For example, zombies and skeletons use this to react to sunlight and start to
	 * burn.
	 */
	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		if (this.ticksExisted % 5 == 0) {
			addPotionEffect(new PotionEffect(WizardryPotions.ice_shroud, 20));
		}
	}


	@Override
	public void onUpdate() {
		super.onUpdate();
		onGolemUpdate(this);

		if (!world.isRemote && world.getTotalWorldTime() % 20 == 0 && world.rand.nextInt(10) == 0) {
			if (getCaster() instanceof EntityPlayer) {
				EntityPlayer caster = (EntityPlayer) getCaster();
				SpellModifiers modifiers = new SpellModifiers();

				Spell spell = Spells.freezing_weapon;
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

		if (world.isRemote) {
			for (int i = 0; i < 3; i++) {
				double speed = (rand.nextBoolean() ? 1 : -1) * (0.1 + 0.05 * rand.nextDouble());
				ParticleBuilder.create(ParticleBuilder.Type.SNOW).pos(this.posX, this.posY + rand.nextDouble() * height, this.posZ).vel(0, 0, 0)
						.time(50).scale(2).spin(rand.nextDouble() * (1 - 0.5) + 0.5, speed).shaded(true).spawn(world);
			}

			for (int i = 0; i < 2; i++) {
				double speed = (rand.nextBoolean() ? 1 : -1) * (0.05 + 0.02 * rand.nextDouble());
				ParticleBuilder.create(ParticleBuilder.Type.CLOUD).pos(this.posX, this.posY + rand.nextDouble() * (height - 0.5), this.posZ)
						.clr(0xffffff).shaded(true).spin(rand.nextDouble() * (1 - 1) + 0.5, speed).spawn(world);
			}
		}
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_SNOWMAN_DEATH;
	}

	@Override
	public SoundEvent getGolemSound() {	return SoundEvents.BLOCK_SNOW_STEP;	}

	@Override
	public void onDeath(DamageSource cause) {
		onDeathDelegate(this);
		super.onDeath(cause);
	}


	@Override
	public List<Spell> getSpells(){
		return attack;
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
}
