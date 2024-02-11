package tv.mongotheelder.weedbgone.capabilities;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.energy.EnergyStorage;

public class PoweredItem extends EnergyStorage {
    private final ItemStack stack;

    public PoweredItem(ItemStack stack, int capacity) {
        super(capacity, Integer.MAX_VALUE, Integer.MAX_VALUE);

        this.stack = stack;
        this.energy = stack.hasTag() && stack.getTag().contains("energy") ? stack.getTag().getInt("energy") : 0;
    }

    @Override
    public int extractEnergy(int extract, boolean simulate) {
        int amount = super.extractEnergy(extract, simulate);
        if (!simulate)
            stack.getOrCreateTag().putInt("energy", this.energy);

        return amount;
    }

    @Override
    public int receiveEnergy(int receieve, boolean simulate) {
        int amount = super.receiveEnergy(receieve, simulate);
        if (!simulate)
            stack.getOrCreateTag().putInt("energy", this.energy);

        return amount;
    }
}
