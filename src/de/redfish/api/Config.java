package de.redfish.api;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Config {

  File file;
  FileConfiguration config;


  public Config() {

    file = new File("plugins/PlayerSync/config.yml");
    config = YamlConfiguration.loadConfiguration(file);


    config.addDefault("host", "localhost");
    config.addDefault("user", "root");
    config.addDefault("password", "");
    config.addDefault("port", "3306");
    config.addDefault("database", "player_sync");
    config.addDefault("table", "player_data");
    config.options().copyDefaults(true);
    save();

  }

  public String getString(String path) {

    return config.getString(path);

  }

  public boolean setString(String path, String content){

      if(config.get(path) != null) return false;

      config.set(path, content);
      save();
      return true;

  }

  public void save(){
      try {
          config.save(file);
      } catch (IOException e) {
          e.printStackTrace();
      }
  }


}
