package com.windanesz.wizardrygolems.client.renderer.entity.layers;

import com.golems.renders.ModelGolem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LayerChargeOverlay implements LayerRenderer<EntityLivingBase> {

	private static final ResourceLocation LIGHTNING_TEXTURE = new ResourceLocation("textures/entity/creeper/creeper_armor.png");
	private final RenderLiving<EntityLiving> renderer;
	private final ModelGolem model = new ModelGolem();

	public LayerChargeOverlay(RenderLiving renderer) {
		this.renderer = renderer;
	}

	public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		boolean flag = entitylivingbaseIn.isInvisible();
		GlStateManager.depthMask(!flag);
		this.renderer.bindTexture(LIGHTNING_TEXTURE);
		GlStateManager.matrixMode(5890);
		GlStateManager.loadIdentity();
		float f = (float) entitylivingbaseIn.ticksExisted + partialTicks;
		GlStateManager.translate(f * 0.01F, f * 0.01F, 0.0F);
		GlStateManager.matrixMode(5888);
		GlStateManager.enableBlend();
		float f1 = 0.5F;
		GlStateManager.color(0.5F, 0.5F, 0.5F, 1.0F);
		GlStateManager.disableLighting();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
		this.model.setModelAttributes(this.renderer.getMainModel());
		Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
		this.model.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
		GlStateManager.matrixMode(5890);
		GlStateManager.loadIdentity();
		GlStateManager.matrixMode(5888);
		GlStateManager.enableLighting();
		GlStateManager.disableBlend();
		GlStateManager.depthMask(flag);
	}

	public boolean shouldCombineTextures() {
		return false;
	}
}