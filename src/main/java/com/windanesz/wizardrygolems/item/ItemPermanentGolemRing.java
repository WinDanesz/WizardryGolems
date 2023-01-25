package com.windanesz.wizardrygolems.item;

import com.windanesz.wizardrygolems.registry.WizardryGolemsItems;
import electroblob.wizardry.constants.Element;
import electroblob.wizardry.data.IStoredVariable;
import electroblob.wizardry.data.Persistence;
import electroblob.wizardry.data.WizardData;
import electroblob.wizardry.item.ItemArtefact;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.EntityUtils;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.UUID;

import static electroblob.wizardry.spell.SpellMinion.MINION_LIFETIME;

public class ItemPermanentGolemRing extends ItemGolemancyArtefact {

	public static final IStoredVariable<UUID> UUID_KEY = IStoredVariable.StoredVariable.ofUUID("permanentGolemUUID", Persistence.ALWAYS);

	public ItemPermanentGolemRing(EnumRarity rarity, Type type, Element element) {
		super(rarity, type, element);
		WizardData.registerStoredVariables(UUID_KEY);
	}

	public static void setEntity(EntityPlayer caster, EntityLivingBase entity) {
		WizardData data = WizardData.get(caster);
		data.setVariable(UUID_KEY, entity.getUniqueID());
	}

	@Nullable
	public static Entity getEntity(EntityPlayer caster, World world) {
		WizardData data = WizardData.get(caster);

		if (!world.isRemote) {
			return EntityUtils.getEntityByUUID(world, data.getVariable(UUID_KEY));
		}
		return null;
	}

	public static int getLifeTime(EntityLivingBase caster, EntityLivingBase entity, Spell spell, SpellModifiers modifiers) {
		int lifetime = (int) (spell.getProperty(MINION_LIFETIME).floatValue() * modifiers.get(WizardryItems.duration_upgrade));

		if (caster instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) caster;
			if (ItemArtefact.isArtefactActive(player, WizardryGolemsItems.ring_permanent_golem)

					&& !player.getCooldownTracker().hasCooldown(WizardryGolemsItems.ring_permanent_golem)) {
				if (getEntity(player, caster.world) == null) {
					setEntity(player, entity);
					player.getCooldownTracker().setCooldown(WizardryGolemsItems.ring_permanent_golem, 6000);
					return -1;
				}
			}
		}
		return lifetime;
	}
}
