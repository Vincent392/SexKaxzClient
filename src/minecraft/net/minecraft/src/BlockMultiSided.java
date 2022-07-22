package net.minecraft.src;

import java.util.Random;

public class BlockMultiSided extends Block {
	private int texTop;
	private int texSide;
	private int texBottom;

	protected BlockMultiSided(int var1, int var2, int var3, int var4) {
		super(var1, Material.grass);
		this.blockIndexInTexture = 3;
		this.setTickOnLoad(true);
		this.texTop = var2;
		this.texSide = var3;
		this.texBottom = var4;
	}

	public int getBlockTexture(IBlockAccess var1, int var2, int var3, int var4, int var5) {
		return var5 == 1?this.texTop:(var5 == 0?this.texBottom:this.texSide);
	}

	public void updateTick(World var1, int var2, int var3, int var4, Random var5) {
		if(var1.getBlockLightValue(var2, var3 + 1, var4) < 4 && var1.getBlockMaterial(var2, var3 + 1, var4).getCanBlockGrass()) {
			if(var5.nextInt(4) != 0) {
				return;
			}

			var1.setBlockWithNotify(var2, var3, var4, Block.dirt.blockID);
		} else if(var1.getBlockLightValue(var2, var3 + 1, var4) >= 9) {
			int var6 = var2 + var5.nextInt(3) - 1;
			int var7 = var3 + var5.nextInt(5) - 3;
			int var8 = var4 + var5.nextInt(3) - 1;
			if(var1.getBlockId(var6, var7, var8) == Block.dirt.blockID && var1.getBlockLightValue(var6, var7 + 1, var8) >= 4 && !var1.getBlockMaterial(var6, var7 + 1, var8).getCanBlockGrass()) {
				var1.setBlockWithNotify(var6, var7, var8, Block.grass.blockID);
			}
		}

	}

	public int idDropped(int var1, Random var2) {
		return Block.dirt.idDropped(0, var2);
	}
}
