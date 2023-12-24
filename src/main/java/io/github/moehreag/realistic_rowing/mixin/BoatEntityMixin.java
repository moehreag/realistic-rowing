package io.github.moehreag.realistic_rowing.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.moehreag.realistic_rowing.RealisticRowing;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.entity.vehicle.ChestBoatEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BoatEntity.class)
public abstract class BoatEntityMixin extends Entity {

	public BoatEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@WrapOperation(method = "getPassengerAttachmentPos", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/vehicle/BoatEntity;getPassengerHorizontalOffset()F"))
	private float realisticrowing$movePassenger(BoatEntity instance, Operation<Float> original){
		if (RealisticRowing.shouldActivate(instance)) {
			return original.call(instance) + 0.6F + (!(instance instanceof ChestBoatEntity) ? 0.15F : 0);
		}
		return original.call(instance);
	}

	@Inject(method = "clampPassengerYaw", at = @At("HEAD"))
	private void realisiticRowing$turnPlayer(Entity passenger, CallbackInfo ci){
		if (RealisticRowing.shouldActivate(this)) {
			if (MathHelper.angleBetween(passenger.getYaw(), this.getYaw()) < 5) {
				passenger.setYaw(getYaw() + 180);
			}
		}
	}

	@ModifyArg(method = "clampPassengerYaw", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;setBodyYaw(F)V", ordinal = 0))
	private float realisticRowing$clampPassengerYaw$setBodyYaw(float bodyYaw){
		if (RealisticRowing.shouldActivate(this)) {
			return bodyYaw + 180;
		}
		return bodyYaw;
	}

	@ModifyArg(method = "clampPassengerYaw", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;wrapDegrees(F)F"))
	private float realisticRowing$clampPassengerYaw$wrapDegrees(float degrees){
		if (RealisticRowing.shouldActivate(this)) {
			return degrees - 180;
		}
		return degrees;
	}

}
