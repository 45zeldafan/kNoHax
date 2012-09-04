package kookaburra.minecraft.plugins.nohax;

import java.util.Hashtable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class EventListener
  implements Listener
{
  private Hashtable<String, Boolean> KickedPlayers = new Hashtable<String, Boolean>();

  @EventHandler
  public void onEntityDamage(EntityDamageEvent event)
  {
    if ((event.getEntity() instanceof Player))
    {
      Player player = (Player)event.getEntity();

      MoveCheck.Invalidate(player, 1000L);
    }
  }

  @EventHandler
  public void onEntityDeath(EntityDeathEvent event) {
    if ((event instanceof PlayerDeathEvent))
      MoveCheck.Invalidate((Player)event.getEntity(), 4000L);
  }

  @EventHandler
  public void onPlayerLogin(PlayerLoginEvent event) {
    MoveCheck.Invalidate(event.getPlayer(), 4000L);
  }
  @EventHandler
  public void onPlayerRespawn(PlayerRespawnEvent event) {
    MoveCheck.Invalidate(event.getPlayer(), 4000L);
  }
  @EventHandler
  public void onPlayerMove(PlayerMoveEvent event) {
    MoveCheck.AddMove(event.getPlayer(), event.getTo());
  }
  @EventHandler
  public void onPlayerVelocity(PlayerVelocityEvent event) {
    MoveCheck.Invalidate(event.getPlayer(), 500L);
  }

  @EventHandler
  public void onPlayerKick(PlayerKickEvent event)
  {
    this.KickedPlayers.put(event.getPlayer().getName(), Boolean.valueOf(true));
  }
  @EventHandler
  public void onPlayerTeleport(PlayerTeleportEvent event) {
    MoveCheck.Invalidate(event.getPlayer(), 4000L);
  }
  @EventHandler
  public void onPlayerPortal(PlayerPortalEvent event) {
    MoveCheck.Invalidate(event.getPlayer(), 4000L);
  }
  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    MoveCheck.Invalidate(event.getPlayer(), 4000L);
    Player player = event.getPlayer();

    if ((!player.isOp()) && (!Permissions.CanFly(player)))
    {
      String nofly = "&f &f &1 &0 &2 &4 ";
      nofly = nofly.replaceAll("(&([a-f0-9]))", "¤$2");
      player.sendMessage(nofly);

      String nocheat = "&f &f &2 &0 &4 &8";
      nocheat = nocheat.replaceAll("(&([a-f0-9]))", "¤$2");
      player.sendMessage(nocheat);
    }
  }

  @EventHandler
  public void onPlayerChat(AsyncPlayerChatEvent event) {
    SpamCheck.AsyncPlayerChatEvent(event);
  }
}
