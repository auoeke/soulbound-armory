package transfarmer.soulboundarmory.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.SideOnly;
import transfarmer.soulboundarmory.capability.tool.ISoulTool;
import transfarmer.soulboundarmory.capability.tool.SoulToolProvider;
import transfarmer.soulboundarmory.data.tool.SoulToolType;

import static net.minecraftforge.fml.relauncher.Side.CLIENT;
import static transfarmer.soulboundarmory.ResourceLocations.Client.XP_BAR;
import static transfarmer.soulboundarmory.data.tool.SoulToolDatum.LEVEL;
import static transfarmer.soulboundarmory.data.tool.SoulToolDatum.XP;

@SideOnly(CLIENT)
public class SoulToolTooltipXPBar extends Gui {
    public SoulToolTooltipXPBar(SoulToolType type, int tooltipX, int tooltipY, int originalEnchantments) {
        final Minecraft mc = Minecraft.getMinecraft();
        final FontRenderer fontRenderer = mc.fontRenderer;
        final ISoulTool capability = SoulToolProvider.get(Minecraft.getMinecraft().player);
        int level = capability.getDatum(LEVEL, type);
        int barLeftX = tooltipX + 44;
        int barTopY = tooltipY + 70 + 10 * originalEnchantments;
        int length = 62;

        GlStateManager.disableDepth();
        GlStateManager.disableLighting();
        GlStateManager.color(1F, 1F, 1F, 1F);
        mc.getTextureManager().bindTexture(XP_BAR);

        this.drawTexturedModalRect(barLeftX - length / 2, barTopY, 0, 70, length, 5);
        this.drawTexturedModalRect(barLeftX - length / 2, barTopY, 0, 75,
            Math.min(length, Math.round((float) capability.getDatum(XP, type) / capability.getNextLevelXP(type) * length)), 5);

        mc.getTextureManager().deleteTexture(XP_BAR);

        String levelString = String.format("%d", level);
        int x1 = barLeftX - fontRenderer.getStringWidth(levelString) / 2;
        int y1 = barTopY - 6;
        fontRenderer.drawString(levelString, x1 + 1, y1, 0);
        fontRenderer.drawString(levelString, x1 - 1, y1, 0);
        fontRenderer.drawString(levelString, x1, y1 + 1, 0);
        fontRenderer.drawString(levelString, x1, y1 - 1, 0);
        fontRenderer.drawString(levelString, x1, y1, 0xF7193B);

        GlStateManager.disableLighting();
        GlStateManager.enableDepth();
    }
}