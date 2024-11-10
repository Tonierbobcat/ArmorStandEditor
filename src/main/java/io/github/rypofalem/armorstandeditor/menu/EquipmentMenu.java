/*
 * ArmorStandEditor: Bukkit plugin to allow editing armor stand attributes
 * Copyright (C) 2016-2023  RypoFalem
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package io.github.rypofalem.armorstandeditor.menu;

import io.github.rypofalem.armorstandeditor.PlayerEditor;

import io.github.rypofalem.armorstandeditor.api.events.editor.ArmorStandEditorOpenedEvent;
import net.coreprotect.listener.player.PlayerInteractEntityListener;
import net.coreprotect.utility.Util;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

public class EquipmentMenu implements EditorMenu {

    private static final int HELMET_INDEX;
    private static final int CHEST_INDEX;
    private static final int PANTS_INDEX;
    private static final int BOOTS_INDEX;
    private static final int RIGHT_HAND_INDEX;
    private static final int LEFT_HAND_INDEX;

    Inventory menu;
    private PlayerEditor pe;
    private ArmorStand armorstand;
    static String name = "ArmorStand Equipment";

    public EquipmentMenu(PlayerEditor pe, ArmorStand as) {
        this.pe = pe;
        this.armorstand = as;
        name = pe.plugin.getLang().getMessage("equiptitle", "menutitle");
        menu = Bukkit.createInventory(this, 18, name);
    }

    @Override
    public void open() {
        ArmorStandEditorOpenedEvent event = new ArmorStandEditorOpenedEvent(pe.getPlayer(), this);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) return;


        fillInventory();
        pe.getPlayer().openInventory(this.menu);
    }


    @Override
    public @NotNull Inventory getInventory() {
        return menu;
    }

    @Override
    public ArmorStand getArmorStand() {
        return this.armorstand;
    }

    private void fillInventory() {
        Inventory menu = this.menu;
        menu.clear();
        EntityEquipment equipment = armorstand.getEquipment();
        assert equipment != null;
        ItemStack helmet = equipment.getHelmet();
        ItemStack chest = equipment.getChestplate();
        ItemStack pants = equipment.getLeggings();
        ItemStack feetsies = equipment.getBoots();
        ItemStack rightHand = equipment.getItemInMainHand();
        ItemStack leftHand = equipment.getItemInOffHand();
        equipment.clear();

        ItemStack disabledIcon = new ItemStack(Material.BARRIER);
        ItemMeta meta = disabledIcon.getItemMeta();
        meta.setDisplayName(pe.plugin.getLang().getMessage("disabled", "warn")); //equipslot.msg <option>
        meta.getPersistentDataContainer().set(pe.plugin.getIconKey(), PersistentDataType.STRING, "ase icon"); // mark as icon
        disabledIcon.setItemMeta(meta);

        ItemStack helmetIcon = createIcon(Material.LEATHER_HELMET, "helm");
        ItemStack chestIcon = createIcon(Material.LEATHER_CHESTPLATE, "chest");
        ItemStack pantsIcon = createIcon(Material.LEATHER_LEGGINGS, "pants");
        ItemStack feetsiesIcon = createIcon(Material.LEATHER_BOOTS, "boots");
        ItemStack rightHandIcon = createIcon(Material.WOODEN_SWORD, "rhand");
        ItemStack leftHandIcon = createIcon(Material.SHIELD, "lhand");
        ItemStack[] items =
            {helmetIcon, chestIcon, pantsIcon, feetsiesIcon, rightHandIcon, leftHandIcon, disabledIcon, disabledIcon, disabledIcon,
                helmet, chest, pants, feetsies, rightHand, leftHand, disabledIcon, disabledIcon, disabledIcon
            };
        menu.setContents(items);
    }

    private ItemStack createIcon(Material mat, String slot) {
        ItemStack icon = new ItemStack(mat);
        ItemMeta meta = icon.getItemMeta();
        meta.getPersistentDataContainer().set(pe.plugin.getIconKey(), PersistentDataType.STRING, "ase icon");
        meta.setDisplayName(pe.plugin.getLang().getMessage("equipslot", "iconname", slot)); //equipslot.msg <option>
        ArrayList<String> loreList = new ArrayList<>();
        loreList.add(pe.plugin.getLang().getMessage("equipslot.description", "icondescription", slot)); //equioslot.description.msg <option>
        meta.setLore(loreList);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        icon.setItemMeta(meta);
        return icon;
    }



    public void equipArmorStand() {
        Inventory menu = this.menu;

        EntityEquipment equipment = armorstand.getEquipment();

        Player player = pe.getPlayer();

        ItemStack[] oldContents = Util.getArmorStandContents(equipment);
        ItemStack[] updatedContents = oldContents.clone();
        ItemStack[] newContents = {
                menu.getItem(HELMET_INDEX),
                menu.getItem(CHEST_INDEX),
                menu.getItem(PANTS_INDEX),
                menu.getItem(BOOTS_INDEX),
                menu.getItem(RIGHT_HAND_INDEX),
                menu.getItem(LEFT_HAND_INDEX)
        };

        for (int i = 0; i < newContents.length; i++) {
            if (newContents[i] != null && !newContents[i].getType().equals(Material.AIR)
                    && !newContents[i].equals(oldContents[i])) { //if equipment changed
                updatedContents[i] = newContents[i];
            }
        }

        if (!Arrays.equals(oldContents, updatedContents)) { // Only send the update if there is a change
            PlayerInteractEntityListener.queueContainerSpecifiedItems(
                    player.getName(),
                    Material.ARMOR_STAND,
                    new Object[]{oldContents, updatedContents},
                    armorstand.getLocation(),
                    false);
        }

        equipment.setHelmet(newContents[0]);
        equipment.setChestplate(newContents[1]);
        equipment.setLeggings(newContents[2]);
        equipment.setBoots(newContents[3]);
        equipment.setItemInMainHand(newContents[4]);
        equipment.setItemInOffHand(newContents[5]);
    }

    static {
        HELMET_INDEX = 9;
        CHEST_INDEX = 10;
        PANTS_INDEX = 11;
        BOOTS_INDEX = 12;
        RIGHT_HAND_INDEX = 13;
        LEFT_HAND_INDEX = 14;
    }
}
