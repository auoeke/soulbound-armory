package transfarmer.soulboundarmory.network.server.weapon;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import transfarmer.soulboundarmory.capability.weapon.ISoulWeapon;
import transfarmer.soulboundarmory.capability.weapon.SoulWeaponProvider;
import transfarmer.soulboundarmory.statistics.weapon.SoulWeaponType;
import transfarmer.soulboundarmory.network.client.weapon.CWeaponBindSlot;

public class SWeaponBindSlot implements IMessage {
    private int slot;

    public SWeaponBindSlot() {
    }

    public SWeaponBindSlot(final int slot) {
        this.slot = slot;
    }

    @Override
    public void fromBytes(final ByteBuf buffer) {
        this.slot = buffer.readInt();
    }

    @Override
    public void toBytes(final ByteBuf buffer) {
        buffer.writeInt(this.slot);
    }

    public static final class Handler implements IMessageHandler<SWeaponBindSlot, IMessage> {
        @Override
        public IMessage onMessage(final SWeaponBindSlot message, final MessageContext context) {
            final EntityPlayer player = context.getServerHandler().player;
            final ISoulWeapon capability = SoulWeaponProvider.get(player);
            final NonNullList<ItemStack> inventory = player.inventory.mainInventory;

            if (capability.getBoundSlot() == message.slot) {
                capability.unbindSlot();
            } else {
                if (inventory.get(message.slot).isEmpty()) {
                    for (final ItemStack itemStack : inventory) {
                        if (SoulWeaponType.getType(itemStack) == capability.getCurrentType()) {
                            inventory.set(capability.getBoundSlot(), ItemStack.EMPTY);
                            player.inventory.setInventorySlotContents(message.slot, itemStack);
                        }
                    }
                }

                capability.bindSlot(message.slot);
            }

            return new CWeaponBindSlot(message.slot);
        }
    }
}
