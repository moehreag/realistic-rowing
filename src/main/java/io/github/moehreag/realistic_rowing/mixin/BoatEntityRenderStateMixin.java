package io.github.moehreag.realistic_rowing.mixin;

import io.github.moehreag.realistic_rowing.BoatEntityRenderStateDuck;
import net.minecraft.client.render.entity.state.BoatEntityRenderState;
import net.minecraft.entity.vehicle.AbstractBoatEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BoatEntityRenderState.class)
public abstract class BoatEntityRenderStateMixin implements BoatEntityRenderStateDuck {
	@Override
	public AbstractBoatEntity realistic_rowing$getEntity() {
		return entity;
	}

	@Override
	public void realistic_rowing$setEntity(AbstractBoatEntity entity) {
		this.entity = entity;
	}

	@Unique
	private AbstractBoatEntity entity;
}
