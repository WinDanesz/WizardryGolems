package com.windanesz.wizardrygolems.item;

import com.windanesz.wizardrygolems.block.BlockLivingSnow;
import com.windanesz.wizardrygolems.registry.WizardryGolemsBlocks;
import com.windanesz.wizardrygolems.tile.TileEntityLivingSnow;
import electroblob.wizardry.client.DrawingUtils;
import electroblob.wizardry.constants.Constants;
import electroblob.wizardry.constants.Element;
import electroblob.wizardry.item.IManaStoringItem;
import electroblob.wizardry.item.IWorkbenchItem;
import electroblob.wizardry.item.ItemCrystal;
import electroblob.wizardry.item.ItemWand;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.util.BlockUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemLivingSnowBottle extends ItemGolemancyArtefact implements IManaStoringItem, IWorkbenchItem {

	public static final int COST = 500;

	public ItemLivingSnowBottle(EnumRarity rarity, Type type, Element element) {
		super(rarity, type, element);
		setMaxDamage(1000);

	}

	@Override
	public int getSpellSlotCount(ItemStack stack) { return 0; }

	@Override
	public boolean onApplyButtonPressed(EntityPlayer player, Slot centre, Slot crystals, Slot upgrade, Slot[] spellBooks) {
		boolean changed = false; // Used for advancements
		// Charges by appropriate amount
		if (crystals.getStack() != ItemStack.EMPTY && !this.isManaFull(centre.getStack())) {

			int chargeDepleted = this.getManaCapacity(centre.getStack()) - this.getMana(centre.getStack());

			// Not too pretty but allows addons implementing the IManaStoringItem interface to provide their mana amount for custom crystals,
			// previously this was defaulted to the regular crystal's amount, allowing players to exploit it if a crystal was worth less mana than that.
			int manaPerItem = crystals.getStack().getItem() instanceof IManaStoringItem ?
					((IManaStoringItem) crystals.getStack().getItem()).getMana(crystals.getStack()) :
					crystals.getStack().getItem() instanceof ItemCrystal ? Constants.MANA_PER_CRYSTAL : Constants.MANA_PER_SHARD;

			if (crystals.getStack().getItem() == WizardryItems.crystal_shard) {manaPerItem = Constants.MANA_PER_SHARD;}
			if (crystals.getStack().getItem() == WizardryItems.grand_crystal) {manaPerItem = Constants.GRAND_CRYSTAL_MANA;}

			if (crystals.getStack().getCount() * manaPerItem < chargeDepleted) {
				// If there aren't enough crystals to fully charge the wand
				this.rechargeMana(centre.getStack(), crystals.getStack().getCount() * manaPerItem);
				crystals.decrStackSize(crystals.getStack().getCount());

			} else {
				// If there are excess crystals (or just enough)
				this.setMana(centre.getStack(), this.getManaCapacity(centre.getStack()));
				crystals.decrStackSize((int) Math.ceil(((double) chargeDepleted) / manaPerItem));
			}

			changed = true;
		}

		return changed;
	}

	@Override
	public boolean showTooltip(ItemStack stack) {
		return false;
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

		if (!world.isRemote && getMana(player.getHeldItem(hand)) >= COST && BlockUtils.canPlaceBlock(player, world, pos.up())) {
			world.setBlockState(pos.up(), WizardryGolemsBlocks.living_snow.getDefaultState().withProperty(BlockLivingSnow.LAYERS, 2));
			TileEntity tile = world.getTileEntity(pos.up());
			if (tile instanceof TileEntityLivingSnow) {
				((TileEntityLivingSnow) tile).setCaster(player);
			}

			if (!player.isCreative()) {
				player.getCooldownTracker().setCooldown(this, 600);
				consumeMana(player.getHeldItem(hand), COST, player);
			}
		}

		return super.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
	}

	/**
	 * Does nothing, use {@link ItemWand#setMana(ItemStack, int)} to modify wand mana.
	 */
	@Override
	public void setDamage(ItemStack stack, int damage) {
		// Overridden to do nothing to stop repair things from 'repairing' the mana in a wand
	}

	@Override
	public void setMana(ItemStack stack, int mana) {
		// Using super (which can only be done from in here) bypasses the above override
		super.setDamage(stack, getManaCapacity(stack) - mana);
	}

	@Override
	public int getMana(ItemStack stack) {
		return getManaCapacity(stack) - getDamage(stack);
	}

	@Override
	public int getManaCapacity(ItemStack stack) {
		return this.getMaxDamage(stack);
	}

	@Override
	public int getRGBDurabilityForDisplay(ItemStack stack) {
		return DrawingUtils.mix(0xff8bfe, 0x8e2ee4, (float) getDurabilityForDisplay(stack));
	}

}
