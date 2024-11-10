package io.github.rypofalem.armorstandeditor.menu;

import io.github.rypofalem.armorstandeditor.PlayerEditor;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.Inventory;

public interface EditorMenu extends ASEHolder  {
    void open();
    ArmorStand getArmorStand();
}
