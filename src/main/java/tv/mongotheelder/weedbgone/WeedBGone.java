package tv.mongotheelder.weedbgone;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import org.slf4j.Logger;
import tv.mongotheelder.weedbgone.capabilities.PoweredItem;
import tv.mongotheelder.weedbgone.items.StringTrimmer;
import tv.mongotheelder.weedbgone.setup.Config;
import tv.mongotheelder.weedbgone.setup.ModSetup;
import tv.mongotheelder.weedbgone.setup.Registration;


@Mod(WeedBGone.MODID)
public class WeedBGone {
    public static final String MODID = "weedbgone";
    private static final Logger LOGGER = LogUtils.getLogger();

    public WeedBGone(IEventBus modEventBus) {
        Registration.init(modEventBus);
        Config.register();

        modEventBus.addListener(ModSetup::init);
        ModSetup.TABS.register(modEventBus);
        modEventBus.addListener(this::registerCapabilities);

    }

    private void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerItem(Capabilities.EnergyStorage.ITEM, (itemStack, context) -> new PoweredItem(itemStack, ((StringTrimmer) itemStack.getItem()).getEnergyMax()),
                Registration.STRING_TRIMMER.get()
        );

    }
}
