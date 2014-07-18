package NCWMod.CCTV;

import java.awt.image.BufferedImage;
import java.nio.FloatBuffer;
import java.util.List;
import java.util.Random;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.ContextCapabilities;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.GLU;

public class EntityExtensibleRenderer extends lr
{
  public static boolean anaglyphEnable = false;
  public static int anaglyphField;
  private Minecraft mc;
  private float farPlaneDistance = 0.0F;
  public mn c;
  private int rendererUpdateCount;
  private nn pointedEntity = null;
  private ch mouseFilterXAxis = new ch();
  private ch mouseFilterYAxis = new ch();
  private ch mouseFilterDummy1 = new ch();
  private ch mouseFilterDummy2 = new ch();
  private ch mouseFilterDummy3 = new ch();
  private ch mouseFilterDummy4 = new ch();
  private float thirdPersonDistance = 4.0F;
  private float thirdPersonDistanceTemp = 4.0F;
  private float debugCamYaw = 0.0F;
  private float prevDebugCamYaw = 0.0F;
  private float debugCamPitch = 0.0F;
  private float prevDebugCamPitch = 0.0F;
  private float smoothCamYaw;
  private float smoothCamPitch;
  private float smoothCamFilterX;
  private float smoothCamFilterY;
  private float smoothCamPartialTicks;
  private float debugCamFOV = 0.0F;
  private float prevDebugCamFOV = 0.0F;
  private float camRoll = 0.0F;
  private float prevCamRoll = 0.0F;
  public int emptyTexture;
  private int[] lightmapColors;
  private float fovModifierHand;
  private float fovModifierHandPrev;
  private float fovMultiplierTemp;
  private boolean cloudFog = false;
  private double cameraZoom = 1.0D;
  private double cameraYaw = 0.0D;
  private double cameraPitch = 0.0D;
  private long prevRenderCamTime;
  private long prevFrameTime = System.currentTimeMillis();
  private long renderEndNanoTime = 0L;
  private boolean lightmapUpdateNeeded = false;
  float e = 0.0F;
  float f = 0.0F;
  float g = 0.0F;
  float h = 0.0F;
  private Random random = new Random();
  private int rainSoundCounter = 0;
  float[] i;
  float[] j;
  volatile int unusedVolatile0 = 0;
  volatile int unusedVolatile1 = 0;
  FloatBuffer m = ew.e(16);
  float n;
  float o;
  float p;
  private float fogColor2;
  private float fogColor1;
  public int q;
  public boolean pip;
  public static int texTileSize;
  private int currentCam;

  public EntityExtensibleRenderer(Minecraft var1)
  {
    super(var1);
    this.mc = var1;
    this.c = new mn(var1);
    this.emptyTexture = var1.p.a(new BufferedImage(16, 16, 1));
    this.lightmapColors = new int[256];
    this.pip = false;
    this.mc.u = this;
    texTileSize = mod_CCTV.texTileSize;
  }

  public void a() {
    updateFovModifierHand();
    updateTorchFlicker();
    this.fogColor2 = this.fogColor1;
    this.thirdPersonDistanceTemp = this.thirdPersonDistance;
    this.prevDebugCamYaw = this.debugCamYaw;
    this.prevDebugCamPitch = this.debugCamPitch;
    this.prevDebugCamFOV = this.debugCamFOV;
    this.prevCamRoll = this.camRoll;

    if (this.mc.A.J) {
      float var1 = this.mc.A.c * 0.6F + 0.2F;
      float var2 = var1 * var1 * var1 * 8.0F;
      this.smoothCamFilterX = this.mouseFilterXAxis.a(this.smoothCamYaw, 0.05F * var2);
      this.smoothCamFilterY = this.mouseFilterYAxis.a(this.smoothCamPitch, 0.05F * var2);
      this.smoothCamPartialTicks = 0.0F;
      this.smoothCamYaw = 0.0F;
      this.smoothCamPitch = 0.0F;
    }

    if (this.mc.i == null) {
      this.mc.i = this.mc.h;
    }

    float var1 = this.mc.f.c(gk.c(this.mc.i.o), gk.c(this.mc.i.p), gk.c(this.mc.i.q));
    float var2 = (3 - this.mc.A.e) / 3.0F;
    float var3 = var1 * (1.0F - var2) + var2;
    this.fogColor1 += (var3 - this.fogColor1) * 0.1F;
    this.rendererUpdateCount += 1;
    this.c.a();
    addRainParticles();
  }

  public void a(float var1) {
    if ((this.mc.i != null) && 
      (this.mc.f != null)) {
      double var2 = this.mc.c.b();
      this.mc.z = this.mc.i.a(var2, var1);
      double var4 = var2;
      bo var6 = this.mc.i.j(var1);
      if (this.mc.z != null) {
        var4 = this.mc.z.f.d(var6);
      }

      if (this.mc.c.i()) {
        var2 = 6.0D;
        var4 = 6.0D;
      } else {
        if (var4 > 3.0D) {
          var4 = 3.0D;
        }

        var2 = var4;
      }

      bo var7 = this.mc.i.k(var1);
      bo var8 = var6.c(var7.a * var2, var7.b * var2, var7.c * var2);
      this.pointedEntity = null;
      float var9 = 1.0F;
      List var10 = this.mc.f.b(this.mc.i, this.mc.i.y.a(var7.a * var2, var7.b * var2, var7.c * var2).b(var9, var9, var9));
      double var11 = 0.0D;

      for (int var13 = 0; var13 < var10.size(); var13++) {
        nn var14 = (nn)var10.get(var13);
        if (var14.l_()) {
          float var15 = var14.j_();
          wu var16 = var14.y.b(var15, var15, var15);
          pl var17 = var16.a(var6, var8);
          if (var16.a(var6)) {
            if ((0.0D < var11) || (var11 == 0.0D)) {
              this.pointedEntity = var14;
              var11 = 0.0D;
            }
          } else if (var17 != null) {
            double var18 = var6.d(var17.f);
            if ((var18 < var11) || (var11 == 0.0D)) {
              this.pointedEntity = var14;
              var11 = var18;
            }
          }
        }
      }

      if (this.pointedEntity != null)
        this.mc.z = new pl(this.pointedEntity);
    }
  }

  private void updateFovModifierHand()
  {
    vq var1 = (vq)this.mc.i;
    this.fovMultiplierTemp = var1.I_();
    this.fovModifierHandPrev = this.fovModifierHand;
    this.fovModifierHand += (this.fovMultiplierTemp - this.fovModifierHand) * 0.5F;
  }

