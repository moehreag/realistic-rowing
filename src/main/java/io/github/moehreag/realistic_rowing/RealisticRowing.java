package io.github.moehreag.realistic_rowing;

import io.github.moehreag.realistic_rowing.mixin.BoatEntityAccessor;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.model.ModelPart;
import net.minecraft.entity.vehicle.BoatEntity;

public class RealisticRowing implements ClientModInitializer {
	@Override
	public void onInitializeClient() {

	}

	public static void setPaddleOffset(BoatEntity entity, ModelPart part){
		if(entity.getPassengerList().size() == ((BoatEntityAccessor)entity).getMaxPassengerCount()){
			part.pivotX = 7;
		} else {
			part.pivotX = 3;
		}
	}
}
