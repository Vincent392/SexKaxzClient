package net.minecraft.src;

public class BlockHidable extends BlockBreakable {
	private boolean hide = false;
	private int texID;

	public BlockHidable(int var1, int var2, Material var3, boolean var4) {
		super(var1, var2, var3, var4);
		this.texID = var2;
	}

	public int getBlockTextureFromSide(int var1) {
		return this.hide?160:this.texID;
	}

	public boolean blockActivated(World var1, int var2, int var3, int var4, EntityPlayer var5) {
		this.hide = !this.hide;
		System.out.println("Toggled block: " + this.hide);
		return true;
	}
}
