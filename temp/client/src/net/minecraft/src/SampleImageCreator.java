package net.minecraft.src;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class SampleImageCreator {
	public static void main(String[] var0) {
		try {
			String[] var1 = new String[]{"/sample/s1.dat", "/sample/s2.dat", "/sample/s3.dat"};
			String[] var2 = var1;
			int var3 = var1.length;

			for(int var4 = 0; var4 < var3; ++var4) {
				String var5 = var2[var4];
				InputStream var6 = SampleImageCreator.class.getResourceAsStream(var5);
				String var7 = var5.replace("/sample", "").replace("dat", "png");
				Files.copy(var6, Paths.get(System.getProperty("user.dir") + var7, new String[0]), new CopyOption[]{StandardCopyOption.REPLACE_EXISTING});
				var6.close();
			}
		} catch (IOException var8) {
			var8.printStackTrace();
		}

	}
}
