package de.redfish.api;

import api.bybackfish.DB;
import api.bybackfish.Row;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.List;

public class PlayerVault {


    private DB db;

    public PlayerVault(DB db) {
        this.db = db;
    }

    public void openVault(Player player, int num) throws IOException {

        List<Row> stuff = db.select("SELECT * FROM vaults WHERE uuid = ? AND num = ?", player.getUniqueId(),num);
        int size = player.hasPermission("vaults.op") ? 54 : 27;

        Inventory vault = Bukkit.createInventory(null, size, "Vault " + ChatColor.RED + "#" + num);

        for (Row data : stuff) {

                ItemStack[] content = ItemStackSerializer.itemStackArrayFromBase64(data.get("content"));

                /*

                | vault | uuid | data



                 */


        }

    }

}
