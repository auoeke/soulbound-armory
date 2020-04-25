package transfarmer.soulboundarmory.network.server;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import transfarmer.soulboundarmory.capability.soulbound.common.ISoulbound;
import transfarmer.soulboundarmory.network.ExtendedPacketBuffer;
import transfarmer.soulboundarmory.network.IExtendedMessage;
import transfarmer.soulboundarmory.network.IExtendedMessageHandler;
import transfarmer.soulboundarmory.skill.ISkill;
import transfarmer.soulboundarmory.statistics.base.iface.ICapabilityType;
import transfarmer.soulboundarmory.statistics.base.iface.IItem;

public class C2SUpgradeSkill implements IExtendedMessage {
    private String capability;
    private String item;
    private String skill;

    public C2SUpgradeSkill() {}

    public C2SUpgradeSkill(final ICapabilityType capability, final IItem item, final ISkill skill) {
        this.capability = capability.toString();
        this.item = item.toString();
        this.skill = skill.getRegistryName();
    }

    @Override
    public void fromBytes(final ExtendedPacketBuffer buffer) {
        this.capability = buffer.readString();
        this.item = buffer.readString();
        this.skill = buffer.readString();
    }

    @Override
    public void toBytes(final ExtendedPacketBuffer buffer) {
        buffer.writeString(this.capability);
        buffer.writeString(this.item);
        buffer.writeString(this.skill);
    }

    public static final class Handler implements IExtendedMessageHandler<C2SUpgradeSkill> {
        @Override
        public IExtendedMessage onMessage(final C2SUpgradeSkill message, final MessageContext context) {
            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
                final ISoulbound capability = context.getServerHandler().player.getCapability(ICapabilityType.get(message.capability).getCapability(), null);
                final IItem item = IItem.get(message.item);

                capability.upgradeSkill(item, capability.getSkill(item, message.skill));
                capability.sync();
            });

            return null;
        }
    }
}