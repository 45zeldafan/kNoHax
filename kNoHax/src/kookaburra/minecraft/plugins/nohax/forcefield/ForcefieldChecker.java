package kookaburra.minecraft.plugins.nohax.forcefield;

import kookaburra.minecraft.plugins.nohax.alerts.Alert;
import kookaburra.minecraft.plugins.nohax.alerts.AlertLevel;
import kookaburra.minecraft.plugins.nohax.alerts.AlertType;

public class ForcefieldChecker
  implements Runnable
{
  public void run()
  {
    for (AttackLog log : AttackLog.Attacks.values())
    {
      try
      {
        double speed = log.getAttackSpeed();

        if (speed > 12.0D)
        {
          Alert.Add(log.Player, AlertType.FastAttack, AlertLevel.Definitely);
        }
        else if (speed > 10.0D)
        {
          Alert.Add(log.Player, AlertType.FastAttack, AlertLevel.Probably);
        } else {
          if (speed <= 8.0D)
            continue;
          Alert.Add(log.Player, AlertType.FastAttack, AlertLevel.Maybe);
        }
      }
      catch (Exception localException)
      {
      }
    }
  }
}