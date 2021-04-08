package ru.betterend.entity.render;

import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.resources.ResourceLocation;
import ru.betterend.BetterEnd;
import ru.betterend.entity.ShadowWalkerEntity;

public class RendererEntityShadowWalker
		extends BipedEntityRenderer<ShadowWalkerEntity, PlayerEntityModel<ShadowWalkerEntity>> {
	private static final ResourceLocation TEXTURE = BetterEnd.makeID("textures/entity/shadow_walker.png");

	public RendererEntityShadowWalker(EntityRenderDispatcher entityRenderDispatcher) {
		super(entityRenderDispatcher, new PlayerEntityModel<ShadowWalkerEntity>(0.0F, false), 0.5F);
	}

	@Override
	public ResourceLocation getTexture(ShadowWalkerEntity zombieEntity) {
		return TEXTURE;
	}
}
