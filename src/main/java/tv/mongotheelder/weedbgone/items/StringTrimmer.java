package tv.mongotheelder.weedbgone.items;

import com.mojang.logging.LogUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FarmBlock;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.capabilities.Capabilities;
import org.slf4j.Logger;
import tv.mongotheelder.weedbgone.setup.Config;

import javax.annotation.Nullable;
import java.util.List;

import static tv.mongotheelder.weedbgone.WeedBGone.MODID;


public class StringTrimmer extends Item {
    private static final int TICKS_PER_SECOND = 20;
    private static final String TOOL_TIP_KEY = MODID+".tooltip.power";

    public StringTrimmer() {

        super(new Properties()
                .stacksTo(1)
                .fireResistant()
                .setNoRepair()
        );
    }

    public int getEnergyMax() {
        return Config.MAXPOWER.get();
    }

    public int getEnergyCost() {
        return Config.COST.get();
    }

    private static final Logger LOGGER = LogUtils.getLogger();

    @Override
    public int getMaxDamage(ItemStack stack) {
        return getEnergyMax();
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        var energy = stack.getCapability(Capabilities.EnergyStorage.ITEM);
        if (energy == null) {
            return false;
        }

        return (energy.getEnergyStored() < energy.getMaxEnergyStored());
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        var energy = stack.getCapability(Capabilities.EnergyStorage.ITEM);
        if (energy == null) {
            return 0;
        }

        return Math.min(13 * energy.getEnergyStored() / energy.getMaxEnergyStored(), 13);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        var energy = stack.getCapability(Capabilities.EnergyStorage.ITEM);
        if (energy == null) {
            return super.getBarColor(stack);
        }

        return Mth.hsvToRgb(Math.max(1.0F, (float) energy.getEnergyStored() / (float) energy.getMaxEnergyStored()) / 3.0F, 1.0F, 1.0F);
    }

    public boolean isEnchantable(ItemStack pStack) {
        return false;
    }

    private boolean isHarvestable(Level level, BlockPos pos) {
        boolean isReplaceable = level.getBlockState(pos).canBeReplaced();
        boolean isSolid = level.getBlockState(pos).isSolid();
        boolean isEmpty = level.isEmptyBlock(pos);
        boolean isOnFarmland = (level.getBlockState(pos.below()).getBlock() instanceof FarmBlock) && Config.ENABLE_SAFE_FARMLAND.get();
        return !isEmpty && !isOnFarmland && (!isSolid || isReplaceable);
    }

    @Override
    public boolean canContinueUsing(ItemStack oldStack, ItemStack newStack) {
        return true;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.NONE;
    }

    public int getLightLevel(Level level, Entity player) {
        return level.getRawBrightness(player.getOnPos().above(), 0);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity player, int slotId, boolean selected) {
        // Solar recharging (if enabled) will trickle charge the string trimmer when ambient light level is above threshold
        if (level.isClientSide() && Config.ENABLE_SOLAR_RECHARGING.get() && getLightLevel(level, player) >= Config.MINIMUM_SOLAR_LIGHT_LEVEL.get()) {
            var energy = stack.getCapability(Capabilities.EnergyStorage.ITEM);
            if ((level.getGameTime() % TICKS_PER_SECOND) == 0) {
                energy.receiveEnergy(Config.SOLAR_RECHARGE_RATE.get(), false);
            }
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack trimmer = player.getItemInHand(hand);

        player.startUsingItem(hand);
        return InteractionResultHolder.pass(trimmer);
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack trimmer, int count) {
        if (livingEntity instanceof Player) {
            Player player = (Player) livingEntity;

            BlockPos playerPos = player.getOnPos().above();
            if (playerPos.getY() >= level.getMaxBuildHeight() || playerPos.getY() < level.getMinBuildHeight()) {
                return;
            }
            // Calculate circular area to cut in front of player at feet level
            double yRot = Math.toRadians(player.getYRot() + 90);
            int radius = Config.RADIUS.get();
            double xPos = radius * Math.cos(yRot) + playerPos.getX();
            double zPos = radius * Math.sin(yRot) + playerPos.getZ();
            BlockPos center = new BlockPos((int) Math.round(xPos), playerPos.getY(), (int) Math.round(zPos));

            var energy = trimmer.getCapability(Capabilities.EnergyStorage.ITEM);
            int minX = center.getX() - radius;
            int maxX = center.getX() + radius;
            int minZ = center.getZ() - radius;
            int maxZ = center.getZ() + radius;
            for (int x = minX; x < maxX; x++) {
                for (int z = minZ; z < maxZ; z++) {
                    BlockPos targetPos = new BlockPos(x, center.getY(), z);
                    if (targetPos.closerToCenterThan(center.getCenter(), radius)) {
                        if (isHarvestable(level, targetPos)) {
                            if (energy.getEnergyStored() >= getEnergyCost()) {
                                level.destroyBlock(targetPos, true);
                                energy.extractEnergy(getEnergyCost(), false);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity livingEntity, int timeLeft) {
        if (livingEntity instanceof Player) {
            livingEntity.stopUsingItem();
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        Minecraft mc = Minecraft.getInstance();

        if (level == null || mc.player == null) {
            return;
        }

        var energy = stack.getCapability(Capabilities.EnergyStorage.ITEM);
        if (energy != null && Config.COST.get() > 0) {
            MutableComponent energyToolTip = Component.translatable(TOOL_TIP_KEY, String.format("%,d/%,d", energy.getEnergyStored(), energy.getMaxEnergyStored()));
            tooltip.add(energyToolTip.withStyle(ChatFormatting.GREEN));
        }
    }

}

