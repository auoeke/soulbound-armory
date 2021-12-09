package soulboundarmory.component.soulbound.player;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import soulboundarmory.client.gui.screen.SelectionTab;
import soulboundarmory.client.gui.screen.SoulboundTab;
import soulboundarmory.client.i18n.Translations;
import soulboundarmory.lib.component.ComponentKey;
import soulboundarmory.component.Components;
import soulboundarmory.component.soulbound.item.tool.PickComponent;
import soulboundarmory.item.SoulboundToolItem;

public class ToolComponent extends SoulboundComponent {
    public ToolComponent(PlayerEntity player) {
        super(player);

        this.store(new PickComponent(this));
    }

    @Override
    public ComponentKey<? extends SoulboundComponent> key() {
        return Components.tool;
    }

    @Override
    public boolean accepts(ItemStack stack) {
        return stack.getItem() instanceof SoulboundToolItem;
    }

    @Override
    public SoulboundTab selectionTab() {
        return new SelectionTab(Translations.guiToolSelection);
    }
}
