package io.github.moehreag.realistic_rowing.mixin;

import io.github.moehreag.realistic_rowing.BoatEntityRenderStateDuck;
import net.minecraft.client.renderer.entity.state.BoatRenderState;
import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BoatRenderState.class)
public abstract class BoatEntityRenderStateMixin implements BoatEntityRenderStateDuck {
	@Override
	public AbstractBoat realistic_rowing$getEntity() {
		return entity;
	}

	@Override
	public void realistic_rowing$setEntity(AbstractBoat entity) {
		this.entity = entity;
	}

	@Unique
	private AbstractBoat entity;
}
