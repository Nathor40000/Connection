package NCWMod.CCTV;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EntityCCTV extends nn
{
  private int tickCounter1;
  public int direction;
  public int xPosition;
  public int yPosition;
  public int zPosition;
  public int camerax;
  public int cameray;
  public int cameraz;
  public int camera;
  public EnumCCTV screen;

  public EntityCCTV(xd var1)
  {
    super(var1);
    this.tickCounter1 = 0;
    this.direction = 0;
    this.H = 0.0F;
    a(0.5F, 0.5F);
  }

  public EntityCCTV(xd var1, int var2, int var3, int var4, int var5) {
    this(var1);
    this.xPosition = var2;
    this.yPosition = var3;
    this.zPosition = var4;
    ArrayList var6 = new ArrayList();
    EnumCCTV[] var7 = EnumCCTV.values();
    int var8 = var7.length;

    for (int var9 = 0; var9 < var8; var9++) {
      EnumCCTV var10 = var7[var9];
      this.screen = var10;
      func_412_b(var5);
      if (canStay()) {
        var6.add(var10);
      }
    }

    if (var6.size() > 0) {
      this.screen = ((EnumCCTV)var6.get(this.U.nextInt(var6.size())));
    }

    func_412_b(var5);
  }

  public void setEntityDead() {
    mod_CCTV.unwatchCam(this.camera);
    this.G = true;
  }

  public EntityCCTV(xd var1, int var2, int var3, int var4, int var5, String var6) {
    this(var1);
    this.xPosition = var2;
    this.yPosition = var3;
    this.zPosition = var4;
    EnumCCTV[] var7 = EnumCCTV.values();
    int var8 = var7.length;

    for (int var9 = 0; var9 < var8; var9++) {
      EnumCCTV var10 = var7[var9];
      if (var10.title.equals(var6)) {
        this.screen = var10;
        break;
      }
    }

    func_412_b(var5);
  }

  public boolean c(yw var1) {
    this.camera = mod_CCTV.SelectNewCam(this.camera);
    return true;
  }

  protected void b() {
    this.camera = mod_CCTV.defaultCam;
    mod_CCTV.watchCam(this.camera);
  }

  public void func_412_b(int var1) {
    this.direction = var1;
    this.w = (this.u = var1 * 90);
    float var2 = this.screen.sizeX;
    float var3 = this.screen.sizeY;
    float var4 = this.screen.sizeX;
    if ((var1 != 0) && (var1 != 2))
      var2 = 0.5F;
    else {
      var4 = 0.5F;
    }

    var2 /= 32.0F;
    var3 /= 32.0F;
    var4 /= 32.0F;
    float var5 = this.xPosition + 0.5F;
    float var6 = this.yPosition + 0.5F;
    float var7 = this.zPosition + 0.5F;
    float var8 = 0.5625F;
    if (var1 == 0) {
      var7 -= var8;
    }

    if (var1 == 1) {
      var5 -= var8;
    }

    if (var1 == 2) {
      var7 += var8;
    }

    if (var1 == 3) {
      var5 += var8;
    }

    if (var1 == 0) {
      var5 -= func_411_c(this.screen.sizeX);
    }

    if (var1 == 1) {
      var7 += func_411_c(this.screen.sizeX);
    }

    if (var1 == 2) {
      var5 += func_411_c(this.screen.sizeX);
    }

    if (var1 == 3) {
      var7 -= func_411_c(this.screen.sizeX);
    }

    var6 += func_411_c(this.screen.sizeY);
    d(var5, var6, var7);
    float var9 = -0.00625F;
    this.y.c(var5 - var2 - var9, var6 - var3 - var9, var7 - var4 - var9, var5 + var2 + var9, var6 + var3 + var9, var7 + var4 + var9);
  }

  private float func_411_c(int var1) {
    return var1 != 64 ? 0.0F : var1 == 32 ? 0.5F : 0.5F;
  }

  public void J_() {
    if ((this.tickCounter1++ == 100) && (!this.k.F)) {
      this.tickCounter1 = 0;
      if (!canStay()) {
        setEntityDead();
        this.k.a(new fq(this.k, this.o, this.p, this.q, new aan(mod_CCTV.cctv)));
      }
    }
  }

  public boolean canStay()
  {
    if (this.k.a(this, this.y).size() > 0) {
      return false;
    }
    int var1 = this.screen.sizeX / 16;
    int var2 = this.screen.sizeY / 16;
    int var3 = this.xPosition;
    int var4 = this.yPosition;
    int var5 = this.zPosition;
    if (this.direction == 0) {
      var3 = gk.c(this.o - this.screen.sizeX / 32.0F);
    }

    if (this.direction == 1) {
      var5 = gk.c(this.q - this.screen.sizeX / 32.0F);
    }

    if (this.direction == 2) {
      var3 = gk.c(this.o - this.screen.sizeX / 32.0F);
    }

    if (this.direction == 3) {
      var5 = gk.c(this.q - this.screen.sizeX / 32.0F);
    }

    var4 = gk.c(this.p - this.screen.sizeY / 32.0F);

    for (int var6 = 0; var6 < var1; var6++) {
      for (int var7 = 0; var7 < var2; var7++)
      {
        acn var8;
        acn var8;
        if ((this.direction != 0) && (this.direction != 2))
          var8 = this.k.f(this.xPosition, var4 + var7, var5 + var6);
        else {
          var8 = this.k.f(var3 + var6, var4 + var7, this.zPosition);
        }

        if (!var8.a()) {
          return false;
        }
      }
    }

    List var9 = this.k.b(this, this.y);

    for (int var7 = 0; var7 < var9.size(); var7++) {
      if ((var9.get(var7) instanceof EntityCCTV)) {
        return false;
      }
    }

    return true;
  }

  public boolean l_()
  {
    return true;
  }

  public boolean a(md var1, int var2) {
    if ((!this.G) && (!this.k.F)) {
      setEntityDead();
      K();
      this.k.a(new fq(this.k, this.o, this.p, this.q, new aan(mod_CCTV.cctv)));
    }

    return true;
  }

  public void b(ady var1) {
    var1.a("Dir", (byte)this.direction);
    var1.a("Motive", this.screen.title);
    var1.a("TileX", this.xPosition);
    var1.a("TileY", this.yPosition);
    var1.a("TileZ", this.zPosition);
    var1.a("CameraX", this.camerax);
    var1.a("CameraY", this.cameray);
    var1.a("CameraZ", this.cameraz);
  }

  public void a(ady var1) {
    this.direction = var1.d("Dir");
    this.xPosition = var1.f("TileX");
    this.yPosition = var1.f("TileY");
    this.zPosition = var1.f("TileZ");
    this.camerax = var1.f("CameraX");
    this.cameray = var1.f("CameraY");
    this.cameraz = var1.f("CameraZ");
    this.camera = mod_CCTV.IsRegistered(this.camerax, this.cameray, this.cameraz);
    if (this.camera == -1) {
      this.camera = mod_CCTV.defaultCam;
    }

    String var2 = var1.j("Motive");
    EnumCCTV[] var3 = EnumCCTV.values();
    int var4 = var3.length;

    for (int var5 = 0; var5 < var4; var5++) {
      EnumCCTV var6 = var3[var5];
      if (var6.title.equals(var2)) {
        this.screen = var6;
      }
    }

    if (this.screen == null) {
      this.screen = EnumCCTV.TV1_1;
    }

    func_412_b(this.direction);
  }

  public void b(double var1, double var3, double var5) {
    if ((!this.k.F) && (var1 * var1 + var3 * var3 + var5 * var5 > 0.0D)) {
      setEntityDead();
      this.k.a(new fq(this.k, this.o, this.p, this.q, new aan(mod_CCTV.cctv)));
    }
  }

  public void c(double var1, double var3, double var5)
  {
    if ((!this.k.F) && (var1 * var1 + var3 * var3 + var5 * var5 > 0.0D)) {
      setEntityDead();
      this.k.a(new fq(this.k, this.o, this.p, this.q, new aan(yr.as)));
    }
  }
}