package tv.mongotheelder.weedbgone.setup;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static tv.mongotheelder.weedbgone.WeedBGone.MODID;

public class ModSetup {
    public static void init(final FMLCommonSetupEvent event) {

    }

    public static final String TAB_NAME = MODID;
    public static DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);
    public static DeferredHolder<CreativeModeTab, CreativeModeTab> TAB_WEEDBGONE = TABS.register(TAB_NAME, () -> CreativeModeTab.builder()
            .title(Component.literal("Weed-B-Gone"))
            .icon(() -> new ItemStack(Registration.STRING_TRIMMER.get()))
            .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
            .displayItems((featureFlags, output) -> {
                Registration.ITEMS.getEntries().forEach(e -> {
                    Item item = e.get();
                    output.accept(item);
                });
            })
            .build());
}