  private float getFOVModifier(float var1, boolean var2) {
    if (this.q > 0) {
      return 90.0F;
    }
    yw var3 = (yw)this.mc.i;
    float var4 = 70.0F;
    if (var2) {
      var4 += this.mc.A.N * 40.0F;
      var4 *= (this.fovModifierHandPrev + (this.fovModifierHand - this.fovModifierHandPrev) * var1);
    }

    if (var3.bb() <= 0) {
      float var5 = var3.bD + var1;
      var4 /= ((1.0F - 500.0F / (var5 + 500.0F)) * 2.0F + 1.0F);
    }

    int var6 = aau.a(this.mc.f, var3, var1);
    if ((var6 != 0) && (pb.m[var6].cd == acn.g)) {
      var4 = var4 * 60.0F / 70.0F;
    }

    return var4 + this.prevDebugCamFOV + (this.debugCamFOV - this.prevDebugCamFOV) * var1;
  }

  private void hurtCameraEffect(float var1)
  {
    acq var2 = this.mc.i;
    float var3 = var2.bA - var1;

    if (var2.bb() <= 0) {
      float var4 = var2.bD + var1;
      GL11.glRotatef(40.0F - 8000.0F / (var4 + 200.0F), 0.0F, 0.0F, 1.0F);
    }

    if (var3 >= 0.0F) {
      var3 /= var2.bB;
      var3 = gk.a(var3 * var3 * var3 * var3 * 3.141593F);
      float var4 = var2.bC;
      GL11.glRotatef(-var4, 0.0F, 1.0F, 0.0F);
      GL11.glRotatef(-var3 * 14.0F, 0.0F, 0.0F, 1.0F);
      GL11.glRotatef(var4, 0.0F, 1.0F, 0.0F);
    }
  }

  private void setupViewBobbing(float var1) {
    if ((this.mc.i instanceof yw)) {
      yw var2 = (yw)this.mc.i;
      float var3 = var2.L - var2.K;
      float var4 = -(var2.L + var3 * var1);
      float var5 = var2.aw + (var2.ax - var2.aw) * var1;
      float var6 = var2.bF + (var2.bG - var2.bF) * var1;
      GL11.glTranslatef(gk.a(var4 * 3.141593F) * var5 * 0.5F, -Math.abs(gk.b(var4 * 3.141593F) * var5), 0.0F);
      GL11.glRotatef(gk.a(var4 * 3.141593F) * var5 * 3.0F, 0.0F, 0.0F, 1.0F);
      GL11.glRotatef(Math.abs(gk.b(var4 * 3.141593F - 0.2F) * var5) * 5.0F, 1.0F, 0.0F, 0.0F);
      GL11.glRotatef(var6, 1.0F, 0.0F, 0.0F);
    }
  }

