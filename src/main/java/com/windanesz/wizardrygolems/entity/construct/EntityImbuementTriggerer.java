package com.windanesz.wizardrygolems.entity.construct;

import com.windanesz.wizardrygolems.item.ItemGolemancyArtefact;
import com.windanesz.wizardrygolems.registry.WizardryGolemsItems;
import electroblob.wizardry.constants.Element;
import electroblob.wizardry.entity.construct.EntityMagicConstruct;
import electroblob.wizardry.item.IManaStoringItem;
import electroblob.wizardry.item.ItemWizardArmour;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.tileentity.TileEntityImbuementAltar;
import electroblob.wizardry.util.GeometryUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Arrays;

public class EntityImbuementTriggerer extends EntityMagicConstruct {

	BlockPos altarPos;
	TileEntityImbuementAltar altar;

	public EntityImbuementTriggerer(World world) {
		super(world);
	}

	public EntityImbuementTriggerer(World world, BlockPos pos, TileEntityImbuementAltar altar) {
		super(world);
		this.altarPos = pos;
		this.altar = altar;
	}

	@Override
	public void onEntityUpdate() {

	}

	@Override
	public void onUpdate() {

		if (this.ticksExisted > 60) {
			if (altar != null) {
				//				if (world.isRemote) {

				//				Element[] elements = getReceptacleElements();

				Vec3d centre = GeometryUtils.getCentre(altarPos.up());

				//				for (int i = 0; i < 4; i++) {
				//
				//					//					if(elements[i] == null) continue;
				//
				//					Vec3d offset = new Vec3d(EnumFacing.byHorizontalIndex(i).getDirectionVec());
				//					Vec3d vec = GeometryUtils.getCentre(altarPos).add(0, 0.3, 0).add(offset.scale(0.7));
				//
				//					int[] intArray = new int[] {1, 1, 1, 1};
				//					int[] colours = BlockReceptacle.PARTICLE_COLOURS.get(intArray);
				//
				//					ParticleBuilder.create(ParticleBuilder.Type.DUST, world.rand, vec.x, vec.y, vec.z, 0.1, false)
				//							.vel(centre.subtract(vec).scale(0.02)).clr(colours[1]).fade(colours[2]).time(50).spawn(world);
				//					System.out.println("particle");
				//				}
				//				}
			} else {
				this.setDead();
			}

		}
		super.onUpdate();

	}

	public static ItemStack getImbuementResult(ItemStack input, Element[] receptacleElements, boolean fullLootGen, World world, EntityPlayer lastUser) {

		if (input.getItem() instanceof ItemGolemancyArtefact) {

			if (Arrays.stream(receptacleElements).distinct().count() == 1 && receptacleElements[0] != null) { // All the same element

				ItemStack book = new ItemStack(WizardryGolemsItems.golemancy_spell_book);
				Element element = Element.values()[receptacleElements[0].ordinal()];

				book.setItemDamage(Spell.get("earth_golemancy").metadata());

				ItemStack result = new ItemStack(WizardryItems.getArmour(receptacleElements[0], ((ItemWizardArmour) input.getItem()).armorType));

				result.setTagCompound(input.getTagCompound());
				((IManaStoringItem) result.getItem()).setMana(result, ((ItemWizardArmour) input.getItem()).getMana(input));

				return result;
			}
		}

		return ItemStack.EMPTY;
	}

}
