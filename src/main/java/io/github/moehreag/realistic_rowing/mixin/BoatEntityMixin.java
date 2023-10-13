package io.github.moehreag.realistic_rowing.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.BoatEntity;
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

	/**
	 * @author moeahreag
	 * @reason turn around the player and adjust the normal midpoint to x+180°
	 */
	/*@Overwrite
	public void clampPassengerYaw(Entity passenger) {

		if (MathHelper.angleBetween(passenger.getYaw(), this.getYaw()) < 5) {
			passenger.setYaw(getYaw() + 180);
		}

		passenger.setBodyYaw(this.getYaw()+180);
		float f = MathHelper.wrapDegrees((passenger.getYaw()-180) - this.getYaw());
		float g = MathHelper.clamp(f, -105.0F, 105.0F);
		passenger.prevYaw += g - f;
		passenger.setYaw(passenger.getYaw() + g - f);
		passenger.setHeadYaw(passenger.getYaw());
	}*/

	@Inject(method = "clampPassengerYaw", at = @At("HEAD"))
	private void realisiticRowing$turnPlayer(Entity passenger, CallbackInfo ci){
		if (MathHelper.angleBetween(passenger.getYaw(), this.getYaw()) < 5) {
			passenger.setYaw(getYaw() + 180);
		}
	}

	@ModifyArg(method = "clampPassengerYaw", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;setBodyYaw(F)V", ordinal = 0))
	private float realisticRowing$clampPassengerYaw$setBodyYaw(float bodyYaw){

		return bodyYaw + 180;
	}

	@ModifyArg(method = "clampPassengerYaw", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;wrapDegrees(F)F"))
	private float realisticRowing$clampPassengerYaw$wrapDegrees(float degrees){
		return degrees - 180;
	}

}
