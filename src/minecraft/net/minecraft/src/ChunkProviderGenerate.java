package net.minecraft.src;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

import net.minecraft.client.Minecraft;

public class ChunkProviderGenerate implements IChunkProvider {
	private Random rand;
	private NoiseGeneratorOctaves noiseGen1;
	private NoiseGeneratorOctaves noiseGen2;
	private NoiseGeneratorOctaves noiseGen3;
	private NoiseGeneratorOctaves noiseGen4;
	private NoiseGeneratorOctaves noiseGen5;
	public NoiseGeneratorOctaves noiseGen6;
	public NoiseGeneratorOctaves noiseGen7;
	public NoiseGeneratorOctaves mobSpawnerNoise;
	private World worldObj;
	private double[] noiseArray;
	private double[] sandNoise = new double[256];
	private double[] gravelNoise = new double[256];
	private double[] stoneNoise = new double[256];
	double[] noise3;
	double[] noise1;
	double[] noise2;
	double[] noise6;
	double[] noise7;
	int[][] unused = new int[32][32];
	private int time_hr = 0;
	private long lastUpdate = 0L;

	public ChunkProviderGenerate(World var1, long var2) {
		this.worldObj = var1;
		this.rand = new Random(var2);
		this.noiseGen1 = new NoiseGeneratorOctaves(this.rand, 16);
		this.noiseGen2 = new NoiseGeneratorOctaves(this.rand, 16);
		this.noiseGen3 = new NoiseGeneratorOctaves(this.rand, 8);
		this.noiseGen4 = new NoiseGeneratorOctaves(this.rand, 4);
		this.noiseGen5 = new NoiseGeneratorOctaves(this.rand, 4);
		this.noiseGen6 = new NoiseGeneratorOctaves(this.rand, 10);
		this.noiseGen7 = new NoiseGeneratorOctaves(this.rand, 16);
		this.mobSpawnerNoise = new NoiseGeneratorOctaves(this.rand, 8);
	}

	public void generateTerrain(int var1, int var2, byte[] var3) {
		this.noiseArray = this.initializeNoiseField(this.noiseArray, var1 * 4, 0, var2 * 4, 5, 17, 5);

		for(int var9 = 0; var9 < 4; ++var9) {
			for(int var10 = 0; var10 < 4; ++var10) {
				for(int var11 = 0; var11 < 16; ++var11) {
					double var14 = this.noiseArray[((var9 + 0) * 5 + var10 + 0) * 17 + var11 + 0];
					double var16 = this.noiseArray[((var9 + 0) * 5 + var10 + 1) * 17 + var11 + 0];
					double var18 = this.noiseArray[((var9 + 1) * 5 + var10 + 0) * 17 + var11 + 0];
					double var20 = this.noiseArray[((var9 + 1) * 5 + var10 + 1) * 17 + var11 + 0];
					double var22 = (this.noiseArray[((var9 + 0) * 5 + var10 + 0) * 17 + var11 + 1] - var14) * 0.125D;
					double var24 = (this.noiseArray[((var9 + 0) * 5 + var10 + 1) * 17 + var11 + 1] - var16) * 0.125D;
					double var26 = (this.noiseArray[((var9 + 1) * 5 + var10 + 0) * 17 + var11 + 1] - var18) * 0.125D;
					double var28 = (this.noiseArray[((var9 + 1) * 5 + var10 + 1) * 17 + var11 + 1] - var20) * 0.125D;

					for(int var30 = 0; var30 < 8; ++var30) {
						double var33 = var14;
						double var35 = var16;
						double var37 = (var18 - var14) * 0.25D;
						double var39 = (var20 - var16) * 0.25D;

						for(int var41 = 0; var41 < 4; ++var41) {
							int var42 = var41 + var9 * 4 << 11 | 0 + var10 * 4 << 7 | var11 * 8 + var30;
							double var46 = var33;
							double var48 = (var35 - var33) * 0.25D;

							for(int var50 = 0; var50 < 4; ++var50) {
								int var51 = 0;
								if(var11 * 8 + var30 < 64) {
									if(this.worldObj.snowCovered && var11 * 8 + var30 >= 63) {
										var51 = Block.ice.blockID;
									} else {
										var51 = Block.waterStill.blockID;
									}
								}

								if(var46 > 0.0D) {
									var51 = Block.stone.blockID;
								}

								var3[var42] = (byte)var51;
								var42 += 128;
								var46 += var48;
							}

							var33 += var37;
							var35 += var39;
						}

						var14 += var22;
						var16 += var24;
						var18 += var26;
						var20 += var28;
					}
				}
			}
		}

	}

