package com.windanesz.wizardrygolems.entity.living;

import com.golems.entity.EntityCraftingGolem;
import com.golems.main.ExtraGolems;
import com.golems.util.GolemNames;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

/**
 * Based on {@link com.golems.entity.EntityCraftingGolem} - Author: skyjay1
 * Author: WinDanesz
 */
public class EntityCraftingGolemMinion extends EntityGolemBaseMinion {

	public static final String ALLOW_SPECIAL = "Allow Special: Crafting";

	public EntityCraftingGolemMinion(World world) {
		super(world);

		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.29D);
	}

	@Override
	protected ResourceLocation applyTexture() {
		return makeTexture(ExtraGolems.MODID, GolemNames.CRAFTING_GOLEM);
	}

	@Override
	protected boolean processInteract(final EntityPlayer player, final EnumHand hand) {
		final ItemStack itemstack = player.getHeldItem(hand);
		if (!player.world.isRemote && itemstack.isEmpty() && !player.isSneaking()) {
			// display crafting grid for player
			player.displayGui(new EntityCraftingGolem.InterfaceCraftingGrid(player.world, player.bedLocation));
			player.addStat(StatList.CRAFTING_TABLE_INTERACTION);
			player.swingArm(hand);
		}

		return super.processInteract(player, hand);
	}

	@Override
	public void onUpdate(){
		super.onUpdate();
		this.updateDelegate();
	}

	@Override
	public SoundEvent getGolemSound() {
		return SoundEvents.BLOCK_WOOD_STEP;
	}
}
