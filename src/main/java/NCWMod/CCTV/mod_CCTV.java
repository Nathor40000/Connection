package NCWMod.CCTV;

import java.io.File;
import java.util.Map;
import net.minecraft.client.Minecraft;

public class mod_CCTV
{
  static boolean rendererReplaced = false;
  public static EntityExtensibleRenderer c;
  public static pb cctvCamera;
  public static yr cctv;
  public static int[][] cctvIndex = new int[15][16];
  private static PlasmaProps props = new PlasmaProps(new File(Minecraft.b() + "/config/mod_CCTV.props").getPath());
  public static int CCTVFrontTexture;
  public static int texTileSize;
  public static int defaultCam;
  public static double[] cctvX = new double[15];
  public static double[] cctvY = new double[15];
  public static double[] cctvZ = new double[15];
  public static double[] cctvOffsetX = new double[15];
  public static double[] cctvOffsetY = new double[15];
  public static double[] cctvOffsetZ = new double[15];
  public static double[] cctvPitch = new double[15];
  public static double[] cctvYaw = new double[15];
  public static boolean[] cctvRegistered = new boolean[15];
  public static int[] cctvWatched = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
  public static String TextureName;
  private static final boolean[] usedCCTVSprites = new boolean[256];
  private static int CCTVSpriteIndex = 0;
  public static boolean lowRes;
  public static int camSpeed;
  public static int fieldDepth;
  public static int custom_width;
  public static int custom_height;

  public static void prepareProps()
  {
    props.getInt("cctvCameraID", 150);
    props.getInt("cctvScreenID", 1500);
    props.getInt("low_resolution", 0);
    props.getInt("camera_speed", 0);
    props.getInt("depth_of_field", 64);
    props.getInt("custom_camera_width", 4);
    props.getInt("custom_camera_height", 3);
  }

  public boolean onTickInGame(float var1, Minecraft var2) {
    if ((!rendererReplaced) && (var2.u != null)) {
      rendererReplaced = true;
      c = new EntityExtensibleRenderer(var2);
    }

    if (var2.h != null) {
      cctvX[13] = var2.h.o;
      cctvY[13] = var2.h.p;
      cctvZ[13] = var2.h.q;
      cctvOffsetX[13] = 0.0D;
      cctvOffsetY[13] = 0.0D;
      cctvOffsetZ[13] = 0.0D;
      cctvPitch[13] = (-var2.h.v);
      cctvYaw[13] = (var2.h.u + 180.0F);
    }

    return true;
  }

  public static boolean CanRegisterCCTV() {
    for (int var0 = 0; var0 < 13; var0++) {
      if (cctvRegistered[var0] == 0) {
        return true;
      }
    }

    return false;
  }

  public static void RegisterCCTV(double var0, double var2, double var4, double var6, float var8, float var9, float var10) {
    int var11 = 13;

    for (int var12 = 0; var12 < 13; var12++) {
      if (cctvRegistered[var12] == 0) {
        var11 = var12;
      }
    }

    cctvOffsetX[var11] = var8;
    cctvOffsetY[var11] = var9;
    cctvOffsetZ[var11] = var10;
    cctvX[var11] = var0;
    cctvY[var11] = var2;
    cctvZ[var11] = var4;
    cctvYaw[var11] = var6;
    cctvPitch[var11] = 0.0D;
    cctvRegistered[var11] = true;
  }

  public static void UnregisterCCTV(int var0, int var1, int var2) {
    for (int var3 = 0; var3 < 13; var3++)
      if ((cctvX[var3] == var0) && (cctvY[var3] == var1) && (cctvZ[var3] == var2)) {
        cctvRegistered[var3] = false;
        cctvWatched[var3] = 0;
      }
  }

  public static int IsRegistered(int var0, int var1, int var2)
  {
    for (int var3 = 0; var3 < 13; var3++) {
      if ((cctvX[var3] == var0) && (cctvY[var3] == var1) && (cctvZ[var3] == var2)) {
        return var3;
      }
    }

    return -1;
  }

  public static boolean IsWatched(int var0, int var1, int var2) {
    for (int var3 = 0; var3 < 13; var3++) {
      if ((cctvX[var3] == var0) && (cctvY[var3] == var1) && (cctvZ[var3] == var2)) {
        return cctvWatched[var3] > 0;
      }
    }

    return false;
  }

