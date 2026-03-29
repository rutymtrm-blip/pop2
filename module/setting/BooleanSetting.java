package net.raphimc.immediatelyfast.module.setting;

import java.util.function.Supplier;

public final class BooleanSetting extends Setting<BooleanSetting> {
	private boolean value;
	private final boolean originalValue;
	private Runnable onChange;
	private Supplier<Boolean> visibilitySupplier;

	public BooleanSetting(CharSequence name, boolean value) {
		super(name);
		this.value = value;
		this.originalValue = value;
	}

	public BooleanSetting(String name, String description, boolean value) {
		super(name);
		this.setDescription(description);
		this.value = value;
		this.originalValue = value;
	}

	public void toggle() {
		setValue(!value);
	}

	public void setValue(boolean value) {
		this.value = value;
		if (onChange != null) {
			onChange.run();
		}
	}

	public boolean getOriginalValue() {
		return originalValue;
	}

	public boolean getValue() {
		return value;
	}

	public void setOnChange(Runnable onChange) {
		this.onChange = onChange;
	}

	public void setVisibilitySupplier(Supplier<Boolean> supplier) {
		this.visibilitySupplier = supplier;
	}

	public boolean isVisible() {
		return visibilitySupplier == null || visibilitySupplier.get();
	}
}
