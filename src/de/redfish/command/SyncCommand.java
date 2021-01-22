package de.redfish.command;

import de.redfish.PlayerSync;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;

public class SyncCommand implements CommandExecutor {

  @Override
  public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
    if (commandSender instanceof Player) {
        if(!((Player) commandSender).hasPermission("sync.sync")) return true;

     if(args.length == 1){

         PlayerSync.getInstance().getSync().syncPlayer((Player) commandSender);

     }else{

         try {
             PlayerSync.getInstance().getSync().giveStuff((Player) commandSender);
         } catch (IOException e) {
             e.printStackTrace();
         }
     }


    }
      return true;
  }
}