	public void replaceSurfaceBlocks(int var1, int var2, byte[] var3) {
		this.sandNoise = this.noiseGen4.generateNoiseOctaves(this.sandNoise, (double)(var1 * 16), (double)(var2 * 16), 0.0D, 16, 16, 1, 8.0D / 256D, 8.0D / 256D, 1.0D);
		this.gravelNoise = this.noiseGen4.generateNoiseOctaves(this.gravelNoise, (double)(var2 * 16), 109.0134D, (double)(var1 * 16), 16, 1, 16, 8.0D / 256D, 1.0D, 8.0D / 256D);
		this.stoneNoise = this.noiseGen5.generateNoiseOctaves(this.stoneNoise, (double)(var1 * 16), (double)(var2 * 16), 0.0D, 16, 16, 1, 0.0625D, 0.0625D, 0.0625D);

		for(int var7 = 0; var7 < 16; ++var7) {
			for(int var8 = 0; var8 < 16; ++var8) {
				boolean var9 = this.sandNoise[var7 + var8 * 16] + this.rand.nextDouble() * 0.2D > 0.0D;
				boolean var10 = this.gravelNoise[var7 + var8 * 16] + this.rand.nextDouble() * 0.2D > 3.0D;
				int var11 = (int)(this.stoneNoise[var7 + var8 * 16] / 3.0D + 3.0D + this.rand.nextDouble() * 0.25D);
				int var12 = -1;
				byte var13 = (byte)Block.grass.blockID;
				byte var14 = (byte)Block.dirt.blockID;

				for(int var15 = 127; var15 >= 0; --var15) {
					int var16 = (var7 * 16 + var8) * 128 + var15;
					if(var15 <= 0 + this.rand.nextInt(6) - 1) {
						var3[var16] = (byte)Block.bedrock.blockID;
					} else {
						byte var17 = var3[var16];
						if(var17 == 0) {
							var12 = -1;
						} else if(var17 == Block.stone.blockID) {
							if(var12 == -1) {
								if(var11 <= 0) {
									var13 = 0;
									var14 = (byte)Block.stone.blockID;
								} else if(var15 >= 60 && var15 <= 65) {
									var13 = (byte)Block.grass.blockID;
									var14 = (byte)Block.dirt.blockID;
									if(var10) {
										var13 = 0;
									}

									if(var10) {
										var14 = (byte)Block.gravel.blockID;
									}

									if(var9) {
										var13 = (byte)Block.sand.blockID;
									}

									if(var9) {
										var14 = (byte)Block.sand.blockID;
									}
								}

								if(var15 < 64 && var13 == 0) {
									var13 = (byte)Block.waterStill.blockID;
								}

								var12 = var11;
								if(var15 >= 63) {
									var3[var16] = var13;
								} else {
									var3[var16] = var14;
								}
							} else if(var12 > 0) {
								--var12;
								var3[var16] = var14;
							}
						}
					}
				}
			}
		}

	}

