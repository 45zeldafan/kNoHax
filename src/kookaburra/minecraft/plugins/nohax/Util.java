package kookaburra.minecraft.plugins.nohax;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Random;
import kookaburra.minecraft.plugins.nohax.alerts.Alert;
import kookaburra.minecraft.plugins.nohax.forcefield.ForcefieldChecker;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;

public class Util
{
  public static Server server;
  public static JavaPlugin plugin;

  public static void Initialize(JavaPlugin plugin)
  {
    Util.plugin = plugin;
    server = plugin.getServer();
    server.getScheduler().scheduleSyncRepeatingTask(plugin, new MoveCheck(server), 41L, 40L);
    server.getScheduler().scheduleSyncRepeatingTask(plugin, new Alert(), 6007L, 6000L);
    server.getScheduler().scheduleSyncRepeatingTask(plugin, new Lag(), 100L, 1L);
    server.getScheduler().scheduleSyncRepeatingTask(plugin, new ForcefieldChecker(), 114L, 10L);

    Settings.i = new Settings();
    try
    {
      File detailed = new File("plugins/kNoHax/detailed.log");
      detailed.createNewFile();

      File summary = new File("plugins/kNoHax/summary.log");
      summary.createNewFile();

      File config = new File("plugins/kNoHax/kNoHax.config");
      config.createNewFile();

      readCatch(config);
    }
    catch (Exception localException)
    {
    }
  }

  public static void write(File file, String content)
    throws IOException
  {
    BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, false), "UTF8"));
    out.write(content);
    out.close();
  }

  public static String read(File file) throws IOException
  {
    BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
    String ret = new String(new byte[0], "UTF-8");
    while ((in.readLine()) != null)
    {
      String line1 = null;
      ret = ret + line1;
    }

    in.close();
    return ret;
  }

  public static boolean writeCatch(File file, String content)
  {
    try
    {
      write(file, content);
      return true;
    }
    catch (Exception e) {
    }
    return false;
  }

  public static String readCatch(File file)
  {
    try
    {
      return read(file);
    }
    catch (IOException e) {
    }
    return null;
  }

  public static double magnitude(double p1x, double p1y, double p1z, double p2x, double p2y, double p2z)
  {
    double xdist = p1x - p2x;
    double ydist = p1y - p2y;
    double zdist = p1z - p2z;
    return Math.sqrt(xdist * xdist + ydist * ydist + zdist * zdist);
  }

  public static double point_distance(Location loc1, Location loc2) {
    double p1x = loc1.getX();
    double p1y = loc1.getY();
    double p1z = loc1.getZ();

    double p2x = loc2.getX();
    double p2y = loc2.getY();
    double p2z = loc2.getZ();
    return magnitude(p1x, p1y, p1z, p2x, p2y, p2z);
  }

  public static int random(int x) {
    Random rand = new Random();
    return rand.nextInt(x);
  }

  public static double lengthdir_x(double len, double dir) {
    return len * Math.cos(Math.toRadians(dir));
  }

  public static double lengthdir_y(double len, double dir) {
    return -len * Math.sin(Math.toRadians(dir));
  }
  public static double point_direction(double x1, double y1, double x2, double y2) {
    double d = 0;
    try {
      d = Math.toDegrees(Math.atan((y2 - y1) / (x2 - x1)));
    }
    catch (Exception e)
    {
    }
    if ((x1 > x2) && (y1 > y2))
    {
      return -d + 180.0D;
    }
    if ((x1 < x2) && (y1 > y2))
    {
      return -d;
    }
    if (x1 == x2)
    {
      if (y1 > y2)
        return 90.0D;
      if (y1 < y2)
        return 270.0D;
    }
    if ((x1 > x2) && (y1 < y2))
    {
      return -d + 180.0D;
    }
    if ((x1 < x2) && (y1 < y2))
    {
      return -d + 360.0D;
    }
    if (y1 == y2)
    {
      if (x1 > x2)
        return 180.0D;
      if (x1 < x2)
        return 0.0D;
    }
    return 0.0D;
  }
}
