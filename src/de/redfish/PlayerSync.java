package de.redfish;

import api.bybackfish.DB;
import api.bybackfish.Table;
import de.redfish.api.Config;
import de.redfish.api.Sync;
import de.redfish.command.SyncCommand;
import de.redfish.listener.ConnectionEvent;
import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerSync extends JavaPlugin {

    private static PlayerSync instance;
    private static Sync sync;
    private static Config config;
    private Economy economy;


    private static DB db;
    @Override
    public void onEnable() {

        instance = this;
        config = new Config();
        initDatabase();
        sync = new Sync(db, config.getString("table"));
        getCommand("sync").setExecutor(new SyncCommand());

        if (!setupEconomy() ) {
            System.out.println(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        getServer().getPluginManager().registerEvents(new ConnectionEvent(), this);
        
    }

    @Override
    public void onDisable() {

        for(Player player : Bukkit.getOnlinePlayers()){
            PlayerSync.getInstance().getSync().syncPlayer(player);
          player.sendMessage("Syncing your Inventory....");

            Bukkit.getScheduler().scheduleSyncDelayedTask(PlayerSync.getInstance(), new Runnable() {
                @Override
                public void run() {
                  player.getInventory().clear();
                }
            }, 25);
        }
    }

    public void initDatabase(){


        db = new DB(config.getString("host"), config.getString("user"), config.getString("password"), config.getString("database"));

        Table tb = new Table(config.getString("table"));
        if(!db.hasTable(config.getString("table"))){

            tb.varchar("uuid",70);

            tb.longtext("inventory");
            tb.longtext("armor");
            tb.longtext("enderchest");
            tb.uniqueIndex("uuid");
            tb.varchar("coins", 255);

            db.addTable(tb);

        }

    }

    public static PlayerSync getInstance() {
        return instance;
    }


    public Economy getEconomy() {
        return economy;
    }

    public  Sync getSync() {
        return sync;
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }
}