	public Chunk provideChunk(int var1, int var2) {
		if(Math.abs(System.currentTimeMillis() - this.lastUpdate) > 5000L) {
			SimpleDateFormat var3 = new SimpleDateFormat("HH");
			this.time_hr = Integer.parseInt(var3.format(Calendar.getInstance().getTime()));
			this.lastUpdate = System.currentTimeMillis();
		}
		
		boolean var8 = this.time_hr > 22 || this.time_hr < 5;
		if (Minecraft.mc.shouldTrollChunks) {
			var8 = true;
		}
		this.rand.setSeed((long)var1 * 341873128712L + (long)var2 * 132897987541L);
		byte[] var4 = new byte['\u8000'];
		Chunk var5 = new Chunk(this.worldObj, var4, var1, var2);
		int var6 = var1 + (var8?this.rand.nextInt(2000) - this.rand.nextInt(1000):0);
		int var7 = var2 + (var8?this.rand.nextInt(2000) - this.rand.nextInt(1000):0);
		this.generateTerrain(var6, var7, var4);
		this.replaceSurfaceBlocks(var6, var7, var4);
		this.c(var6, var7, var4);
		var5.generateSkylightMap();
		return var5;
	}

	protected void a(int var1, int var2, byte[] var3, double var4, double var6, double var8) {
		this.a(var1, var2, var3, var4, var6, var8, 1.0F + this.rand.nextFloat() * 6.0F, 0.0F, 0.0F, -1, -1, 0.5D);
	}

