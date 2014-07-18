package NCWMod.CCTV;

import java.util.Random;
import org.lwjgl.opengl.GL11;

public class RenderCCTV extends um
{
  private Random rand = new Random();

  public void func_158_a(EntityCCTV var1, double var2, double var4, double var6, float var8, float var9)
  {
    this.rand.setSeed(187L);
    GL11.glPushMatrix();
    GL11.glTranslatef((float)var2, (float)var4, (float)var6);
    GL11.glRotatef(var8, 0.0F, 1.0F, 0.0F);
    GL11.glEnable(32826);
    a(mod_CCTV.TextureName);
    EnumCCTV var10 = var1.screen;
    float var11 = 0.0625F;
    GL11.glScalef(var11, var11, var11);
    func_159_a(var1, var10.sizeX, var10.sizeY);
    GL11.glDisable(32826);
    GL11.glPopMatrix();
  }

  private void func_159_a(EntityCCTV var1, int var2, int var3) {
    int var4 = var1.camera;
    if ((var4 != 13) && (mod_CCTV.cctvRegistered[var4] == 0)) {
      var1.camera = mod_CCTV.SelectNewCam(var4);
      var4 = var1.camera;
    }

    byte var5 = 4;
    float var6 = -var2 / 2.0F;
    float var7 = -var3 / 2.0F;
    float var8 = -2.0F;
    float var9 = 1.0F;
    GL11.glColor3f(1.0F, 1.0F, 1.0F);
    float var10 = 0.75F;
    float var11 = 0.8125F;
    float var12 = 1.0F;
    float var13 = 0.0F;
    float var14 = 0.0625F;
    float var15 = 0.25F;
    adz var16 = adz.a;
    var16.b();
    var16.b(0.0F, 1.0F, 0.0F);

    if (var4 == 14) {
      float var18 = var6 + var2;
      float var20 = var7 + var3;
      float var21 = 0.75F;
      float var22 = 1.0F;
      float var23 = 0.25F;
      float var24 = 0.5F;
      var16.a(var6, var20, var8, var21, var23);
      var16.a(var18, var20, var8, var22, var23);
      var16.a(var18, var7, var8, var22, var24);
      var16.a(var6, var7, var8, var21, var24);
    } else {
      for (int var17 = 0; var17 < 16; var17++) {
        int var31 = mod_CCTV.cctvIndex[var4][var17];
        int var19 = (var31 & 0xF) << 4;
        int var32 = var31 & 0xF0;
        float var21 = var17 / var5;
        float var22 = var17 % var5;
        float var23 = var6 + var2 / var5 * var22;
        float var24 = var6 + var2 / var5 * (var22 + 1.0F);
        float var25 = var7 + var3 / var5 * var21;
        float var26 = var7 + var3 / var5 * (var21 + 1.0F);
        float var27 = (var19 + 16) / 256.0F;
        float var28 = var19 / 256.0F;
        float var29 = (var32 + 16) / 256.0F;
        float var30 = var32 / 256.0F;
        var16.a(var23, var26, var8, var27, var29);
        var16.a(var24, var26, var8, var28, var29);
        var16.a(var24, var25, var8, var28, var30);
        var16.a(var23, var25, var8, var27, var30);
      }
    }

    var16.a();
    var16.b();
    func_160_a(var1, 0.0F, 0.0F);
    var16.b(0.0F, 0.0F, 1.0F);
    var16.a(var6, var7, var9, var10, var13);
    var16.a(var6 + var2, var7, var9, var12, var13);
    var16.a(var6 + var2, var7 + var3, var9, var12, var15);
    var16.a(var6, var7 + var3, var9, var10, var15);
    var16.b(0.0F, -1.0F, 0.0F);
    var16.a(var6, var7, var8, var10, var13);
    var16.a(var6 + var2, var7, var8, var12, var13);
    var16.a(var6 + var2, var7, var9, var12, var14);
    var16.a(var6, var7, var9, var10, var14);
    var16.b(0.0F, 1.0F, 0.0F);
    var16.a(var6, var7 + var3, var9, var10, var13);
    var16.a(var6 + var2, var7 + var3, var9, var12, var13);
    var16.a(var6 + var2, var7 + var3, var8, var12, var14);
    var16.a(var6, var7 + var3, var8, var10, var14);
    var16.b(-1.0F, 0.0F, 0.0F);
    var16.a(var6, var7, var9, var11, var13);
    var16.a(var6, var7 + var3, var9, var11, var15);
    var16.a(var6, var7 + var3, var8, var10, var15);
    var16.a(var6, var7, var8, var10, var13);
    var16.b(1.0F, 0.0F, 0.0F);
    var16.a(var6 + var2, var7, var8, var11, var13);
    var16.a(var6 + var2, var7 + var3, var8, var11, var15);
    var16.a(var6 + var2, var7 + var3, var9, var10, var15);
    var16.a(var6 + var2, var7, var9, var10, var13);
    var16.a();
  }

  private void func_160_a(EntityCCTV var1, float var2, float var3) {
    int var4 = gk.c(var1.o);
    int var5 = gk.c(var1.p + var3 / 16.0F);
    int var6 = gk.c(var1.q);
    if (var1.direction == 0) {
      var4 = gk.c(var1.o + var2 / 16.0F);
    }

    if (var1.direction == 1) {
      var6 = gk.c(var1.q - var2 / 16.0F);
    }

    if (var1.direction == 2) {
      var4 = gk.c(var1.o - var2 / 16.0F);
    }

    if (var1.direction == 3) {
      var6 = gk.c(var1.q + var2 / 16.0F);
    }

    float var7 = this.e.g.c(var4, var5, var6);
    GL11.glColor3f(var7, var7, var7);
  }

  public void a(nn var1, double var2, double var4, double var6, float var8, float var9) {
    func_158_a((EntityCCTV)var1, var2, var4, var6, var8, var9);
  }
}