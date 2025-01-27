package net.minecraft.src;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

public class GuiIngame extends Gui {
	private static RenderItem itemRenderer = new RenderItem();
	List chatMessageList = new ArrayList();
	private Random rand = new Random();
	private Minecraft mc;
	public String testMessage = null;
	private int updateCounter = 0;
	private String recordPlaying = "";
	private int recordPlayingUpFor = 0;
	public float damageGuiPartialTime;
	float prevVignetteBrightness = 1.0F;

	public void renderGameOverlay(float var1, boolean var2, int var3, int var4) {
		ScaledResolution var5 = new ScaledResolution(this.mc.displayWidth, this.mc.displayHeight);
		int var6 = var5.getScaledWidth();
		int var7 = var5.getScaledHeight();
		FontRenderer var8 = this.mc.fontRenderer;
		this.mc.entityRenderer.setupOverlayRendering();
		GL11.glEnable(GL11.GL_BLEND);
		if(this.mc.options.fancyGraphics) {
			this.renderVignette(this.mc.thePlayer.getBrightness(var1), var6, var7);
		}

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/gui/gui.png"));
		InventoryPlayer var9 = this.mc.thePlayer.inventory;
		this.zLevel = -90.0F;
		this.drawTexturedModalRect(var6 / 2 - 91, var7 - 22, 0, 0, 182, 22);
		this.drawTexturedModalRect(var6 / 2 - 91 - 1 + var9.currentItem * 20, var7 - 22 - 1, 0, 22, 24, 22);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/gui/icons.png"));
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_ONE_MINUS_DST_COLOR, GL11.GL_ONE_MINUS_SRC_COLOR);
		this.drawTexturedModalRect(var6 / 2 - 7, var7 / 2 - 7, 0, 0, 16, 16);
		GL11.glDisable(GL11.GL_BLEND);
		boolean var10 = this.mc.thePlayer.heartsLife / 3 % 2 == 1;
		if(this.mc.thePlayer.heartsLife < 10) {
			var10 = false;
		}

		int var11 = this.mc.thePlayer.health;
		int var12 = this.mc.thePlayer.prevHealth;
		this.rand.setSeed((long)(this.updateCounter * 312871));
		int var14;
		int var15;
		int var16;
		if(this.mc.playerController.shouldDrawHUD()) {
			int var13 = this.mc.thePlayer.getPlayerArmorValue();

			for(var14 = 0; var14 < 10; ++var14) {
				var15 = var7 - 32;
				if(var13 > 0) {
					var16 = var6 / 2 + 91 - var14 * 8 - 9;
					if(var14 * 2 + 1 < var13) {
						this.drawTexturedModalRect(var16, var15, 34, 9, 9, 9);
					}

					if(var14 * 2 + 1 == var13) {
						this.drawTexturedModalRect(var16, var15, 25, 9, 9, 9);
					}

					if(var14 * 2 + 1 > var13) {
						this.drawTexturedModalRect(var16, var15, 16, 9, 9, 9);
					}
				}

				byte var28 = 0;
				if(var10) {
					var28 = 1;
				}

				int var17 = var6 / 2 - 91 + var14 * 8;
				if(var11 <= 4) {
					var15 += this.rand.nextInt(2);
				}

				this.drawTexturedModalRect(var17, var15, 16 + var28 * 9, 0, 9, 9);
				if(var10) {
					if(var14 * 2 + 1 < var12) {
						this.drawTexturedModalRect(var17, var15, 70, 0, 9, 9);
					}

					if(var14 * 2 + 1 == var12) {
						this.drawTexturedModalRect(var17, var15, 79, 0, 9, 9);
					}
				}

				if(var14 * 2 + 1 < var11) {
					this.drawTexturedModalRect(var17, var15, 52, 0, 9, 9);
				}

				if(var14 * 2 + 1 == var11) {
					this.drawTexturedModalRect(var17, var15, 61, 0, 9, 9);
				}
			}

			if(this.mc.thePlayer.isInsideOfMaterial(Material.water)) {
				var14 = (int)Math.ceil((double)(this.mc.thePlayer.air - 2) * 10.0D / 300.0D);
				var15 = (int)Math.ceil((double)this.mc.thePlayer.air * 10.0D / 300.0D) - var14;

				for(byte var29 = 0; var29 < var14 + var15; ++var29) {
					if(var29 < var14) {
						this.drawTexturedModalRect(var6 / 2 - 91 + var29 * 8, var7 - 32 - 9, 16, 18, 9, 9);
					} else {
						this.drawTexturedModalRect(var6 / 2 - 91 + var29 * 8, var7 - 32 - 9, 25, 18, 9, 9);
					}
				}
			}
		}

		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable('\u803a');
		GL11.glPushMatrix();
		GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
		GLStatics.b();
		GL11.glPopMatrix();

