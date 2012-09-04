package kookaburra.minecraft.plugins.nohax;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class Permissions
{
  public static boolean CanFly(Player player)
  {
    return (player.getGameMode() == GameMode.CREATIVE) || (player.hasPermission("NoHax.Fly"));
  }

  public static boolean CanSpeed(Player player) {
    return (player.getGameMode() == GameMode.CREATIVE) || (player.hasPermission("NoHax.Speed"));
  }

  public static boolean CanViewAlerts(Player player) {
    return player.hasPermission("NoHax.ViewAlerts");
  }
}
