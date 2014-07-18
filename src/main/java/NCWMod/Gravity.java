package NCWMod;

import ibxm.Player;

import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;
import java.util.Vector;

import net.minecraft.world.World;

import com.jcraft.jorbis.Block;
import com.sun.prism.Material;
import com.sun.xml.internal.bind.v2.runtime.Location;
import com.sun.xml.internal.stream.Entity;
import com.sun.xml.internal.ws.api.pipe.Fiber;
import com.sun.xml.internal.ws.api.pipe.Fiber.Listener;

import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.eventhandler.EventPriority;

public class Gravity
  implements Listener
{
  protected HashMap<UUID, Vector> velocities;
  protected HashMap<UUID, Location> positions;
  protected HashMap<UUID, Boolean> onGround;

  public void onEnable()
  {
    //getLogger().info("BlingGravity has been loaded.");
    new GravityTask(this).runTaskLater(this, 1L);
    this.velocities = new HashMap();
    this.onGround = new HashMap();
    this.positions = new HashMap();
    
  }

  public void onDisable() {
    
  }

  public void UpdateVelocities()
  {
    Iterator j;
    for (Iterator i = getWorlds().iterator(); i.hasNext(); 
      j.hasNext())
    {
      World world = (World)i.next();
      j = world.getEntities().iterator(); continue;
      Entity e = (Entity)j.next();
      Vector newv = e.getVelocity().clone();
      UUID uuid = e.getUniqueId();

      if ((this.velocities.containsKey(uuid)) && (this.onGround.containsKey(uuid)) && (!e.isOnGround()) && (!e.isInsideVehicle())) {
        Vector oldv = (Vector)this.velocities.get(uuid);
        if (!((Boolean)this.onGround.get(uuid)).booleanValue()) {
          Vector d = oldv.clone();
          d.subtract(newv);
          double dy = d.getY();
          if ((dy > 0.0D) && ((newv.getY() < -0.01D) || (newv.getY() > 0.01D))) {
            Location loc = e.getLocation().clone();
            double gravity = 1.0D;
            while (loc.getBlockY() >= 0) {
              Block block = loc.getBlock();
              if (block.getType() == Material.WOOL) {
                if (block.getData() == 5)
                  gravity = 0.2D;
                else if (block.getData() == 14)
                  gravity = 5.0D;
                else if (block.getData() == 9)
                  gravity = -0.2D;
                else if (block.getData() == 2) {
                  gravity = -5.0D;
                }
              }

              if (block.getType() != Material.AIR) {
                break;
              }
              loc.setY(loc.getY() - 1.0D);
            }

            newv.setY(oldv.getY() - dy * gravity);
            boolean newxchanged = (newv.getX() < -0.001D) || (newv.getX() > 0.001D);
            boolean oldxchanged = (oldv.getX() < -0.001D) || (oldv.getX() > 0.001D);
            if ((newxchanged) && (oldxchanged)) {
              newv.setX(oldv.getX());
            }

            boolean newzchanged = (newv.getZ() < -0.001D) || (newv.getZ() > 0.001D);
            boolean oldzchanged = (oldv.getZ() < -0.001D) || (oldv.getZ() > 0.001D);
            if ((newzchanged) && (oldzchanged)) {
              newv.setZ(oldv.getZ());
            }
            e.setVelocity(newv.clone());
          }
        }
        else if (((e instanceof Player)) && 
          (this.positions.containsKey(uuid))) {
          Vector pos = e.getLocation().toVector();
          Vector oldpos = ((Location)this.positions.get(uuid)).toVector();
          Vector velocity = pos.subtract(oldpos);
          newv.setX(velocity.getX());
          newv.setZ(velocity.getZ());
        }

        e.setVelocity(newv.clone());
      }

      this.velocities.put(uuid, newv.clone());
      this.onGround.put(uuid, Boolean.valueOf(e.isOnGround()));
      this.positions.put(uuid, e.getLocation());
    }
  }

  @EventHandler(priority=EventPriority.HIGHEST)
  public void onEntityDamageEvent(EntityDamageEvent e)
  {
    if (e.getCause() == EntityDamageEvent.DamageCause.FALL)
      e.setCancelled(true);
  }

@Override
public void fiberResumed(Fiber arg0) {
	// TODO Auto-generated method stub
	
}

@Override
public void fiberSuspended(Fiber arg0) {
	// TODO Auto-generated method stub
	
}
}