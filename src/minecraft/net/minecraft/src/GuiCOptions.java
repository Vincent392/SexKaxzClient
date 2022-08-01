package net.minecraft.src;

public class GuiCOptions extends GuiScreen {
	private GuiScreen parentScreen;
	protected String screenTitle = "Client settings";
	private ClientSettings options;

	public GuiCOptions(GuiScreen var1, ClientSettings var2) {
		this.parentScreen = var1;
		this.options = var2;
	}
	
	public boolean editingBSize = false;

	public void initGui() {
		for(int var1 = 0; var1 < this.options.settings.size(); ++var1) {
			this.controlList.add(new GuiSmallButton(var1, this.width / 2 - 155 + var1 % 2 * 160, this.height / 6 + 24 * (var1 >> 1), this.options.settings.get(var1).name + ": " + this.options.settings.get(var1).value));
		}
		this.controlList.add(new GuiButton(100, this.width / 2 - 100, this.height / 6 + 168, "Done"));
	}

	protected void actionPerformed(GuiButton var1) {
		if(var1.enabled) {
			if(var1.id < 100) {
				int id = var1.id;
				ClientSetting setting = this.options.settings.get(id);
				switch (id) {
					case 0:
						String a = String.valueOf(!Boolean.parseBoolean(String.valueOf(setting.value)));
						setting.value = a;
						this.options.saveOptions();
						var1.displayString = setting.name + ": " + setting.value;
						break;
					case 1:
						String a1 = String.valueOf(!Boolean.parseBoolean(String.valueOf(setting.value)));
						setting.value = a1;
						this.options.saveOptions();
						var1.displayString = setting.name + ": " + setting.value;
						break;
					case 2:
						String a2 = String.valueOf(!Boolean.parseBoolean(String.valueOf(setting.value)));
						setting.value = a2;
						this.options.saveOptions();
						var1.displayString = setting.name + ": " + setting.value;
						break;
					case 3:
						String a3 = String.valueOf(!Boolean.parseBoolean(String.valueOf(setting.value)));
						setting.value = a3;
						this.options.saveOptions();
						var1.displayString = setting.name + ": " + setting.value;
						break;
					case 4:
						String a4 = String.valueOf(!Boolean.parseBoolean(String.valueOf(setting.value)));
						setting.value = a4;
						this.options.saveOptions();
						var1.displayString = setting.name + ": " + setting.value;
						break;
					case 5:
						String a5 = String.valueOf(!Boolean.parseBoolean(String.valueOf(setting.value)));
						setting.value = a5;
						this.options.saveOptions();
						var1.displayString = setting.name + ": " + setting.value;
						break;
					case 6:
						String a6 = String.valueOf(!Boolean.parseBoolean(String.valueOf(setting.value)));
						setting.value = a6;
						this.options.saveOptions();
						var1.displayString = setting.name + ": " + setting.value;
						break;
					case 7:
						String a7 = String.valueOf(!Boolean.parseBoolean(String.valueOf(setting.value)));
						setting.value = a7;
						this.options.saveOptions();
						var1.displayString = setting.name + ": " + setting.value;
						break;
					case 8:
						String a8 = String.valueOf(!Boolean.parseBoolean(String.valueOf(setting.value)));
						setting.value = a8;
						this.options.saveOptions();
						var1.displayString = setting.name + ": " + setting.value;
						break;
					case 9:
						String a9 = String.valueOf(!Boolean.parseBoolean(String.valueOf(setting.value)));
						setting.value = a9;
						this.options.saveOptions();
						var1.displayString = setting.name + ": " + setting.value;
						break;
					case 10:
						this.editingBSize = true;
						var1.displayString = "Waiting for input";
						break;
					case 11:
						String a11 = String.valueOf(!Boolean.parseBoolean(String.valueOf(setting.value)));
						setting.value = a11;
						this.options.saveOptions();
						var1.displayString = setting.name + ": " + setting.value;
						break;
				}
			}

			if(var1.id == 100) {
				this.mc.displayGuiScreen(this.parentScreen);
			}

		}
	}
	
	protected void keyTyped(char var1, int var2) {
		if(var2 == 1) {
			for (Object lol : this.controlList) {
				GuiButton l = (GuiButton)(lol);
				if (l.id == 100) {
					this.actionPerformed(l);
				}
			}
		}
		if (editingBSize && "0123456789".indexOf(var1) >= 0) {
			ClientSetting setting = this.options.settings.get(10);
			editingBSize = false;
			setting.value = String.valueOf(var1);
			GuiButton btn = (GuiButton)(this.controlList.get(10));
			btn.displayString = setting.name + ": " + setting.value;
		}
	}

	public void drawScreen(int var1, int var2, float var3) {
		this.drawDefaultBackground();
		this.drawCenteredString(this.fontRenderer, this.screenTitle, this.width / 2, 20, 0xFFFFFF);
		super.drawScreen(var1, var2, var3);
	}
}
