package io.github.moehreag.realistic_rowing;

import java.util.HashMap;
import java.util.Map;

import io.github.moehreag.realistic_rowing.mixin.BoatEntityAccessor;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.model.ModelPart;
import net.minecraft.entity.Entity;
import net.minecraft.entity.vehicle.BoatEntity;

public class RealisticRowing implements ClientModInitializer {
	private static final Map<Class<?>, Boolean> boats = new HashMap<>();
	private static final Map<Entity, Boolean> activationCache = new HashMap<>();
	@Override
	public void onInitializeClient() {
		try {
			if (FabricLoader.getInstance().isModLoaded("smallships")) {
				boats.put(Class.forName("com.talhanation.smallships.world.entity.ship.Ship"), false);
			}
		} catch (ClassNotFoundException ignored) {

		}
	}

	public static boolean shouldActivate(Entity e){
		if (activationCache.containsKey(e)){
			return activationCache.get(e);
		}
		boolean value = true;
		for (Map.Entry<Class<?>, Boolean> entry : boats.entrySet()){
			if (entry.getKey().isAssignableFrom(e.getClass())){
				value = entry.getValue();
			}
		}
		activationCache.put(e, value);
		return value;
	}

	public static void setPaddleOffset(BoatEntity entity, ModelPart part){
		if (shouldActivate(entity)) {
			if (entity.getPassengerList().size() == ((BoatEntityAccessor) entity).getMaxPassengerCount()) {
				part.pivotX = 7;
			} else {
				part.pivotX = 3;
			}
		}
	}
}
