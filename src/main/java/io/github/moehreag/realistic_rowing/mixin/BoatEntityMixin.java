package io.github.moehreag.realistic_rowing.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.moehreag.realistic_rowing.RealisticRowing;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.entity.vehicle.ChestBoatEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BoatEntity.class)
public abstract class BoatEntityMixin extends Entity {

	@Shadow
	private boolean pressingBack;

	@Shadow
	private boolean pressingForward;

	@Shadow private boolean pressingLeft;
	@Shadow private boolean pressingRight;
	@Unique
	private boolean startedRiding;

	public BoatEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@WrapOperation(method = "getPassengerAttachmentPos", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/vehicle/BoatEntity;getPassengerHorizontalOffset()F"))
	private float movePassenger(BoatEntity instance, Operation<Float> original) {
		if (RealisticRowing.shouldActivate(instance)) {
			return original.call(instance) + 0.6F + (!(instance instanceof ChestBoatEntity) ? 0.15F : 0);
		}
		return original.call(instance);
	}

	@Inject(method = "clampPassengerYaw", at = @At("HEAD"), cancellable = true)
	private void turnPlayer(Entity passenger, CallbackInfo ci) {
		if (RealisticRowing.shouldActivate(this)) {
			if (startedRiding){
				if (!pressingBack && !pressingForward && !pressingLeft && !pressingRight){
					ci.cancel();
					return;
				}
			}

			if (MathHelper.angleBetween(passenger.getYaw(), this.getYaw()) < 25) {
				passenger.setYaw(getYaw() + 180);
				startedRiding = true;
			}
		}
	}

	@ModifyArg(method = "clampPassengerYaw", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;setBodyYaw(F)V", ordinal = 0))
	private float clampPassengerYaw$setBodyYaw(float bodyYaw) {
		if (RealisticRowing.shouldActivate(this)) {
			return bodyYaw + 180;
		}
		return bodyYaw;
	}

	@ModifyArg(method = "clampPassengerYaw", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;wrapDegrees(F)F"))
	private float clampPassengerYaw$wrapDegrees(float degrees) {
		if (RealisticRowing.shouldActivate(this)) {
			return degrees - 180;
		}
		return degrees;
	}

	@WrapOperation(method = "interact", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;startRiding(Lnet/minecraft/entity/Entity;)Z"))
	private boolean turnPlayerOnInteract(PlayerEntity passenger, Entity entity, Operation<Boolean> original) {
		boolean riding = original.call(passenger, entity);

		if (riding && RealisticRowing.shouldActivate(this)) {
			startedRiding = false;
		}

		return riding;
	}

	@WrapOperation(method = "setInputs", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/vehicle/BoatEntity;pressingForward:Z"))
	private void invertControls$forward(BoatEntity instance, boolean value, Operation<Void> original) {
		if (RealisticRowing.shouldActivate(this)) {
			if (!MinecraftClient.getInstance().options.getPerspective().isFirstPerson()) {
				pressingBack = value;
			} else {
				original.call(instance, value);
			}
		}
	}

	@WrapOperation(method = "setInputs", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/vehicle/BoatEntity;pressingBack:Z"))
	private void invertControls$backward(BoatEntity instance, boolean value, Operation<Void> original) {
		if (RealisticRowing.shouldActivate(this)) {
			if (!MinecraftClient.getInstance().options.getPerspective().isFirstPerson()) {
				pressingForward = value;
			} else {
				original.call(instance, value);
			}
		}
	}
}
