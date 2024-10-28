package io.github.moehreag.realistic_rowing.mixin;

import net.minecraft.entity.vehicle.AbstractBoatEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(AbstractBoatEntity.class)
public interface BoatEntityAccessor {

	@Invoker("getMaxPassengers")
	int getMaxPassengerCount();
}
