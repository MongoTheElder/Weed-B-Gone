package tv.mongotheelder.weedbgone.setup;

import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import tv.mongotheelder.weedbgone.items.CraftingComponent;
import tv.mongotheelder.weedbgone.items.StringTrimmer;

import static tv.mongotheelder.weedbgone.WeedBGone.MODID;

public class Registration {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, MODID);
    private static final DeferredRegister<SoundEvent> SOUND_REGISTRY = DeferredRegister.create(Registries.SOUND_EVENT, MODID);

    public static void init(IEventBus eventBus) {

        ITEMS.register(eventBus);
        SOUND_REGISTRY.register(eventBus);
    }

    public static final DeferredHolder<Item, StringTrimmer> STRING_TRIMMER = ITEMS.register("string_trimmer", StringTrimmer::new);
    public static final DeferredHolder<Item, CraftingComponent> MOTOR = ITEMS.register("motor", CraftingComponent::new);
    public static final DeferredHolder<Item, CraftingComponent> SPOOL = ITEMS.register("spool", CraftingComponent::new);

}
