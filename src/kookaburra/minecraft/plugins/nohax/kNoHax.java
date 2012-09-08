package kookaburra.minecraft.plugins.nohax;

import kookaburra.minecraft.plugins.nohax.forcefield.ForcefieldListener;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class kNoHax extends JavaPlugin
{
  public void onDisable()
  {
	  PluginDescriptionFile pdfFile = getDescription();

	  Util.Initialize(this);
	    
	  System.out.println(pdfFile.getName() + " version " + pdfFile.getVersion() + " is disabled!");
  }

  public void onLoad()
  {
    super.onLoad();
  }

  public void onEnable() {
    PluginManager pm = getServer().getPluginManager();

    pm.registerEvents(new EventListener(), this);
    pm.registerEvents(new ForcefieldListener(), this);

    PluginDescriptionFile pdfFile = getDescription();

    Util.Initialize(this);

    System.out.println(pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!");
  }

  public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
  {
    if (label.equalsIgnoreCase("lag"))
    {
      double tps = Lag.getTPS();
      double lag = Math.round((1.0D - tps / 20.0D) * 100.0D);

      sender.sendMessage(ChatColor.GOLD + "Server running at " + tps + " tps");
      sender.sendMessage(ChatColor.GOLD + "Lag is approx " + lag + "%");
      return true;
    }

    return false;
  }
}
