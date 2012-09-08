package kookaburra.minecraft.plugins.nohax.forcefield;

import java.util.Hashtable;
import kookaburra.minecraft.plugins.nohax.Lag;
import org.bukkit.entity.Player;

public class AttackLog
{
  public static Hashtable<Player, AttackLog> Attacks = new Hashtable<Player, AttackLog>();
  public Player Player;
  public int[] AttackTicks;
  public int AttackCount;

  public static void Put(Player player)
  {
    AttackLog log = (AttackLog)Attacks.get(player);

    if (log == null)
    {
      log = new AttackLog(player);
      Attacks.put(player, log);
    }

    log.AttackTicks[(log.AttackCount % log.AttackTicks.length)] = Lag.TickCount;

    log.AttackCount += 1;
  }

  private AttackLog(Player player)
  {
    this.Player = player;
    this.AttackTicks = new int[100];
    this.AttackCount = 0;
  }

  public double getAttackSpeed()
  {
    if (this.AttackCount < 17)
    {
      return 1.0D;
    }

    int tick10 = this.AttackTicks[((this.AttackCount - 10) % this.AttackTicks.length)];

    int prevTick = tick10 - 1;

    return 10.0D / (Lag.getElapsed(prevTick) / 1000.0D);
  }
}
