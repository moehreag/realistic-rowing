package io.github.moehreag.realistic_rowing.mixin;

import io.github.moehreag.realistic_rowing.BoatEntityRenderStateDuck;
import net.minecraft.client.renderer.entity.AbstractBoatRenderer;
import net.minecraft.client.renderer.entity.state.BoatRenderState;
import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractBoatRenderer.class)
public abstract class AbstractBoatEntityRendererMixin {

	@Inject(method = "extractRenderState(Lnet/minecraft/world/entity/vehicle/boat/AbstractBoat;Lnet/minecraft/client/renderer/entity/state/BoatRenderState;F)V", at = @At("TAIL"))
	private void updateStatePassengerCount(AbstractBoat abstractBoat, BoatRenderState boatRenderState, float f, CallbackInfo ci) {
		((BoatEntityRenderStateDuck) boatRenderState).realistic_rowing$setEntity(abstractBoat);
	}

}
