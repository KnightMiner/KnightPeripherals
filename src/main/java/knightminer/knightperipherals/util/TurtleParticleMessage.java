package knightminer.knightperipherals.util;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import knightminer.knightperipherals.KnightPeripherals;
import net.minecraft.network.play.server.S2APacketParticles;

/**
 * Contains a particle type integer and two sets of coordinates for spawning
 * particles Note that the particle type stuff is determined by myself, so
 * really, make your own class if you want to do this
 */
public class TurtleParticleMessage implements IMessage {

	public int type;
	public float startX, startY, startZ, endX, endY, endZ;

	public TurtleParticleMessage() {}

	public TurtleParticleMessage(int type, float startX, float startY, float startZ, float endX, float endY,
	        float endZ) {
		this.type = type;
		this.startX = startX;
		this.startY = startY;
		this.startZ = startZ;
		this.endX = endX;
		this.endY = endY;
		this.endZ = endZ;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		type = buf.readInt();
		startX = buf.readFloat();
		startY = buf.readFloat();
		startZ = buf.readFloat();
		endX = buf.readFloat();
		endY = buf.readFloat();
		endZ = buf.readFloat();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(type);
		buf.writeFloat(startX);
		buf.writeFloat(startY);
		buf.writeFloat(startZ);
		buf.writeFloat(endX);
		buf.writeFloat(endY);
		buf.writeFloat(endZ);
	}

	public static class TurtleParticleHandler implements IMessageHandler<TurtleParticleMessage, IMessage> {
		@Override
		public IMessage onMessage(TurtleParticleMessage message, MessageContext ctx) {
			switch (message.type) {
				case 0:
					for (float px = message.startX; px <= message.endX; px += 0.1D)
						for (float py = message.startY; py <= message.endY; py += 0.1D)
							for (float pz = message.startZ; pz <= message.endZ; pz += 0.1D)
								ctx.getClientHandler().handleParticles(new S2APacketParticles(
										"reddust",
										px + 0.5F, py + 0.5F, pz + 0.5F,
								        -1.0F, 1.0F, 1.0F,
								        1.0F, 0));

					break;

				default:
					KnightPeripherals.logger.error("Particle type " + message.type + " is undefined.");
			}

			return null;
		}
	}
}
