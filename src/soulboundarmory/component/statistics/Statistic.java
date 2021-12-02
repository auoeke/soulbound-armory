package soulboundarmory.component.statistics;

import java.math.BigDecimal;
import net.minecraft.nbt.CompoundNBT;
import soulboundarmory.serial.CompoundSerializable;

public class Statistic extends Number implements CompoundSerializable {
    protected final Category category;
    protected final StatisticType type;

    protected BigDecimal value;
    protected double min;
    protected double max;
    protected int points;

    public Statistic(Category category, StatisticType statistic) {
        this.type = statistic;
        this.category = category;
        this.value = BigDecimal.ZERO;
        this.min = 0;
        this.max = Double.MAX_VALUE;
    }

    @Override
    public String toString() {
        return String.format("Statistic {name: %s, type: %s, min: %.3f, max: %.3f, value: %s}", this.type, this.category, this.min, this.max, this.value.toString());
    }

    public StatisticType type() {
        return this.type;
    }

    public Category getCategory() {
        return this.category;
    }

    public boolean aboveMin() {
        return this.greaterThan(this.min);
    }

    public double min() {
        return this.min;
    }

    public void min(double min) {
        this.min = min;

        if (this.doubleValue() < min) {
            this.setToMin();
        }
    }

    public void setToMin() {
        this.setValue(this.min);
    }

    public boolean belowMax() {
        return this.lessThan(this.max);
    }

    public double max() {
        return this.max;
    }

    public void max(double max) {
        this.max = max;

        if (this.doubleValue() > max) {
            this.setToMax();
        }
    }

    public void setToMax() {
        this.setValue(this.max);
    }

    public int points() {
        return this.points;
    }

    public void points(int points) {
        this.points = points;
    }

    public void incrementPoints(int points) {
        this.points += points;
    }

    public void incrementPoints() {
        ++this.points;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public void setValue(Number value) {
        this.value = BigDecimal.valueOf(value.doubleValue());
    }

    @Override
    public int intValue() {
        return this.value.intValue();
    }

    @Override
    public long longValue() {
        return this.value.longValue();
    }

    @Override
    public float floatValue() {
        return this.value.floatValue();
    }

    @Override
    public double doubleValue() {
        return this.value.doubleValue();
    }

    @Override
    public byte byteValue() {
        return this.value.byteValue();
    }

    @Override
    public short shortValue() {
        return this.value.shortValue();
    }

    public boolean lessThan(Number number) {
        return this.doubleValue() < number.doubleValue();
    }

    public boolean lessThanOrEqualTo(Number number) {
        return !this.greaterThan(number);
    }

    public boolean greaterThan(Number number) {
        return this.doubleValue() > number.doubleValue();
    }

    public boolean greaterThanOrEqualTo(Number number) {
        return !this.lessThan(number);
    }

    public void add(Number number) {
        var currentValue = this.value.doubleValue();
        var addition = number.doubleValue();

        if (currentValue + addition < this.min) {
            this.setToMin();
        } else if (currentValue + addition > this.max) {
            this.setToMax();
        } else {
            this.value = this.value.add(number instanceof BigDecimal ? (BigDecimal) number : BigDecimal.valueOf(addition));
        }
    }

    public void reset() {
        this.setToMin();
        this.points(0);
    }

    @Override
    public void serializeNBT(CompoundNBT tag) {
        tag.putDouble("min", this.min);
        tag.putDouble("max", this.max);
        tag.putString("value", this.value.toString());
        tag.putInt("points", this.points);
    }

    @Override
    public void deserializeNBT(CompoundNBT tag) {
        this.min = tag.getDouble("min");
        this.max = tag.getDouble("max");
        this.value = new BigDecimal(tag.getString("value"));
        this.points = tag.getInt("points");
    }
}
