package net.minecraft.src;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

import net.minecraft.client.Minecraft;

public class ClientSettings {
	protected Minecraft mc;
	private File optionsFile;
	
	public ArrayList<ClientSetting> settings;
	public ArrayList<ClientSetting> defaults;
	
	public ClientSettings(Minecraft var1, File var2) {
		this.mc = var1;
		this.optionsFile = new File(var2, "sex2options.txt");
		this.settings = new ArrayList<ClientSetting>();
		this.defaults = new ArrayList<ClientSetting>();
		this.preloadDefaults();
		this.loadOptions();
	}

	public ClientSettings() {
	}
	
	public void preloadDefaults() {
		this.defaults.add(new ClientSetting("Rainbow", "true"));
		this.defaults.add(new ClientSetting("Show coords", "true"));
		this.defaults.add(new ClientSetting("Speed", "false"));
		this.defaults.add(new ClientSetting("Render shadow players", "false"));
		this.defaults.add(new ClientSetting("Weird generation", "false"));
		this.defaults.add(new ClientSetting("Bridge (bedrock)", "false"));
		this.defaults.add(new ClientSetting("Air jump", "false"));
		this.defaults.add(new ClientSetting("No tool damage", "false"));
		this.defaults.add(new ClientSetting("God mode", "false"));
		this.defaults.add(new ClientSetting("Nuker", "false"));
		this.defaults.add(new ClientSetting("Bridge scale", "4"));
		this.defaults.add(new ClientSetting("Disable lighting", "false"));
	}
	
	public Object getValue(int id) {
		return this.settings.get(id).value;
	}

	public void loadOptions() {
		try {
			if(!this.optionsFile.exists()) {
				for (ClientSetting setting : this.defaults) {
					this.settings.add(setting);
				}
				this.saveOptions();
				return;
			}

			BufferedReader var1 = new BufferedReader(new FileReader(this.optionsFile));
			String var2 = "";

			while((var2 = var1.readLine()) != null) {
				String[] ss = var2.split(": ");
				this.settings.add(new ClientSetting(ss[0], ss[1]));
			}
			if (this.settings.size() != this.defaults.size()) {
				for (int i = 0; i < this.defaults.size(); i++) {
					try {
						if (this.settings.get(i) == null) {
							System.out.println("what ");
						}
					} catch (Exception e) {
						this.settings.add(this.defaults.get(i));
					}
				}
			}
			int x = 0;
			for (ClientSetting setting : this.settings) {
				if (!setting.name.equals(this.defaults.get(x).name)) {
					setting.name = this.defaults.get(x).name;
				}
				x++;
			}
			var1.close();
		} catch (Exception var5) {
			System.out.println("Failed to load options");
			var5.printStackTrace();
		}

	}

	public void saveOptions() {
		try {
			PrintWriter var1 = new PrintWriter(new FileWriter(this.optionsFile));
			for (ClientSetting setting : this.settings) {
				var1.println(setting.name + ": " + setting.value);
			}
			var1.close();
		} catch (Exception var3) {
			System.out.println("Failed to save options");
			var3.printStackTrace();
		}

	}
}