  public static void watchCam(int var0, int var1) {
    cctvWatched[var0] += 1;
    if (cctvWatched[var1] != 0)
      cctvWatched[var1] -= 1;
  }

  public static void watchCam(int var0)
  {
    cctvWatched[var0] += 1;
  }

  public static void unwatchCam(int var0) {
    cctvWatched[var0] -= 1;
  }

  public static int SelectNewCam(int var0)
  {
    for (int var1 = var0 + 1; var1 < 14; var1++) {
      if ((cctvRegistered[var1] != 0) || (var1 == 13)) {
        watchCam(var1, var0);
        return var1;
      }
    }

    for (var1 = 0; var1 < var0 + 1; var1++) {
      if (cctvRegistered[var1] != 0) {
        watchCam(var1, var0);
        return var1;
      }
    }

    watchCam(14, var0);
    return 14;
  }

  public static int GetNextCam(int var0)
  {
    for (int var1 = var0 + 1; var1 < 14; var1++) {
      if (((cctvRegistered[var1] != 0) || (var1 == 13)) && (cctvWatched[var1] != 0)) {
        return var1;
      }
    }

    for (var1 = 0; var1 < var0 + 1; var1++) {
      if ((cctvRegistered[var1] != 0) && (cctvWatched[var1] != 0)) {
        return var1;
      }
    }

    return -1;
  }

  private static int getUniqueCCTVSpriteIndex() {
    while (CCTVSpriteIndex < usedCCTVSprites.length) {
      if (usedCCTVSprites[CCTVSpriteIndex] == 0) {
        usedCCTVSprites[CCTVSpriteIndex] = true;
        return CCTVSpriteIndex++;
      }

      CCTVSpriteIndex += 1;
    }

    return 0;
  }

  public void load() {
    ModLoader.setInGameHook(this, true, false);
    String var1 = "0000000000001111000000000000111100000000000011110000000000001111000000000000111100000000000011110000000000001111000000000000111100000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";

    for (int var2 = 0; var2 < 256; var2++) {
      usedCCTVSprites[var2] = (var1.charAt(var2) == '1' ? 1 : false);
    }

    for (var2 = 0; var2 < 14; var2++) {
      for (int var3 = 0; var3 < 16; var3++) {
        cctvIndex[var2][var3] = getUniqueCCTVSpriteIndex();
      }
    }

    lowRes = props.getInt("low_resolution") != 0;
    camSpeed = props.getInt("camera_speed");
    fieldDepth = props.getInt("depth_of_field");
    custom_width = props.getInt("custom_camera_width") * 16;
    custom_height = props.getInt("custom_camera_height") * 16;
    texTileSize = 32;
    TextureName = "/kaevator/CCTV_32.png";
    if (lowRes) {
      texTileSize = 16;
      TextureName = "/kaevator/CCTV_16.png";
    }

    CCTVFrontTexture = ModLoader.addOverride("/terrain.png", "/kaevator/blockCCTVMonitor.png");
    cctvCamera = new BlockCCTVCamera(props.getInt("cctvCameraID")).a("cctvCamera").a(pb.h);
    cctv = new ItemCCTV(props.getInt("cctvScreenID", 1500)).e(ModLoader.addOverride("/gui/items.png", "/kaevator/itemCCTVMonitor.png")).a("cctv");
    ModLoader.registerBlock(cctvCamera);
    ModLoader.registerEntityID(EntityCCTV.class, "CCTV", ModLoader.getUniqueEntityId());
    ModLoader.addName(cctvCamera, "CCTV Camera");
    ModLoader.addName(cctv, "CCTV Monitor");
    ModLoader.addRecipe(new aan(cctv, 1), new Object[] { "DCD", "CDC", "DED", Character.valueOf('D'), pb.M, Character.valueOf('C'), yr.o, Character.valueOf('E'), yr.aC });
    ModLoader.addRecipe(new aan(cctvCamera, 1), new Object[] { "CCC", "CDC", "EEE", Character.valueOf('D'), pb.M, Character.valueOf('C'), yr.o, Character.valueOf('E'), yr.aC });
  }

  public void addRenderer(Map var1) {
    var1.put(EntityCCTV.class, new RenderCCTV());
  }

  public String getVersion() {
    return "V1 - 1.2.5";
  }

  static {
    prepareProps();
    defaultCam = 14;
    cctvRegistered[14] = true;
    cctvRegistered[13] = false;
  }
}