package io.github.moehreag.realistic_rowing.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.moehreag.realistic_rowing.BoatEntityRenderStateDuck;
import io.github.moehreag.realistic_rowing.RealisticRowing;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.object.boat.AbstractBoatModel;
import net.minecraft.client.renderer.entity.state.BoatRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractBoatModel.class)
public abstract class BoatEntityModelMixin {


	@WrapOperation(method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/BoatRenderState;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/object/boat/AbstractBoatModel;animatePaddle(FILnet/minecraft/client/model/geom/ModelPart;)V"))
	private void changePaddle(float angle, int paddle, ModelPart modelPart, Operation<Void> original, BoatRenderState state) {
		original.call(angle, paddle, modelPart);
		RealisticRowing.setPaddleOffset(((BoatEntityRenderStateDuck) state).realistic_rowing$getEntity(), modelPart);
	}
}
