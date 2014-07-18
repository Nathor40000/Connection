package NCWMod.CCTV;
public class ItemCCTV extends yr
{
  public ItemCCTV(int var1)
  {
    super(var1);
  }

  public boolean a(aan var1, yw var2, xd var3, int var4, int var5, int var6, int var7) {
    if (var7 == 0)
      return false;
    if (var7 == 1) {
      return false;
    }
    byte var8 = 0;
    if (var7 == 4) {
      var8 = 1;
    }

    if (var7 == 3) {
      var8 = 2;
    }

    if (var7 == 5) {
      var8 = 3;
    }

    EntityCCTV var9 = new EntityCCTV(var3, var4, var5, var6, var8);
    if (var9.canStay()) {
      if (!var3.F) {
        var3.a(var9);
      }

      var1.a -= 1;
    }

    return true;
  }
}