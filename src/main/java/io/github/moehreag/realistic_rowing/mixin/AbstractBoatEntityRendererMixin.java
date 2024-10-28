package io.github.moehreag.realistic_rowing.mixin;

import io.github.moehreag.realistic_rowing.BoatEntityRenderStateDuck;
import net.minecraft.client.render.entity.AbstractBoatEntityRenderer;
import net.minecraft.client.render.entity.state.BoatEntityRenderState;
import net.minecraft.entity.vehicle.AbstractBoatEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractBoatEntityRenderer.class)
public abstract class AbstractBoatEntityRendererMixin {

	@Inject(method = "updateRenderState(Lnet/minecraft/entity/vehicle/AbstractBoatEntity;Lnet/minecraft/client/render/entity/state/BoatEntityRenderState;F)V", at = @At("TAIL"))
	private void updateStatePassengerCount(AbstractBoatEntity abstractBoatEntity, BoatEntityRenderState boatEntityRenderState, float f, CallbackInfo ci){
		((BoatEntityRenderStateDuck) boatEntityRenderState).setEntity(abstractBoatEntity);
	}

}
