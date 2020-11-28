package com.windanesz.wizardrygolems.handler;

//import com.windanesz.wizardrygolems.entity.construct.EntityImbuementTriggerer;
//import com.windanesz.wizardrygolems.item.ItemGolemancyArtefact;
//import electroblob.wizardry.registry.WizardryBlocks;
//import electroblob.wizardry.tileentity.TileEntityImbuementAltar;
//import electroblob.wizardry.util.EntityUtils;
//import net.minecraft.util.math.BlockPos;
//import net.minecraftforge.event.entity.player.PlayerInteractEvent;
//import net.minecraftforge.fml.common.Mod;
//import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
//
//import java.util.List;

//@Mod.EventBusSubscriber
public class ImbuementTest {

//	@SubscribeEvent
//	public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
//		BlockPos pos = event.getPos();
//
//		if (event.getWorld().getBlockState(pos).getBlock() == WizardryBlocks.imbuement_altar) {
//			System.out.println("its an altar.");
//
//			if (!event.getWorld().isRemote && event.getWorld().getTileEntity(pos) instanceof TileEntityImbuementAltar) {
//				TileEntityImbuementAltar altar = (TileEntityImbuementAltar) event.getWorld().getTileEntity(pos);
//				if (altar.getStack().isEmpty()) {
//					System.out.println("empty altar");
//					if (event.getItemStack().getItem() instanceof ItemGolemancyArtefact) {
//						//					spawn entity for fake particles
//						System.out.println("its a golemancy artefact");
//						List<EntityImbuementTriggerer> list = EntityUtils.getEntitiesWithinRadius(3, pos.getX(), pos.getY(), pos.getZ(), event.getWorld(), EntityImbuementTriggerer.class);
//						if (!list.isEmpty()) {
//							for (EntityImbuementTriggerer entity : list) {
//								entity.setDead();
//							}
//						}
//						EntityImbuementTriggerer entityImbuementTriggerer = new EntityImbuementTriggerer(event.getWorld(), pos.up(), altar);
//
//						entityImbuementTriggerer.lifetime = 600;
//						event.getWorld().spawnEntity(entityImbuementTriggerer);
//
//						System.out.println("spawned entity");
//
//					}
//				}
//			}
//		}
//	}



}