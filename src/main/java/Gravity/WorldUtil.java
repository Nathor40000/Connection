/*     */ package Gravity;
/*     */ 
/*     */ //import buildcraft.api.power.IPowerReceptor;
/*     */ import cofh.api.energy.IEnergyHandler;
/*     */ import cpw.mods.fml.client.FMLClientHandler;
/*     */ import cpw.mods.fml.common.FMLCommonHandler;
/*     */ import cpw.mods.fml.common.registry.GameRegistry;
/*     */ import cpw.mods.fml.relauncher.Side;
/*     */ import ic2.api.energy.tile.IEnergyAcceptor;
/*     */ import ic2.api.energy.tile.IEnergyEmitter;
/*     */ import ic2.api.energy.tile.IEnergyTile;
/*     */ import java.io.File;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import mekanism.api.energy.IStrictEnergyAcceptor;
/*     */ import mekanism.api.gas.IGasTransmitter;
/*     */ import mekanism.api.gas.ITubeConnection;
/*     */ import mekanism.api.transmitters.TransmissionType;
/*     */ import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
/*     */ import micdoodle8.mods.galacticraft.api.entity.IWorldTransferCallback;
/*     */ import micdoodle8.mods.galacticraft.api.prefab.entity.EntityAutoRocket;
/*     */ import micdoodle8.mods.galacticraft.api.prefab.entity.EntitySpaceshipBase;
/*     */ import micdoodle8.mods.galacticraft.api.recipe.SpaceStationRecipe;
/*     */ import micdoodle8.mods.galacticraft.api.transmission.NetworkType;
/*     */ import micdoodle8.mods.galacticraft.api.transmission.compatibility.NetworkConfigHandler;
/*     */ import micdoodle8.mods.galacticraft.api.transmission.tile.IConnector;
/*     */ import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
/*     */ import micdoodle8.mods.galacticraft.api.vector.Vector3;
/*     */ import micdoodle8.mods.galacticraft.api.world.ICelestialBody;
/*     */ import micdoodle8.mods.galacticraft.api.world.ICelestialBodyRenderer;
/*     */ import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
/*     */ import micdoodle8.mods.galacticraft.api.world.IMapObject;
/*     */ import micdoodle8.mods.galacticraft.api.world.IOrbitDimension;
/*     */ import micdoodle8.mods.galacticraft.api.world.ITeleportType;
/*     */ import micdoodle8.mods.galacticraft.api.world.SpaceStationType;
/*     */ import micdoodle8.mods.galacticraft.core.GCCoreConfigManager;
/*     */ import micdoodle8.mods.galacticraft.core.GCLog;
/*     */ import micdoodle8.mods.galacticraft.core.dimension.GCCoreSpaceStationData;
/*     */ import micdoodle8.mods.galacticraft.core.dimension.GCCoreWorldProviderSpaceStation;
/*     */ import micdoodle8.mods.galacticraft.core.entities.player.GCCorePlayerMP;
/*     */ import micdoodle8.mods.galacticraft.core.entities.player.GCCorePlayerSP;
/*     */ import micdoodle8.mods.galacticraft.core.inventory.GCCoreInventoryExtended;
/*     */ import micdoodle8.mods.galacticraft.core.items.GCCoreItemParachute;
/*     */ import micdoodle8.mods.galacticraft.core.network.GCCorePacketDimensionListPlanets;
/*     */ import micdoodle8.mods.galacticraft.core.network.GCCorePacketDimensionListSpaceStations;
/*     */ import micdoodle8.mods.galacticraft.core.network.GCCorePacketHandlerClient.EnumPacketClient;
/*     */ import micdoodle8.mods.galacticraft.core.network.GCCorePacketSpaceStationData;
/*     */ import micdoodle8.mods.galacticraft.moon.dimension.GCMoonWorldProvider;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.entity.EntityClientPlayerMP;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityList;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.EntityTracker;
/*     */ import net.minecraft.entity.item.EntityItem;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.EntityPlayerMP;
/*     */ import net.minecraft.item.ItemInWorldManager;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.network.NetServerHandler;
/*     */ import net.minecraft.network.packet.Packet41EntityEffect;
/*     */ import net.minecraft.network.packet.Packet43Experience;
/*     */ import net.minecraft.network.packet.Packet9Respawn;
/*     */ import net.minecraft.potion.PotionEffect;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.management.PlayerManager;
/*     */ import net.minecraft.server.management.ServerConfigurationManager;
/*     */ import net.minecraft.tileentity.TileEntity;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.Vec3;
/*     */ import net.minecraft.world.ChunkCoordIntPair;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.WorldProvider;
/*     */ import net.minecraft.world.WorldProviderSurface;
/*     */ import net.minecraft.world.WorldServer;
/*     */ import net.minecraft.world.chunk.Chunk;
/*     */ import net.minecraft.world.chunk.IChunkProvider;
/*     */ import net.minecraft.world.gen.ChunkProviderServer;
/*     */ import net.minecraft.world.storage.WorldInfo;
/*     */ import net.minecraftforge.common.DimensionManager;
/*     */ import net.minecraftforge.common.ForgeDirection;
/*     */ 
/*     */ public class WorldUtil
/*     */ {
/*     */   public static Collection<Integer> registeredSpaceStations;
/*     */   public static Collection<Integer> registeredPlanets;
/*     */   public static Collection<String> registeredPlanetNames;
/*     */ 
/*     */   public static double getGravityForEntity(EntityLivingBase eLiving)
/*     */   {
/*  97 */     if ((eLiving.field_70170_p.field_73011_w instanceof IGalacticraftWorldProvider))
/*     */     {
/*  99 */       IGalacticraftWorldProvider customProvider = (IGalacticraftWorldProvider)eLiving.field_70170_p.field_73011_w;
/*     */ 
/* 101 */       if ((eLiving instanceof EntityPlayer))
/*     */       {
/* 103 */         if ((FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) && ((eLiving instanceof GCCorePlayerSP)))
/*     */         {
/* 105 */           return ((GCCorePlayerSP)eLiving).touchedGround ? 0.08D - customProvider.getGravity() : 0.08D;
/*     */         }
/* 107 */         if ((eLiving instanceof GCCorePlayerMP))
/*     */         {
/* 109 */           return ((GCCorePlayerMP)eLiving).isTouchedGround() ? 0.08D - customProvider.getGravity() : 0.08D;
/*     */         }
/*     */ 
/* 113 */         return 0.08D;
/*     */       }
/*     */ 
/* 118 */       return 0.08D - customProvider.getGravity();
/*     */     }
/*     */ 
/* 123 */     return 0.08D;
/*     */   }
/*     */ 
/*     */   public static double getItemGravity(EntityItem e)
/*     */   {
/* 129 */     if ((e.field_70170_p.field_73011_w instanceof IGalacticraftWorldProvider))
/*     */     {
/* 131 */       IGalacticraftWorldProvider customProvider = (IGalacticraftWorldProvider)e.field_70170_p.field_73011_w;
/* 132 */       return 0.03999999910593033D - ((customProvider instanceof IOrbitDimension) ? 0.05999999910593033D : customProvider.getGravity()) / 1.75D;
/*     */     }
/*     */ 
/* 136 */     return 0.03999999910593033D;
/*     */   }
/*     */ 
/*     */   public static double getItemGravity2(EntityItem e)
/*     */   {
/* 142 */     if ((e.field_70170_p.field_73011_w instanceof IGalacticraftWorldProvider))
/*     */     {
/* 144 */       return 1.0D;
/*     */     }
/*     */ 
/* 148 */     return 0.9800000190734863D;
/*     */   }
/*     */ 
/*     */   public static Vector3 getWorldColor(World world)
/*     */   {
/* 154 */     if ((world.field_73011_w instanceof GCMoonWorldProvider))
/*     */     {
/* 156 */       float f1 = world.func_72826_c(1.0F);
/* 157 */       float f2 = 1.0F - (MathHelper.func_76134_b(f1 * 3.141593F * 2.0F) * 2.0F + 0.25F);
/*     */ 
/* 159 */       if (f2 < 0.0F)
/*     */       {
/* 161 */         f2 = 0.0F;
/*     */       }
/*     */ 
/* 164 */       if (f2 > 1.0F)
/*     */       {
/* 166 */         f2 = 1.0F;
/*     */       }
/*     */ 
/* 169 */       double d = 1.0D - f2 * f2 * 0.7D;
/* 170 */       return new Vector3(d, d, d);
/*     */     }
/*     */ 
/* 173 */     return new Vector3(1.0D, 1.0D, 1.0D);
/*     */   }
/*     */ 
/*     */   public static float getColorRed(World world)
/*     */   {
/* 178 */     return (float)getWorldColor(world).x;
/*     */   }
/*     */ 
/*     */   public static float getColorGreen(World world)
/*     */   {
/* 183 */     return (float)getWorldColor(world).y;
/*     */   }
/*     */ 
/*     */   public static float getColorBlue(World world)
/*     */   {
/* 188 */     return (float)getWorldColor(world).z;
/*     */   }
/*     */ 
/*     */   public static Vec3 getFogColorHook(World world)
/*     */   {
/* 193 */     if (((world.field_73011_w instanceof WorldProviderSurface)) && (FMLClientHandler.instance().getClient().field_71439_g.field_70163_u >= 200.0D))
/*     */     {
/* 195 */       float var20 = (float)(FMLClientHandler.instance().getClient().field_71439_g.field_70163_u - 200.0D) / 1000.0F;
/* 196 */       float var21 = Math.max(1.0F - var20 * 4.0F, 0.0F);
/*     */ 
/* 198 */       Vec3 vec = world.func_72948_g(1.0F);
/*     */ 
/* 200 */       return Vec3.func_72443_a(vec.field_72450_a * var21, vec.field_72448_b * var21, vec.field_72449_c * var21);
/*     */     }
/*     */ 
/* 203 */     return world.func_72948_g(1.0F);
/*     */   }
/*     */ 
/*     */   public static Vec3 getSkyColorHook(World world)
/*     */   {
/* 208 */     if (((world.field_73011_w instanceof WorldProviderSurface)) && (FMLClientHandler.instance().getClient().field_71439_g.field_70163_u >= 200.0D))
/*     */     {
/* 210 */       float var20 = (float)(FMLClientHandler.instance().getClient().field_71439_g.field_70163_u - 200.0D) / 1000.0F;
/* 211 */       float var21 = Math.max(1.0F - var20 * 2.0F, 0.0F);
/*     */ 
/* 213 */       Vec3 vec = world.func_72833_a(FMLClientHandler.instance().getClient().field_71451_h, 1.0F);
/*     */ 
/* 215 */       return Vec3.func_72443_a(vec.field_72450_a * var21, vec.field_72448_b * var21, vec.field_72449_c * var21);
/*     */     }
/*     */ 
/* 218 */     return world.func_72833_a(FMLClientHandler.instance().getClient().field_71451_h, 1.0F);
/*     */   }
/*     */ 
/*     */   public static WorldProvider getProviderForName(String par1String)
/*     */   {
/* 223 */     Integer[] var1 = getArrayOfPossibleDimensions();
/*     */ 
/* 225 */     for (Integer element : var1)
/*     */     {
/* 227 */       if ((WorldProvider.func_76570_a(element.intValue()) != null) && (WorldProvider.func_76570_a(element.intValue()).func_80007_l() != null))
/*     */       {
/* 229 */         if (par1String.contains("$"))
/*     */         {
/* 231 */           String[] twoDimensions = par1String.split("\\$");
/*     */ 
/* 233 */           if (WorldProvider.func_76570_a(element.intValue()).func_80007_l().equals(twoDimensions[0]))
/*     */           {
/* 235 */             return WorldProvider.func_76570_a(element.intValue());
/*     */           }
/*     */         }
/* 238 */         else if (WorldProvider.func_76570_a(element.intValue()).func_80007_l().equals(par1String))
/*     */         {
/* 240 */           return WorldProvider.func_76570_a(element.intValue());
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 245 */     return null;
/*     */   }
/*     */ 
/*     */   public static List<Integer> getPossibleDimensionsForSpaceshipTier(int tier)
/*     */   {
/* 250 */     List temp = new ArrayList();
/*     */ 
/* 252 */     temp.add(Integer.valueOf(0));
/*     */ 
/* 254 */     for (Integer element : registeredPlanets)
/*     */     {
/* 256 */       WorldProvider provider = WorldProvider.func_76570_a(element.intValue());
/*     */ 
/* 258 */       if (provider != null)
/*     */       {
/* 260 */         if ((provider instanceof IGalacticraftWorldProvider))
/*     */         {
/* 262 */           if (((IGalacticraftWorldProvider)provider).canSpaceshipTierPass(tier))
/*     */           {
/* 264 */             temp.add(element);
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/* 269 */           temp.add(element);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 274 */     for (Integer element : registeredSpaceStations)
/*     */     {
/* 276 */       WorldProvider provider = WorldProvider.func_76570_a(element.intValue());
/*     */ 
/* 278 */       if (provider != null)
/*     */       {
/* 280 */         if ((provider instanceof IGalacticraftWorldProvider))
/*     */         {
/* 282 */           if (((IGalacticraftWorldProvider)provider).canSpaceshipTierPass(tier))
/*     */           {
/* 284 */             temp.add(element);
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/* 289 */           temp.add(element);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 294 */     return temp;
/*     */   }
/*     */ 
/*     */   public static HashMap<String, Integer> getArrayOfPossibleDimensions(List<Integer> ids, GCCorePlayerMP playerBase)
/*     */   {
/* 299 */     HashMap map = new HashMap();
/*     */ 
/* 301 */     for (Integer id : ids)
/*     */     {
/* 303 */       if (WorldProvider.func_76570_a(id.intValue()) != null)
/*     */       {
/* 305 */         if ((((WorldProvider.func_76570_a(id.intValue()) instanceof IGalacticraftWorldProvider)) && (!(WorldProvider.func_76570_a(id.intValue()) instanceof IOrbitDimension))) || (WorldProvider.func_76570_a(id.intValue()).field_76574_g == 0))
/*     */         {
/* 307 */           map.put(WorldProvider.func_76570_a(id.intValue()).func_80007_l(), Integer.valueOf(WorldProvider.func_76570_a(id.intValue()).field_76574_g));
/*     */         }
/* 309 */         else if ((playerBase != null) && ((WorldProvider.func_76570_a(id.intValue()) instanceof IOrbitDimension)))
/*     */         {
/* 311 */           GCCoreSpaceStationData data = GCCoreSpaceStationData.getStationData(playerBase.field_70170_p, id.intValue(), playerBase);
/*     */ 
/* 313 */           if ((!GCCoreConfigManager.spaceStationsRequirePermission) || (data.getAllowedPlayers().contains(playerBase.field_71092_bJ.toLowerCase())) || (data.getAllowedPlayers().contains(playerBase.field_71092_bJ)))
/*     */           {
/* 315 */             map.put(WorldProvider.func_76570_a(id.intValue()).func_80007_l() + "$" + data.getOwner() + "$" + data.getSpaceStationName(), Integer.valueOf(WorldProvider.func_76570_a(id.intValue()).field_76574_g));
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 321 */     for (int j = 0; j < GalacticraftRegistry.getCelestialBodies().size(); j++)
/*     */     {
/* 323 */       ICelestialBody object = (ICelestialBody)GalacticraftRegistry.getCelestialBodies().get(j);
/*     */ 
/* 325 */       if ((!object.isReachable()) && (object.addToList()))
/*     */       {
/* 327 */         map.put(object.getName() + "*", Integer.valueOf(0));
/*     */       }
/*     */     }
/*     */ 
/* 331 */     return map;
/*     */   }
/*     */ 
/*     */   public static List<String> getPlayersOnPlanet(IMapObject planet)
/*     */   {
/* 336 */     List list = new ArrayList();
/*     */ 
/* 338 */     for (WorldServer world : DimensionManager.getWorlds())
/*     */     {
/* 340 */       if ((world != null) && ((world.field_73011_w instanceof IGalacticraftWorldProvider)))
/*     */       {
/* 342 */         if (planet.getSlotRenderer().getPlanetName().toLowerCase().equals(world.field_73011_w.func_80007_l().toLowerCase()))
/*     */         {
/* 344 */           for (int j = 0; j < world.func_72910_y().size(); j++)
/*     */           {
/* 346 */             if ((world.func_72910_y().get(j) != null) && ((world.func_72910_y().get(j) instanceof EntityPlayer)))
/*     */             {
/* 348 */               list.add(((EntityPlayer)world.func_72910_y().get(j)).field_71092_bJ);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 355 */     return list;
/*     */   }
/*     */ 
/*     */   private static List<Integer> getExistingSpaceStationList(File var0)
/*     */   {
/* 360 */     ArrayList var1 = new ArrayList();
/* 361 */     File[] var2 = var0.listFiles();
/* 362 */     int var3 = var2.length;
/*     */ 
/* 364 */     for (int var4 = 0; var4 < var3; var4++)
/*     */     {
/* 366 */       File var5 = var2[var4];
/*     */ 
/* 368 */       if (var5.getName().contains("spacestation_"))
/*     */       {
/* 370 */         String var6 = var5.getName();
/* 371 */         var6 = var6.substring(13, var6.length() - 4);
/* 372 */         var1.add(Integer.valueOf(Integer.parseInt(var6)));
/*     */       }
/*     */     }
/*     */ 
/* 376 */     return var1;
/*     */   }
/*     */ 
/*     */   public static void unregisterSpaceStations()
/*     */   {
/* 381 */     if (registeredSpaceStations != null)
/*     */     {
/* 383 */       for (Integer registeredID : registeredSpaceStations)
/*     */       {
/* 385 */         DimensionManager.unregisterDimension(registeredID.intValue());
/*     */       }
/*     */ 
/* 388 */       registeredSpaceStations = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void registerSpaceStations(File spaceStationList)
/*     */   {
/* 394 */     registeredSpaceStations = getExistingSpaceStationList(spaceStationList);
/*     */ 
/* 396 */     for (Integer registeredID : registeredSpaceStations)
/*     */     {
/* 398 */       int id = Arrays.binarySearch(GCCoreConfigManager.staticLoadDimensions, registeredID.intValue());
/*     */ 
/* 400 */       if (id >= 0)
/*     */       {
/* 402 */         DimensionManager.registerDimension(registeredID.intValue(), GCCoreConfigManager.idDimensionOverworldOrbitStatic);
/* 403 */         FMLCommonHandler.instance().getMinecraftServerInstance().func_71218_a(registeredID.intValue());
/*     */       }
/*     */       else
/*     */       {
/* 407 */         DimensionManager.registerDimension(registeredID.intValue(), GCCoreConfigManager.idDimensionOverworldOrbit);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void registerPlanet(int planetID, boolean isStatic)
/*     */   {
/* 417 */     if (registeredPlanets == null)
/*     */     {
/* 419 */       registeredPlanets = new ArrayList();
/*     */     }
/*     */ 
/* 422 */     registeredPlanets.add(Integer.valueOf(planetID));
/*     */ 
/* 424 */     if (isStatic)
/*     */     {
/* 426 */       DimensionManager.registerDimension(planetID, planetID);
/* 427 */       GCLog.info("Registered Dimension: " + planetID);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void unregisterPlanets()
/*     */   {
/* 433 */     if (registeredPlanets != null)
/*     */     {
/* 435 */       Iterator var0 = registeredPlanets.iterator();
/*     */ 
/* 437 */       while (var0.hasNext())
/*     */       {
/* 439 */         Integer var1 = (Integer)var0.next();
/* 440 */         DimensionManager.unregisterDimension(var1.intValue());
/* 441 */         GCLog.info("Unregistered Dimension: " + var1.intValue());
/*     */       }
/*     */ 
/* 444 */       registeredPlanets = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static Integer[] getArrayOfPossibleDimensions()
/*     */   {
/* 454 */     ArrayList temp = new ArrayList();
/*     */ 
/* 456 */     temp.add(Integer.valueOf(0));
/*     */ 
/* 458 */     for (Integer i : registeredPlanets)
/*     */     {
/* 460 */       temp.add(i);
/*     */     }
/*     */ 
/* 463 */     for (Integer i : registeredSpaceStations)
/*     */     {
/* 465 */       temp.add(i);
/*     */     }
/*     */ 
/* 468 */     Integer[] finalArray = new Integer[temp.size()];
/*     */ 
/* 470 */     int count = 0;
/*     */ 
/* 472 */     for (Integer integ : temp)
/*     */     {
/* 474 */       finalArray[(count++)] = integ;
/*     */     }
/*     */ 
/* 477 */     return finalArray;
/*     */   }
/*     */ 
/*     */   public static GCCoreSpaceStationData bindSpaceStationToNewDimension(World world, GCCorePlayerMP player)
/*     */   {
/* 482 */     int newID = DimensionManager.getNextFreeDimId();
/* 483 */     GCCoreSpaceStationData data = createSpaceStation(world, newID, player);
/* 484 */     player.setSpaceStationDimensionID(newID);
/* 485 */     player.field_71135_a.func_72567_b(PacketUtil.createPacket("GalacticraftCore", GCCorePacketHandlerClient.EnumPacketClient.UPDATE_SPACESTATION_CLIENT_ID, new Object[] { Integer.valueOf(newID) }));
/* 486 */     return data;
/*     */   }
/*     */ 
/*     */   public static GCCoreSpaceStationData createSpaceStation(World world, int dimID, GCCorePlayerMP player)
/*     */   {
/* 491 */     registeredSpaceStations.add(Integer.valueOf(dimID));
/* 492 */     int id = Arrays.binarySearch(GCCoreConfigManager.staticLoadDimensions, dimID);
/*     */ 
/* 494 */     if (id >= 0)
/*     */     {
/* 496 */       DimensionManager.registerDimension(dimID, GCCoreConfigManager.idDimensionOverworldOrbitStatic);
/*     */     }
/*     */     else
/*     */     {
/* 500 */       DimensionManager.registerDimension(dimID, GCCoreConfigManager.idDimensionOverworldOrbit);
/*     */     }
/*     */ 
/* 503 */     MinecraftServer var2 = FMLCommonHandler.instance().getMinecraftServerInstance();
/*     */ 
/* 505 */     if (var2 != null)
/*     */     {
/* 507 */       ArrayList var1 = new ArrayList();
/* 508 */       var1.add(Integer.valueOf(dimID));
/* 509 */       var2.func_71203_ab().func_72384_a(GCCorePacketDimensionListSpaceStations.buildDimensionListPacket(var1));
/*     */     }
/*     */ 
/* 512 */     GCCoreSpaceStationData var3 = GCCoreSpaceStationData.getStationData(world, dimID, player);
/* 513 */     return var3;
/*     */   }
/*     */ 
/*     */   public static Entity transferEntityToDimension(Entity entity, int dimensionID, WorldServer world)
/*     */   {
/* 518 */     return transferEntityToDimension(entity, dimensionID, world, true, null);
/*     */   }
/*     */ 
/*     */   public static Entity transferEntityToDimension(Entity entity, int dimensionID, WorldServer world, boolean transferInv, EntityAutoRocket ridingRocket)
/*     */   {
/* 523 */     if (!world.field_72995_K)
/*     */     {
/* 525 */       MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
/*     */ 
/* 527 */       if (server != null)
/*     */       {
/* 529 */         ArrayList array = new ArrayList();
/*     */ 
/* 531 */         for (Iterator i$ = registeredPlanets.iterator(); i$.hasNext(); ) { int i = ((Integer)i$.next()).intValue();
/*     */ 
/* 533 */           array.add(Integer.valueOf(i));
/*     */         }
/*     */ 
/* 536 */         server.func_71203_ab().func_72384_a(GCCorePacketDimensionListPlanets.buildDimensionListPacket(array));
/*     */       }
/*     */ 
/* 539 */       MinecraftServer mcServer = FMLCommonHandler.instance().getMinecraftServerInstance();
/*     */ 
/* 541 */       if (mcServer != null)
/*     */       {
/* 543 */         WorldServer var6 = mcServer.func_71218_a(dimensionID);
/*     */ 
/* 545 */         if (var6 == null)
/*     */         {
/* 547 */           System.err.println("Cannot Transfer Entity to Dimension: Could not get World for Dimension " + dimensionID);
/*     */         }
/*     */ 
/* 550 */         ITeleportType type = GalacticraftRegistry.getTeleportTypeForDimension(var6.field_73011_w.getClass());
/*     */ 
/* 552 */         if (type != null)
/*     */         {
/* 554 */           return teleportEntity(var6, entity, dimensionID, type, transferInv, ridingRocket);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 559 */     return null;
/*     */   }
/*     */ 
/*     */   private static Entity teleportEntity(World worldNew, Entity entity, int dimID, ITeleportType type, boolean transferInv, EntityAutoRocket ridingRocket)
/*     */   {
/* 564 */     if ((entity.field_70154_o != null) && ((entity.field_70154_o instanceof EntitySpaceshipBase)))
/*     */     {
/* 566 */       entity.func_70078_a(entity.field_70154_o);
/*     */     }
/*     */ 
/* 569 */     boolean dimChange = entity.field_70170_p != worldNew;
/* 570 */     entity.field_70170_p.func_72866_a(entity, false);
/* 571 */     GCCorePlayerMP player = null;
/*     */ 
/* 573 */     if ((entity instanceof GCCorePlayerMP))
/*     */     {
/* 575 */       player = (GCCorePlayerMP)entity;
/* 576 */       player.func_71053_j();
/*     */ 
/* 578 */       if (dimChange)
/*     */       {
/* 580 */         player.field_71093_bK = dimID;
/* 581 */         player.field_71135_a.func_72567_b(new Packet9Respawn(player.field_71093_bK, (byte)player.field_70170_p.field_73013_u, worldNew.func_72912_H().func_76067_t(), worldNew.func_72800_K(), player.field_71134_c.func_73081_b()));
/*     */ 
/* 583 */         if (((worldNew.field_73011_w instanceof GCCoreWorldProviderSpaceStation)) && (registeredSpaceStations.contains(player)))
/*     */         {
/* 585 */           player.field_71135_a.func_72567_b(GCCorePacketSpaceStationData.buildSpaceStationDataPacket(worldNew, worldNew.field_73011_w.field_76574_g, player));
/*     */         }
/*     */ 
/* 588 */         ((WorldServer)entity.field_70170_p).func_73040_p().func_72695_c(player);
/*     */       }
/*     */ 
/* 591 */       player.setNotUsingPlanetGui();
/*     */     }
/*     */ 
/* 594 */     if (dimChange)
/*     */     {
/* 596 */       if (ridingRocket == null)
/*     */       {
/* 598 */         removeEntityFromWorld(entity.field_70170_p, entity, true);
/*     */       }
/*     */       else
/*     */       {
/* 602 */         removeEntityFromWorld(entity.field_70170_p, entity, true);
/*     */       }
/*     */     }
/*     */ 
/* 606 */     if (dimChange)
/*     */     {
/* 608 */       if ((entity instanceof EntityPlayerMP))
/*     */       {
/* 610 */         player = (GCCorePlayerMP)entity;
/* 611 */         entity.func_70012_b(type.getPlayerSpawnLocation((WorldServer)entity.field_70170_p, player).x, type.getPlayerSpawnLocation((WorldServer)entity.field_70170_p, player).y, type.getPlayerSpawnLocation((WorldServer)entity.field_70170_p, player).z, entity.field_70177_z, entity.field_70125_A);
/* 612 */         Vector3 spawnPos = type.getPlayerSpawnLocation((WorldServer)entity.field_70170_p, (EntityPlayerMP)entity);
/* 613 */         ChunkCoordIntPair pair = worldNew.func_72964_e(spawnPos.intX(), spawnPos.intZ()).func_76632_l();
/* 614 */         ((WorldServer)worldNew).field_73059_b.func_73158_c(pair.field_77276_a, pair.field_77275_b);
/*     */ 
/* 616 */         entity.func_70107_b(spawnPos.x, spawnPos.y, spawnPos.z);
/*     */       }
/*     */     }
/*     */ 
/* 620 */     if (ridingRocket != null)
/*     */     {
/* 622 */       NBTTagCompound nbt = new NBTTagCompound();
/* 623 */       ridingRocket.field_70128_L = false;
/* 624 */       ridingRocket.field_70153_n = null;
/* 625 */       ridingRocket.func_70039_c(nbt);
/*     */ 
/* 627 */       ((WorldServer)ridingRocket.field_70170_p).func_73039_n().func_72790_b(ridingRocket);
/* 628 */       ridingRocket.field_70170_p.field_72996_f.remove(ridingRocket);
/* 629 */       ridingRocket.field_70170_p.func_72847_b(ridingRocket);
/*     */ 
/* 631 */       ridingRocket = (EntityAutoRocket)EntityList.func_75615_a(nbt, worldNew);
/*     */ 
/* 633 */       if (ridingRocket != null)
/*     */       {
/* 635 */         ridingRocket.setWaitForPlayer(true);
/*     */ 
/* 637 */         if ((ridingRocket instanceof IWorldTransferCallback))
/*     */         {
/* 639 */           ((IWorldTransferCallback)ridingRocket).onWorldTransferred(worldNew);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 644 */     if (dimChange)
/*     */     {
/* 646 */       if (!(entity instanceof EntityPlayer))
/*     */       {
/* 648 */         NBTTagCompound nbt = new NBTTagCompound();
/* 649 */         entity.field_70128_L = false;
/* 650 */         entity.func_70039_c(nbt);
/* 651 */         entity.field_70128_L = true;
/* 652 */         entity = EntityList.func_75615_a(nbt, worldNew);
/*     */ 
/* 654 */         if (entity == null)
/*     */         {
/* 656 */           return null;
/*     */         }
/*     */ 
/* 659 */         if ((entity instanceof IWorldTransferCallback))
/*     */         {
/* 661 */           ((IWorldTransferCallback)entity).onWorldTransferred(worldNew);
/*     */         }
/*     */       }
/*     */ 
/* 665 */       worldNew.func_72838_d(entity);
/* 666 */       entity.func_70029_a(worldNew);
/*     */     }
/*     */ 
/* 669 */     if (dimChange)
/*     */     {
/* 671 */       if ((entity instanceof EntityPlayer))
/*     */       {
/* 673 */         entity.func_70012_b(type.getPlayerSpawnLocation((WorldServer)entity.field_70170_p, (EntityPlayerMP)entity).x, type.getPlayerSpawnLocation((WorldServer)entity.field_70170_p, (EntityPlayerMP)entity).y, type.getPlayerSpawnLocation((WorldServer)entity.field_70170_p, (EntityPlayerMP)entity).z, entity.field_70177_z, entity.field_70125_A);
/*     */       }
/*     */     }
/*     */ 
/* 677 */     worldNew.func_72866_a(entity, false);
/*     */ 
/* 679 */     if (dimChange)
/*     */     {
/* 681 */       if ((entity instanceof EntityPlayer))
/*     */       {
/* 683 */         entity.func_70012_b(type.getPlayerSpawnLocation((WorldServer)entity.field_70170_p, (EntityPlayerMP)entity).x, type.getPlayerSpawnLocation((WorldServer)entity.field_70170_p, (EntityPlayerMP)entity).y, type.getPlayerSpawnLocation((WorldServer)entity.field_70170_p, (EntityPlayerMP)entity).z, entity.field_70177_z, entity.field_70125_A);
/*     */       }
/*     */     }
/*     */ 
/* 687 */     if ((entity instanceof GCCorePlayerMP))
/*     */     {
/* 689 */       player = (GCCorePlayerMP)entity;
/*     */ 
/* 691 */       if (dimChange)
/*     */       {
/* 693 */         player.field_71133_b.func_71203_ab().func_72375_a(player, (WorldServer)worldNew);
/*     */       }
/*     */ 
/* 696 */       player.field_71135_a.func_72569_a(type.getPlayerSpawnLocation((WorldServer)entity.field_70170_p, (EntityPlayerMP)entity).x, type.getPlayerSpawnLocation((WorldServer)entity.field_70170_p, (EntityPlayerMP)entity).y, type.getPlayerSpawnLocation((WorldServer)entity.field_70170_p, (EntityPlayerMP)entity).z, entity.field_70177_z, entity.field_70125_A);
/*     */ 
/* 698 */       GCLog.info("Server attempting to transfer player " + player.field_71092_bJ + " to dimension " + worldNew.field_73011_w.field_76574_g);
/*     */     }
/*     */ 
/* 701 */     worldNew.func_72866_a(entity, false);
/*     */ 
/* 703 */     if ((entity instanceof GCCorePlayerMP))
/*     */     {
/* 705 */       player = (GCCorePlayerMP)entity;
/*     */ 
/* 707 */       if ((ridingRocket == null) && (type.useParachute()) && (player.getExtendedInventory().func_70301_a(4) != null) && ((player.getExtendedInventory().func_70301_a(4).func_77973_b() instanceof GCCoreItemParachute)))
/*     */       {
/* 709 */         player.setUsingParachute(true);
/*     */       }
/*     */       else
/*     */       {
/* 713 */         player.setUsingParachute(false);
/*     */       }
/*     */     }
/*     */ 
/* 717 */     if (((entity instanceof GCCorePlayerMP)) && (dimChange))
/*     */     {
/* 719 */       player = (GCCorePlayerMP)entity;
/* 720 */       player.field_71134_c.func_73080_a((WorldServer)worldNew);
/* 721 */       player.field_71133_b.func_71203_ab().func_72354_b(player, (WorldServer)worldNew);
/* 722 */       player.field_71133_b.func_71203_ab().func_72385_f(player);
/* 723 */       Iterator var9 = player.func_70651_bq().iterator();
/*     */ 
/* 725 */       while (var9.hasNext())
/*     */       {
/* 727 */         PotionEffect var10 = (PotionEffect)var9.next();
/* 728 */         player.field_71135_a.func_72567_b(new Packet41EntityEffect(player.field_70157_k, var10));
/*     */       }
/*     */ 
/* 731 */       player.field_71135_a.func_72567_b(new Packet43Experience(player.field_71106_cc, player.field_71067_cb, player.field_71068_ca));
/*     */     }
/*     */ 
/* 734 */     if ((entity instanceof GCCorePlayerMP))
/*     */     {
/* 736 */       Vector3 spawnPos = null;
/*     */ 
/* 738 */       if (player != null)
/*     */       {
/* 740 */         spawnPos = type.getPlayerSpawnLocation((WorldServer)entity.field_70170_p, (EntityPlayerMP)entity);
/* 741 */         entity.func_70012_b(spawnPos.x, spawnPos.y, spawnPos.z, entity.field_70177_z, entity.field_70125_A);
/*     */       }
/*     */       else
/*     */       {
/* 745 */         spawnPos = type.getEntitySpawnLocation((WorldServer)entity.field_70170_p, entity);
/* 746 */         entity.func_70012_b(spawnPos.x, spawnPos.y, spawnPos.z, entity.field_70177_z, entity.field_70125_A);
/*     */       }
/*     */     }
/*     */ 
/* 750 */     if ((entity instanceof GCCorePlayerMP))
/*     */     {
/* 752 */       player = (GCCorePlayerMP)entity;
/*     */ 
/* 754 */       if ((player.getRocketStacks() != null) && (player.getRocketStacks().length > 0))
/*     */       {
/* 756 */         for (int stack = 0; stack < player.getRocketStacks().length; stack++)
/*     */         {
/* 758 */           if (transferInv)
/*     */           {
/* 760 */             if (player.getRocketStacks()[stack] == null)
/*     */             {
/* 762 */               if (stack == player.getRocketStacks().length - 1)
/*     */               {
/* 764 */                 if (player.getRocketItem() != null)
/*     */                 {
/* 766 */                   player.getRocketStacks()[stack] = new ItemStack(player.getRocketItem(), 1, player.getRocketType());
/*     */                 }
/*     */               }
/* 769 */               else if (stack == player.getRocketStacks().length - 2)
/*     */               {
/* 771 */                 player.getRocketStacks()[stack] = player.getLaunchpadStack();
/* 772 */                 player.setLaunchpadStack(null);
/*     */               }
/*     */             }
/*     */           }
/*     */           else
/*     */           {
/* 778 */             player.getRocketStacks()[stack] = null;
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/* 783 */       if ((transferInv) && (player.getChestSpawnCooldown() == 0))
/*     */       {
/* 785 */         player.setChestSpawnVector(type.getParaChestSpawnLocation((WorldServer)entity.field_70170_p, player, new Random()));
/* 786 */         player.setChestSpawnCooldown(200);
/*     */       }
/*     */     }
/*     */ 
/* 790 */     if (ridingRocket != null)
/*     */     {
/* 792 */       entity.func_70080_a(ridingRocket.field_70165_t, ridingRocket.field_70163_u, ridingRocket.field_70161_v, 0.0F, 0.0F);
/* 793 */       worldNew.func_72866_a(entity, true);
/*     */ 
/* 795 */       worldNew.func_72838_d(ridingRocket);
/* 796 */       ridingRocket.func_70029_a(worldNew);
/*     */ 
/* 798 */       worldNew.func_72866_a(ridingRocket, true);
/*     */     }
/*     */ 
/* 801 */     if ((entity instanceof EntityPlayerMP))
/*     */     {
/* 803 */       GameRegistry.onPlayerChangedDimension((EntityPlayerMP)entity);
/* 804 */       type.onSpaceDimensionChanged(worldNew, (EntityPlayerMP)entity, ridingRocket != null);
/*     */     }
/*     */ 
/* 807 */     if (ridingRocket != null)
/*     */     {
/* 809 */       entity.field_70154_o = ridingRocket;
/* 810 */       ridingRocket.field_70153_n = entity;
/*     */     }
/*     */ 
/* 813 */     return entity;
/*     */   }
/*     */ 
/*     */   private static void removeEntityFromWorld(World var0, Entity var1, boolean directlyRemove)
/*     */   {
/* 818 */     if ((var1 instanceof EntityPlayer))
/*     */     {
/* 820 */       EntityPlayer var2 = (EntityPlayer)var1;
/* 821 */       var2.func_71053_j();
/* 822 */       var0.field_73010_i.remove(var2);
/* 823 */       var0.func_72854_c();
/* 824 */       int var3 = var1.field_70176_ah;
/* 825 */       int var4 = var1.field_70164_aj;
/*     */ 
/* 827 */       if ((var1.field_70175_ag) && (var0.func_72863_F().func_73149_a(var3, var4)))
/*     */       {
/* 829 */         var0.func_72964_e(var3, var4).func_76622_b(var1);
/* 830 */         var0.func_72964_e(var3, var4).field_76643_l = true;
/*     */       }
/*     */ 
/* 833 */       if (directlyRemove)
/*     */       {
/* 835 */         var0.field_72996_f.remove(var1);
/* 836 */         var0.func_72847_b(var1);
/*     */       }
/*     */     }
/*     */ 
/* 840 */     var1.field_70128_L = false;
/*     */   }
/*     */ 
/*     */   public static SpaceStationRecipe getSpaceStationRecipe(int planetID)
/*     */   {
/* 845 */     for (SpaceStationType type : GalacticraftRegistry.getSpaceStationData())
/*     */     {
/* 847 */       if (type.getWorldToOrbitID() == planetID)
/*     */       {
/* 849 */         return type.getRecipeForSpaceStation();
/*     */       }
/*     */     }
/*     */ 
/* 853 */     return null;
/*     */   }
/*     */ 
/*     */   public static TileEntity[] getAdjacentOxygenConnections(TileEntity tile)
/*     */   {
/* 858 */     TileEntity[] adjacentConnections = new TileEntity[ForgeDirection.VALID_DIRECTIONS.length];
/*     */ 
/* 860 */     boolean isMekLoaded = NetworkConfigHandler.isMekanismLoaded();
/*     */ 
/* 862 */     BlockVec3 thisVec = new BlockVec3(tile);
/* 863 */     for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS)
/*     */     {
/* 865 */       TileEntity tileEntity = thisVec.getTileEntityOnSide(tile.field_70331_k, direction);
/*     */ 
/* 867 */       if ((tileEntity instanceof IConnector))
/*     */       {
/* 869 */         if (((IConnector)tileEntity).canConnect(direction.getOpposite(), NetworkType.OXYGEN))
/*     */         {
/* 871 */           adjacentConnections[direction.ordinal()] = tileEntity;
/*     */         }
/*     */       }
/* 874 */       else if (isMekLoaded)
/*     */       {
/* 876 */         if (((tileEntity instanceof ITubeConnection)) && ((!(tileEntity instanceof IGasTransmitter)) || (TransmissionType.checkTransmissionType(tileEntity, TransmissionType.GAS, tileEntity))))
/*     */         {
/* 878 */           if (((ITubeConnection)tileEntity).canTubeConnect(direction))
/*     */           {
/* 880 */             adjacentConnections[direction.ordinal()] = tileEntity;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 886 */     return adjacentConnections;
/*     */   }
/*     */ 
/*     */   public static TileEntity[] getAdjacentPowerConnections(TileEntity tile)
/*     */   {
/* 891 */     TileEntity[] adjacentConnections = new TileEntity[6];
/*     */ 
/* 893 */     boolean isMekLoaded = NetworkConfigHandler.isMekanismLoaded();
/* 894 */     boolean isTELoaded = NetworkConfigHandler.isThermalExpansionLoaded();
/* 895 */     boolean isIC2Loaded = NetworkConfigHandler.isIndustrialCraft2Loaded();
/* 896 */     boolean isBCLoaded = NetworkConfigHandler.isBuildcraftLoaded();
/*     */ 
/* 898 */     BlockVec3 thisVec = new BlockVec3(tile);
/* 899 */     for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS)
/*     */     {
/* 901 */       TileEntity tileEntity = thisVec.getTileEntityOnSide(tile.field_70331_k, direction);
/*     */ 
/* 903 */       if ((tileEntity instanceof IConnector))
/*     */       {
/* 905 */         if (((IConnector)tileEntity).canConnect(direction.getOpposite(), NetworkType.POWER))
/*     */         {
/* 907 */           adjacentConnections[direction.ordinal()] = tileEntity;
/*     */         }
/*     */       }
/* 910 */       else if ((isMekLoaded) && ((tileEntity instanceof IStrictEnergyAcceptor)))
/*     */       {
/* 912 */         if (((IStrictEnergyAcceptor)tileEntity).canReceiveEnergy(direction.getOpposite()))
/*     */         {
/* 914 */           adjacentConnections[direction.ordinal()] = tileEntity;
/*     */         }
/*     */       }
/* 917 */       else if ((isTELoaded) && ((tileEntity instanceof IEnergyHandler)))
/*     */       {
/* 919 */         if (((IEnergyHandler)tileEntity).canInterface(direction.getOpposite()))
/*     */         {
/* 921 */           adjacentConnections[direction.ordinal()] = tileEntity;
/*     */         }
/*     */       }
/* 924 */       else if ((isIC2Loaded) && ((tileEntity instanceof IEnergyTile)))
/*     */       {
/* 926 */         if ((tileEntity instanceof IEnergyAcceptor))
/*     */         {
/* 928 */           if (((IEnergyAcceptor)tileEntity).acceptsEnergyFrom(tile, direction.getOpposite()))
/*     */           {
/* 930 */             adjacentConnections[direction.ordinal()] = tileEntity;
/* 931 */             continue;
/*     */           }
/*     */         }
/*     */ 
/* 935 */         if ((tileEntity instanceof IEnergyEmitter))
/*     */         {
/* 937 */           if (((IEnergyEmitter)tileEntity).emitsEnergyTo(tileEntity, direction.getOpposite()))
/*     */           {
/* 939 */             adjacentConnections[direction.ordinal()] = tileEntity;
/* 940 */             continue;
/*     */           }
/*     */         }
/*     */ 
/* 944 */         adjacentConnections[direction.ordinal()] = tileEntity;
/*     */       }
/* 946 */       else if ((isBCLoaded) && ((tileEntity instanceof IPowerReceptor)))
/*     */       {
/* 948 */         if (((IPowerReceptor)tileEntity).getPowerReceiver(direction.getOpposite()) != null)
/*     */         {
/* 950 */           adjacentConnections[direction.ordinal()] = tileEntity;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 955 */     return adjacentConnections;
/*     */   }
/*     */ }

/* Location:           C:\Users\Nigel\Desktop\Nathan\Modding\jd-gui-0.3.5.windows\Galacticraft-1.6.4-2.0.13.1063.jar
 * Qualified Name:     micdoodle8.mods.galacticraft.core.util.WorldUtil
 * JD-Core Version:    0.6.2
 */