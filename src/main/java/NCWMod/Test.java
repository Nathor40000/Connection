package NCWMod;

import NCWMod_Karma.TickHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.common.registry.I
@Mod(modid = Test.MODID, version = Test.VERSION)

public class Test {
	public static final String MODID = "NCWMod Core";
    public static final String VERSION = "1.0";
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        
    	
    }
    public void TickHandler() {
        TickRegistry.registerTickHandler(new TickHandler(), Side.CLIENT);
    	}
    
}

    
    
   
