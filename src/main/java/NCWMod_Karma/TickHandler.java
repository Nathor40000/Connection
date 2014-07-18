package NCWMod_Karma;

import java.util.EnumSet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;

public class TickHandler implements ITickHandler {
private final Minecraft mc;

public TickHandler() {
mc = Minecraft.getMinecraft();
}

public void tickStart(EnumSet<TickType> type, Object... tickData) {

}

public void tickEnd(EnumSet<TickType> type, Object... tickData) {
// setup render
ScaledResolution res = new ScaledResolution(this.mc.gameSettings,
this.mc.displayWidth, this.mc.displayHeight);
FontRenderer fontRender = mc.fontRenderer;
int width = res.getScaledWidth();
int height = res.getScaledHeight();
mc.entityRenderer.setupOverlayRendering();

// draw
String text = "Hello World";
int x = 100;
int y = 200;
int color = 0xFFFFFF;
fontRender.drawStringWithShadow(text, x, y, color);

}

public EnumSet<TickType> ticks() {
return EnumSet.of(TickType.RENDER);
}

public String getLabel() {
return "tickhandler";
}

}