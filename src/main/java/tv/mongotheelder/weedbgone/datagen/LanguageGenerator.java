package tv.mongotheelder.weedbgone.datagen;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;
import tv.mongotheelder.weedbgone.setup.Registration;

import static tv.mongotheelder.weedbgone.setup.ModSetup.TAB_NAME;
import static tv.mongotheelder.weedbgone.WeedBGone.MODID;

public class LanguageGenerator extends LanguageProvider {
    private final String locale;

    public LanguageGenerator(PackOutput output, String locale) {

        super(output, MODID, locale);
        this.locale = locale;
    }

    @Override
    protected void addTranslations() {
        add("itemGroup." + TAB_NAME, "Weed-B-Gone");
        switch (locale) {
            case "en_us" -> en_us();
            case "es_mx" -> es_mx();
        }
    }

    private void en_us() {
        // Items
        add(Registration.STRING_TRIMMER.get(), "String Trimmer");
        add(Registration.MOTOR.get(), "Motor");
        add(Registration.SPOOL.get(), "Spool");

        // General
        add(MODID+".tooltip.power", "Energy: %s");
    }

    private void es_mx() {
        // Items
        add(Registration.STRING_TRIMMER.get(), "Recortadora de hilo");
        add(Registration.MOTOR.get(), "Motora");
        add(Registration.SPOOL.get(), "Carrete");

        // General
        add(MODID+".tooltip.power", "Energía: %s");
    }
}
