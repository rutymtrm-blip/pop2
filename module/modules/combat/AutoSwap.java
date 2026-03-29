package net.raphimc.immediatelyfast.module.modules.combat;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;
import net.minecraft.screen.slot.SlotActionType;
import net.raphimc.immediatelyfast.event.events.HudListener;
import net.raphimc.immediatelyfast.module.Category;
import net.raphimc.immediatelyfast.module.Module;
import net.raphimc.immediatelyfast.module.setting.BooleanSetting;
import net.raphimc.immediatelyfast.module.setting.ModeSetting;
import net.raphimc.immediatelyfast.utils.EncryptedString;
import net.raphimc.immediatelyfast.utils.TimerUtils;
import org.lwjgl.glfw.GLFW;

public class AutoSwap extends Module implements HudListener {
    
    public enum SwapMode {
        Normal, Bypass
    }
    
    public enum ItemType {
        Pearl, GoldenApple, Shield, Totem
    }
    
    private final ModeSetting<SwapMode> mode = new ModeSetting<>(EncryptedString.of("Mode"), SwapMode.Normal, SwapMode.class);
    private final ModeSetting<ItemType> firstItem = new ModeSetting<>(EncryptedString.of("First Item"), ItemType.Pearl, ItemType.class);
    private final ModeSetting<ItemType> secondItem = new ModeSetting<>(EncryptedString.of("Second Item"), ItemType.Pearl, ItemType.class);
    private final BooleanSetting showNotification = new BooleanSetting(EncryptedString.of("Show Notification"), true);
    private final BooleanSetting onlyEnchanted = new BooleanSetting(EncryptedString.of("Only Enchanted Totems"), false);
    
    private boolean swapPressed;
    private boolean swapToOffhand;
    private final TimerUtils swapTimer = new TimerUtils();
    private boolean bypassActive;
    private boolean bypassSwapped;
    private int bypassSlot = -1;
    private String bypassItemName = "";
    private boolean movementLocked = false;
    
    public AutoSwap() {
        super(EncryptedString.of("AutoSwap"),
                EncryptedString.of("Swaps items in hands on button press"),
                GLFW.GLFW_KEY_UNKNOWN,
                Category.COMBAT);
        
        addSettings(mode, firstItem, secondItem, showNotification, onlyEnchanted);
    }
    
    @Override
    public void onEnable() {
        eventManager.add(HudListener.class, this);
        swapPressed = false;
        swapToOffhand = false;
        bypassActive = false;
        bypassSwapped = false;
        bypassSlot = -1;
        movementLocked = false;
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        eventManager.remove(HudListener.class, this);
        if (movementLocked) {
            movementLocked = false;
        }
        super.onDisable();
    }
    
    @Override
    public void onRenderHud(HudEvent event) {
        if (mc.player == null) return;
        
        // Check if swap key is pressed
        if (GLFW.glfwGetMouseButton(mc.getWindow().getHandle(), getKey()) == GLFW.GLFW_PRESS) {
            if (!swapPressed) {
                swapPressed = true;
            }
        } else {
            swapPressed = false;
        }
        
        // Handle bypass mode timing
        if (bypassActive) {
            if (swapTimer.delay(50) && !bypassSwapped && bypassSlot != -1) {
                performSwap(bypassSlot);
                if (showNotification.getValue()) {
                    // TODO: Show notification: "AutoSwap - swapped to " + bypassItemName
                }
                bypassSwapped = true;
            }
            
            if (swapTimer.delay(100)) {
                if (movementLocked) {
                    movementLocked = false;
                }
                bypassActive = false;
                bypassSwapped = false;
                bypassSlot = -1;
            }
            return;
        }
        
        // Handle swap logic
        if (swapPressed && swapToOffhand) {
            ItemType itemType = firstItem.getMode();
            Item item = getItemFromType(itemType);
            String itemName = getItemName(itemType);
            if (item != null) {
                swap(item, itemName, itemType == ItemType.Totem && onlyEnchanted.getValue());
            }
            swapToOffhand = false;
        }
        
        if (swapPressed && !swapToOffhand) {
            ItemType itemType = secondItem.getMode();
            Item item = getItemFromType(itemType);
            String itemName = getItemName(itemType);
            if (item != null) {
                swap(item, itemName, itemType == ItemType.Totem && onlyEnchanted.getValue());
            }
            swapToOffhand = true;
        }
    }
    
    private void swap(Item item, String itemName, boolean onlyEnchanted) {
        if (mc.player == null) return;
        
        int slot = findItem(item, onlyEnchanted);
        if (slot == -1) {
            swapPressed = false;
            return;
        }
        
        if (mode.isMode(SwapMode.Normal)) {
            performSwap(slot);
            if (showNotification.getValue()) {
                // TODO: Show notification: "AutoSwap - swapped to " + itemName
            }
            swapPressed = false;
        } else if (mode.isMode(SwapMode.Bypass)) {
            movementLocked = true;
            bypassActive = true;
            bypassSwapped = false;
            bypassSlot = slot;
            bypassItemName = itemName;
            swapTimer.reset();
            swapPressed = false;
        }
    }
    
    private void performSwap(int slot) {
        if (mc.player == null || mc.interactionManager == null) return;
        
        int actualSlot = slot < 9 ? slot + 36 : slot;
        mc.interactionManager.clickSlot(0, actualSlot, 40, SlotActionType.SWAP, mc.player);
        
        // Send close window packet
        if (mc.player.networkHandler != null) {
            mc.player.networkHandler.sendPacket(new CloseHandledScreenC2SPacket(0));
        }
    }
    
    private int findItem(Item item, boolean onlyEnchanted) {
        if (mc.player == null) return -1;
        
        PlayerInventory inventory = mc.player.getInventory();
        
        for (int i = 0; i < 36; i++) {
            ItemStack stack = inventory.getStack(i);
            if (stack.getItem() == item) {
                if (onlyEnchanted && item == Items.TOTEM_OF_UNDYING) {
                    if (stack.hasEnchantments()) {
                        return i;
                    }
                } else {
                    return i;
                }
            }
        }
        
        return -1;
    }
    
    private Item getItemFromType(ItemType type) {
        return switch (type) {
            case Pearl -> Items.ENDER_PEARL;
            case GoldenApple -> Items.GOLDEN_APPLE;
            case Shield -> Items.SHIELD;
            case Totem -> Items.TOTEM_OF_UNDYING;
        };
    }
    
    private String getItemName(ItemType type) {
        return switch (type) {
            case Pearl -> "Pearl";
            case GoldenApple -> "Golden Apple";
            case Shield -> "Shield";
            case Totem -> "Totem";
        };
    }
    
    public boolean isMoving() {
        return mc.player != null && (mc.player.forwardSpeed != 0 || mc.player.sidewaysSpeed != 0);
    }
}
