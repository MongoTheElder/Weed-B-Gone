package tv.mongotheelder.weedbgone.setup;

import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;

public class Config
{
    public static final ModConfigSpec.Builder CLIENT_BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec.Builder COMMON_BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec.Builder SERVER_BUILDER = new ModConfigSpec.Builder();

    public static final String CATEGORY_GENERAL = "general";
    public static final String CATEGORY_POWER = "power";

    public static ModConfigSpec.IntValue MAXPOWER;
    public static ModConfigSpec.IntValue COST;
    public static ModConfigSpec.IntValue RADIUS;
    public static ModConfigSpec.BooleanValue ENABLE_SOLAR_RECHARGING;
    public static ModConfigSpec.IntValue SOLAR_RECHARGE_RATE;
    public static ModConfigSpec.IntValue MINIMUM_SOLAR_LIGHT_LEVEL;


    public static void register() {
        registerCommonConfigs();
    }

    private static void registerClientConfigs() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, CLIENT_BUILDER.build());
    }

    private static void registerCommonConfigs() {
        COMMON_BUILDER.comment("Welcome to Weed-B-Gone. The default values should be appropriate for general use.\nModpack builders: There are a couple settings you should evaluate to see if the default values work for your implementation.\nIf you provide an early game recharging solution, you might want to decrease the the recharging solar charging rate or outright disable solar charging\nIf you do not include a third-party charging solution, consider increasing the solar charging rate");
        COMMON_BUILDER.comment("General settings").push(CATEGORY_GENERAL);
        generalConfig();
        COMMON_BUILDER.pop();

        COMMON_BUILDER.comment("Power settings").push(CATEGORY_POWER);
        powerConfig();
        COMMON_BUILDER.pop();

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, COMMON_BUILDER.build());
    }

    private static void registerServerConfigs() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, SERVER_BUILDER.build());
    }

    private static void generalConfig() {
        RADIUS = COMMON_BUILDER.comment("Radius of cut (blocks)")
                .defineInRange("cutRadius", 5, 1, 64);
        ENABLE_SOLAR_RECHARGING = COMMON_BUILDER.comment("Enable solar recharging")
                .define("enableSolar", true);
        MINIMUM_SOLAR_LIGHT_LEVEL = COMMON_BUILDER.comment("Minimum light level for solar charging (0 = charge in total darkness)")
                .defineInRange("minLight", 10, 0, 16);
    }

    private static void powerConfig() {
        MAXPOWER = COMMON_BUILDER.comment("Maximum power storage")
                .defineInRange("maxPower", 10000, 10000, Integer.MAX_VALUE);
        COST = COMMON_BUILDER.comment("Base RF cost per plant broken (0 = no RF required)")
                .defineInRange("baseCost", 1, 0, 1000);
        SOLAR_RECHARGE_RATE = COMMON_BUILDER.comment("Solar charging rate (RF/second)")
                .defineInRange("rechargeRate", 10, 1, 10000);
    }

}
