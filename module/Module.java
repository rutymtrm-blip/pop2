package net.raphimc.immediatelyfast.module;

import net.raphimc.immediatelyfast.Argon;
import net.raphimc.immediatelyfast.event.EventManager;
import net.raphimc.immediatelyfast.module.setting.Setting;

import net.minecraft.client.MinecraftClient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Module implements Serializable {
	private final List<Setting<?>> settings = new ArrayList<>();
	public final EventManager eventManager = Argon.INSTANCE.eventManager;
	protected MinecraftClient mc = MinecraftClient.getInstance();
	private CharSequence name;
	private CharSequence description;
	private boolean enabled;
	private int key;
	private Category category;

	public Module(CharSequence name, CharSequence description, int key, Category category) {
		this.name = name;
		this.description = description;
		this.enabled = false;
		this.key = key;
		this.category = category;
	}

	public void toggle() {
		enabled = !enabled;
		if (enabled)
			onEnable();
		else onDisable();
	}

	public CharSequence getName() {
		return name;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public CharSequence getDescription() {
		return description;
	}

	public int getKey() {
		return key;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public void setName(CharSequence name) {
		this.name = name;
	}

	public void setDescription(CharSequence description) {
		this.description = description;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public List<Setting<?>> getSettings() {
		return settings;
	}

	public void onEnable() {}

	public void onDisable() {}

	public void addSetting(Setting<?> setting) {
		this.settings.add(setting);
	}

	public void addSettings(Setting<?>... settings) {
		this.settings.addAll(Arrays.asList(settings));
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		if (enabled)
			onEnable();
		else onDisable();
	}

	public void setEnabledStatus(boolean enabled) {
		this.enabled = enabled;
	}

	// Helper methods for modules
	protected MinecraftClient getClient() {
		return MinecraftClient.getInstance();
	}

	protected net.minecraft.entity.player.PlayerEntity getPlayer() {
		return this.getClient().player;
	}

	protected net.minecraft.world.World getWorld() {
		return this.getClient().world;
	}

	protected net.minecraft.client.world.ClientWorld getClientWorld() {
		return this.getClient().world;
	}

	protected net.minecraft.client.network.ClientPlayerEntity getClientPlayer() {
		return this.getClient().player;
	}

	protected net.minecraft.util.Hand getMainHand() {
		return net.minecraft.util.Hand.MAIN_HAND;
	}

	protected boolean isFriendPlayer(net.minecraft.entity.player.PlayerEntity player) {
		// TODO: Implement friend system
		return false;
	}

	// Event handlers that can be overridden
	public void onAttackEntity(net.minecraft.entity.player.PlayerEntity player, net.minecraft.world.World world, 
								net.minecraft.util.Hand hand, net.minecraft.entity.Entity entity, 
								net.minecraft.util.hit.EntityHitResult hitResult) {}

	public void onUseItem(net.raphimc.immediatelyfast.event.events.UseItemEvent event) {}

	public void onRotation(net.raphimc.immediatelyfast.event.events.RotationEvent event) {}

	public void onRenderBeforeEntities(net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext context) {}

	public void onRenderAfterEntities(net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext context) {}

	public void onPlayerDeath(net.minecraft.entity.player.PlayerEntity player) {}

}
