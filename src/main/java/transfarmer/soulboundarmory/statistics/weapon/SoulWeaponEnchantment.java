package transfarmer.soulboundarmory.statistics.weapon;

import net.minecraft.enchantment.Enchantment;
import transfarmer.soulboundarmory.statistics.IEnchantment;

public enum SoulWeaponEnchantment implements IEnchantment {
    SHARPNESS(0, Enchantment.getEnchantmentByLocation("sharpness")),
    SWEEPING_EDGE(1, Enchantment.getEnchantmentByLocation("sweeping")),
    LOOTING(2, Enchantment.getEnchantmentByLocation("looting")),
    FIRE_ASPECT(3, Enchantment.getEnchantmentByLocation("fire_aspect")),
    KNOCKBACK(4, Enchantment.getEnchantmentByLocation("knockback")),
    SMITE(5, Enchantment.getEnchantmentByLocation("smite")),
    BANE_OF_ARTHROPODS(6, Enchantment.getEnchantmentByLocation("bane_of_arthropods"));

    private final int index;
    private final Enchantment enchantment;

    private static final SoulWeaponEnchantment[] ENCHANTMENTS = {SHARPNESS, SWEEPING_EDGE, LOOTING, FIRE_ASPECT, KNOCKBACK, SMITE, BANE_OF_ARTHROPODS};

    SoulWeaponEnchantment(final int index, final Enchantment enchantment) {
        this.index = index;
        this.enchantment = enchantment;
    }

    public static int getAmount() {
        return ENCHANTMENTS.length;
    }

    @Override
    public int getIndex() {
        return this.index;
    }

    @Override
    public Enchantment getEnchantment() {
        return this.enchantment;
    }

    public static SoulWeaponEnchantment getEnchantment(int index) {
        return ENCHANTMENTS[index];
    }

    public static String getName(int index) {
        return getEnchantment(index).toString();
    }

    public static SoulWeaponEnchantment[] getEnchantments() {
        return ENCHANTMENTS;
    }
}