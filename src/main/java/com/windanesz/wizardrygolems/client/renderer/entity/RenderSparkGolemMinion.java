package com.windanesz.wizardrygolems.client.renderer.entity;

import com.golems.entity.GolemBase;
import com.golems.renders.RenderGolem;
import com.windanesz.wizardrygolems.client.renderer.entity.layers.LayerChargeOverlay;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import org.lwjgl.opengl.GL11;

public class RenderSparkGolemMinion extends RenderGolem<GolemBase> {

	public RenderSparkGolemMinion(RenderManager renderManagerIn) {
		super(renderManagerIn);
		this.addLayer(new LayerChargeOverlay(this));
	}

	@Override
	protected void preRenderCallback(GolemBase entity, float partialTickTime) {
		super.preRenderCallback(entity, partialTickTime);
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		//GlStateManager.color(2.4f,2.4f,2.4f, 0.7f);
		//	GlStateManager.color(1.0f,1.0f,1.0f, 0.7f);
		// Apply vertex displacement for a jagged effect
		// Apply custom shader effects for glowing and pulsating
		float brightnessFactor = (float) Math.max(0.4, Math.abs(Math.sin(entity.ticksExisted * 0.25f)));
		GlStateManager.color(2.4f, 2.4f, 2.4f, brightnessFactor);

		float displacementFactor = (float) (Math.random() * 0.04); // Adjust displacement range as needed
		GlStateManager.translate(0, displacementFactor, 0);

		// Render the mob with dynamic lighting effects
		// (Assuming dynamic lighting is implemented elsewhere in your mod)
	}


}
