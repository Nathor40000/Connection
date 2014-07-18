/*     */ package Gravity;
/*     */ 
/*     */ import cpw.mods.fml.relauncher.Side;
/*     */ import cpw.mods.fml.relauncher.SideOnly;
/*     */ import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
/*     */ import micdoodle8.mods.galacticraft.api.world.ISolarLevel;
/*     */ import micdoodle8.mods.galacticraft.core.GCCoreConfigManager;
/*     */ import micdoodle8.mods.galacticraft.moon.GCMoonConfigManager;
/*     */ import micdoodle8.mods.galacticraft.moon.world.gen.GCMoonChunkProvider;
/*     */ import micdoodle8.mods.galacticraft.moon.world.gen.GCMoonWorldChunkManager;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.Vec3;
/*     */ import net.minecraft.util.Vec3Pool;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.WorldProvider;
/*     */ import net.minecraft.world.chunk.Chunk;
/*     */ import net.minecraft.world.chunk.IChunkProvider;
/*     */ import net.minecraft.world.storage.WorldInfo;
/*     */ 
/*     */ public class GCMoonWorldProvider extends WorldProvider
/*     */   implements IGalacticraftWorldProvider, ISolarLevel
/*     */ {
/*     */   public void setDimension(int var1)
/*     */   {
/*  32 */     this.field_76574_g = var1;
/*  33 */     super.setDimension(var1);
/*     */   }
/*     */ 
/*     */   protected void func_76556_a()
/*     */   {
/*  39 */     float var1 = 0.0F;
/*     */ 
/*  41 */     for (int var2 = 0; var2 <= 15; var2++)
/*     */     {
/*  43 */       float var3 = 1.0F - var2 / 15.0F;
/*  44 */       //this.field_76573_f[var2] = ((1.0F - var3) / (var3 * 3.0F + 1.0F) * 1.0F + 0.0F);
/*     */     }
/*     */   }
/*     */ 
/*     */   public float[] func_76560_a(float var1, float var2)
/*     */   {
/*  51 */     return null;
/*     */   }
/*     */ 
/*     */   public void func_76572_b()
/*     */   {
/*  57 */     //this.field_76578_c = new GCMoonWorldChunkManager();
/*     */   }
/*     */ 
/*     */   @SideOnly(Side.CLIENT)
/*     */   public Vec3 func_76562_b(float var1, float var2)
/*     */   {
/*  64 */     return this.field_76579_a.func_82732_R().func_72345_a(0.0D, 0.0D, 0.0D);
/*     */   }
/*     */ 
/*     */   public Vec3 getSkyColor(Entity cameraEntity, float partialTicks)
/*     */   {
/*  70 */     return this.field_76579_a.func_82732_R().func_72345_a(0.0D, 0.0D, 0.0D);
/*     */   }
/*     */ 
/*     */   public float func_76563_a(long par1, float par3)
/*     */   {
/*  76 */     int var4 = (int)(par1 % 192000L);
/*  77 */     float var5 = (var4 + par3) / 192000.0F - 0.25F;
/*     */ 
/*  79 */     if (var5 < 0.0F)
/*     */     {
/*  81 */       var5 += 1.0F;
/*     */     }
/*     */ 
/*  84 */     if (var5 > 1.0F)
/*     */     {
/*  86 */       var5 -= 1.0F;
/*     */     }
/*     */ 
/*  89 */     float var6 = var5;
/*  90 */     var5 = 1.0F - (float)((Math.cos(var5 * 3.141592653589793D) + 1.0D) / 2.0D);
/*  91 */     var5 = var6 + (var5 - var6) / 3.0F;
/*  92 */     return var5;
/*     */   }
/*     */ 
/*     */   @SideOnly(Side.CLIENT)
/*     */   public float getStarBrightness(float par1)
/*     */   {
/*  99 */     float var2 = this.field_76579_a.func_72826_c(par1);
/* 100 */     float var3 = 1.0F - (MathHelper.func_76134_b(var2 * 3.141593F * 2.0F) * 2.0F + 0.25F);
/*     */ 
/* 102 */     if (var3 < 0.0F)
/*     */     {
/* 104 */       var3 = 0.0F;
/*     */     }
/*     */ 
/* 107 */     if (var3 > 1.0F)
/*     */     {
/* 109 */       var3 = 1.0F;
/*     */     }
/*     */ 
/* 112 */     return var3 * var3 * 0.5F + 0.3F;
/*     */   }
/*     */ 
/*     */   public float calculatePhobosAngle(long par1, float par3)
/*     */   {
/* 117 */     return func_76563_a(par1, par3) * 3000.0F;
/*     */   }
/*     */ 
/*     */   public float calculateDeimosAngle(long par1, float par3)
/*     */   {
/* 122 */     return calculatePhobosAngle(par1, par3) * 1.0E-010F;
/*     */   }
/*     */ 
/*     */   public IChunkProvider func_76555_c()
/*     */   {
/* 128 */     return new GCMoonChunkProvider(this.field_76579_a, this.field_76579_a.func_72905_C(), this.field_76579_a.func_72912_H().func_76089_r());
/*     */   }
/*     */ 
/*     */   public void updateWeather()
/*     */   {
/* 134 */     this.field_76579_a.func_72912_H().func_76080_g(0);
/* 135 */     this.field_76579_a.func_72912_H().func_76084_b(false);
/* 136 */     this.field_76579_a.func_72912_H().func_76090_f(0);
/* 137 */     this.field_76579_a.func_72912_H().func_76069_a(false);
/* 138 */     this.field_76579_a.field_73004_o = 0.0F;
/* 139 */     this.field_76579_a.field_73017_q = 0.0F;
/*     */   }
/*     */ 
/*     */   public boolean func_76561_g()
/*     */   {
/* 145 */     return false;
/*     */   }
/*     */ 
/*     */   public double getHorizon()
/*     */   {
/* 151 */     return 44.0D;
/*     */   }
/*     */ 
/*     */   public int func_76557_i()
/*     */   {
/* 157 */     return 44;
/*     */   }
/*     */ 
/*     */   public boolean func_76569_d()
/*     */   {
/* 163 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean func_76566_a(int var1, int var2)
/*     */   {
/* 169 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean func_76567_e()
/*     */   {
/* 175 */     return !GCCoreConfigManager.forceOverworldRespawn;
/*     */   }
/*     */ 
/*     */   public String getSaveFolder()
/*     */   {
/* 181 */     return "DIM" + GCMoonConfigManager.dimensionIDMoon;
/*     */   }
/*     */ 
/*     */   public String getWelcomeMessage()
/*     */   {
/* 187 */     return "Entering The Moon";
/*     */   }
/*     */ 
/*     */   public String getDepartMessage()
/*     */   {
/* 193 */     return "Leaving The Moon";
/*     */   }
/*     */ 
/*     */   public String func_80007_l()
/*     */   {
/* 199 */     return "Moon";
/*     */   }
/*     */ 
/*     */   public boolean canSnowAt(int x, int y, int z)
/*     */   {
/* 205 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean canBlockFreeze(int x, int y, int z, boolean byWater)
/*     */   {
/* 211 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean canDoLightning(Chunk chunk)
/*     */   {
/* 217 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean canDoRainSnowIce(Chunk chunk)
/*     */   {
/* 223 */     return false;
/*     */   }
/*     */ 
/*     */   public float getGravity()
/*     */   {
/* 229 */     return 0.062F;
/*     */   }
/*     */ 
/*     */   public int getHeight()
/*     */   {
/* 235 */     return 800;
/*     */   }
/*     */ 
/*     */   public double getMeteorFrequency()
/*     */   {
/* 241 */     return 7.0D;
/*     */   }
/*     */ 
/*     */   public double getFuelUsageMultiplier()
/*     */   {
/* 247 */     return 0.7D;
/*     */   }
/*     */ 
/*     */   public double getSolarEnergyMultiplier()
/*     */   {
/* 253 */     return 1.4D;
/*     */   }
/*     */ 
/*     */   public boolean canSpaceshipTierPass(int tier)
/*     */   {
/* 259 */     return tier > 0;
/*     */   }
/*     */ 
/*     */   public float getFallDamageModifier()
/*     */   {
/* 265 */     return 0.18F;
/*     */   }
/*     */ 
/*     */   public float getSoundVolReductionAmount()
/*     */   {
/* 271 */     return 20.0F;
/*     */   }
/*     */ }

/* Location:           C:\Users\Nigel\Desktop\Nathan\Modding\jd-gui-0.3.5.windows\Galacticraft-1.6.4-2.0.13.1063.jar
 * Qualified Name:     micdoodle8.mods.galacticraft.moon.dimension.GCMoonWorldProvider
 * JD-Core Version:    0.6.2
 */