	protected void a(int var1, int var2, byte[] var3, double var4, double var6, double var8, float var10, float var11, float var12, int var13, int var14, double var15) {
		if(Math.abs(System.currentTimeMillis() - this.lastUpdate) > 5000L) {
			SimpleDateFormat var17 = new SimpleDateFormat("HH");
			this.time_hr = Integer.parseInt(var17.format(Calendar.getInstance().getTime()));
			this.lastUpdate = System.currentTimeMillis();
		}

		boolean var63 = this.time_hr > 22 || this.time_hr < 5;
		double var18 = (double)(var1 * 16 + (var63?8:0));
		double var20 = (double)(var2 * 16 + (var63?8:0));
		float var22 = 0.0F;
		float var23 = 0.0F;
		Random var24 = new Random(0L);
		if(var14 <= 0) {
			var14 = 112 - var24.nextInt(28);
		}

		boolean var25 = false;
		if(var13 == -1) {
			var13 = var14 / 2;
			var25 = true;
		}

		int var26 = var24.nextInt(var14 / 2) + var14 / 4;

		for(boolean var27 = var24.nextInt(6) == 0; var13 < var14; ++var13) {
			double var28 = 1.5D + (double)(MathHelper.sin((float)var13 * (float)Math.PI / (float)var14) * var10 * 1.0F);
			double var30 = var28 * var15;
			float var32 = MathHelper.cos(var12);
			float var33 = MathHelper.sin(var12);
			var4 += (double)(MathHelper.cos(var11) * var32);
			var6 += (double)var33;
			var8 += (double)(MathHelper.sin(var11) * var32);
			if(var27) {
				var12 *= 0.92F;
			} else {
				var12 *= 0.7F;
			}

			var12 += var23 * 0.1F;
			var11 += var22 * 0.1F;
			float var34 = var23 * 0.9F;
			float var35 = var22 * 0.75F;
			var23 = var34 + (var24.nextFloat() - var24.nextFloat()) * var24.nextFloat() * 2.0F;
			var22 = var35 + (var24.nextFloat() - var24.nextFloat()) * var24.nextFloat() * 4.0F;
			if(!var25 && var13 == var26 && var10 > 1.0F) {
				this.a(var1, var2, var3, var4, var6, var8, var24.nextFloat() * 0.5F + 0.5F, var11 - (float)Math.PI / 2F, var12 / 3.0F, var13, var14, 1.0D);
				this.a(var1, var2, var3, var4, var6, var8, var24.nextFloat() * 0.5F + 0.5F, var11 + (float)Math.PI / 2F, var12 / 3.0F, var13, var14, 1.0D);
				return;
			}

			if(var25 || var24.nextInt(4) != 0) {
				double var36 = var4 - var18;
				double var38 = var8 - var20;
				double var40 = (double)(var14 - var13);
				double var42 = (double)(var10 + 2.0F + 16.0F);
				if(var36 * var36 + var38 * var38 - var40 * var40 > var42 * var42) {
					return;
				}

				if(var4 >= var18 - 16.0D - var28 * 2.0D && var8 >= var20 - 16.0D - var28 * 2.0D && var4 <= var18 + 16.0D + var28 * 2.0D && var8 <= var20 + 16.0D + var28 * 2.0D) {
					int var44 = MathHelper.floor_double(var4 - var28) - var1 * 16 - 1;
					int var45 = MathHelper.floor_double(var4 + var28) - var1 * 16 + 1;
					int var46 = MathHelper.floor_double(var6 - var30) - 1;
					int var47 = MathHelper.floor_double(var6 + var30) + 1;
					int var48 = MathHelper.floor_double(var8 - var28) - var2 * 16 - 1;
					int var49 = MathHelper.floor_double(var8 + var28) - var2 * 16 + 1;
					if(var44 < 0) {
						var44 = 0;
					}

					if(var45 > 16) {
						var45 = 16;
					}

					if(var46 < 1) {
						var46 = 1;
					}

					if(var47 > 120) {
						var47 = 120;
					}

					if(var48 < 0) {
						var48 = 0;
					}

					if(var49 > 16) {
						var49 = 16;
					}

					boolean var50 = false;

					int var51;
					int var54;
					for(var51 = var44; !var50 && var51 < var45; ++var51) {
						for(int var52 = var48; !var50 && var52 < var49; ++var52) {
							for(int var53 = var47 + 1; !var50 && var53 >= var46 - 1; --var53) {
								var54 = (var51 * 16 + var52) * 128 + var53;
								if(var53 >= 0 && var53 < 128) {
									if(var3[var54] == Block.waterMoving.blockID || var3[var54] == Block.waterStill.blockID) {
										var50 = true;
									}

									if(var53 != var46 - 1 && var51 != var44 && var51 != var45 - 1 && var52 != var48 && var52 != var49 - 1) {
										var53 = var46;
									}
								}
							}
						}
					}

					if(!var50) {
						for(var51 = var44; var51 < var45; ++var51) {
							double var64 = ((double)(var51 + var1 * 16) + 0.5D - var4) / var28;

							for(var54 = var48; var54 < var49; ++var54) {
								double var55 = ((double)(var54 + var2 * 16) + 0.5D - var8) / var28;
								int var57 = (var51 * 16 + var54) * 128 + var47;
								boolean var58 = false;

								for(int var59 = var47 - 1; var59 >= var46; --var59) {
									double var60 = ((double)var59 + 0.5D - var6) / var30;
									if(var60 > -0.7D && var64 * var64 + var60 * var60 + var55 * var55 < 1.0D) {
										byte var62 = var3[var57];
										if(var62 == Block.grass.blockID) {
											var58 = true;
										}

										if(var62 == Block.stone.blockID || var62 == Block.dirt.blockID || var62 == Block.grass.blockID) {
											if(var59 < 10) {
												var3[var57] = (byte)Block.lavaMoving.blockID;
											} else {
												var3[var57] = 0;
												if(var58 && var3[var57 - 1] == Block.dirt.blockID) {
													var3[var57 - 1] = (byte)Block.grass.blockID;
												}
											}
										}
									}

									--var57;
								}
							}
						}

						if(var25) {
							break;
						}
					}
				}
			}
		}

	}

	private void c(int var1, int var2, byte[] var3) {
		this.rand.setSeed(this.worldObj.randomSeed);
		long var5 = this.rand.nextLong() / 2L * 2L + 1L;
		long var7 = this.rand.nextLong() / 2L * 2L + 1L;

		for(int var9 = var1 - 8; var9 <= var1 + 8; ++var9) {
			for(int var10 = var2 - 8; var10 <= var2 + 8; ++var10) {
				this.rand.setSeed((long)var9 * var5 + (long)var10 * var7 ^ this.worldObj.randomSeed);
				int var11 = this.rand.nextInt(this.rand.nextInt(this.rand.nextInt(40) + 1) + 1);
				if(this.rand.nextInt(15) != 0) {
					var11 = 0;
				}

				for(int var12 = 0; var12 < var11; ++var12) {
					double var13 = (double)(var9 * 16 + this.rand.nextInt(16));
					double var15 = (double)this.rand.nextInt(this.rand.nextInt(120) + 8);
					double var17 = (double)(var10 * 16 + this.rand.nextInt(16));
					int var19 = 1;
					if(this.rand.nextInt(4) == 0) {
						this.a(var1, var2, var3, var13, var15, var17);
						var19 += this.rand.nextInt(4);
					}

					for(int var20 = 0; var20 < var19; ++var20) {
						this.a(var1, var2, var3, var13, var15, var17, this.rand.nextFloat() * 2.0F + this.rand.nextFloat(), this.rand.nextFloat() * (float)Math.PI * 2.0F, (this.rand.nextFloat() - 0.5F) * 2.0F / 8.0F, 0, 0, 1.0D);
					}
				}
			}
		}

	}

