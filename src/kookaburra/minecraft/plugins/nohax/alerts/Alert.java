package kookaburra.minecraft.plugins.nohax.alerts;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintStream;
import java.util.Hashtable;
import kookaburra.minecraft.plugins.nohax.Permissions;
import kookaburra.minecraft.plugins.nohax.Util;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;


//need to add in spout features class in here for hud//
public class Alert
  implements Runnable
{
  public static Hashtable<String, Hashtable<AlertType, Hashtable<AlertLevel, Integer>>> Recent = new Hashtable();

  public static void Add(Player player, AlertType type, AlertLevel level)
  {
    String name = ChatColor.stripColor(player.getName());

    Hashtable types = (Hashtable)Recent.get(name);

    if (types == null)
    {
      types = new Hashtable();
      Recent.put(name, types);
    }

    Hashtable levels = (Hashtable)types.get(type);

    if (levels == null)
    {
      levels = new Hashtable();
      types.put(type, levels);
    }

    Integer count = (Integer)levels.get(level);

    if (count == null)
    {
      count = Integer.valueOf(0);
    }

    levels.put(level, Integer.valueOf(count.intValue() + 1));

    if ((level == AlertLevel.Probably) || (level == AlertLevel.Definitely))
    {
      TellAdmins(type.getMessage().replaceAll("%p", name).replaceAll("%l", level.name()) + " (" + count + ")");
    }
  }

  public static void TellAdmins(String msg)
  {
    for (Player player : Util.server.getOnlinePlayers())
    {
      if (!Permissions.CanViewAlerts(player))
        continue;
      Player p = Util.server.getPlayer(player.getName());

      if (p != null)
        p.sendMessage(msg);
    }
  }

  public void run()
  {
    try
    {
      File dir = new File("plugins/kNoHax");

      if (!dir.exists()) {
        dir.mkdirs();
      }
      File file = new File("plugins/kNoHax/kNoHax.log");

      file.createNewFile();

      FileWriter fout = new FileWriter(file);

      for (String player : Recent.keySet())
      {
        fout.write("*****" + player + "*****\n");
        Hashtable types = (Hashtable)Recent.get(player);

        for (AlertType type : types.keySet())
        {
          Hashtable levels = (Hashtable)types.get(type);

          for (AlertLevel level : levels.keySet())
          {
            Integer count = (Integer)levels.get(level);

            fout.write(type.name() + "[" + level.name() + "]: " + count + "\n");
          }
        }
        fout.write("\n");
      }

      fout.close();
    }
    catch (Exception ex)
    {
      System.out.println("Error when saving kNoHax log file: " + ex);
    }
  }
}