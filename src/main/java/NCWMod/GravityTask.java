package NCWMod;

public class GravityTask
{
  private final Gravity plugin;

  public GravityTask(Gravity plugin)
  {
    this.plugin = plugin;
  }

  public void run()
  {
    this.plugin.UpdateVelocities();
    new GravityTask(this.plugin).runTaskLater(this.plugin, 1L);
  }
}