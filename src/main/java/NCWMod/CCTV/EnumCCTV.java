package NCWMod.CCTV;

public enum EnumCCTV
{
  public final String title;
  public final int sizeX;
  public final int sizeY;
  public final int camera;
  private static final EnumCCTV[] $VALUES = { TV1_1, TV2_1, TV2_2, TV3_2, TV3_3, TV_Custom };

  private EnumCCTV(String var1, int var2, String var3, int var4, int var5, int var6)
  {
    this.title = var3;
    this.sizeX = var5;
    this.sizeY = var6;
    this.camera = 0;
  }
}