package io.github.moehreag.realistic_rowing.mixin;

import io.github.moehreag.realistic_rowing.RealisticRowing;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.RaftEntityModel;
import net.minecraft.entity.vehicle.BoatEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RaftEntityModel.class)
public class RaftEntityModelMixin {
	@Inject(method = "setPaddleAngle", at = @At("TAIL"))
	private static void changePaddle(BoatEntity entity, int sigma, ModelPart part, float angle, CallbackInfo ci){
		RealisticRowing.setPaddleOffset(entity, part);
	}
}
