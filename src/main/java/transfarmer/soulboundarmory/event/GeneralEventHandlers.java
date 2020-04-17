package transfarmer.soulboundarmory.event;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.world.GetCollisionBoxesEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.SideOnly;
import transfarmer.soulboundarmory.Main;
import transfarmer.soulboundarmory.capability.soulbound.ISoulCapability;
import transfarmer.soulboundarmory.capability.soulbound.SoulItemHelper;
import transfarmer.soulboundarmory.capability.soulbound.weapon.ISoulWeapon;
import transfarmer.soulboundarmory.capability.soulbound.weapon.SoulWeaponProvider;
import transfarmer.soulboundarmory.client.gui.TooltipXPBar;
import transfarmer.soulboundarmory.item.ISoulItem;
import transfarmer.soulboundarmory.statistics.SoulType;
import transfarmer.soulboundarmory.util.EntityHelper;

import java.util.Arrays;
import java.util.List;

import static net.minecraft.inventory.EntityEquipmentSlot.MAINHAND;
import static net.minecraftforge.fml.relauncher.Side.CLIENT;

@EventBusSubscriber(modid = Main.MOD_ID)
public class GeneralEventHandlers {
    @SubscribeEvent
    public static void onGetCollsionBoxes(final GetCollisionBoxesEvent event) {
        final Entity entity = event.getEntity();

        if (!event.getWorld().isRemote && entity instanceof EntityPlayer) {
            final EntityPlayer player = (EntityPlayer) entity;
            final ISoulWeapon capability = SoulWeaponProvider.get(player);
            final double leapForce = capability.getLeapForce();

            if (leapForce > 0) {
                final List<Entity> nearbyEntities = player.world.getEntitiesWithinAABBExcludingEntity(player, event.getAabb());

                for (final Entity nearbyEntity : nearbyEntities) {
                    capability.freeze(nearbyEntity, (float) EntityHelper.getVelocity(player) * (float) leapForce, (int) (20 * leapForce));
                }

                if (capability.getLeapDuration() <= 0 && player.onGround && (player.motionY <= 0.01 || player.isCreative())) {
                    capability.setLeapDuration(7);
                }

                if (player.isInLava()) {
                    capability.resetLeapForce();
                }
            }
        }
    }

    @SideOnly(CLIENT)
    @SubscribeEvent
    public static void onItemTooltip(final ItemTooltipEvent event) {
        final EntityPlayer player = event.getEntityPlayer();

        if (player != null) {
            final ItemStack itemStack = event.getItemStack();

            if (itemStack.getItem() instanceof ISoulItem) {
                final ISoulCapability capability = SoulItemHelper.getCapability(player, itemStack.getItem());
                final SoulType type = capability.getType(itemStack);
                final List<String> tooltip = event.getToolTip();
                final int startIndex = tooltip.indexOf(I18n.format("item.modifiers.mainhand")) + 1;

                final String[] prior = tooltip.subList(0, startIndex).toArray(new String[0]);
                final String[] posterior = tooltip.subList(startIndex + itemStack.getAttributeModifiers(MAINHAND).size(), tooltip.size()).toArray(new String[0]);

                tooltip.clear();
                tooltip.addAll(Arrays.asList(prior));
                tooltip.addAll(capability.getTooltip(type));
                tooltip.addAll(Arrays.asList(posterior));
            }
        }
    }

    @SideOnly(CLIENT)
    @SubscribeEvent
    public static void onRenderTooltip(final RenderTooltipEvent.PostText event) {
        if (event.getStack().getItem() instanceof ISoulItem) {
            new TooltipXPBar(event.getX(), event.getY(), event.getStack());
        }
    }
}