package com.windanesz.wizardrygolems.entity.living;

import com.golems.main.ExtraGolems;
import com.golems.util.GolemNames;
import electroblob.wizardry.entity.living.ISpellCaster;
import electroblob.wizardry.event.SpellCastEvent;
import electroblob.wizardry.registry.Spells;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

public class EntityGoldGolemMinion extends EntityGolemBaseMinion implements ISpellCaster {

	public EntityGoldGolemMinion(World world) {
		super(world);

		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.19D);
		this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(0.9D);
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();

		if (this.rand.nextInt(100) == 0) {
			SpellModifiers modifiers = new SpellModifiers();
			if (MinecraftForge.EVENT_BUS.post(new SpellCastEvent.Pre(SpellCastEvent.Source.NPC, getSpells().get(0), this, modifiers))) {
				if (getSpells().get(0).cast(this.world, this, EnumHand.MAIN_HAND, 0, this, modifiers)) {

					MinecraftForge.EVENT_BUS.post(new SpellCastEvent.Post(SpellCastEvent.Source.NPC, getSpells().get(0), this, modifiers));

					// For now, the cooldown is just added to the co nstant base cooldown. I think this
					// is a reasonable way of doing things; it's certainly better than before.
				}
			}
		}
	}

	@Override
	public void onUpdate(){
		super.onUpdate();
		this.updateDelegate();
	}

	@Override
	protected ResourceLocation applyTexture() {
		return makeTexture(ExtraGolems.MODID, GolemNames.GOLD_GOLEM);
	}

	@Override
	public SoundEvent getGolemSound() {
		return SoundEvents.BLOCK_STONE_STEP;
	}

	@Nonnull
	@Override
	public List<Spell> getSpells() {
		return Collections.singletonList(Spells.group_heal);
	}
}
