package user11681.soulboundarmory.client.gui.screen;

import net.minecraft.client.util.math.MatrixStack;
import java.util.List;
import net.minecraft.text.Text;
import net.minecraft.text.LiteralText;
import user11681.cell.client.gui.widget.callback.PressCallback;
import user11681.cell.client.gui.widget.scalable.ScalableWidget;
import user11681.soulboundarmory.capability.statistics.Statistic;
import user11681.soulboundarmory.client.i18n.Translations;
import user11681.soulboundarmory.network.ExtendedPacketBuffer;
import user11681.soulboundarmory.registry.Packets;

import static user11681.soulboundarmory.capability.statistics.Category.attribute;
import static user11681.soulboundarmory.capability.statistics.StatisticType.attributePoints;
import static user11681.soulboundarmory.capability.statistics.StatisticType.spentAttributePoints;

public class AttributeTab extends SoulboundTab {
    protected List<StatisticEntry> attributes;

    public AttributeTab() {
        super(Translations.menuButtonAttributes);
    }

    @Override
    public void init() {
        super.init();

        this.attributes = this.parent.storage.screenAttributes();
        int size = this.attributes.size();
        int start = (this.height - (size - 1) * this.height / 16) / 2;
        ScalableWidget resetButton = this.add(this.resetButton(this.resetAction(attribute)));

        resetButton.active = this.parent.storage.datum(spentAttributePoints) > 0;

        for (int index = 0; index < size; index++) {
            Statistic attribute = this.attributes.get(index).statistic;
            ScalableWidget add = this.add(this.squareButton((this.width + 182) / 2, start + index * this.height / 16 + 4, new LiteralText("+"), this.addPointAction(attribute)));
            ScalableWidget remove = this.add(this.squareButton((this.width + 182) / 2 - 20, start + index * this.height / 16 + 4, new LiteralText("-"), this.removePointAction(attribute)));

            remove.active = attribute.isAboveMin();
            add.active = this.parent.storage.datum(attributePoints) > 0 && attribute.isBelowMax();
        }
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float tickDelta) {
        super.render(matrices, mouseX, mouseY, tickDelta);

        int points = this.parent.storage.datum(attributePoints);

        if (points > 0) {
            drawCenteredText(matrices, this.textRenderer, String.format("%s: %s", Translations.menuUnspentPoints, points), Math.round(this.width / 2F), 4, 0xFFFFFF);
        }

        for (int row = 0, size = this.attributes.size(); row < size; row++) {
            this.drawAttribute(matrices, this.attributes.get(row).text, row, size);
        }
    }

    public void drawAttribute(MatrixStack stack, Text format, int row, int rows) {
        this.textRenderer.draw(stack, format, (this.width - 182) / 2F, this.height(rows, row), 0xFFFFFF);
    }

    protected PressCallback<ScalableWidget> addPointAction(Statistic statistic) {
        return button -> Packets.serverAttribute.send(new ExtendedPacketBuffer(this.parent.storage)
            .writeIdentifier(statistic.type().id())
            .writeInt(hasShiftDown() ? this.parent.storage.datum(attributePoints) : 1)
        );
    }

    protected PressCallback<ScalableWidget> removePointAction(Statistic statistic) {
        return button -> Packets.serverAttribute.send(new ExtendedPacketBuffer(this.parent.storage)
            .writeIdentifier(statistic.type().id())
            .writeInt(hasShiftDown() ? -statistic.getPoints() : -1)
        );
    }
}