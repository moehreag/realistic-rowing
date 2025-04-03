package io.github.moehreag.realistic_rowing.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.moehreag.realistic_rowing.BoatEntityRenderStateDuck;
import io.github.moehreag.realistic_rowing.RealisticRowing;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.AbstractBoatEntityModel;
import net.minecraft.client.render.entity.state.BoatEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractBoatEntityModel.class)
public abstract class BoatEntityModelMixin {


	@WrapOperation(method = "setAngles(Lnet/minecraft/client/render/entity/state/BoatEntityRenderState;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/model/AbstractBoatEntityModel;setPaddleAngles(FILnet/minecraft/client/model/ModelPart;)V"))
	private void changePaddle(float angle, int paddle, ModelPart modelPart, Operation<Void> original, BoatEntityRenderState state) {
		original.call(angle, paddle, modelPart);
		RealisticRowing.setPaddleOffset(((BoatEntityRenderStateDuck) state).getEntity(), modelPart);
	}
}