  private void orientCamera(float var1) {
    acq var2 = this.mc.i;
    float var3 = var2.H - 1.62F;
    double var4 = var2.l + (var2.o - var2.l) * var1;
    double var6 = var2.m + (var2.p - var2.m) * var1 - var3;
    double var8 = var2.n + (var2.q - var2.n) * var1;
    GL11.glRotatef(this.prevCamRoll + (this.camRoll - this.prevCamRoll) * var1, 0.0F, 0.0F, 1.0F);
    if (var2.az()) {
      var3 = (float)(var3 + 1.0D);
      GL11.glTranslatef(0.0F, 0.3F, 0.0F);
      if (!this.mc.A.K) {
        int var10 = this.mc.f.a(gk.c(var2.o), gk.c(var2.p), gk.c(var2.q));
        if (var10 == pb.S.bO) {
          int var11 = this.mc.f.e(gk.c(var2.o), gk.c(var2.p), gk.c(var2.q));
          int var12 = var11 & 0x3;
          GL11.glRotatef(var12 * 90, 0.0F, 1.0F, 0.0F);
        }

        GL11.glRotatef(var2.w + (var2.u - var2.w) * var1 + 180.0F, 0.0F, -1.0F, 0.0F);
        GL11.glRotatef(var2.x + (var2.v - var2.x) * var1, -1.0F, 0.0F, 0.0F);
      }
    } else if ((this.mc.A.E > 0) && (!this.pip)) {
      double var27 = this.thirdPersonDistanceTemp + (this.thirdPersonDistance - this.thirdPersonDistanceTemp) * var1;

      if (this.mc.A.K) {
        float var28 = this.prevDebugCamYaw + (this.debugCamYaw - this.prevDebugCamYaw) * var1;
        float var13 = this.prevDebugCamPitch + (this.debugCamPitch - this.prevDebugCamPitch) * var1;
        GL11.glTranslatef(0.0F, 0.0F, (float)-var27);
        GL11.glRotatef(var13, 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(var28, 0.0F, 1.0F, 0.0F);
      } else {
        float var28 = var2.u;
        float var13 = var2.v;
        if (this.mc.A.E == 2) {
          var13 += 180.0F;
        }

        double var14 = -gk.a(var28 / 180.0F * 3.141593F) * gk.b(var13 / 180.0F * 3.141593F) * var27;
        double var16 = gk.b(var28 / 180.0F * 3.141593F) * gk.b(var13 / 180.0F * 3.141593F) * var27;
        double var18 = -gk.a(var13 / 180.0F * 3.141593F) * var27;

        for (int var20 = 0; var20 < 8; var20++) {
          float var21 = (var20 & 0x1) * 2 - 1;
          float var22 = (var20 >> 1 & 0x1) * 2 - 1;
          float var23 = (var20 >> 2 & 0x1) * 2 - 1;
          var21 *= 0.1F;
          var22 *= 0.1F;
          var23 *= 0.1F;
          pl var24 = this.mc.f.a(bo.b(var4 + var21, var6 + var22, var8 + var23), bo.b(var4 - var14 + var21 + var23, var6 - var18 + var22, var8 - var16 + var23));
          if (var24 != null) {
            double var25 = var24.f.d(bo.b(var4, var6, var8));
            if (var25 < var27) {
              var27 = var25;
            }
          }
        }

        if (this.mc.A.E == 2) {
          GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
        }

        GL11.glRotatef(var2.v - var13, 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(var2.u - var28, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(0.0F, 0.0F, (float)-var27);
        GL11.glRotatef(var28 - var2.u, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(var13 - var2.v, 1.0F, 0.0F, 0.0F);
      }
    } else {
      GL11.glTranslatef(0.0F, 0.0F, -0.1F);
    }

    if (!this.mc.A.K) {
      if ((this.pip) && (mod_CCTV.cctvRegistered[this.currentCam] != 0)) {
        GL11.glRotatef((float)mod_CCTV.cctvPitch[this.currentCam], 1.0F, 0.0F, 0.0F);
        GL11.glRotatef((float)mod_CCTV.cctvYaw[this.currentCam] + 180.0F, 0.0F, 1.0F, 0.0F);
      } else {
        GL11.glRotatef(var2.x + (var2.v - var2.x) * var1, 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(var2.w + (var2.u - var2.w) * var1 + 180.0F, 0.0F, 1.0F, 0.0F);
      }
    }

    if ((this.pip) && (mod_CCTV.cctvRegistered[this.currentCam] != 0)) {
      GL11.glTranslatef((float)-(mod_CCTV.cctvX[this.currentCam] - var4), (float)-(mod_CCTV.cctvY[this.currentCam] - var6), (float)-(mod_CCTV.cctvZ[this.currentCam] - var8));
      GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
    } else {
      GL11.glTranslatef(0.0F, var3, 0.0F);
    }

    var4 = var2.l + (var2.o - var2.l) * var1;
    var6 = var2.m + (var2.p - var2.m) * var1 - var3;
    var8 = var2.n + (var2.q - var2.n) * var1;
    this.cloudFog = this.mc.g.a(var4, var6, var8, var1);
  }

  private void setupCameraTransform(float var1, int var2) {
    if (!this.pip)
      this.farPlaneDistance = (256 >> this.mc.A.e);
    else {
      this.farPlaneDistance = mod_CCTV.fieldDepth;
    }

    GL11.glMatrixMode(5889);
    GL11.glLoadIdentity();
    float var3 = 0.07F;
    if ((!this.pip) && (this.mc.A.g)) {
      GL11.glTranslatef(-(var2 * 2 - 1) * var3, 0.0F, 0.0F);
    }

    if (this.cameraZoom != 1.0D) {
      GL11.glTranslatef((float)this.cameraYaw, (float)-this.cameraPitch, 0.0F);
      GL11.glScaled(this.cameraZoom, this.cameraZoom, 1.0D);
      GLU.gluPerspective(getFOVModifier(var1, true), this.mc.d / this.mc.e, 0.05F, this.farPlaneDistance * 2.0F);
    } else {
      GLU.gluPerspective(getFOVModifier(var1, true), this.mc.d / this.mc.e, 0.05F, this.farPlaneDistance * 2.0F);
    }

    if (this.mc.c.e()) {
      float var4 = 0.6666667F;
      GL11.glScalef(1.0F, var4, 1.0F);
    }

    GL11.glMatrixMode(5888);
    GL11.glLoadIdentity();
    if (!this.pip) {
      if (this.mc.A.g) {
        GL11.glTranslatef((var2 * 2 - 1) * 0.1F, 0.0F, 0.0F);
      }

      hurtCameraEffect(var1);
      if (this.mc.A.f) {
        setupViewBobbing(var1);
      }

      float var4 = this.mc.h.aS + (this.mc.h.aR - this.mc.h.aS) * var1;
      if (var4 > 0.0F) {
        byte var5 = 20;
        if (this.mc.h.a(aad.k)) {
          var5 = 7;
        }

        float var6 = 5.0F / (var4 * var4 + 5.0F) - var4 * 0.04F;
        var6 *= var6;
        GL11.glRotatef((this.rendererUpdateCount + var1) * var5, 0.0F, 1.0F, 1.0F);
        GL11.glScalef(1.0F / var6, 1.0F, 1.0F);
        GL11.glRotatef(-(this.rendererUpdateCount + var1) * var5, 0.0F, 1.0F, 1.0F);
      }
    }

    orientCamera(var1);
    if (this.q > 0) {
      int var7 = this.q - 1;
      if (var7 == 1) {
        GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
      }

      if (var7 == 2) {
        GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
      }

      if (var7 == 3) {
        GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
      }

      if (var7 == 4) {
        GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
      }

      if (var7 == 5)
        GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
    }
  }

  private void renderHand(float var1, int var2)
  {
    if (this.q <= 0) {
      GL11.glMatrixMode(5889);
      GL11.glLoadIdentity();
      float var3 = 0.07F;
      if (this.mc.A.g) {
        GL11.glTranslatef(-(var2 * 2 - 1) * var3, 0.0F, 0.0F);
      }

      if (this.cameraZoom != 1.0D) {
        GL11.glTranslatef((float)this.cameraYaw, (float)-this.cameraPitch, 0.0F);
        GL11.glScaled(this.cameraZoom, this.cameraZoom, 1.0D);
        GLU.gluPerspective(getFOVModifier(var1, false), this.mc.d / this.mc.e, 0.05F, this.farPlaneDistance * 2.0F);
      } else {
        GLU.gluPerspective(getFOVModifier(var1, false), this.mc.d / this.mc.e, 0.05F, this.farPlaneDistance * 2.0F);
      }

      if (this.mc.c.e()) {
        float var4 = 0.6666667F;
        GL11.glScalef(1.0F, var4, 1.0F);
      }

      GL11.glMatrixMode(5888);
      GL11.glLoadIdentity();
      if (this.mc.A.g) {
        GL11.glTranslatef((var2 * 2 - 1) * 0.1F, 0.0F, 0.0F);
      }

      GL11.glPushMatrix();
      hurtCameraEffect(var1);
      if (this.mc.A.f) {
        setupViewBobbing(var1);
      }

      if ((this.mc.A.E == 0) && (!this.mc.i.az()) && (!this.mc.A.D) && (!this.mc.c.e())) {
        b(var1);
        this.c.a(var1);
        a(var1);
      }

      GL11.glPopMatrix();
      if ((this.mc.A.E == 0) && (!this.mc.i.az())) {
        this.c.b(var1);
        hurtCameraEffect(var1);
      }

      if (this.mc.A.f)
        setupViewBobbing(var1);
    }
  }

  public void a(double var1)
  {
    es.a(es.b);
    GL11.glDisable(3553);
    es.a(es.a);
  }

  public void b(double var1) {
    es.a(es.b);
    GL11.glMatrixMode(5890);
    GL11.glLoadIdentity();
    float var3 = 0.0039063F;
    GL11.glScalef(var3, var3, var3);
    GL11.glTranslatef(8.0F, 8.0F, 8.0F);
    GL11.glMatrixMode(5888);
    this.mc.p.b(this.emptyTexture);
    GL11.glTexParameteri(3553, 10241, 9729);
    GL11.glTexParameteri(3553, 10240, 9729);
    GL11.glTexParameteri(3553, 10241, 9729);
    GL11.glTexParameteri(3553, 10240, 9729);
    GL11.glTexParameteri(3553, 10242, 10496);
    GL11.glTexParameteri(3553, 10243, 10496);
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    GL11.glEnable(3553);
    es.a(es.a);
  }

  private void updateTorchFlicker() {
    this.f = ((float)(this.f + (Math.random() - Math.random()) * Math.random() * Math.random()));
    this.h = ((float)(this.h + (Math.random() - Math.random()) * Math.random() * Math.random()));
    this.f = ((float)(this.f * 0.9D));
    this.h = ((float)(this.h * 0.9D));
    this.e += (this.f - this.e) * 1.0F;
    this.g += (this.h - this.g) * 1.0F;
    this.lightmapUpdateNeeded = true;
  }

  private void updateLightmap() {
    xd var1 = this.mc.f;
    if (var1 != null) {
      for (int var2 = 0; var2 < 256; var2++) {
        float var3 = var1.b(1.0F) * 0.95F + 0.05F;
        float var4 = var1.t.f[(var2 / 16)] * var3;
        float var5 = var1.t.f[(var2 % 16)] * (this.e * 0.1F + 1.5F);
        if (var1.n > 0) {
          var4 = var1.t.f[(var2 / 16)];
        }

        float var6 = var4 * (var1.b(1.0F) * 0.65F + 0.35F);
        float var7 = var4 * (var1.b(1.0F) * 0.65F + 0.35F);
        float var10 = var5 * ((var5 * 0.6F + 0.4F) * 0.6F + 0.4F);
        float var11 = var5 * (var5 * var5 * 0.6F + 0.4F);
        float var12 = var6 + var5;
        float var13 = var7 + var10;
        float var14 = var4 + var11;
        var12 = var12 * 0.96F + 0.03F;
        var13 = var13 * 0.96F + 0.03F;
        var14 = var14 * 0.96F + 0.03F;
        if (var1.t.g == 1) {
          var12 = 0.22F + var5 * 0.75F;
          var13 = 0.28F + var10 * 0.75F;
          var14 = 0.25F + var11 * 0.75F;
        }

        float var15 = this.mc.A.O;
        if (var12 > 1.0F) {
          var12 = 1.0F;
        }

        if (var13 > 1.0F) {
          var13 = 1.0F;
        }

        if (var14 > 1.0F) {
          var14 = 1.0F;
        }

        float var16 = 1.0F - var12;
        float var17 = 1.0F - var13;
        float var18 = 1.0F - var14;
        var16 = 1.0F - var16 * var16 * var16 * var16;
        var17 = 1.0F - var17 * var17 * var17 * var17;
        var18 = 1.0F - var18 * var18 * var18 * var18;
        var12 = var12 * (1.0F - var15) + var16 * var15;
        var13 = var13 * (1.0F - var15) + var17 * var15;
        var14 = var14 * (1.0F - var15) + var18 * var15;
        var12 = var12 * 0.96F + 0.03F;
        var13 = var13 * 0.96F + 0.03F;
        var14 = var14 * 0.96F + 0.03F;
        if (var12 > 1.0F) {
          var12 = 1.0F;
        }

        if (var13 > 1.0F) {
          var13 = 1.0F;
        }

        if (var14 > 1.0F) {
          var14 = 1.0F;
        }

        if (var12 < 0.0F) {
          var12 = 0.0F;
        }

        if (var13 < 0.0F) {
          var13 = 0.0F;
        }

        if (var14 < 0.0F) {
          var14 = 0.0F;
        }

        short var19 = 255;
        int var20 = (int)(var12 * 255.0F);
        int var21 = (int)(var13 * 255.0F);
        int var22 = (int)(var14 * 255.0F);
        this.lightmapColors[var2] = (var19 << 24 | var20 << 16 | var21 << 8 | var22);
      }

      this.mc.p.a(this.lightmapColors, 16, 16, this.emptyTexture);
    }
  }

  private void copyDownsizedRender(aaw var1, int var2, int var3) {
    GL11.glBindTexture(3553, var1.b(mod_CCTV.TextureName));

    for (int var4 = 0; var4 < 16; var4++)
      GL11.glCopyTexSubImage2D(3553, 0, mod_CCTV.cctvIndex[this.currentCam][var4] % 16 * texTileSize, mod_CCTV.cctvIndex[this.currentCam][var4] / 16 * texTileSize, 3 * texTileSize - var4 % 4 * texTileSize, var3 - texTileSize - (3 - var4 / 4) * texTileSize, texTileSize, texTileSize);
  }

  public void b(float var1)
  {
    lv.a("lightTex");
    boolean var2 = false;
    if (System.currentTimeMillis() - this.prevRenderCamTime > mod_CCTV.camSpeed) {
      var2 = true;
      this.prevRenderCamTime = System.currentTimeMillis();
      this.currentCam = mod_CCTV.GetNextCam(this.currentCam);
    }

    if (this.lightmapUpdateNeeded) {
      updateLightmap();
    }

    lv.b();
    if (!Display.isActive()) {
      if (System.currentTimeMillis() - this.prevFrameTime > 500L)
        this.mc.i();
    }
    else {
      this.prevFrameTime = System.currentTimeMillis();
    }

    lv.a("mouse");
    if (this.mc.R) {
      this.mc.D.c();
      float var3 = this.mc.A.c * 0.6F + 0.2F;
      float var4 = var3 * var3 * var3 * 8.0F;
      float var5 = this.mc.D.a * var4;
      float var6 = this.mc.D.b * var4;
      byte var7 = 1;
      if (this.mc.A.d) {
        var7 = -1;
      }

      if (this.mc.A.J) {
        this.smoothCamYaw += var5;
        this.smoothCamPitch += var6;
        float var8 = var1 - this.smoothCamPartialTicks;
        this.smoothCamPartialTicks = var1;
        var5 = this.smoothCamFilterX * var8;
        var6 = this.smoothCamFilterY * var8;
        this.mc.h.c(var5, var6 * var7);
      } else {
        this.mc.h.c(var5, var6 * var7);
      }
    }

    lv.b();
    if (!this.mc.x) {
      anaglyphEnable = this.mc.A.g;
      agd var15 = new agd(this.mc.A, this.mc.d, this.mc.e);
      int var16 = var15.a();
      int var17 = var15.b();
      int var18 = Mouse.getX() * var16 / this.mc.d;
      int var20 = var17 - Mouse.getY() * var17 / this.mc.e - 1;
      short var19 = 200;
      if (this.mc.A.i == 1) {
        var19 = 120;
      }

      if (this.mc.A.i == 2) {
        var19 = 40;
      }

      if (this.mc.f != null) {
        this.pip = true;
        int var9 = this.mc.A.E;
        if ((this.currentCam != -1) && (var2)) {
          this.mc.A.E = 2;
          if (this.currentCam == 13) {
            this.mc.A.E = 0;
          }

          if (this.mc.A.i == 0)
            a(var1, 0L);
          else {
            a(var1, this.renderEndNanoTime + 1000000000 / var19);
          }

          copyDownsizedRender(this.mc.p, this.mc.d, this.mc.e);
        }

        this.mc.A.E = var9;
        this.pip = false;
        lv.a("level");
        if (this.mc.A.i == 0)
          a(var1, 0L);
        else {
          a(var1, this.renderEndNanoTime + 1000000000 / var19);
        }

        lv.c("sleep");
        if (this.mc.A.i == 2) {
          long var10 = (this.renderEndNanoTime + 1000000000 / var19 - System.nanoTime()) / 1000000L;
          if ((var10 > 0L) && (var10 < 500L)) {
            try {
              Thread.sleep(var10);
            } catch (InterruptedException var14) {
              var14.printStackTrace();
            }
          }
        }

        this.renderEndNanoTime = System.nanoTime();
        lv.c("gui");
        if ((!this.mc.A.D) || (this.mc.s != null)) {
          this.mc.w.a(var1, this.mc.s != null, var18, var20);
        }

        lv.b();
      } else {
        GL11.glViewport(0, 0, this.mc.d, this.mc.e);
        GL11.glMatrixMode(5889);
        GL11.glLoadIdentity();
        GL11.glMatrixMode(5888);
        GL11.glLoadIdentity();
        b();
        if (this.mc.A.i == 2) {
          long var21 = (this.renderEndNanoTime + 1000000000 / var19 - System.nanoTime()) / 1000000L;
          if (var21 < 0L) {
            var21 += 10L;
          }

          if ((var21 > 0L) && (var21 < 500L)) {
            try {
              Thread.sleep(var21);
            } catch (InterruptedException var13) {
              var13.printStackTrace();
            }
          }
        }

        this.renderEndNanoTime = System.nanoTime();
      }

      if (this.mc.s != null) {
        GL11.glClear(256);
        this.mc.s.a(var18, var20, var1);
        if ((this.mc.s != null) && (this.mc.s.v != null))
          this.mc.s.v.a(var1);
      }
    }
  }

  public void a(float var1, long var2)
  {
    lv.a("lightTex");
    if (this.lightmapUpdateNeeded) {
      updateLightmap();
    }

    GL11.glEnable(2884);
    GL11.glEnable(2929);
    if (this.mc.i == null) {
      this.mc.i = this.mc.h;
    }

    lv.c("pick");
    a(var1);
    acq var4 = this.mc.i;
    l var5 = this.mc.g;
    cw var6 = this.mc.j;
    double var7 = var4.N + (var4.o - var4.N) * var1;
    double var9 = var4.O + (var4.p - var4.O) * var1;
    double var11 = var4.P + (var4.q - var4.P) * var1;
    lv.c("center");
    ca var13 = this.mc.f.z();

    if ((var13 instanceof hy)) {
      hy var14 = (hy)var13;
      int var15 = gk.d((int)var7) >> 4;
      int var16 = gk.d((int)var11) >> 4;
      var14.d(var15, var16);
    }

    for (int var19 = 0; var19 < 2; var19++) {
      if ((this.mc.A.g) && (!this.pip)) {
        anaglyphField = var19;
        if (anaglyphField == 0)
          GL11.glColorMask(false, true, true, false);
        else {
          GL11.glColorMask(true, false, false, false);
        }
      }

      lv.c("clear");
      if (!this.pip) {
        GL11.glViewport(0, 0, this.mc.d, this.mc.e);
      } else {
        GL11.glViewport(0, this.mc.e - texTileSize * 4, texTileSize * 4, texTileSize * 4);
        GL11.glEnable(3089);
        GL11.glScissor(0, this.mc.e - texTileSize * 4, texTileSize * 4, texTileSize * 4);
      }

      updateFogColor(var1);
      GL11.glClear(16640);
      GL11.glEnable(2884);
      lv.c("camera");
      setupCameraTransform(var1, var19);
      aau.a(this.mc.h, this.mc.A.E == 2);
      lv.c("frustrum");
      r.a();
      if (this.mc.A.e < 2) {
        setupFog(-1, var1);
        lv.c("sky");
        var5.a(var1);
      }

      GL11.glEnable(2912);
      setupFog(1, var1);
      if (this.mc.A.k) {
        GL11.glShadeModel(7425);
      }

      lv.c("culling");
      nq var18 = new nq();
      var18.a(var7, var9, var11);
      this.mc.g.a(var18, var1);
      if (var19 == 0) {
        lv.c("updatechunks");

        while ((!this.mc.g.a(var4, false)) && (var2 != 0L)) {
          long var21 = var2 - System.nanoTime();
          if ((var21 < 0L) || (var21 > 1000000000L))
          {
            break;
          }
        }
      }
      setupFog(0, var1);
      GL11.glEnable(2912);
      GL11.glBindTexture(3553, this.mc.p.b("/terrain.png"));
      tf.a();
      lv.c("terrain");
      var5.a(var4, 0, var1);
      GL11.glShadeModel(7424);

      if (this.q == 0) {
        tf.b();
        lv.c("entities");
        if ((this.pip) && (mod_CCTV.cctvRegistered[this.currentCam] != 0)) {
          bo var20 = bo.b(mod_CCTV.cctvX[this.currentCam], mod_CCTV.cctvY[this.currentCam], mod_CCTV.cctvZ[this.currentCam]);
          var5.a(var20, var18, var1);
        } else {
          var5.a(var4.j(var1), var18, var1);
        }

        b(var1);
        lv.c("litParticles");
        var6.b(var4, var1);
        tf.a();
        setupFog(0, var1);
        lv.c("particles");
        var6.a(var4, var1);
        a(var1);
        if ((this.mc.z != null) && (var4.a(acn.g)) && ((var4 instanceof yw)) && (!this.pip)) {
          yw var22 = (yw)var4;
          GL11.glDisable(3008);
          lv.c("outline");
          var5.a(var22, this.mc.z, 0, var22.ap.b(), var1);
          var5.b(var22, this.mc.z, 0, var22.ap.b(), var1);
          GL11.glEnable(3008);
        }
      }

      GL11.glDisable(3042);
      GL11.glEnable(2884);
      GL11.glBlendFunc(770, 771);
      GL11.glDepthMask(true);
      setupFog(0, var1);
      GL11.glEnable(3042);
      GL11.glDisable(2884);
      GL11.glBindTexture(3553, this.mc.p.b("/terrain.png"));
      if (this.mc.A.j) {
        lv.c("water");
        if (this.mc.A.k) {
          GL11.glShadeModel(7425);
        }

        GL11.glColorMask(false, false, false, false);
        int var16 = var5.a(var4, 1, var1);
        if (this.mc.A.g) {
          if (anaglyphField == 0)
            GL11.glColorMask(false, true, true, true);
          else
            GL11.glColorMask(true, false, false, true);
        }
        else {
          GL11.glColorMask(true, true, true, true);
        }

        if (var16 > 0) {
          var5.a(1, var1);
        }

        GL11.glShadeModel(7424);
      } else {
        lv.c("water");
        var5.a(var4, 1, var1);
      }

      GL11.glDepthMask(true);
      GL11.glEnable(2884);
      GL11.glDisable(3042);
      if ((this.cameraZoom == 1.0D) && ((var4 instanceof yw)) && (this.mc.z != null) && (!var4.a(acn.g)) && (!this.pip)) {
        yw var22 = (yw)var4;
        GL11.glDisable(3008);
        lv.c("outline");
        var5.a(var22, this.mc.z, 0, var22.ap.b(), var1);
        var5.b(var22, this.mc.z, 0, var22.ap.b(), var1);
        GL11.glEnable(3008);
      }

      lv.c("weather");
      c(var1);
      GL11.glDisable(2912);
      if ((this.pointedEntity != null) || 
        (this.mc.A.c())) {
        lv.c("clouds");
        GL11.glPushMatrix();
        setupFog(0, var1);
        GL11.glEnable(2912);
        var5.b(var1);
        GL11.glDisable(2912);
        setupFog(1, var1);
        GL11.glPopMatrix();
      }

      lv.c("hand");
      if ((this.cameraZoom == 1.0D) && (!this.pip)) {
        GL11.glClear(256);
        renderHand(var1, var19);
      }

      if ((!this.mc.A.g) || (this.pip)) {
        if (this.pip) {
          GL11.glDisable(3089);
          GL11.glViewport(0, 0, this.mc.d, this.mc.e);
        }

        lv.b();
        return;
      }
    }

    GL11.glColorMask(true, true, true, false);
    lv.b();
  }

  private void addRainParticles() {
    float var1 = this.mc.f.j(1.0F);
    if (!this.mc.A.j) {
      var1 /= 2.0F;
    }

    if (var1 != 0.0F) {
      this.random.setSeed(this.rendererUpdateCount * 312987231L);
      acq var2 = this.mc.i;
      xd var3 = this.mc.f;
      int var4 = gk.c(var2.o);
      int var5 = gk.c(var2.p);
      int var6 = gk.c(var2.q);
      byte var7 = 10;
      double var8 = 0.0D;
      double var10 = 0.0D;
      double var12 = 0.0D;
      int var14 = 0;
      int var15 = (int)(100.0F * var1 * var1);
      if (this.mc.A.Q == 1)
        var15 >>= 1;
      else if (this.mc.A.Q == 2) {
        var15 = 0;
      }

      for (int var16 = 0; var16 < var15; var16++) {
        int var17 = var4 + this.random.nextInt(var7) - this.random.nextInt(var7);
        int var18 = var6 + this.random.nextInt(var7) - this.random.nextInt(var7);
        int var19 = var3.f(var17, var18);
        int var20 = var3.a(var17, var19 - 1, var18);
        if ((var19 <= var5 + var7) && (var19 >= var5 - var7) && (var3.i().a(var17, var18).d()) && (var3.i().a(var17, var19) > 0.2F)) {
          float var21 = this.random.nextFloat();
          float var22 = this.random.nextFloat();
          if (var20 > 0) {
            if (pb.m[var20].cd == acn.h) {
              this.mc.j.a(new alh(var3, var17 + var21, var19 + 0.1F - pb.m[var20].bW, var18 + var22, 0.0D, 0.0D, 0.0D));
            } else {
              var14++;
              if (this.random.nextInt(var14) == 0) {
                var8 = var17 + var21;
                var10 = var19 + 0.1F - pb.m[var20].bW;
                var12 = var18 + var22;
              }

              this.mc.j.a(new rg(var3, var17 + var21, var19 + 0.1F - pb.m[var20].bW, var18 + var22));
            }
          }
        }
      }

      if ((var14 > 0) && (this.random.nextInt(3) < this.rainSoundCounter++)) {
        this.rainSoundCounter = 0;
        if ((var10 > var2.p + 1.0D) && (var3.f(gk.c(var2.o), gk.c(var2.q)) > gk.c(var2.p)))
          this.mc.f.a(var8, var10, var12, "ambient.weather.rain", 0.1F, 0.5F);
        else
          this.mc.f.a(var8, var10, var12, "ambient.weather.rain", 0.2F, 1.0F);
      }
    }
  }

  protected void c(float var1)
  {
    float var2 = this.mc.f.j(var1);
    if (var2 > 0.0F) {
      b(var1);
      if (this.i == null) {
        this.i = new float[1024];
        this.j = new float[1024];

        for (int var3 = 0; var3 < 32; var3++) {
          for (int var4 = 0; var4 < 32; var4++) {
            float var5 = var4 - 16;
            float var6 = var3 - 16;
            float var7 = gk.c(var5 * var5 + var6 * var6);
            this.i[(var3 << 5 | var4)] = (-var6 / var7);
            this.j[(var3 << 5 | var4)] = (var5 / var7);
          }
        }
      }

      acq var43 = this.mc.i;
      xd var44 = this.mc.f;
      int var45 = gk.c(var43.o);
      int var46 = gk.c(var43.p);
      int var47 = gk.c(var43.q);
      adz var8 = adz.a;
      GL11.glDisable(2884);
      GL11.glNormal3f(0.0F, 1.0F, 0.0F);
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glAlphaFunc(516, 0.01F);
      GL11.glBindTexture(3553, this.mc.p.b("/environment/snow.png"));
      double var9 = var43.N + (var43.o - var43.N) * var1;
      double var11 = var43.O + (var43.p - var43.O) * var1;
      double var13 = var43.P + (var43.q - var43.P) * var1;
      int var15 = gk.c(var11);
      byte var16 = 5;
      if (this.mc.A.j) {
        var16 = 10;
      }

      abn[] var17 = var44.i().a(null, var45 - var16, var47 - var16, var16 * 2 + 1, var16 * 2 + 1, this.cloudFog);
      float[] var18 = var44.i().a(this.i, var45 - var16, var47 - var16, var16 * 2 + 1, var16 * 2 + 1);
      boolean var19 = false;
      byte var20 = -1;
      float var21 = this.rendererUpdateCount + var1;
      if (this.mc.A.j) {
        var16 = 10;
      }

      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      int var48 = 0;

      for (int var22 = var47 - var16; var22 <= var47 + var16; var22++) {
        for (int var23 = var45 - var16; var23 <= var45 + var16; var23++) {
          int var24 = (var22 - var47 + 16) * 32 + var23 - var45 + 16;
          float var25 = this.i[var24] * 0.5F;
          float var26 = this.j[var24] * 0.5F;
          abn var27 = var17[(var48++)];
          if ((var27.d()) || (var27.c())) {
            int var28 = var44.f(var23, var22);
            int var29 = var46 - var16;
            int var30 = var46 + var16;
            if (var29 < var28) {
              var29 = var28;
            }

            if (var30 < var28) {
              var30 = var28;
            }

            float var31 = 1.0F;
            int var32 = var28;
            if (var28 < var15) {
              var32 = var15;
            }

            if (var29 != var30) {
              this.random.setSeed(var23 * var23 * 3121 + var23 * 45238971 ^ var22 * var22 * 418711 + var22 * 13761);
              float var33 = var18[(var48 - 1)];

              if (var44.i().a(var33, var28) >= 0.15F) {
                if (var20 != 0) {
                  if (var20 >= 0) {
                    var8.a();
                  }

                  var20 = 0;
                  GL11.glBindTexture(3553, this.mc.p.b("/environment/rain.png"));
                  var8.b();
                }

                float var34 = ((this.rendererUpdateCount + var23 * var23 * 3121 + var23 * 45238971 + var22 * var22 * 418711 + var22 * 13761 & 0x1F) + var1) / 32.0F * (3.0F + this.random.nextFloat());
                double var35 = var23 + 0.5F - var43.o;
                double var37 = var22 + 0.5F - var43.q;
                float var39 = gk.a(var35 * var35 + var37 * var37) / var16;
                float var40 = 1.0F;
                var8.b(var44.b(var23, var32, var22, 0));
                var8.a(var40, var40, var40, ((1.0F - var39 * var39) * 0.5F + 0.5F) * var2);
                var8.b(-var9 * 1.0D, -var11 * 1.0D, -var13 * 1.0D);
                var8.a(var23 - var25 + 0.5D, var29, var22 - var26 + 0.5D, 0.0F * var31, var29 * var31 / 4.0F + var34 * var31);
                var8.a(var23 + var25 + 0.5D, var29, var22 + var26 + 0.5D, 1.0F * var31, var29 * var31 / 4.0F + var34 * var31);
                var8.a(var23 + var25 + 0.5D, var30, var22 + var26 + 0.5D, 1.0F * var31, var30 * var31 / 4.0F + var34 * var31);
                var8.a(var23 - var25 + 0.5D, var30, var22 - var26 + 0.5D, 0.0F * var31, var30 * var31 / 4.0F + var34 * var31);
                var8.b(0.0D, 0.0D, 0.0D);
              } else {
                if (var20 != 1) {
                  if (var20 >= 0) {
                    var8.a();
                  }

                  var20 = 1;
                  GL11.glBindTexture(3553, this.mc.p.b("/environment/snow.png"));
                  var8.b();
                }

                float var34 = ((this.rendererUpdateCount & 0x1FF) + var1) / 512.0F;
                float var49 = this.random.nextFloat() + var21 * 0.01F * (float)this.random.nextGaussian();
                float var36 = this.random.nextFloat() + var21 * (float)this.random.nextGaussian() * 0.001F;
                double var37 = var23 + 0.5F - var43.o;
                double var50 = var22 + 0.5F - var43.q;
                float var41 = gk.a(var37 * var37 + var50 * var50) / var16;
                float var42 = 1.0F;
                var8.b((var44.b(var23, var32, var22, 0) * 3 + 15728880) / 4);
                var8.a(var42, var42, var42, ((1.0F - var41 * var41) * 0.3F + 0.5F) * var2);
                var8.b(-var9 * 1.0D, -var11 * 1.0D, -var13 * 1.0D);
                var8.a(var23 - var25 + 0.5D, var29, var22 - var26 + 0.5D, 0.0F * var31 + var49, var29 * var31 / 4.0F + var34 * var31 + var36);
                var8.a(var23 + var25 + 0.5D, var29, var22 + var26 + 0.5D, 1.0F * var31 + var49, var29 * var31 / 4.0F + var34 * var31 + var36);
                var8.a(var23 + var25 + 0.5D, var30, var22 + var26 + 0.5D, 1.0F * var31 + var49, var30 * var31 / 4.0F + var34 * var31 + var36);
                var8.a(var23 - var25 + 0.5D, var30, var22 - var26 + 0.5D, 0.0F * var31 + var49, var30 * var31 / 4.0F + var34 * var31 + var36);
                var8.b(0.0D, 0.0D, 0.0D);
              }
            }
          }
        }
      }

      if (var20 >= 0) {
        var8.a();
      }

      GL11.glEnable(2884);
      GL11.glDisable(3042);
      GL11.glAlphaFunc(516, 0.1F);
      a(var1);
    }
  }

  public void b() {
    agd var1 = new agd(this.mc.A, this.mc.d, this.mc.e);
    GL11.glClear(256);
    GL11.glMatrixMode(5889);
    GL11.glLoadIdentity();
    GL11.glOrtho(0.0D, var1.a, var1.b, 0.0D, 1000.0D, 3000.0D);
    GL11.glMatrixMode(5888);
    GL11.glLoadIdentity();
    GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
  }

  private void updateFogColor(float var1) {
    xd var2 = this.mc.f;
    acq var3 = this.mc.i;
    float var4 = 1.0F / (4 - this.mc.A.e);
    var4 = 1.0F - (float)Math.pow(var4, 0.25D);
    bo var5 = var2.a(this.mc.i, var1);
    float var6 = (float)var5.a;
    float var7 = (float)var5.b;
    float var8 = (float)var5.c;
    bo var9 = var2.g(var1);
    this.n = ((float)var9.a);
    this.o = ((float)var9.b);
    this.p = ((float)var9.c);

    if (this.mc.A.e < 2) {
      bo var10 = gk.a(var2.e(var1)) <= 0.0F ? bo.b(1.0D, 0.0D, 0.0D) : bo.b(-1.0D, 0.0D, 0.0D);
      float var11 = (float)var3.k(var1).b(var10);
      if (var11 < 0.0F) {
        var11 = 0.0F;
      }

      if (var11 > 0.0F) {
        float[] var12 = var2.t.a(var2.c(var1), var1);
        if (var12 != null) {
          var11 *= var12[3];
          this.n = (this.n * (1.0F - var11) + var12[0] * var11);
          this.o = (this.o * (1.0F - var11) + var12[1] * var11);
          this.p = (this.p * (1.0F - var11) + var12[2] * var11);
        }
      }
    }

    this.n += (var6 - this.n) * var4;
    this.o += (var7 - this.o) * var4;
    this.p += (var8 - this.p) * var4;
    float var19 = var2.j(var1);

    if (var19 > 0.0F) {
      float var11 = 1.0F - var19 * 0.5F;
      float var20 = 1.0F - var19 * 0.4F;
      this.n *= var11;
      this.o *= var11;
      this.p *= var20;
    }

    float var11 = var2.i(var1);
    if (var11 > 0.0F) {
      float var20 = 1.0F - var11 * 0.5F;
      this.n *= var20;
      this.o *= var20;
      this.p *= var20;
    }

    int var21 = aau.a(this.mc.f, var3, var1);
    if (this.cloudFog) {
      bo var13 = var2.f(var1);
      this.n = ((float)var13.a);
      this.o = ((float)var13.b);
      this.p = ((float)var13.c);
    } else if ((var21 != 0) && (pb.m[var21].cd == acn.g)) {
      this.n = 0.02F;
      this.o = 0.02F;
      this.p = 0.2F;
    } else if ((var21 != 0) && (pb.m[var21].cd == acn.h)) {
      this.n = 0.6F;
      this.o = 0.1F;
      this.p = 0.0F;
    }

    float var22 = this.fogColor2 + (this.fogColor1 - this.fogColor2) * var1;
    this.n *= var22;
    this.o *= var22;
    this.p *= var22;
    double var14 = (var3.O + (var3.p - var3.O) * var1) / 32.0D;
    if (var3.a(aad.q)) {
      int var16 = var3.b(aad.q).b();
      if (var16 < 20)
        var14 *= (1.0F - var16 / 20.0F);
      else {
        var14 = 0.0D;
      }
    }

    if (var14 < 1.0D) {
      if (var14 < 0.0D) {
        var14 = 0.0D;
      }

      var14 *= var14;
      this.n = ((float)(this.n * var14));
      this.o = ((float)(this.o * var14));
      this.p = ((float)(this.p * var14));
    }

    if (this.mc.A.g) {
      float var23 = (this.n * 30.0F + this.o * 59.0F + this.p * 11.0F) / 100.0F;
      float var17 = (this.n * 30.0F + this.o * 70.0F) / 100.0F;
      float var18 = (this.n * 30.0F + this.p * 70.0F) / 100.0F;
      this.n = var23;
      this.o = var17;
      this.p = var18;
    }

    if (!this.pip)
      GL11.glClearColor(this.n, this.o, this.p, 0.0F);
    else
      GL11.glClearColor(this.n, this.o, this.p, 1.0F);
  }

  private void setupFog(int var1, float var2)
  {
    acq var3 = this.mc.i;
    if (var1 == 999) {
      GL11.glFog(2918, setFogColorBuffer(0.0F, 0.0F, 0.0F, 1.0F));
      GL11.glFogi(2917, 9729);
      GL11.glFogf(2915, 0.0F);
      GL11.glFogf(2916, 8.0F);
      if (GLContext.getCapabilities().GL_NV_fog_distance) {
        GL11.glFogi(34138, 34139);
      }

      GL11.glFogf(2915, 0.0F);
    } else {
      GL11.glFog(2918, setFogColorBuffer(this.n, this.o, this.p, 1.0F));
      GL11.glNormal3f(0.0F, -1.0F, 0.0F);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      int var4 = aau.a(this.mc.f, var3, var2);

      if (var3.a(aad.q)) {
        float var5 = 5.0F;
        int var6 = var3.b(aad.q).b();
        if (var6 < 20) {
          var5 = 5.0F + (this.farPlaneDistance - 5.0F) * (1.0F - var6 / 20.0F);
        }

        GL11.glFogi(2917, 9729);
        if (var1 < 0) {
          GL11.glFogf(2915, 0.0F);
          GL11.glFogf(2916, var5 * 0.8F);
        } else {
          GL11.glFogf(2915, var5 * 0.25F);
          GL11.glFogf(2916, var5);
        }

        if (GLContext.getCapabilities().GL_NV_fog_distance)
          GL11.glFogi(34138, 34139);
      }
      else
      {
        float var10;
        if (this.cloudFog) {
          GL11.glFogi(2917, 2048);
          GL11.glFogf(2914, 0.1F);
          float var5 = 1.0F;
          float var12 = 1.0F;
          float var7 = 1.0F;
          if (this.mc.A.g) {
            float var8 = (var5 * 30.0F + var12 * 59.0F + var7 * 11.0F) / 100.0F;
            float var9 = (var5 * 30.0F + var12 * 70.0F) / 100.0F;
            var10 = (var5 * 30.0F + var7 * 70.0F) / 100.0F;
          }
        }
        else
        {
          float var10;
          if ((var4 > 0) && (pb.m[var4].cd == acn.g)) {
            GL11.glFogi(2917, 2048);
            if (!var3.a(aad.o))
              GL11.glFogf(2914, 0.1F);
            else {
              GL11.glFogf(2914, 0.05F);
            }

            float var5 = 0.4F;
            float var12 = 0.4F;
            float var7 = 0.9F;
            if (this.mc.A.g) {
              float var8 = (var5 * 30.0F + var12 * 59.0F + var7 * 11.0F) / 100.0F;
              float var9 = (var5 * 30.0F + var12 * 70.0F) / 100.0F;
              var10 = (var5 * 30.0F + var7 * 70.0F) / 100.0F;
            }
          }
          else
          {
            float var10;
            if ((var4 > 0) && (pb.m[var4].cd == acn.h)) {
              GL11.glFogi(2917, 2048);
              GL11.glFogf(2914, 2.0F);
              float var5 = 0.4F;
              float var12 = 0.3F;
              float var7 = 0.3F;
              if (this.mc.A.g) {
                float var8 = (var5 * 30.0F + var12 * 59.0F + var7 * 11.0F) / 100.0F;
                float var9 = (var5 * 30.0F + var12 * 70.0F) / 100.0F;
                var10 = (var5 * 30.0F + var7 * 70.0F) / 100.0F;
              }
            } else {
              float var5 = this.farPlaneDistance;
              if (!this.mc.f.t.e) {
                double var11 = ((var3.b(var2) & 0xF00000) >> 20) / 16.0D + (var3.O + (var3.p - var3.O) * var2 + 4.0D) / 32.0D;
                if (var11 < 1.0D) {
                  if (var11 < 0.0D) {
                    var11 = 0.0D;
                  }

                  var11 *= var11;
                  float var8 = 100.0F * (float)var11;
                  if (var8 < 5.0F) {
                    var8 = 5.0F;
                  }

                  if (var5 > var8) {
                    var5 = var8;
                  }
                }
              }

              GL11.glFogi(2917, 9729);
              if (var1 < 0) {
                GL11.glFogf(2915, 0.0F);
                GL11.glFogf(2916, var5 * 0.8F);
              } else {
                GL11.glFogf(2915, var5 * 0.25F);
                GL11.glFogf(2916, var5);
              }

              if (GLContext.getCapabilities().GL_NV_fog_distance) {
                GL11.glFogi(34138, 34139);
              }

              if (this.mc.f.t.d)
                GL11.glFogf(2915, 0.0F);
            }
          }
        }
      }
      GL11.glEnable(2903);
      GL11.glColorMaterial(1028, 4608);
    }
  }

  private FloatBuffer setFogColorBuffer(float var1, float var2, float var3, float var4) {
    this.m.clear();
    this.m.put(var1).put(var2).put(var3).put(var4);
    this.m.flip();
    return this.m;
  }
}