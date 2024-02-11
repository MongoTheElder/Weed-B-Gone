package tv.mongotheelder.weedbgone.items;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class CraftingComponent extends Item {
    public CraftingComponent() {
        super(new Properties().stacksTo(1).fireResistant());
    }

    public boolean isEnchantable(ItemStack pStack) {
        return false;
    }

}