		for(byte var23 = 0; var23 < 9; ++var23) {
			var14 = var6 / 2 - 90 + var23 * 20 + 2;
			var15 = var7 - 16 - 3;
			this.renderInventorySlot(var23, var14, var15, var1);
		}

		GLStatics.a();
		GL11.glDisable('\u803a');
		if(this.mc.options.d) {
			var8.drawStringWithShadow("Minecraft Alpha v1.0.16.05 [UNR.PREVIEW2]", 2, 2, this.mc.rainbowColor);
			var8.drawStringWithShadow(this.mc.debug, 2, 12, 0xFFFFFF);
			var8.drawStringWithShadow(this.mc.debugInfoRenders(), 2, 22, 0xFFFFFF);
			var8.drawStringWithShadow(this.mc.getEntityDebug(), 2, 32, 0xFFFFFF);
			var8.drawStringWithShadow(this.mc.debugInfoEntities(), 2, 42, 0xFFFFFF);
			long var25 = Runtime.getRuntime().maxMemory();
			long var31 = Runtime.getRuntime().totalMemory();
			long var18 = Runtime.getRuntime().freeMemory();
			long var20 = var31 - var18;
			if (this.mc.thePlayer != null && this.mc.shouldCoords) {
				var8.drawStringWithShadow("x: " + this.mc.thePlayer.posX, 2, 52, this.mc.rainbowColor);
				var8.drawStringWithShadow("y: " + this.mc.thePlayer.posY, 2, 62, this.mc.rainbowColor);
				var8.drawStringWithShadow("z: " + this.mc.thePlayer.posZ, 2, 72, this.mc.rainbowColor);
			}
			String var22 = "Used memory: " + var20 * 100L / var25 + "% (" + var20 / 1024L / 1024L + "MB) of " + var25 / 1024L / 1024L + "MB";
			this.drawString(var8, var22, var6 - var8.getStringWidth(var22) - 2, 2, 14737632);
			var22 = "Allocated memory: " + var31 * 100L / var25 + "% (" + var31 / 1024L / 1024L + "MB)";
			this.drawString(var8, var22, var6 - var8.getStringWidth(var22) - 2, 12, 14737632);
		} else {
			var8.drawStringWithShadow("Minecraft Alpha v1.0.16.05 [UNR.PREVIEW2]", 2, 2, this.mc.rainbowColor);
			if (this.mc.thePlayer != null && this.mc.shouldCoords) {
				var8.drawStringWithShadow("x: " + this.mc.thePlayer.posX, 2, 12, this.mc.rainbowColor);
				var8.drawStringWithShadow("y: " + this.mc.thePlayer.posY, 2, 22, this.mc.rainbowColor);
				var8.drawStringWithShadow("z: " + this.mc.thePlayer.posZ, 2, 32, this.mc.rainbowColor);
			}
		}

		if(this.recordPlayingUpFor > 0) {
			float var26 = (float)this.recordPlayingUpFor - var1;
			var15 = (int)(var26 * 256.0F / 20.0F);
			if(var15 > 255) {
				var15 = 255;
			}

			if(var15 > 0) {
				GL11.glPushMatrix();
				GL11.glTranslatef((float)(var6 / 2), (float)(var7 - 48), 0.0F);
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				var16 = Color.HSBtoRGB(var26 / 50.0F, 0.7F, 0.6F) & 0xFFFFFF;
				var8.drawString(this.recordPlaying, -var8.getStringWidth(this.recordPlaying) / 2, -4, var16 + (var15 << 24));
				GL11.glDisable(GL11.GL_BLEND);
				GL11.glPopMatrix();
			}
		}

		byte var24 = 10;
		boolean var27 = false;
		if(this.mc.currentScreen instanceof GuiChat) {
			var24 = 20;
			var27 = true;
		}

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glPushMatrix();
		GL11.glTranslatef(0.0F, (float)(var7 - 48), 0.0F);

		for(byte var30 = 0; var30 < this.chatMessageList.size() && var30 < var24; ++var30) {
			if(((ChatLine)this.chatMessageList.get(var30)).updateCounter < 200 || var27) {
				double var33 = (double)((ChatLine)this.chatMessageList.get(var30)).updateCounter / 200.0D;
				var33 = 1.0D - var33;
				var33 *= 10.0D;
				if(var33 < 0.0D) {
					var33 = 0.0D;
				}

				if(var33 > 1.0D) {
					var33 = 1.0D;
				}

				var33 *= var33;
				int var32 = (int)(255.0D * var33);
				if(var27) {
					var32 = 255;
				}

				if(var32 > 0) {
					byte var19 = 2;
					int var34 = -var30 * 9;
					String var21 = ((ChatLine)this.chatMessageList.get(var30)).message;
					this.drawRect(var19, var34 - 1, var19 + 320, var34 + 8, var32 / 2 << 24);
					GL11.glEnable(GL11.GL_BLEND);
					var8.drawStringWithShadow(var21, var19+1, var34, 0xFFFFFF + (var32 << 24));
				}
			}
		}

