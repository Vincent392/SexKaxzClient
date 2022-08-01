import os

natives = os.path.abspath("./lib/natives")
lwjgl = os.path.abspath("./lib/lwjgl.jar")

name = input("Choose your username (keep blank for random name): ").strip()

if len(name) == 0:
    os.system("java -Djava.util.Arrays.useLegacyMergeSort=true -Xmx1024M -Xms1024M -Dhttp.proxyHost=betacraft.uk -Dhttp.proxyPort=11702 -Dorg.lwjgl.librarypath=" + natives + " -Dnet.java.games.input.librarypath=" + natives + " -cp ./bin/minecraft:./lib/jinput.jar:./lib/minecraft.jar:./lib/lwjgl_util.jar:" + lwjgl + " Start")
else:
    os.system("java -Djava.util.Arrays.useLegacyMergeSort=true -Xmx1024M -Xms1024M -Dhttp.proxyHost=betacraft.uk -Dhttp.proxyPort=11702 -Dorg.lwjgl.librarypath=" + natives + " -Dnet.java.games.input.librarypath=" + natives + " -cp ./bin/minecraft:./lib/jinput.jar:./lib/minecraft.jar:./lib/lwjgl_util.jar:" + lwjgl + " Start " + name)
