package com.thevoxelbox.minecraft.src;

import com.thevoxelbox.minecraft.block.VoxelMapBlockProtectedFieldsHelper;
import com.thevoxelbox.minecraft.client.renderer.entity.VoxelMapRenderProtectedFieldsHelper;
import net.minecraft.block.Block;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

public class VoxelMapProtectedFieldsHelper {
	static boolean getRendersResourceLocationDirect = true;
	static boolean getRendersModelDirect = true;
	static boolean getRendersPassModelDirect = true;
	static boolean preRenderDirect = true;
	static boolean setLightOpacityDirect = true;

	public static ResourceLocation getRendersResourceLocation(Render render, Entity entity) {
		if (preRenderDirect) {
			preRenderDirect = false;
			return VoxelMapRenderProtectedFieldsHelper.getRendersResourceLocation(render, entity);
		}
		return VoxelMapRenderProtectedFieldsHelper.getRendersResourceLocation(render, entity);
	}

	public static ModelBase getRendersModel(RendererLivingEntity render) {
		if (getRendersModelDirect) {
			getRendersModelDirect = false;
			return VoxelMapRenderProtectedFieldsHelper.getRendersModel(render);
		}
		return VoxelMapRenderProtectedFieldsHelper.getRendersModel(render);
	}

	public static ModelBase getRendersPassModel(Render render, Entity entity) {
		if (getRendersPassModelDirect) {
			getRendersPassModelDirect = false;
			return VoxelMapRenderProtectedFieldsHelper.getRendersPassModel(render, entity);
		}
		return VoxelMapRenderProtectedFieldsHelper.getRendersPassModel(render, entity);
	}

	public static void preRender(EntityLivingBase par1EntityLivingBase, RendererLivingEntity render) {
		if (preRenderDirect) {
			preRenderDirect = false;
			VoxelMapRenderProtectedFieldsHelper.preRender(par1EntityLivingBase, render);
		} else {
			VoxelMapRenderProtectedFieldsHelper.preRender(par1EntityLivingBase, render);
		}
	}

	public static void setLightOpacity(Block block, int opacity) {
		if (setLightOpacityDirect) {
			try {
				block.setLightOpacity(opacity);
			} catch (IllegalAccessError e) {
				setLightOpacityDirect = false;
				VoxelMapBlockProtectedFieldsHelper.setLightOpacity(block, opacity);
			}
		} else {
			VoxelMapBlockProtectedFieldsHelper.setLightOpacity(block, opacity);
		}
	}
}
