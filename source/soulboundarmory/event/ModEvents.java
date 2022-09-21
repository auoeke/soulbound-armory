package soulboundarmory.event;

import net.gudenau.lib.unsafe.Unsafe;
import net.minecraft.command.argument.ArgumentTypes;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.NewRegistryEvent;
import soulboundarmory.SoulboundArmory;
import soulboundarmory.command.argument.ItemComponentArgumentType;
import soulboundarmory.command.argument.RegistryArgumentType;
import soulboundarmory.component.Components;
import soulboundarmory.util.Util;

@EventBusSubscriber(modid = SoulboundArmory.ID, bus = EventBusSubscriber.Bus.MOD)
public final class ModEvents {
    @SubscribeEvent
    public static void setup(FMLCommonSetupEvent event) {
        ArgumentTypes.registerByClass(Util.cast(ItemComponentArgumentType.class), ConstantArgumentSerializer.of(ItemComponentArgumentType::itemComponents));
        ArgumentTypes.registerByClass(Util.cast(RegistryArgumentType.class), new RegistryArgumentType.Serializer());
    }

    @SubscribeEvent
    public static void registerRegistries(NewRegistryEvent event) {
        Unsafe.ensureClassInitialized(Components.class);
    }
}
