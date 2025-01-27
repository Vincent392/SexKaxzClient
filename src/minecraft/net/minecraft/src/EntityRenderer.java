package net.minecraft.src;

import java.nio.FloatBuffer;
import java.util.List;
import java.util.Random;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.GLU;

public class EntityRenderer {
	private Minecraft mc;
	private float farPlaneDistance = 0.0F;
	public ItemRenderer itemRenderer;
	private int rendererUpdateCount;
	private Entity jx2 = null;
	private long prevFrameTime = System.currentTimeMillis();
	private Random random = new Random();
	volatile int unusedInt1 = 0;
	volatile int unusedInt2 = 0;
	FloatBuffer fogColorBuffer = GLAllocation.createDirectFloatBuffer(16);
	float fogColorRed;
	float fogColorGreen;
	float fogColorBlue;
	private float prevFogColor;
	private float fogColor;

	public EntityRenderer(Minecraft var1) {
		this.mc = var1;
		this.itemRenderer = new ItemRenderer(var1);
	}

	public void updateRenderer() {
		this.prevFogColor = this.fogColor;
		float var1 = this.mc.theWorld.getBrightness(MathHelper.floor_double(this.mc.thePlayer.posX), MathHelper.floor_double(this.mc.thePlayer.posY), MathHelper.floor_double(this.mc.thePlayer.posZ));
		float var2 = (float)(3 - this.mc.options.renderDistance) / 3.0F;
		this.fogColor += (var1 * (1.0F - var2) + var2 - this.fogColor) * 0.1F;
		++this.rendererUpdateCount;
		this.itemRenderer.updateEquippedItem();
		if(this.mc.isRaining) {
			this.addRainParticles();
		}

	}

	public void getMouseOver(float var1) {
		if(this.mc.thePlayer != null) {
			double var2 = (double)this.mc.playerController.getBlockReachDistance();
			this.mc.objectMouseOver = this.mc.thePlayer.rayTrace(var2, var1);
			double var4 = var2;
			Vec3D var6 = this.mc.thePlayer.getPosition(var1);
			if(this.mc.objectMouseOver != null) {
				var4 = this.mc.objectMouseOver.hitVec.distanceTo(var6);
			}

			double var7;
			if(this.mc.playerController instanceof PlayerControllerCreative) {
				var7 = 32.0D;
			} else {
				if(var4 > 3.0D) {
					var4 = 3.0D;
				}

				var7 = var4;
			}

			Vec3D var9 = this.mc.thePlayer.getLook(var1);
			Vec3D var10 = var6.addVector(var9.xCoord * var7, var9.yCoord * var7, var9.zCoord * var7);
			this.jx2 = null;
			List var11 = this.mc.theWorld.getEntitiesWithinAABBExcludingEntity(this.mc.thePlayer, this.mc.thePlayer.boundingBox.addCoord(var9.xCoord * var7, var9.yCoord * var7, var9.zCoord * var7));
			double var12 = 0.0D;

			for(int var14 = 0; var14 < var11.size(); ++var14) {
				Entity var15 = (Entity)var11.get(var14);
				if(var15.canBeCollidedWith()) {
					MovingObjectPosition var17 = var15.boundingBox.expand((double)0.1F, (double)0.1F, (double)0.1F).calculateIntercept(var6, var10);
					if(var17 != null) {
						double var18 = var6.distanceTo(var17.hitVec);
						if(var18 < var12 || var12 == 0.0D) {
							this.jx2 = var15;
							var12 = var18;
						}
					}
				}
			}

			if(this.jx2 != null && !(this.mc.playerController instanceof PlayerControllerCreative)) {
				this.mc.objectMouseOver = new MovingObjectPosition(this.jx2);
			}

		}
	}

