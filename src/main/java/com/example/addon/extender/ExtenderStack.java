package com.example.addon.extender;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import java.util.Collections;
import java.util.Map;
import java.util.Random;

import static net.kyori.adventure.text.format.NamedTextColor.YELLOW;
import static net.kyori.adventure.text.format.TextDecoration.BOLD;

public class ExtenderStack extends ItemStack {
    private static final int CUSTOM_MODEL_DATE = 914_342_302;
    private static final NamespacedKey NAMESPACE_EXPANSION_SIZE = new NamespacedKey("ppb_extender", "expansion_size");
    private static final Random RANDOM = new Random();

    public ExtenderStack(Material material, int expansionSize) {
        this(material, expansionSize, 1);
    }

    public ExtenderStack(Material material, int expansionSize, int amount) {
        super(material, amount);
        initializeMeta(expansionSize);
    }

    public Integer getExpansionSize() {
        return getExpansionSize(this);
    }

    public static ExtenderStack create(ItemStack item) {
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta.hasCustomModelData() && itemMeta.getCustomModelData() == CUSTOM_MODEL_DATE) {
            return new ExtenderStack(item.getType(), getExpansionSize(item));
        }
        return null;
    }

    public static ExtenderStack createRandom(Material material, Map<String, Integer> expansionSizes, Map<String, Double> weights) {
        double sum = weights.values().stream().mapToDouble(i -> i).sum();
        if (Math.abs(sum) < 1e-6) {
            return null;
        }
        String key = null;
        double acc = 0;
        double chance = RANDOM.nextDouble();
        for (Map.Entry<String, Double> entry : weights.entrySet()) {
            acc += entry.getValue() / sum;
            key = entry.getKey();
            if (chance < acc) {
                break;
            }
        }
        return key == null ? null : new ExtenderStack(material, expansionSizes.get(key));
    }

    private void initializeMeta(Integer expansionSize) {
        ItemMeta meta = getItemMeta();
        meta.setCustomModelData(CUSTOM_MODEL_DATE);
        meta.displayName(Component.text("Border extender by " + expansionSize, YELLOW).decoration(BOLD, true));
        meta.lore(Collections.singletonList(Component.text("Right click to use")));
        meta.getPersistentDataContainer().set(NAMESPACE_EXPANSION_SIZE, PersistentDataType.INTEGER, expansionSize);
        setItemMeta(meta);
    }

    private static int getExpansionSize(ItemStack item) {
        Integer expansionSize = item.getItemMeta().getPersistentDataContainer().get(NAMESPACE_EXPANSION_SIZE, PersistentDataType.INTEGER);
        return expansionSize == null ? 0 : expansionSize;
    }
}