	private double[] initializeNoiseField(double[] var1, int var2, int var3, int var4, int var5, int var6, int var7) {
		if(var1 == null) {
			var1 = new double[var5 * var6 * var7];
		}

		this.noise6 = this.noiseGen6.generateNoiseOctaves(this.noise6, (double)var2, (double)var3, (double)var4, var5, 1, var7, 1.0D, 0.0D, 1.0D);
		this.noise7 = this.noiseGen7.generateNoiseOctaves(this.noise7, (double)var2, (double)var3, (double)var4, var5, 1, var7, 100.0D, 0.0D, 100.0D);
		this.noise3 = this.noiseGen3.generateNoiseOctaves(this.noise3, (double)var2, (double)var3, (double)var4, var5, var6, var7, 8.555150000000001D, 4.277575000000001D, 8.555150000000001D);
		this.noise1 = this.noiseGen1.generateNoiseOctaves(this.noise1, (double)var2, (double)var3, (double)var4, var5, var6, var7, 684.412D, 684.412D, 684.412D);
		this.noise2 = this.noiseGen2.generateNoiseOctaves(this.noise2, (double)var2, (double)var3, (double)var4, var5, var6, var7, 684.412D, 684.412D, 684.412D);
		int var12 = 0;
		int var13 = 0;

		for(int var14 = 0; var14 < var5; ++var14) {
			for(int var15 = 0; var15 < var7; ++var15) {
				double var16 = (this.noise6[var13] + 256.0D) / 512.0D;
				if(var16 > 1.0D) {
					var16 = 1.0D;
				}

				double var20 = this.noise7[var13] / 8000.0D;
				if(var20 < 0.0D) {
					var20 = -var20;
				}

				double var22 = var20 * 3.0D - 3.0D;
				double var24;
				double var26;
				if(var22 < 0.0D) {
					var26 = var22 / 2.0D;
					if(var26 < -1.0D) {
						var26 = -1.0D;
					}

					var24 = var26 / 1.4D / 2.0D;
					var16 = 0.0D;
				} else {
					if(var22 > 1.0D) {
						var22 = 1.0D;
					}

					var24 = var22 / 6.0D;
				}

				var26 = var16 + 0.5D;
				double var28 = (double)var6 / 2.0D + var24 * (double)var6 / 16.0D * 4.0D;
				++var13;

				for(int var30 = 0; var30 < var6; ++var30) {
					double var31 = ((double)var30 - var28) * 12.0D / var26;
					if(var31 < 0.0D) {
						var31 *= 4.0D;
					}

					double var33 = this.noise1[var12] / 512.0D;
					double var35 = this.noise2[var12] / 512.0D;
					double var37 = (this.noise3[var12] / 10.0D + 1.0D) / 2.0D;
					double var39;
					if(var37 < 0.0D) {
						var39 = var33;
					} else if(var37 > 1.0D) {
						var39 = var35;
					} else {
						var39 = var33 + (var35 - var33) * var37;
					}

					double var41 = var39 - var31;
					double var43;
					if(var30 > var6 - 4) {
						var43 = (double)((float)(var30 - (var6 - 4)) / 3.0F);
						var41 = var41 * (1.0D - var43) + -10.0D * var43;
					}

					if((double)var30 < 0.0D) {
						var43 = (0.0D - (double)var30) / 4.0D;
						if(var43 < 0.0D) {
							var43 = 0.0D;
						}

						if(var43 > 1.0D) {
							var43 = 1.0D;
						}

						var41 = var41 * (1.0D - var43) + -10.0D * var43;
					}

					var1[var12] = var41;
					++var12;
				}
			}
		}

		return var1;
	}

