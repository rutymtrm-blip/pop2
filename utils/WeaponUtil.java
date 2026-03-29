package net.raphimc.immediatelyfast.utils;

import net.minecraft.item.*;

public class WeaponUtil {
    
    public static boolean isWeapon(ItemStack itemstack) {
        if (itemstack.isEmpty()) {
            return false;
        }
        Item item = itemstack.getItem();
        return item instanceof SwordItem || 
               item instanceof AxeItem || 
               item instanceof TridentItem || 
               item instanceof BowItem || 
               item instanceof CrossbowItem;
    }
}
