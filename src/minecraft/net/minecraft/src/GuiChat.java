package net.minecraft.src;

import java.lang.reflect.Field;

import org.lwjgl.input.Keyboard;

public class GuiChat extends GuiScreen {
	private String message = "";
	private int updateCounter = 0;

	public void initGui() {
		Keyboard.enableRepeatEvents(true);
	}

	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
	}

	public void updateScreen() {
		++this.updateCounter;
	}
	
	public void giveItem(ItemStack item) {
		if (this.mc.thePlayer.inventory.addItemStackToInventory(item)) {
			System.out.println("success");
		}
	}
	
	public void teleportTo(double d, double d2, double d3, float f, float f2) {
		EntityPlayer player = this.mc.thePlayer;
		/*player.hasMoved = false;
		player.lastPosX = d;
		player.lastPosY = d2;
		player.lastPosZ = d3;*/
		player.setPositionAndRotation(d, d2, d3, f, f2);
		// player.sendQueue.addToSendQueue(new Packet13PlayerLookMove(d, d2 + (double)1.62f, d2, d3, f, f2, false));
    }

	protected void keyTyped(char var1, int var2) {
		if(var1 == 22) {
			String var3 = GuiScreen.getClipboardString();
			if(var3 == null) {
				var3 = "";
			}

			if(var3.length() > 0) {
				this.message = this.message + var3;
			}
		}
		if(var2 == 1) {
			this.mc.displayGuiScreen((GuiScreen)null);
		} else if(var2 == 28) {
			String var3 = this.message.trim();
			if(var3.length() > 0 && !var3.startsWith(".")) {
				this.mc.thePlayer.sendChatMessage(this.message.trim());
			}
			this.mc.displayGuiScreen((GuiScreen)null);
			if (var3.startsWith(".")) {
				if (var3.startsWith(".kit give ")) {
					if (var3.equalsIgnoreCase(".kit give obsidian")) {
						giveItem(new ItemStack(Item.obsidianArmor0, 1));
						giveItem(new ItemStack(Item.obsidianArmor1, 1));
						giveItem(new ItemStack(Item.obsidianArmor2, 1));
						giveItem(new ItemStack(Item.obsidianArmor3, 1));
						giveItem(new ItemStack(Item.obsidianPick, 1));
						giveItem(new ItemStack(Item.obsidianAxe, 1));
						giveItem(new ItemStack(Item.obsidianShovel, 1));
						giveItem(new ItemStack(Item.obsidianHoe, 1));
						giveItem(new ItemStack(Item.obsidianSword, 1));
						giveItem(new ItemStack(Item.obsidianIngot, 64));
						giveItem(new ItemStack(Block.obsidian, 64));
					}
					else if (var3.equalsIgnoreCase(".kit give grief")) {
						giveItem(new ItemStack(Item.bucketLava, 1));
						giveItem(new ItemStack(Block.tnt, 64));
					}
					else {
						this.mc.ingameGUI.addChatMessage("§cKit " + var3.substring(".kit give ".length()) + " doesn't exist! Run the .kits command for a full list.");
					}
				}
				if (var3.startsWith(".say ")) {
					this.mc.thePlayer.sendChatMessage(var3.substring(".say ".length()));
				}
				/*if (var3.startsWith(".spoofname ")) {
					this.mc.thePlayer.username = var3.substring(".spoofname ".length());
				}*/
				if (var3.equalsIgnoreCase(".kits")) {
					this.mc.ingameGUI.addChatMessage("§eKits: §robsidian, grief");
				}
				if (var3.equalsIgnoreCase(".help")) {
					this.mc.ingameGUI.addChatMessage("Invalid arguments! Try .help 1");
				}
				if (var3.equalsIgnoreCase(".dupe")) {
					if (this.mc.thePlayer.getCurrentEquippedItem() != null) {
						this.giveItem(this.mc.thePlayer.getCurrentEquippedItem().duplicate());
					}else {
						this.mc.ingameGUI.addChatMessage("You aren't holding anything to dupe!");
					}
				}
				if (var3.equalsIgnoreCase(".crash")) {
					teleportTo(12550700, 420, 12550700, 0, 0);
				}
				if (var3.startsWith(".help ")) {
					String[] ss = var3.split(" ");
					if (ss.length != 2) {
						this.mc.ingameGUI.addChatMessage("Invalid arguments! Try .help 1");
					}else {
						try {
							int a = Integer.parseInt(ss[1]);
						}catch (Exception e) {
							this.mc.ingameGUI.addChatMessage("Invalid arguments! Try .help 1");
							return;
						}
						int page = Integer.parseInt(ss[1]);
						int pages = 3;
						this.mc.ingameGUI.addChatMessage("§esex kaxz client commands");
						this.mc.ingameGUI.addChatMessage("§aPage " + page + "/" + pages);
						if (page == 1) {
							this.mc.ingameGUI.addChatMessage(".help [page] - shows this message");
							this.mc.ingameGUI.addChatMessage(".kit give [name] - gives a kit");
							this.mc.ingameGUI.addChatMessage(".kits - lists all the kits");
							this.mc.ingameGUI.addChatMessage(".say [message] - says a message");
						} else if (page == 2) {
							this.mc.ingameGUI.addChatMessage(".players - shows all the players in a world");
							this.mc.ingameGUI.addChatMessage(".give [id] [count] - gives an item/block");
							this.mc.ingameGUI.addChatMessage(".getid [item/block] [name] - gets an id from name and type");
							this.mc.ingameGUI.addChatMessage(".clearchat - clears chat");
						} else if (page == 3) {
							this.mc.ingameGUI.addChatMessage(".all [item/block] - lists every item/block name");
							this.mc.ingameGUI.addChatMessage(".dupe - duplicates the held item and gives it");
							this.mc.ingameGUI.addChatMessage(".tp [x] [y] [z] - teleport to xyz");
							this.mc.ingameGUI.addChatMessage(".reload - reloads renderer");
						} else {
							this.mc.ingameGUI.addChatMessage("Invalid page number!");	
						}
					}
				}
				if (var3.equalsIgnoreCase(".players")) {
					String plrVal = "Players (" + this.mc.theWorld.playerEntities.size() + "): ";
					int x = 0;
					for (Object oplr : this.mc.theWorld.playerEntities) {
						EntityPlayer plr = (EntityPlayer)(oplr);
						if (x == this.mc.theWorld.playerEntities.size()-1) {
							plrVal += plr.username;
						}else {
							plrVal += plr.username + ", ";
						}
						x++;
					}
					this.mc.ingameGUI.addChatMessage(plrVal);
				}
				if (var3.equalsIgnoreCase(".clearchat")) {
					this.mc.ingameGUI.chatMessageList.clear();
				}
				if (var3.startsWith(".give ")) {
					String[] ss = var3.split(" ");
					String command = ss[0];
					if (ss.length == 3) {
						String id = ss[1];
						String count = ss[2];
						try {
							int realID = Integer.parseInt(id);
							int realCount = Integer.parseInt(count);
						}catch (Exception e) {
							this.mc.ingameGUI.addChatMessage("Invalid arguments!");
							return;
						}
						int realID = Integer.parseInt(id);
						int realCount = Integer.parseInt(count);
						boolean can = false;
						for (Block b : Block.blocksList) {
							if (b != null) {
								if (b.blockID == realID) {
									can = true;
								}
							}
						}
						for (Item i : Item.itemsList) {
							if (i != null) {
								if (i.shiftedIndex == realID) {
									can = true;
								}
							}
						}
						if (can) {
							this.giveItem(new ItemStack(realID, realCount));
						}else {
							this.mc.ingameGUI.addChatMessage("Item with ID " + id + " doesn't exist!");
						}
					}else {
						this.mc.ingameGUI.addChatMessage("Invalid arguments!");
					}
				}
				if (var3.startsWith(".tp ")) {
					String[] ss = var3.split(" ");
					String command = ss[0];
					if (ss.length == 4) {
						String x = ss[1];
						String y = ss[2];
						String z = ss[3];
						try {
							double realX = Double.parseDouble(x);
							double realY = Double.parseDouble(y);
							double realZ = Double.parseDouble(z);
						}catch (Exception e) {
							this.mc.ingameGUI.addChatMessage("Invalid arguments!");
							return;
						}
						double realX = Double.parseDouble(x);
						double realY = Double.parseDouble(y);
						double realZ = Double.parseDouble(z);
						teleportTo(realX, realY, realZ, 0, 0);
						// player.sendQueue.addToSendQueue(new Packet11PlayerPosition(realX, /*player.boundingBox.minY*/realY+0.2, realY, realZ, player.onGround));
						// player.sendQueue.addToSendQueue(new Packet53BlockChange(realX, realY, realZ, realType, 0));
					}else {
						this.mc.ingameGUI.addChatMessage("Invalid arguments!");
					}
				}
				if (var3.equalsIgnoreCase(".reload")) {
					this.mc.renderGlobal.loadRenderers();
				}
				if (var3.startsWith(".getid ")) {
					String[] ss = var3.split(" ");
					String command = ss[0];
					if (ss.length == 3) {
						String type = ss[1].trim();
						String name = ss[2].trim();
						if (type.equalsIgnoreCase("item")) {
							try {
								Item x = (Item) Class.forName("net.minecraft.src.Item").getField(name).get(null);
								this.mc.ingameGUI.addChatMessage("Item §9" + name + "§r has ID " + x.shiftedIndex + "!");
							}catch(Exception e) {
								this.mc.ingameGUI.addChatMessage("Name invalid!");
								e.printStackTrace();
								this.mc.ingameGUI.addChatMessage(e.getMessage());
							}
						} else if (type.equalsIgnoreCase("block")) {
							try {
								Block x = (Block) Class.forName("net.minecraft.src.Block").getField(name).get(null);
								this.mc.ingameGUI.addChatMessage("Block §9" + name + "§r has ID " + x.blockID + "!");
							}catch(Exception e) {
								this.mc.ingameGUI.addChatMessage("Name invalid!");
								e.printStackTrace();
								this.mc.ingameGUI.addChatMessage(e.getMessage());
							}
						} else {
							this.mc.ingameGUI.addChatMessage("Type invalid! Please use \"item\" or \"block\"!");
						}
					}else {
						this.mc.ingameGUI.addChatMessage("Invalid arguments!");
					}
				}
				if (var3.startsWith(".all ")) {
					String[] ss = var3.split(" ");
					String command = ss[0];
					if (ss.length == 2) {
						String type = ss[1].trim();
						if (type.equalsIgnoreCase("item")) {
							try {
								Field[] lol = Class.forName("net.minecraft.src.Item").getFields();
								String funny = "Items (" + lol.length + "): ";
								int x = 0;
								for (Field fld : lol) {
									if (x == lol.length-1) {
										funny += fld.getName();
									}else {
										funny += fld.getName() + ", ";
									}
								}
								this.mc.ingameGUI.addChatMessage(funny);
							}catch (Exception e) {
								this.mc.ingameGUI.addChatMessage("Failed to find Item class!");
							}
						} else if (type.equalsIgnoreCase("block")) {
							try {
								Field[] lol = Class.forName("net.minecraft.src.Block").getFields();
								String funny = "Blocks (" + lol.length + "): ";
								int x = 0;
								for (Field fld : lol) {
									if (x == lol.length-1) {
										funny += fld.getName();
									}else {
										funny += fld.getName() + ", ";
									}
								}
								this.mc.ingameGUI.addChatMessage(funny);
							}catch (Exception e) {
								this.mc.ingameGUI.addChatMessage("Failed to find Block class!");
							}
						} else {
							this.mc.ingameGUI.addChatMessage("Type invalid! Please use \"item\" or \"block\"!");
						}
					}else {
						this.mc.ingameGUI.addChatMessage("Invalid arguments!");
					}
				}
			}
		} else {
			if(var2 == 14 && this.message.length() > 0) {
				this.message = this.message.substring(0, this.message.length() - 1);
			}

			if(" !\"#$%&\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_\'abcdefghijklmnopqrstuvwxyz{|}~\u2302\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb".indexOf(var1) >= 0 && this.message.length() < 100) {
				this.message = this.message + var1;
			}

		}
	}

	public void drawScreen(int var1, int var2, float var3) {
		this.drawRect(2, this.height - 14, this.width - 2, this.height - 2, Integer.MIN_VALUE);
		if (this.message.startsWith(".")) {
            this.drawRect(1, this.height - 14, 2, this.height - 1, this.mc.rainbowColor);
            this.drawRect(this.width - 2, this.height - 14, this.width - 1, this.height - 1, this.mc.rainbowColor);
            this.drawRect(2, this.height - 14, this.width - 2, this.height - 13, this.mc.rainbowColor);
            this.drawRect(2, this.height - 2, this.width - 2, this.height - 1, this.mc.rainbowColor);
        }
		this.drawString(this.fontRenderer, "> ", 4, this.height - 12, this.mc.rainbowColor);
		this.drawString(this.fontRenderer, "  " + this.message + (this.updateCounter / 6 % 2 == 0?"_":""), 4, this.height - 12, 14737632);
	}

	protected void mouseClicked(int var1, int var2, int var3) {
		if(var3 == 0 && this.mc.ingameGUI.testMessage != null) {
			if(this.message.length() > 0 && !this.message.endsWith(" ")) {
				this.message = this.message + " ";
			}

			this.message = this.message + this.mc.ingameGUI.testMessage;
			byte var4 = 100;
			if(this.message.length() > var4) {
				this.message = this.message.substring(0, var4);
			}
		}

	}
}
