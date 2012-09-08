package kookaburra.minecraft.plugins.nohax.forcefield;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ForcefieldListener
  implements Listener
{
  @EventHandler
  public void OnPlayerLogout(PlayerQuitEvent event)
  {
    AttackLog.Attacks.remove(event.getPlayer());
  }

  @EventHandler
  public void OnPlayerKick(PlayerKickEvent event) {
    AttackLog.Attacks.remove(event.getPlayer());
  }

  @EventHandler(priority=EventPriority.HIGHEST)
  public void OnPlayerInteract(PlayerAnimationEvent ev) {
    if (ev.getAnimationType() == PlayerAnimationType.ARM_SWING);
  }

  @EventHandler(priority=EventPriority.HIGHEST)
  public void OnEntityDamage(EntityDamageEvent ev)
  {
    try
    {
      if (!(ev instanceof EntityDamageByEntityEvent)) {
        return;
      }
      EntityDamageByEntityEvent event = (EntityDamageByEntityEvent)ev;

      if (!(event.getDamager() instanceof Player)) {
        return;
      }
      Player attacker = (Player)event.getDamager();

      AttackLog.Put(attacker);
    }
    catch (Exception ex)
    {
      System.out.println(ex);
    }
  }
}
