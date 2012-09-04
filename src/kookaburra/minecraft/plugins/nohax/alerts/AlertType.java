package kookaburra.minecraft.plugins.nohax.alerts;

public enum AlertType
{
  FastAttack("%p is [%l] attacking too fast!"), 
  Flying("%p is [%l] flying!"), 
  Speeding("%p is [%l] speeding!");

  String message;

  private AlertType(String msg) {
    this.message = msg;
  }

  public String getMessage() {
    return this.message;
  }
}