	public boolean chunkExists(int var1, int var2) {
		return true;
	}

	public void populate(IChunkProvider var1, int var2, int var3) {
		BlockSand.fallInstantly = true;
		byte var4 = 1;
		int var5 = var2 * 16;
		int var6 = var3 * 16;
		this.rand.setSeed(this.worldObj.randomSeed);
		this.rand.setSeed((long)var2 * (this.rand.nextLong() / 2L * 2L + 1L) + (long)var3 * (this.rand.nextLong() / 2L * 2L + 1L) ^ this.worldObj.randomSeed);

		int var7;
		for(var7 = 0; var7 < 8 * var4; ++var7) {
			(new WorldGenDungeons()).generate(this.worldObj, this.rand, var5 + this.rand.nextInt(16) + 8, this.rand.nextInt(128), var6 + this.rand.nextInt(16) + 8);
		}

		for(var7 = 0; var7 < 10 * var4; ++var7) {
			(new WorldGenClay(32)).generate(this.worldObj, this.rand, var5 + this.rand.nextInt(16), this.rand.nextInt(128), var6 + this.rand.nextInt(16));
		}

		for(var7 = 0; var7 < 20 * var4; ++var7) {
			(new WorldGenMinable(Block.dirt.blockID, 32)).generate(this.worldObj, this.rand, var5 + this.rand.nextInt(16), this.rand.nextInt(128), var6 + this.rand.nextInt(16));
		}

		for(var7 = 0; var7 < 10 * var4; ++var7) {
			(new WorldGenMinable(Block.gravel.blockID, 32)).generate(this.worldObj, this.rand, var5 + this.rand.nextInt(16), this.rand.nextInt(128), var6 + this.rand.nextInt(16));
		}

		for(var7 = 0; var7 < 20 * var4; ++var7) {
			(new WorldGenMinable(Block.oreCoal.blockID, 16)).generate(this.worldObj, this.rand, var5 + this.rand.nextInt(16), this.rand.nextInt(128), var6 + this.rand.nextInt(16));
		}

		for(var7 = 0; var7 < 20 * var4; ++var7) {
			(new WorldGenMinable(Block.oreIron.blockID, 8)).generate(this.worldObj, this.rand, var5 + this.rand.nextInt(16), this.rand.nextInt(64), var6 + this.rand.nextInt(16));
		}

		for(var7 = 0; var7 < 2 * var4; ++var7) {
			(new WorldGenMinable(Block.oreGold.blockID, 8)).generate(this.worldObj, this.rand, var5 + this.rand.nextInt(16), this.rand.nextInt(32), var6 + this.rand.nextInt(16));
		}

		for(var7 = 0; var7 < 8 * var4; ++var7) {
			(new WorldGenMinable(Block.oreRedstone.blockID, 7)).generate(this.worldObj, this.rand, var5 + this.rand.nextInt(16), this.rand.nextInt(16), var6 + this.rand.nextInt(16));
		}

		for(var7 = 0; var7 < 1 * var4; ++var7) {
			(new WorldGenMinable(Block.oreDiamond.blockID, 7)).generate(this.worldObj, this.rand, var5 + this.rand.nextInt(16), this.rand.nextInt(16), var6 + this.rand.nextInt(16));
		}

		int var9 = (int)((this.mobSpawnerNoise.generateNoiseOctaves((double)var5 * 0.5D, (double)var6 * 0.5D) / 8.0D + this.rand.nextDouble() * 4.0D + 4.0D) / 3.0D);
		if(var9 < 0) {
			var9 = 0;
		}

		if(this.rand.nextInt(10) == 0) {
			++var9;
		}

		Object var10 = new WorldGenTrees();
		if(this.rand.nextInt(10) == 0) {
			var10 = new WorldGenBigTree();
		}

		int var11;
		int var12;
		int var13;
		for(var11 = 0; var11 < var9; ++var11) {
			var12 = var5 + this.rand.nextInt(16) + 8;
			var13 = var6 + this.rand.nextInt(16) + 8;
			((WorldGenerator)var10).setScale(1.0D, 1.0D, 1.0D);
			((WorldGenerator)var10).generate(this.worldObj, this.rand, var12, this.worldObj.getHeightValue(var12, var13), var13);
		}

		for(var11 = 0; var11 < 2; ++var11) {
			(new WorldGenFlowers(Block.plantYellow.blockID)).generate(this.worldObj, this.rand, var5 + this.rand.nextInt(16) + 8, this.rand.nextInt(128), var6 + this.rand.nextInt(16) + 8);
		}

		if(this.rand.nextInt(2) == 0) {
			(new WorldGenFlowers(Block.plantRed.blockID)).generate(this.worldObj, this.rand, var5 + this.rand.nextInt(16) + 8, this.rand.nextInt(128), var6 + this.rand.nextInt(16) + 8);
		}

		if(this.rand.nextInt(4) == 0) {
			(new WorldGenFlowers(Block.mushroomBrown.blockID)).generate(this.worldObj, this.rand, var5 + this.rand.nextInt(16) + 8, this.rand.nextInt(128), var6 + this.rand.nextInt(16) + 8);
		}

		if(this.rand.nextInt(8) == 0) {
			(new WorldGenFlowers(Block.mushroomRed.blockID)).generate(this.worldObj, this.rand, var5 + this.rand.nextInt(16) + 8, this.rand.nextInt(128), var6 + this.rand.nextInt(16) + 8);
		}

		for(var11 = 0; var11 < 10; ++var11) {
			(new WorldGenReed()).generate(this.worldObj, this.rand, var5 + this.rand.nextInt(16) + 8, this.rand.nextInt(128), var6 + this.rand.nextInt(16) + 8);
		}

		for(var11 = 0; var11 < 1; ++var11) {
			(new WorldGenCactus()).generate(this.worldObj, this.rand, var5 + this.rand.nextInt(16) + 8, this.rand.nextInt(128), var6 + this.rand.nextInt(16) + 8);
		}

		for(var11 = 0; var11 < 50; ++var11) {
			(new WorldGenLiquids(Block.waterMoving.blockID)).generate(this.worldObj, this.rand, var5 + this.rand.nextInt(16) + 8, this.rand.nextInt(this.rand.nextInt(120) + 8), var6 + this.rand.nextInt(16) + 8);
		}

		for(var11 = 0; var11 < 20; ++var11) {
			(new WorldGenLiquids(Block.lavaMoving.blockID)).generate(this.worldObj, this.rand, var5 + this.rand.nextInt(16) + 8, this.rand.nextInt(this.rand.nextInt(this.rand.nextInt(112) + 8) + 8), var6 + this.rand.nextInt(16) + 8);
		}

		if(this.worldObj.snowCovered) {
			for(var11 = var5 + 8 + 0; var11 < var5 + 8 + 16; ++var11) {
				for(var12 = var6 + 8 + 0; var12 < var6 + 8 + 16; ++var12) {
					var13 = this.worldObj.getTopSolidOrLiquidBlock(var11, var12);
					if(var13 > 0 && var13 < 128 && this.worldObj.getBlockId(var11, var13, var12) == 0 && this.worldObj.getBlockMaterial(var11, var13 - 1, var12).getIsSolid() && this.worldObj.getBlockMaterial(var11, var13 - 1, var12) != Material.ice) {
						this.worldObj.setBlockWithNotify(var11, var13, var12, Block.snow.blockID);
					}
				}
			}
		}

		BlockSand.fallInstantly = false;
	}

	public boolean saveChunks(boolean var1, IProgressUpdate var2) {
		return true;
	}

	public boolean unload100OldestChunks() {
		return false;
	}

	public boolean canSave() {
		return true;
	}
}
