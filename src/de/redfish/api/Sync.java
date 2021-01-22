package de.redfish.api;

import api.bybackfish.DB;
import api.bybackfish.Row;
import de.redfish.PlayerSync;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.List;

public class Sync {

  private DB db;
  private String table;

  public Sync(DB db, String table) {
    this.db = db;
    this.table = table;
  }

  public boolean syncPlayer(Player player) {
    if (!db.hasTable(table)) {
      return false;
    }
    System.out.println("Uploading Stuff");



    Row row = new Row();
    row.with("uuid", player.getUniqueId());
    row.with("inventory",
            ItemStackSerializer.itemStackArrayToBase64(player.getInventory().getContents()));
    row.with("armor",
            ItemStackSerializer.itemStackArrayToBase64(player.getInventory().getArmorContents()));
    row.with("enderchest",
            ItemStackSerializer.itemStackArrayToBase64(player.getEnderChest().getContents()));


    row.with("coins", PlayerSync.getInstance().getEconomy().getBalance(player));

    db.replace(table, row);

    return true;

  }


  public void giveStuff(Player player) throws IOException {
    System.out.println("Syncing Stuff");

    List<Row> row = db.select("SELECT * FROM " + table + " WHERE uuid = ?", player.getUniqueId());

    Row r = row.get(0);
    player.getInventory().clear();

    ItemStack[] items = ItemStackSerializer.itemStackArrayFromBase64(r.get("inventory"));
    for (int i = 0; i < items.length; i++) {

      player.getInventory().setItem(i, items[i] != null ? items[i] : new ItemStack(Material.AIR));

    }

    ItemStack[] armor = ItemStackSerializer.itemStackArrayFromBase64(r.get("armor"));

    player.getInventory().setBoots(armor[0] != null ? armor[0] : new ItemStack(Material.AIR));
    player.getInventory()
            .setLeggings(armor[1] != null ? armor[1] : new ItemStack(Material.AIR));
    player.getInventory().setChestplate(armor[2] != null ? armor[2] : new ItemStack(Material.AIR));
    player.getInventory().setHelmet(armor[3] != null ? armor[3] : new ItemStack(Material.AIR));


    ItemStack[] ec = ItemStackSerializer.itemStackArrayFromBase64(r.get("enderchest"));
    for (int i = 0; i < ec.length; i++) {

      player.getEnderChest().setItem(i, ec[i] != null ? ec[i] : new ItemStack(Material.AIR));

    }


    PlayerSync.getInstance().getEconomy().withdrawPlayer(player, PlayerSync.getInstance().getEconomy().getBalance(player));

    double coins = Double.parseDouble(r.get("coins"));
    System.out.println(coins);

    EconomyResponse response = PlayerSync.getInstance().getEconomy().depositPlayer(player, coins);
    if(response.transactionSuccess()) {
      player.sendMessage("Deposited Money from Sync!");
    } else {
      player.sendMessage(String.format("An error occured: %s", response.errorMessage));
    }
 

  }


}
