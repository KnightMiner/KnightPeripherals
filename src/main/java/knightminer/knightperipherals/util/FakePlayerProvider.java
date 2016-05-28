package knightminer.knightperipherals.util;

import java.util.UUID;

import com.mojang.authlib.GameProfile;

import dan200.computercraft.api.turtle.ITurtleAccess;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;

public class FakePlayerProvider {

	private static FakePlayer player;
	private static GameProfile profile = new GameProfile(UUID.fromString("6b49a094-c3de-11e5-9912-ba0be0483c18"),
	        "KnightPeripherals");

	public static FakePlayer get(ITurtleAccess turtle) {
		if (player == null) {
			player = FakePlayerFactory.get((WorldServer) turtle.getWorld(), profile);
		}
		player.setPosition(turtle.getPosition().getX(), turtle.getPosition().getY(), turtle.getPosition().getZ());
		return player;
	}

}
