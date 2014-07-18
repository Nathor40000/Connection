package micdoodle8.mods.galacticraft.api.world;

public abstract interface IGalacticraftWorldProvider
{
  public abstract float getGravity();

  public abstract double getMeteorFrequency();

  public abstract double getFuelUsageMultiplier();

  public abstract boolean canSpaceshipTierPass(int paramInt);

  public abstract float getFallDamageModifier();

  public abstract float getSoundVolReductionAmount();
}

/* Location:           C:\Users\Nigel\Desktop\Nathan\Modding\jd-gui-0.3.5.windows\Galacticraft-1.6.4-2.0.13.1063.jar
 * Qualified Name:     micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider
 * JD-Core Version:    0.6.2
 */