package de.redfish.listener;

import de.redfish.PlayerSync;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class ConnectionEvent implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event){

        event.getPlayer().getInventory().clear();
        event.getPlayer().getInventory().setHelmet(new ItemStack(Material.AIR));
        event.getPlayer().getInventory().setChestplate(new ItemStack(Material.AIR));
        event.getPlayer().getInventory().setLeggings(new ItemStack(Material.AIR));
        event.getPlayer().getInventory().setBoots(new ItemStack(Material.AIR));
        event.getPlayer().getEnderChest().clear();


        Bukkit.getScheduler().scheduleSyncDelayedTask(PlayerSync.getInstance(), new Runnable() {
            @Override
            public void run() {
                PlayerSync.getInstance().getSync().syncPlayer(event.getPlayer());
                event.getPlayer().sendMessage("Syncing your Inventory....");
            }
        }, 40);

    }


    @EventHandler
    public void onQuit(PlayerQuitEvent event){

        PlayerSync.getInstance().getSync().syncPlayer(event.getPlayer());
        event.getPlayer().sendMessage("Syncing your Inventory....");

        Bukkit.getScheduler().scheduleSyncDelayedTask(PlayerSync.getInstance(), new Runnable() {
            @Override
            public void run() {
              event.getPlayer().getInventory().clear();
            }
        }, 40);

    }





}
