package com.thevoxelbox.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

public class VoxelMapRenderProtectedFieldsHelper {
	public static ResourceLocation getRendersResourceLocation(Render render, Entity entity) {
		return render.getEntityTexture(entity);
	}

	public static ModelBase getRendersModel(RendererLivingEntity render) {
		return render.mainModel;
	}

	public static ModelBase getRendersPassModel(Render render, Entity entity) {
		((RendererLivingEntity) render).shouldRenderPass((EntityLivingBase) entity, 0, 0.0F);
		return ((RendererLivingEntity) render).renderPassModel;
	}

	public static void preRender(EntityLivingBase par1EntityLivingBase, RendererLivingEntity render) {
		render.preRenderCallback(par1EntityLivingBase, 1.0F);
	}
}
