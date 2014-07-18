package NCWMod.CCTV;

import java.util.Random;

public class BlockCCTVCamera extends pb
{
  public BlockCCTVCamera(int var1)
  {
    super(var1, 74, acn.d);
  }

  public int d(ali var1, int var2, int var3, int var4, int var5) {
    return var5 == var1.e(var2, var3, var4) ? mod_CCTV.CCTVFrontTexture : this.bN;
  }

  public int a_(int var1) {
    return var1 == 3 ? mod_CCTV.CCTVFrontTexture : this.bN;
  }

  public boolean e(xd var1, int var2, int var3, int var4) {
    if (!mod_CCTV.CanRegisterCCTV()) {
      return false;
    }
    int var5 = var1.a(var2, var3, var4);
    return (var5 == 0) || (m[var5].cd.i());
  }

  public void a(xd var1, int var2, int var3, int var4, acq var5)
  {
    int var6 = gk.c(var5.u * 4.0F / 360.0F + 0.5D) & 0x3;
    switch (var6) {
    case 0:
      var1.f(var2, var3, var4, 3);
      mod_CCTV.RegisterCCTV(var2, var3, var4, 0.0D, 0.0F, 0.0F, 1.0F);
      break;
    case 1:
      var1.f(var2, var3, var4, 4);
      mod_CCTV.RegisterCCTV(var2, var3, var4, 90.0D, -1.0F, 0.0F, 0.0F);
      break;
    case 2:
      var1.f(var2, var3, var4, 2);
      mod_CCTV.RegisterCCTV(var2, var3, var4, 180.0D, 0.0F, 0.0F, -1.0F);
      break;
    case 3:
      var1.f(var2, var3, var4, 5);
      mod_CCTV.RegisterCCTV(var2, var3, var4, 270.0D, 1.0F, 0.0F, 0.0F);
    }
  }

  public void b(xd var1, int var2, int var3, int var4, Random var5)
  {
    if (mod_CCTV.IsRegistered(var2, var3, var4) == -1) {
      int var6 = var1.e(var2, var3, var4);
      switch (var6) {
      case 2:
        mod_CCTV.RegisterCCTV(var2, var3, var4, 180.0D, 0.0F, 0.0F, -1.0F);
        break;
      case 3:
        mod_CCTV.RegisterCCTV(var2, var3, var4, 0.0D, 0.0F, 0.0F, 1.0F);
        break;
      case 4:
        mod_CCTV.RegisterCCTV(var2, var3, var4, 90.0D, -1.0F, 0.0F, 0.0F);
        break;
      case 5:
        mod_CCTV.RegisterCCTV(var2, var3, var4, 270.0D, 1.0F, 0.0F, 0.0F);
      }
    }
  }

  public void b_(xd var1, int var2, int var3, int var4)
  {
    super.b_(var1, var2, var3, var4);
    mod_CCTV.UnregisterCCTV(var2, var3, var4);
  }
}