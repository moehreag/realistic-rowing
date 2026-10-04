package io.github.moehreag.realistic_rowing.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.moehreag.realistic_rowing.RealisticRowing;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
import net.minecraft.world.level.Level;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractBoat.class)
public abstract class BoatEntityMixin extends Entity {

	@Shadow
	private boolean inputDown;

	@Shadow
	private boolean inputUp;

	@Shadow
	private boolean inputLeft;
	@Shadow
	private boolean inputRight;
	@Unique
	private boolean startedRiding;

	public BoatEntityMixin(EntityType<?> type, Level world) {
		super(type, world);
	}

	@ModifyArg(method = "getPassengerAttachmentPoint", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/Vec3;<init>(DDD)V"), index = 2)
	private double movePassenger(double x, @Local(argsOnly = true) final Entity passenger) {
		if (passenger instanceof Player && RealisticRowing.shouldActivate(this)) {
			return x + 0.6F;
		}
		return x;
	}

	@Inject(method = "clampRotation", at = @At("HEAD"), cancellable = true)
	private void turnPlayer(Entity passenger, CallbackInfoReturnable<Float> cir) {
		if (passenger instanceof Player && RealisticRowing.shouldActivate(this)) {
			if (startedRiding) {
				if (!inputDown && !inputUp && !inputLeft && !inputRight) {
					cir.cancel();
					return;
				}
			}

			if (Mth.degreesDifferenceAbs(passenger.getYRot(), this.getYRot()) < 25) {
				passenger.setYRot(getYRot() + 180);
				startedRiding = true;
			}
		}
	}

	@ModifyArg(method = "clampRotation", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;setYBodyRot(F)V", ordinal = 0))
	private float clampPassengerYaw$setBodyYaw(float bodyYaw) {
		if (RealisticRowing.shouldActivate(this)) {
			return bodyYaw + 180;
		}
		return bodyYaw;
	}

	@ModifyArg(method = "clampRotation", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Mth;wrapDegrees(F)F"))
	private float clampPassengerYaw$wrapDegrees(float degrees) {
		if (RealisticRowing.shouldActivate(this)) {
			return degrees - 180;
		}
		return degrees;
	}

	@WrapOperation(method = "interact", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;startRiding(Lnet/minecraft/world/entity/Entity;)Z"))
	private boolean turnPlayerOnInteract(Player passenger, Entity entity, Operation<Boolean> original) {
		boolean riding = original.call(passenger, entity);

		if (riding && RealisticRowing.shouldActivate(this)) {
			startedRiding = false;
		}

		return riding;
	}

	@WrapOperation(method = "setInput", at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/vehicle/boat/AbstractBoat;inputUp:Z", opcode = Opcodes.PUTFIELD))
	private void invertControls$forward(AbstractBoat instance, boolean value, Operation<Void> original) {
		if (RealisticRowing.shouldActivate(this)) {
			if (!Minecraft.getInstance().options.getCameraType().isFirstPerson()) {
				inputDown = value;
			} else {
				original.call(instance, value);
			}
		}
	}

	@WrapOperation(method = "setInput", at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/vehicle/boat/AbstractBoat;inputDown:Z", opcode = Opcodes.PUTFIELD))
	private void invertControls$backward(AbstractBoat instance, boolean value, Operation<Void> original) {
		if (RealisticRowing.shouldActivate(this)) {
			if (!Minecraft.getInstance().options.getCameraType().isFirstPerson()) {
				inputUp = value;
			} else {
				original.call(instance, value);
			}
		}
	}
}
