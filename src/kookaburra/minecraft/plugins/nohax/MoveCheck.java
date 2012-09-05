package kookaburra.minecraft.plugins.nohax;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;

import kookaburra.minecraft.plugins.nohax.MoveCheck.Jump;
import kookaburra.minecraft.plugins.nohax.alerts.Alert;
import kookaburra.minecraft.plugins.nohax.alerts.AlertLevel;
import kookaburra.minecraft.plugins.nohax.alerts.AlertType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class MoveCheck
  implements Runnable
{
  public static Hashtable<Player, Location> LastLocation;
  public static Hashtable<Player, ArrayList<MoveLog>> Moves;
  public static Hashtable<Player, Long> InvalidateExpires;
  public static MoveCheck Instance;
  public static int CheckCounter = 0;
  public static Player LastHacker;
  public static Server server;

  public static void AddMove(Player player, Location to)
  {
    if ((Permissions.CanFly(player)) && (Permissions.CanSpeed(player))) {
      return;
    }
    Long expires = (Long)InvalidateExpires.get(player);

    if ((expires != null) && (expires.longValue() > System.currentTimeMillis()))
    {
      return;
    }

    if (!Moves.containsKey(player))
    {
      Moves.put(player, new ArrayList());
    }

    synchronized ((ArrayList)Moves.get(player))
    {
      MoveCheck tmp97_94 = Instance; tmp97_94.getClass(); ((ArrayList)Moves.get(player)).add(MoveLog(player, to));
    }
  }

  private static Object MoveLog(Player player, Location to) {
	return null;
}

public static void Invalidate(Player player, long time)
  {
    if (Moves.containsKey(player))
    {
      ((ArrayList)Moves.get(player)).clear();
    }

    time += System.currentTimeMillis();
    Long expires = (Long)InvalidateExpires.get(player);

    if ((expires == null) || (expires.longValue() < time))
      InvalidateExpires.put(player, Long.valueOf(time));
  }

  public static void Clear(Player player)
  {
    if (Moves.containsKey(player)) {
      Moves.remove(player);
    }
    if (LastLocation.containsKey(player))
      LastLocation.remove(player);
  }

  public MoveCheck(Server server)
  {
    server = server;

    LastLocation = new Hashtable();
    Moves = new Hashtable();
    InvalidateExpires = new Hashtable();

    Instance = this;
  }

  public void run()
  {
    for (Player player : server.getOnlinePlayers())
    {
      if (player.isOp())
      {
        if (!Moves.containsKey(player))
          continue;
        ((ArrayList)Moves.get(player)).clear();
      }
      else if ((!Moves.containsKey(player)) || (!LastLocation.containsKey(player)))
      {
        LastLocation.put(player, player.getLocation().clone());
      }
      else
      {
        try {
          synchronized ((ArrayList)Moves.get(player))
          {
            ArrayList<Jump> jumps = GetJumps((ArrayList)Moves.get(player));

            for (Jump jump : jumps)
            {
              if ((!jump.isOnGround) && (!Permissions.CanFly(player)))
              {
                if (jump.height > 5.0D)
                {
                  Alert.Add(player, AlertType.Flying, AlertLevel.Definitely);
                }
                else if (((jump.height >= 1.3D) && (!jump.isOnFire)) || (jump.height >= 2.0D))
                {
                  Alert.Add(player, AlertType.Flying, AlertLevel.Probably);
                }

              }

              if (Permissions.CanSpeed(player))
                continue;
              if (((jump.horizontalSpeed > 11.0D) && (jump.time > 0.5D)) || ((jump.horizontalSpeed > 9.0D) && (jump.time > 1.5D)))
              {
                Alert.Add(player, AlertType.Speeding, AlertLevel.Probably);
              } else {
                if ((jump.horizontalSpeed <= 13.0D) || (jump.time <= 0.5D))
                  continue;
                Alert.Add(player, AlertType.Speeding, AlertLevel.Definitely);
              }

            }

            ((ArrayList)Moves.get(player)).clear();
          }

          LastLocation.put(player, player.getLocation());
        }
        catch (Exception e)
        {
          e.printStackTrace();
        }
      }
    }
    if (Moves.keySet().size() > Util.server.getMaxPlayers() * 3)
    {
      Moves.clear();
      Moves = new Hashtable();
    }
  }

  public String GetLocationString(Location l)
  {
    return "(" + l.getX() + ", " + l.getY() + ", " + l.getZ() + ")";
  }

  @SuppressWarnings("unchecked")
public static ArrayList<Jump> GetJumps(ArrayList<MoveLog> moves) {
    int inc = 1;

    ArrayList jumps = new ArrayList();

    while (inc < moves.size())
    {
      if (((MoveLog)moves.get(inc)).isInVehicle) {
        return new ArrayList();
      }

      int startInc = inc;

      while ((inc < moves.size()) && (!((MoveLog)moves.get(inc)).isAir)) inc++;

      if (inc > startInc + 5)
      {
        MoveCheck tmp79_76 = Instance; tmp79_76.getClass(); Jump jump = new Jump((MoveLog)moves.get(startInc), (MoveLog)moves.get((inc + startInc - 1) / 2), (MoveLog)moves.get(inc - 1));
        jump.isOnGround = true;
        jumps.add(jump);
      }

      if (inc >= moves.size())
      {
        break;
      }
      MoveLog start = (MoveLog)moves.get(inc - 1);

      while ((inc < moves.size()) && (((MoveLog)moves.get(inc)).isAir) && (((MoveLog)moves.get(inc)).location.getY() > ((MoveLog)moves.get(inc - 1)).location.getY())) inc++;

      if (inc >= moves.size())
      {
        MoveCheck tmp235_232 = Instance; tmp235_232.getClass(); jumps.add(new Jump(start, (MoveLog)moves.get(inc - 1), (MoveLog)moves.get(inc - 1)));
        break;
      }

      MoveLog apex = (MoveLog)moves.get(inc - 1);
      boolean isFloating = false;
      boolean isOnFire = false;
      int floatCount = 0;

      while ((inc < moves.size()) && (((MoveLog)moves.get(inc)).isAir))
      {
        if (((MoveLog)moves.get(inc - 1)).location.getY() <= ((MoveLog)moves.get(inc)).location.getY())
        {
          floatCount++;

          if (floatCount > 3)
            isFloating = true;
        }
        else
        {
          floatCount = 0;
        }

        if (((MoveLog)moves.get(inc)).isOnFire) {
          isOnFire = true;
        }
        inc++;
      }
      MoveLog end;
      MoveLog end;
      if (inc >= moves.size())
        end = (MoveLog)moves.get(moves.size() - 1);
      else
        end = (MoveLog)moves.get(inc);
      MoveCheck tmp433_430 = Instance; tmp433_430.getClass(); Jump jump = new Jump(start, apex, end);
      jump.isFloating = isFloating;
      jump.isOnFire = isOnFire;
      jumps.add(jump);
    }

    return jumps;
  }

  public class Jump
  {
    public MoveCheck.MoveLog start;
    public MoveCheck.MoveLog apex;
    public MoveCheck.MoveLog end;
    public double height;
    public double fallDistance;
    public double length;
    public double time;
    public double jumpTime;
    public double fallTime;
    public double verticalSpeed;
    public double jumpSpeed;
    public double fallSpeed;
    public double horizontalSpeed;
    public boolean isFloating = false;
    public boolean isOnGround = false;
    public boolean isOnFire = false;

    public Jump(MoveCheck.MoveLog start, MoveCheck.MoveLog apex, MoveCheck.MoveLog end)
    {
      this.start = start;
      this.apex = apex;
      this.end = end;

      if ((start.location.getY() == apex.location.getY()) && (apex.location.getY() == end.location.getY()) && (!start.isAir) && (!apex.isAir) && (!end.isAir)) {
        this.isOnGround = true;
      }
      this.height = (apex.location.getY() - start.location.getY());
      this.fallDistance = (apex.location.getY() - end.location.getY());
      this.length = (GetHorzDistance(start.location, apex.location) + GetHorzDistance(apex.location, end.location));

      this.time = ((end.time - start.time) / 1000.0D);
      this.jumpTime = ((apex.time - start.time) / 1000.0D);
      this.fallTime = ((end.time - apex.time) / 1000.0D);

      this.jumpSpeed = (this.height / this.jumpTime);
      this.fallSpeed = (this.fallDistance / this.fallTime);
      this.verticalSpeed = ((this.height + this.fallDistance) / this.time);

      this.horizontalSpeed = (this.length / this.time);
    }

    private double GetHorzDistance(Location a, Location b)
    {
      double x = Math.abs(a.getX() - b.getX());
      double z = Math.abs(a.getZ() - b.getZ());

      return Math.sqrt(x * x + z * z); }  } 
  public class MoveLog { public Player player;
    public long time;
    public Location location;
    public Vector velocity;
    public boolean isSprinting;
    public boolean isSneaking;
    public boolean isAir;
    public boolean isOnFire;
    public boolean isInVehicle;

    public MoveLog(Player p, Location l) { this.player = p;
      this.location = l.clone();
      this.time = System.currentTimeMillis();
      this.velocity = p.getVelocity();
      this.isSprinting = p.isSprinting();
      this.isSneaking = p.isSneaking();
      this.isInVehicle = p.isInsideVehicle();
      this.isOnFire = (p.getFireTicks() > 0);

      if (Math.abs(this.velocity.getX()) < 0.001D)
        this.velocity.setX(0);
      if (Math.abs(this.velocity.getY()) < 0.001D)
        this.velocity.setY(0);
      if (Math.abs(this.velocity.getZ()) < 0.001D) {
        this.velocity.setZ(0);
      }
      this.isAir = isBlockAir(this.location);
    }

    public double Speed(MoveLog other)
    {
      if (other == null) {
        return 0.0D;
      }
      long time = Math.abs(this.time - other.time);
      double distance = this.location.distance(other.location);

      return distance / time;
    }

    private boolean isMaterialAir(Material type)
    {
      return (type == Material.AIR) || (type == Material.TORCH) || (type == Material.REDSTONE_TORCH_OFF) || (type == Material.REDSTONE_TORCH_ON);
    }

    private boolean isBlockAir(Location location)
    {
      Location l = location.clone();
      double x = l.getX();
      double y = l.getY();
      double z = l.getZ();

      y = Math.floor(y) - 0.001D;

      Material type = new Location(l.getWorld(), x, y, z).getBlock().getType();

      if (!isMaterialAir(type)) {
        return false;
      }
      boolean xup = false;
      boolean xdwn = false;
      boolean zup = false;
      boolean zdwn = false;

      if ((int)x != (int)(x + 0.32D))
      {
        xup = true;
        type = new Location(l.getWorld(), x + 0.32D, y, z).getBlock().getType();

        if (!isMaterialAir(type)) {
          return false;
        }
      }
      if ((int)x != (int)(x - 0.32D))
      {
        xdwn = true;
        type = new Location(l.getWorld(), x - 0.32D, y, z).getBlock().getType();

        if (!isMaterialAir(type)) {
          return false;
        }
      }
      if ((int)z != (int)(z + 0.32D))
      {
        zup = true;
        type = new Location(l.getWorld(), x, y, z + 0.32D).getBlock().getType();

        if (!isMaterialAir(type)) {
          return false;
        }
      }
      if ((int)z != (int)(z - 0.32D))
      {
        zdwn = true;
        type = new Location(l.getWorld(), x, y, z - 0.32D).getBlock().getType();

        if (!isMaterialAir(type)) {
          return false;
        }
      }
      if ((xup) && (zup))
      {
        type = new Location(l.getWorld(), x + 0.32D, y, z + 0.32D).getBlock().getType();

        if (!isMaterialAir(type)) {
          return false;
        }
      }
      if ((xup) && (zdwn))
      {
        type = new Location(l.getWorld(), x + 0.32D, y, z - 0.32D).getBlock().getType();

        if (!isMaterialAir(type)) {
          return false;
        }
      }
      if ((xdwn) && (zup))
      {
        type = new Location(l.getWorld(), x - 0.32D, y, z + 0.32D).getBlock().getType();

        if (!isMaterialAir(type)) {
          return false;
        }
      }
      if ((xdwn) && (zdwn))
      {
        type = new Location(l.getWorld(), x - 0.32D, y, z - 0.32D).getBlock().getType();

        if (!isMaterialAir(type)) {
          return false;
        }
      }
      return true;
    }
  }
}