	private float getFOVModifier(float var1) {
		EntityPlayerSP var2 = this.mc.thePlayer;
		float var3 = 70.0F;
		if(var2.isInsideOfMaterial(Material.water)) {
			var3 = 60.0F;
		}

		if(var2.health <= 0) {
			var3 /= (1.0F - 500.0F / ((float)var2.deathTime + var1 + 500.0F)) * 2.0F + 1.0F;
		}

		return var3;
	}

	private void hurtCameraEffect(float var1) {
		EntityPlayerSP var2 = this.mc.thePlayer;
		float var3 = (float)var2.hurtTime - var1;
		if(var2.health <= 0) {
			GL11.glRotatef(40.0F - 8000.0F / ((float)var2.deathTime + var1 + 200.0F), 0.0F, 0.0F, 1.0F);
		}

		if(var3 >= 0.0F) {
			float var4 = var3 / (float)var2.maxHurtTime;
			float var5 = MathHelper.sin(var4 * var4 * var4 * var4 * (float)Math.PI);
			float var6 = var2.attackedAtYaw;
			GL11.glRotatef(-var6, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(-var5 * 14.0F, 0.0F, 0.0F, 1.0F);
			GL11.glRotatef(var6, 0.0F, 1.0F, 0.0F);
		}
	}

	private void setupViewBobbing(float var1) {
		if(!this.mc.options.thirdPersonView) {
			EntityPlayerSP var2 = this.mc.thePlayer;
			float var3 = var2.distanceWalkedModified + (var2.distanceWalkedModified - var2.prevDistanceWalkedModified) * var1;
			float var4 = var2.prevCameraYaw + (var2.cameraYaw - var2.prevCameraYaw) * var1;
			float var5 = var2.prevCameraPitch + (var2.cameraPitch - var2.prevCameraPitch) * var1;
			GL11.glTranslatef(MathHelper.sin(var3 * (float)Math.PI) * var4 * 0.5F, -Math.abs(MathHelper.cos(var3 * (float)Math.PI) * var4), 0.0F);
			GL11.glRotatef(MathHelper.sin(var3 * (float)Math.PI) * var4 * 3.0F, 0.0F, 0.0F, 1.0F);
			GL11.glRotatef(Math.abs(MathHelper.cos(var3 * (float)Math.PI + 0.2F) * var4) * 5.0F, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(var5, 1.0F, 0.0F, 0.0F);
		}
	}

	private void orientCamera(float var1) {
		EntityPlayerSP var2 = this.mc.thePlayer;
		double var3 = var2.prevPosX + (var2.posX - var2.prevPosX) * (double)var1;
		double var5 = var2.prevPosY + (var2.posY - var2.prevPosY) * (double)var1;
		double var7 = var2.prevPosZ + (var2.posZ - var2.prevPosZ) * (double)var1;
		if(this.mc.options.thirdPersonView) {
			double var9 = 4.0D;
			float var10000 = var2.prevRenderYawOffset + (var2.renderYawOffset - var2.prevRenderYawOffset) * var1;
			float var12 = var2.rotationYaw - 10.0F;
			float var13 = var2.rotationPitch + 2.0F;
			double var14 = (double)(-MathHelper.sin(var12 / 180.0F * (float)Math.PI) * MathHelper.cos(var13 / 180.0F * (float)Math.PI)) * var9;
			double var16 = (double)(MathHelper.cos(var12 / 180.0F * (float)Math.PI) * MathHelper.cos(var13 / 180.0F * (float)Math.PI)) * var9;
			double var18 = (double)(-MathHelper.sin(var13 / 180.0F * (float)Math.PI)) * var9;

			for(int var20 = 0; var20 < 8; ++var20) {
				float var21 = (float)((var20 & 1) * 2 - 1);
				float var22 = (float)((var20 >> 1 & 1) * 2 - 1);
				float var23 = (float)((var20 >> 2 & 1) * 2 - 1);
				float var24 = var21 * 0.1F;
				float var25 = var22 * 0.1F;
				float var26 = var23 * 0.1F;
				MovingObjectPosition var27 = this.mc.theWorld.rayTraceBlocks(Vec3D.createVector(var3 + (double)var24, var5 + (double)var25, var7 + (double)var26), Vec3D.createVector(var3 - var14 + (double)var24 + (double)var26, var5 - var18 + (double)var25, var7 - var16 + (double)var26));
				if(var27 != null) {
					double var28 = var27.hitVec.distanceTo(Vec3D.createVector(var3, var5, var7));
					if(var28 < var9) {
						var9 = var28;
					}
				}
			}

			GL11.glRotatef(var2.rotationPitch - var13, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(var2.rotationYaw - var12, 0.0F, 1.0F, 0.0F);
			GL11.glTranslatef(0.0F, 0.0F, (float)(-var9));
			GL11.glRotatef(var12 - var2.rotationYaw, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(var13 - var2.rotationPitch, 1.0F, 0.0F, 0.0F);
		} else {
			GL11.glTranslatef(0.0F, 0.0F, -0.1F);
		}

		GL11.glRotatef(var2.prevRotationPitch + (var2.rotationPitch - var2.prevRotationPitch) * var1, 1.0F, 0.0F, 0.0F);
		GL11.glRotatef(var2.prevRotationYaw + (var2.rotationYaw - var2.prevRotationYaw) * var1 + 180.0F, 0.0F, 1.0F, 0.0F);
	}

	private void setupCameraTransform(float var1, int var2) {
		this.farPlaneDistance = (float)(256 >> this.mc.options.renderDistance);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		if(this.mc.options.anaglyph) {
			GL11.glTranslatef((float)(-(var2 * 2 - 1)) * 0.07F, 0.0F, 0.0F);
		}

		GLU.gluPerspective(this.getFOVModifier(var1), (float)this.mc.displayWidth / (float)this.mc.displayHeight, 0.05F, this.farPlaneDistance);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		if(this.mc.options.anaglyph) {
			GL11.glTranslatef((float)(var2 * 2 - 1) * 0.1F, 0.0F, 0.0F);
		}

		this.hurtCameraEffect(var1);
		if(this.mc.options.viewBobbing) {
			this.setupViewBobbing(var1);
		}

		this.orientCamera(var1);
	}

	private void renderHand(float var1, int var2) {
		GL11.glLoadIdentity();
		if(this.mc.options.anaglyph) {
			GL11.glTranslatef((float)(var2 * 2 - 1) * 0.1F, 0.0F, 0.0F);
		}

		GL11.glPushMatrix();
		this.hurtCameraEffect(var1);
		if(this.mc.options.viewBobbing) {
			this.setupViewBobbing(var1);
		}

		if(!this.mc.options.thirdPersonView) {
			this.itemRenderer.renderItemInFirstPerson(var1);
		}

		GL11.glPopMatrix();
		if(!this.mc.options.thirdPersonView) {
			this.itemRenderer.renderOverlays(var1);
			this.hurtCameraEffect(var1);
		}

		if(this.mc.options.viewBobbing) {
			this.setupViewBobbing(var1);
		}

	}

	public void updateCameraAndRender(float var1) {
		if(!Display.isActive()) {
			if(System.currentTimeMillis() - this.prevFrameTime > 500L) {
				this.mc.displayInGameMenu();
			}
		} else {
			this.prevFrameTime = System.currentTimeMillis();
		}

		int var3;
		if(this.mc.inGameHasFocus) {
			this.mc.mouseHelper.mouseXYChange();
			int var2 = this.mc.mouseHelper.deltaX;
			var3 = this.mc.mouseHelper.deltaY;
			byte var4 = 1;
			if(this.mc.options.invertMouse) {
				var4 = -1;
			}

			this.mc.thePlayer.setAngles((float)var2, (float)(var3 * var4));
		}

		if(!this.mc.skipRenderWorld) {
			ScaledResolution var7 = new ScaledResolution(this.mc.displayWidth, this.mc.displayHeight);
			var3 = var7.getScaledWidth();
			int var8 = var7.getScaledHeight();
			int var5 = Mouse.getX() * var3 / this.mc.displayWidth;
			int var6 = var8 - Mouse.getY() * var8 / this.mc.displayHeight - 1;
			if(this.mc.theWorld != null) {
				this.renderWorld(var1);
				this.mc.ingameGUI.renderGameOverlay(var1, this.mc.currentScreen != null, var5, var6);
			} else {
				GL11.glViewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
				GL11.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
				GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
				GL11.glMatrixMode(GL11.GL_PROJECTION);
				GL11.glLoadIdentity();
				GL11.glMatrixMode(GL11.GL_MODELVIEW);
				GL11.glLoadIdentity();
				this.setupOverlayRendering();
			}

			if(this.mc.currentScreen != null) {
				GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
				this.mc.currentScreen.drawScreen(var5, var6, var1);
			}

		}
	}

	public void renderWorld(float var1) {
		this.getMouseOver(var1);
		EntityPlayerSP var2 = this.mc.thePlayer;
		RenderGlobal var3 = this.mc.renderGlobal;
		EffectRenderer var4 = this.mc.effectRenderer;
		double var5 = var2.lastTickPosX + (var2.posX - var2.lastTickPosX) * (double)var1;
		double var7 = var2.lastTickPosY + (var2.posY - var2.lastTickPosY) * (double)var1;
		double var9 = var2.lastTickPosZ + (var2.posZ - var2.lastTickPosZ) * (double)var1;

		for(int var11 = 0; var11 < 2; ++var11) {
			if(this.mc.options.anaglyph) {
				if(var11 == 0) {
					GL11.glColorMask(false, true, true, false);
				} else {
					GL11.glColorMask(true, false, false, false);
				}
			}

			GL11.glViewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
			this.updateFogColor(var1);
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			GL11.glEnable(GL11.GL_CULL_FACE);
			this.setupCameraTransform(var1, var11);
			ClippingHelperImplementation.getInstance();
			if(this.mc.options.renderDistance < 2) {
				this.setupFog(-1);
				var3.renderSky(var1);
			}

			GL11.glEnable(GL11.GL_FOG);
			this.setupFog(1);
			Frustum var12 = new Frustum();
			var12.setPosition(var5, var7, var9);
			this.mc.renderGlobal.clipRenderersByFrustum(var12, var1);
			this.mc.renderGlobal.updateRenderers(var2, false);
			this.setupFog(0);
			GL11.glEnable(GL11.GL_FOG);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/terrain.png"));
			GLStatics.a();
			var3.sortAndRender(var2, 0, (double)var1);
			GLStatics.b();
			var3.renderEntities(var2.getPosition(var1), var12, var1);
			var4.renderLitParticles(var2, var1);
			GLStatics.a();
			this.setupFog(0);
			var4.renderParticles(var2, var1);
			if(this.mc.objectMouseOver != null && var2.isInsideOfMaterial(Material.water)) {
				GL11.glDisable(GL11.GL_ALPHA_TEST);
				var3.drawBlockBreaking(var2, this.mc.objectMouseOver, 0, var2.inventory.getCurrentItem(), var1);
				var3.drawSelectionBox(var2, this.mc.objectMouseOver, 0, var2.inventory.getCurrentItem(), var1);
				GL11.glEnable(GL11.GL_ALPHA_TEST);
			}

			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			this.setupFog(0);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glDisable(GL11.GL_CULL_FACE);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/terrain.png"));
			if(this.mc.options.fancyGraphics) {
				GL11.glColorMask(false, false, false, false);
				int var13 = var3.sortAndRender(var2, 1, (double)var1);
				GL11.glColorMask(true, true, true, true);
				if(this.mc.options.anaglyph) {
					if(var11 == 0) {
						GL11.glColorMask(false, true, true, false);
					} else {
						GL11.glColorMask(true, false, false, false);
					}
				}

				if(var13 > 0) {
					var3.renderAllRenderLists(1, (double)var1);
				}
			} else {
				var3.sortAndRender(var2, 1, (double)var1);
			}

			GL11.glDepthMask(true);
			GL11.glEnable(GL11.GL_CULL_FACE);
			GL11.glDisable(GL11.GL_BLEND);
			if(this.mc.objectMouseOver != null && !var2.isInsideOfMaterial(Material.water)) {
				GL11.glDisable(GL11.GL_ALPHA_TEST);
				var3.drawBlockBreaking(var2, this.mc.objectMouseOver, 0, var2.inventory.getCurrentItem(), var1);
				var3.drawSelectionBox(var2, this.mc.objectMouseOver, 0, var2.inventory.getCurrentItem(), var1);
				GL11.glEnable(GL11.GL_ALPHA_TEST);
			}

			GL11.glDisable(GL11.GL_FOG);
			if(this.mc.theWorld.snowCovered) {
				this.renderSnow(var1);
			}

			if(this.mc.isRaining) {
				this.i(var1);
			}

			if(this.jx2 != null) {
							}

			this.setupFog(0);
			GL11.glEnable(GL11.GL_FOG);
			var3.renderClouds(var1);
			GL11.glDisable(GL11.GL_FOG);
			this.setupFog(1);
			GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
			this.renderHand(var1, var11);
			if(!this.mc.options.anaglyph) {
				return;
			}
		}

		GL11.glColorMask(true, true, true, false);
	}

	private void addRainParticles() {
		if(this.mc.options.fancyGraphics) {
			EntityPlayerSP var1 = this.mc.thePlayer;
			World var2 = this.mc.theWorld;
			int var3 = MathHelper.floor_double(var1.posX);
			int var4 = MathHelper.floor_double(var1.posY);
			int var5 = MathHelper.floor_double(var1.posZ);

			for(int var7 = 0; var7 < 150; ++var7) {
				int var8 = var3 + this.random.nextInt(16) - this.random.nextInt(16);
				int var9 = var5 + this.random.nextInt(16) - this.random.nextInt(16);
				int var10 = var2.getPrecipitationHeight(var8, var9);
				int var11 = var2.getBlockId(var8, var10 - 1, var9);
				if(var10 <= var4 + 16 && var10 >= var4 - 16) {
					float var12 = this.random.nextFloat();
					float var13 = this.random.nextFloat();
					if(var11 > 0) {
						this.mc.effectRenderer.addEffect(new EntityRainFX(var2, (double)((float)var8 + var12), (double)((float)var10 + 0.1F) - Block.blocksList[var11].minY, (double)((float)var9 + var13)));
					}
				}
			}

		}
	}

	private void renderSnow(float var1) {
		EntityPlayerSP var2 = this.mc.thePlayer;
		World var3 = this.mc.theWorld;
		int var4 = MathHelper.floor_double(var2.posX);
		int var5 = MathHelper.floor_double(var2.posY);
		int var6 = MathHelper.floor_double(var2.posZ);
		Tessellator var7 = Tessellator.instance;
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glNormal3f(0.0F, 1.0F, 0.0F);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/snow.png"));
		double var8 = var2.lastTickPosX + (var2.posX - var2.lastTickPosX) * (double)var1;
		double var10 = var2.lastTickPosY + (var2.posY - var2.lastTickPosY) * (double)var1;
		double var12 = var2.lastTickPosZ + (var2.posZ - var2.lastTickPosZ) * (double)var1;
		byte var14 = 5;
		if(this.mc.options.fancyGraphics) {
			var14 = 10;
		}

		for(int var15 = var4 - var14; var15 <= var4 + var14; ++var15) {
			for(int var16 = var6 - var14; var16 <= var6 + var14; ++var16) {
				int var17 = var3.getTopSolidOrLiquidBlock(var15, var16);
				if(var17 < 0) {
					var17 = 0;
				}

				int var18 = var5 - var14;
				int var19 = var5 + var14;
				if(var18 < var17) {
					var18 = var17;
				}

				if(var19 < var17) {
					var19 = var17;
				}

				if(var18 != var19) {
					this.random.setSeed((long)(var15 * var15 * 3121 + var15 * 45238971 + var16 * var16 * 418711 + var16 * 13761));
					float var21 = (float)this.rendererUpdateCount + var1;
					float var22 = ((float)(this.rendererUpdateCount & 511) + var1) / 512.0F;
					float var23 = this.random.nextFloat() + var21 * 0.01F * (float)this.random.nextGaussian();
					float var24 = this.random.nextFloat() + var21 * (float)this.random.nextGaussian() * 0.001F;
					double var25 = (double)((float)var15 + 0.5F) - var2.posX;
					double var27 = (double)((float)var16 + 0.5F) - var2.posZ;
					float var29 = MathHelper.sqrt_double(var25 * var25 + var27 * var27) / (float)var14;
					var7.startDrawingQuads();
					float var30 = var3.getBrightness(var15, 128, var16);
					GL11.glColor4f(var30, var30, var30, (1.0F - var29 * var29) * 0.7F);
					var7.setTranslationD(-var8 * 1.0D, -var10 * 1.0D, -var12 * 1.0D);
					var7.addVertexWithUV((double)(var15 + 0), (double)var18, (double)(var16 + 0), (double)(0.0F + var23), (double)((float)var18 * 2.0F / 8.0F + var22 * 2.0F + var24));
					var7.addVertexWithUV((double)(var15 + 1), (double)var18, (double)(var16 + 1), (double)(2.0F + var23), (double)((float)var18 * 2.0F / 8.0F + var22 * 2.0F + var24));
					var7.addVertexWithUV((double)(var15 + 1), (double)var19, (double)(var16 + 1), (double)(2.0F + var23), (double)((float)var19 * 2.0F / 8.0F + var22 * 2.0F + var24));
					var7.addVertexWithUV((double)(var15 + 0), (double)var19, (double)(var16 + 0), (double)(0.0F + var23), (double)((float)var19 * 2.0F / 8.0F + var22 * 2.0F + var24));
					var7.addVertexWithUV((double)(var15 + 0), (double)var18, (double)(var16 + 1), (double)(0.0F + var23), (double)((float)var18 * 2.0F / 8.0F + var22 * 2.0F + var24));
					var7.addVertexWithUV((double)(var15 + 1), (double)var18, (double)(var16 + 0), (double)(2.0F + var23), (double)((float)var18 * 2.0F / 8.0F + var22 * 2.0F + var24));
					var7.addVertexWithUV((double)(var15 + 1), (double)var19, (double)(var16 + 0), (double)(2.0F + var23), (double)((float)var19 * 2.0F / 8.0F + var22 * 2.0F + var24));
					var7.addVertexWithUV((double)(var15 + 0), (double)var19, (double)(var16 + 1), (double)(0.0F + var23), (double)((float)var19 * 2.0F / 8.0F + var22 * 2.0F + var24));
					var7.setTranslationD(0.0D, 0.0D, 0.0D);
					var7.draw();
				}
			}
		}

		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glDisable(GL11.GL_BLEND);
	}

	private void i(float var1) {
		EntityPlayerSP var2 = this.mc.thePlayer;
		World var3 = this.mc.theWorld;
		int var4 = MathHelper.floor_double(var2.posX);
		int var5 = MathHelper.floor_double(var2.posY);
		int var6 = MathHelper.floor_double(var2.posZ);
		Tessellator var7 = Tessellator.instance;
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glNormal3f(0.0F, 1.0F, 0.0F);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/rain.png"));
		double var8 = var2.lastTickPosX + (var2.posX - var2.lastTickPosX) * (double)var1;
		double var10 = var2.lastTickPosY + (var2.posY - var2.lastTickPosY) * (double)var1;
		double var12 = var2.lastTickPosZ + (var2.posZ - var2.lastTickPosZ) * (double)var1;
		byte var14 = 5;
		if(this.mc.options.fancyGraphics) {
			var14 = 10;
		}

		for(int var15 = var4 - var14; var15 <= var4 + var14; ++var15) {
			for(int var16 = var6 - var14; var16 <= var6 + var14; ++var16) {
				int var17 = var3.getPrecipitationHeight(var15, var16);
				int var18 = var5 - var14;
				int var19 = var5 + var14;
				if(var18 < var17) {
					var18 = var17;
				}

				if(var19 < var17) {
					var19 = var17;
				}

				if(var18 != var19) {
					float var21 = ((float)(this.rendererUpdateCount + var15 * var15 * 3121 + var15 * 45238971 + var16 * var16 * 418711 + var16 * 13761 & 31) + var1) / 32.0F;
					double var22 = (double)((float)var15 + 0.5F) - var2.posX;
					double var24 = (double)((float)var16 + 0.5F) - var2.posZ;
					float var26 = MathHelper.sqrt_double(var22 * var22 + var24 * var24) / (float)var14;
					var7.startDrawingQuads();
					float var27 = var3.getBrightness(var15, 128, var16);
					GL11.glColor4f(var27, var27, var27, (1.0F - var26 * var26) * 0.7F);
					var7.setTranslationD(-var8 * 1.0D, -var10 * 1.0D, -var12 * 1.0D);
					var7.addVertexWithUV((double)(var15 + 0), (double)var18, (double)(var16 + 0), 0.0D, (double)((float)var18 * 2.0F / 8.0F + var21 * 2.0F));
					var7.addVertexWithUV((double)(var15 + 1), (double)var18, (double)(var16 + 1), 2.0D, (double)((float)var18 * 2.0F / 8.0F + var21 * 2.0F));
					var7.addVertexWithUV((double)(var15 + 1), (double)var19, (double)(var16 + 1), 2.0D, (double)((float)var19 * 2.0F / 8.0F + var21 * 2.0F));
					var7.addVertexWithUV((double)(var15 + 0), (double)var19, (double)(var16 + 0), 0.0D, (double)((float)var19 * 2.0F / 8.0F + var21 * 2.0F));
					var7.addVertexWithUV((double)(var15 + 0), (double)var18, (double)(var16 + 1), 0.0D, (double)((float)var18 * 2.0F / 8.0F + var21 * 2.0F));
					var7.addVertexWithUV((double)(var15 + 1), (double)var18, (double)(var16 + 0), 2.0D, (double)((float)var18 * 2.0F / 8.0F + var21 * 2.0F));
					var7.addVertexWithUV((double)(var15 + 1), (double)var19, (double)(var16 + 0), 2.0D, (double)((float)var19 * 2.0F / 8.0F + var21 * 2.0F));
					var7.addVertexWithUV((double)(var15 + 0), (double)var19, (double)(var16 + 1), 0.0D, (double)((float)var19 * 2.0F / 8.0F + var21 * 2.0F));
					var7.setTranslationD(0.0D, 0.0D, 0.0D);
					var7.draw();
				}
			}
		}

		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glDisable(GL11.GL_BLEND);
	}

	public void setupOverlayRendering() {
		ScaledResolution var1 = new ScaledResolution(this.mc.displayWidth, this.mc.displayHeight);
		int var2 = var1.getScaledWidth();
		int var3 = var1.getScaledHeight();
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0.0D, (double)var2, (double)var3, 0.0D, 1000.0D, 3000.0D);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
	}

	private void updateFogColor(float var1) {
		World var2 = this.mc.theWorld;
		EntityPlayerSP var3 = this.mc.thePlayer;
		float var4 = 1.0F - (float)Math.pow((double)(1.0F / (float)(4 - this.mc.options.renderDistance)), 0.25D);
		Vec3D var5 = var2.getSkyColor(var1);
		float var6 = (float)var5.xCoord;
		float var7 = (float)var5.yCoord;
		float var8 = (float)var5.zCoord;
		Vec3D var9 = var2.getFogColor(var1);
		this.fogColorRed = (float)var9.xCoord;
		this.fogColorGreen = (float)var9.yCoord;
		this.fogColorBlue = (float)var9.zCoord;
		this.fogColorRed += (var6 - this.fogColorRed) * var4;
		this.fogColorGreen += (var7 - this.fogColorGreen) * var4;
		this.fogColorBlue += (var8 - this.fogColorBlue) * var4;
		if(var3.isInsideOfMaterial(Material.water)) {
			this.fogColorRed = 0.02F;
			this.fogColorGreen = 0.02F;
			this.fogColorBlue = 0.2F;
		} else if(var3.isInsideOfMaterial(Material.lava)) {
			this.fogColorRed = 0.6F;
			this.fogColorGreen = 0.1F;
			this.fogColorBlue = 0.0F;
		}

		float var10 = this.prevFogColor + (this.fogColor - this.prevFogColor) * var1;
		this.fogColorRed *= var10;
		this.fogColorGreen *= var10;
		this.fogColorBlue *= var10;
		float var11;
		if(this.mc.options.anaglyph) {
			var11 = (this.fogColorRed * 30.0F + this.fogColorGreen * 59.0F + this.fogColorBlue * 11.0F) / 100.0F;
			float var12 = (this.fogColorRed * 30.0F + this.fogColorGreen * 70.0F) / 100.0F;
			float var13 = (this.fogColorRed * 30.0F + this.fogColorBlue * 70.0F) / 100.0F;
			this.fogColorRed = var11;
			this.fogColorGreen = var12;
			this.fogColorBlue = var13;
		}

		var11 = (float)(1000L - Math.abs(23000L - var2.worldTime % 24000L)) / 1000.0F;
		float[] var14 = ColorUtil.BlendColor(Math.max(0.0F, var11) / 2.0F, this.fogColorRed, this.fogColorGreen, this.fogColorBlue);
		this.fogColorRed = var14[0];
		this.fogColorGreen = var14[1];
		this.fogColorBlue = var14[2];
		GL11.glClearColor(this.fogColorRed, this.fogColorGreen, this.fogColorBlue, 0.0F);
	}

	private void setupFog(int var1) {
		EntityPlayerSP var2 = this.mc.thePlayer;
		GL11.glFog(GL11.GL_FOG_COLOR, this.setFogColorBuffer(this.fogColorRed, this.fogColorGreen, this.fogColorBlue, 1.0F));
		GL11.glNormal3f(0.0F, -1.0F, 0.0F);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		if(var2.isInsideOfMaterial(Material.water)) {
			GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_EXP);
			GL11.glFogf(GL11.GL_FOG_DENSITY, 0.1F);
			if(this.mc.options.anaglyph) {
							}
		} else if(var2.isInsideOfMaterial(Material.lava)) {
			GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_EXP);
			GL11.glFogf(GL11.GL_FOG_DENSITY, 2.0F);
			if(this.mc.options.anaglyph) {
							}
		} else {
			GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_LINEAR);
			GL11.glFogf(GL11.GL_FOG_START, this.farPlaneDistance * 0.25F);
			GL11.glFogf(GL11.GL_FOG_END, this.farPlaneDistance);
			if(var1 < 0) {
				GL11.glFogf(GL11.GL_FOG_START, 0.0F);
				GL11.glFogf(GL11.GL_FOG_END, this.farPlaneDistance * 0.8F);
			}

			if(GLContext.getCapabilities().GL_NV_fog_distance) {
				GL11.glFogi('\u855a', '\u855b');
			}
		}

		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		GL11.glColorMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT);
	}

	private FloatBuffer setFogColorBuffer(float var1, float var2, float var3, float var4) {
		this.fogColorBuffer.clear();
		this.fogColorBuffer.put(var1).put(var2).put(var3).put(var4);
		this.fogColorBuffer.flip();
		return this.fogColorBuffer;
	}
}