		if(GuiScreen.currentID != "") {
			var8.drawStringWithShadow("[Use numpad to enter, - to clear, + to give]", 2, 10, 16449390);
			var8.drawStringWithShadow("Give item: " + GuiScreen.currentID, 2, 20, 16449390);
			var15 = Integer.parseInt(GuiScreen.currentID);
			if((Block.blocksList.length <= var15 || Block.blocksList[var15] == null) && (Item.itemsList.length <= var15 || Item.itemsList[var15] == null)) {
				var8.drawStringWithShadow("(INVALID)", 2, 30, 16711680);
			}
		}

		GL11.glPopMatrix();
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glDisable(GL11.GL_BLEND);
	}

	public GuiIngame(Minecraft var1) {
		this.mc = var1;
	}

	private void renderVignette(float var1, int var2, int var3) {
		var1 = 1.0F - var1;
		if(var1 < 0.0F) {
			var1 = 0.0F;
		}

		if(var1 > 1.0F) {
			var1 = 1.0F;
		}

		this.prevVignetteBrightness = (float)((double)this.prevVignetteBrightness + (double)(var1 - this.prevVignetteBrightness) * 0.01D);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		GL11.glBlendFunc(GL11.GL_ZERO, GL11.GL_ONE_MINUS_SRC_COLOR);
		GL11.glColor4f(this.prevVignetteBrightness, this.prevVignetteBrightness, this.prevVignetteBrightness, 1.0F);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/misc/vignette.png"));
		Tessellator var4 = Tessellator.instance;
		var4.startDrawingQuads();
		var4.addVertexWithUV(0.0D, (double)var3, -90.0D, 0.0D, 1.0D);
		var4.addVertexWithUV((double)var2, (double)var3, -90.0D, 1.0D, 1.0D);
		var4.addVertexWithUV((double)var2, 0.0D, -90.0D, 1.0D, 0.0D);
		var4.addVertexWithUV(0.0D, 0.0D, -90.0D, 0.0D, 0.0D);
		var4.draw();
		GL11.glDepthMask(true);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	}

	private void renderInventorySlot(int var1, int var2, int var3, float var4) {
		ItemStack var5 = this.mc.thePlayer.inventory.mainInventory[var1];
		if(var5 != null) {
			float var6 = (float)var5.animationsToGo - var4;
			if(var6 > 0.0F) {
				GL11.glPushMatrix();
				float var7 = 1.0F + var6 / 5.0F;
				GL11.glTranslatef((float)(var2 + 8), (float)(var3 + 12), 0.0F);
				GL11.glScalef(1.0F / var7, (var7 + 1.0F) / 2.0F, 1.0F);
				GL11.glTranslatef((float)(-(var2 + 8)), (float)(-(var3 + 12)), 0.0F);
			}

			itemRenderer.renderItemIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, var5, var2, var3);
			if(var6 > 0.0F) {
				GL11.glPopMatrix();
			}

			itemRenderer.renderItemOverlayIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, var5, var2, var3);
		}
	}

	public void updateTick() {
		if(this.recordPlayingUpFor > 0) {
			--this.recordPlayingUpFor;
		}

		++this.updateCounter;

		for(byte var1 = 0; var1 < this.chatMessageList.size(); ++var1) {
			++((ChatLine)this.chatMessageList.get(var1)).updateCounter;
		}

	}

	public void addChatMessage(String var1) {
		while(this.mc.fontRenderer.getStringWidth(var1) > 320) {
			byte var2;
			for(var2 = 1; var2 < var1.length() && this.mc.fontRenderer.getStringWidth(var1.substring(0, var2 + 1)) <= 320; ++var2) {
			}

			this.addChatMessage(var1.substring(0, var2));
			var1 = var1.substring(var2);
		}

		this.chatMessageList.add(0, new ChatLine(var1));

		while(this.chatMessageList.size() > 50) {
			this.chatMessageList.remove(this.chatMessageList.size() - 1);
		}

	}

	public void setRecordPlayingMessage(String var1) {
		this.recordPlaying = "Now playing: " + var1;
		this.recordPlayingUpFor = 60;
	}
}
