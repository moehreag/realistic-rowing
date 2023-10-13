package io.github.moehreag.realistic_rowing.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.vehicle.BoatEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {



	@Shadow public abstract void setYaw(float yaw);

	@Shadow public abstract float getYaw();

	@Shadow public abstract void setBodyYaw(float bodyYaw);

	@Shadow public abstract float getBodyYaw();

	@Shadow public abstract void setHeadYaw(float headYaw);

	/*@Inject(method = "startRiding(Lnet/minecraft/entity/Entity;Z)Z", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/Entity;vehicle:Lnet/minecraft/entity/Entity;"))
	private void realisticRowing$turnPlayer(Entity entity, boolean force, CallbackInfoReturnable<Boolean> cir){
		if (entity instanceof BoatEntity) {
			setBodyYaw(getYaw() + 180);
			setYaw(getBodyYaw());
			setHeadYaw(getYaw());
		}
	}*/

}
