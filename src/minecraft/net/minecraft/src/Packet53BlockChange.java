package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet53BlockChange extends Packet {
	public int xPosition;
	public int yPosition;
	public int zPosition;
	public int type;
	public int metadata;

	public Packet53BlockChange() {
		this.isChunkDataPacket = true;
	}

	public Packet53BlockChange(int var1, int var2, int var3, int var4, int var5) {
		this.xPosition = var1;
		this.yPosition = var2;
		this.zPosition = var3;
		this.type = var4;
		this.metadata = var5;
	}
	
	public void readPacketData(DataInputStream var1) throws IOException {
		this.xPosition = var1.readInt();
		this.yPosition = var1.read();
		this.zPosition = var1.readInt();
		this.type = var1.read();
		this.metadata = var1.read();
	}

	public void writePacket(DataOutputStream var1) throws IOException {
		var1.writeInt(this.xPosition);
		var1.write(this.yPosition);
		var1.writeInt(this.zPosition);
		var1.write(this.type);
		var1.write(this.metadata);
	}

	public void processPacket(NetHandler var1) {
		var1.handleBlockChange(this);
	}

	public int getPacketSize() {
		return 11;
	}
}
