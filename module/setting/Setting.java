package net.raphimc.immediatelyfast.module.setting;

import com.google.gson.JsonObject;

public abstract class Setting<T extends Setting<T>> {
	protected String name;
	protected String description;
	protected boolean visible = true;
	protected Runnable onChange;

	public Setting(CharSequence name) {
		this.name = name.toString();
	}

	public Setting(String name, String description) {
		this.name = name;
		this.description = description;
	}

	public void setName(CharSequence name) {
		this.name = name.toString();
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public T setDescription(CharSequence desc) {
		this.description = desc.toString();
		//noinspection unchecked
		return (T) this;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	protected void notifyChange() {
		if (onChange != null) {
			onChange.run();
		}
	}

	public void setOnChange(Runnable onChange) {
		this.onChange = onChange;
	}

	// Methods for JSON serialization (optional, can be overridden)
	public JsonObject toJson() {
		JsonObject json = new JsonObject();
		json.addProperty("name", name);
		json.addProperty("description", description);
		json.addProperty("visible", visible);
		return json;
	}

	public void fromJson(JsonObject json) {
		if (json.has("visible")) {
			visible = json.get("visible").getAsBoolean();
		}
	}

	public void reset() {
		// Override in subclasses
	}

	public String getType() {
		return "setting";
	}
}
