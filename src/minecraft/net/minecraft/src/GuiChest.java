package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class GuiChest extends GuiContainer {
	private IInventory upperChestInventory;
	private IInventory lowerChestInventory;
	private int inventoryRows = 0;

	public GuiChest(IInventory var1, IInventory var2) {
		this.upperChestInventory = var1;
		this.lowerChestInventory = var2;
		this.allowUserInput = false;
		this.inventoryRows = var2.getSizeInventory() / 9;
		this.ySize = 114 + this.inventoryRows * 18;
		int var4 = (this.inventoryRows - 4) * 18;

		int var5;
		int var6;
		for(var5 = 0; var5 < this.inventoryRows; ++var5) {
			for(var6 = 0; var6 < 9; ++var6) {
				this.inventorySlots.add(new SlotInventory(this, var2, var6 + var5 * 9, 8 + var6 * 18, 18 + var5 * 18));
			}
		}

		for(var5 = 0; var5 < 3; ++var5) {
			for(var6 = 0; var6 < 9; ++var6) {
				this.inventorySlots.add(new SlotInventory(this, var1, var6 + (var5 + 1) * 9, 8 + var6 * 18, 103 + var5 * 18 + var4));
			}
		}

		for(var5 = 0; var5 < 9; ++var5) {
			this.inventorySlots.add(new SlotInventory(this, var1, var5, 8 + var5 * 18, 161 + var4));
		}

	}

	protected void drawGuiContainerForegroundLayer() {
		this.fontRenderer.drawString(this.lowerChestInventory.getInvName(), 8, 6, 0xFFFFFF);
		this.fontRenderer.drawString(this.upperChestInventory.getInvName(), 8, this.ySize - 96 + 2, 0xFFFFFF);
	}

	protected void drawGuiContainerBackgroundLayer(float var1) {
		int var2 = this.mc.renderEngine.getTexture("/gui/container.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(var2);
		int var3 = (this.width - this.xSize) / 2;
		int var4 = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(var3, var4, 0, 0, this.xSize, this.inventoryRows * 18 + 17);
		this.drawTexturedModalRect(var3, var4 + this.inventoryRows * 18 + 17, 0, 126, this.xSize, 96);
	}
}
