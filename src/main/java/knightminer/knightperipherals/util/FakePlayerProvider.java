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

	/**
	 * Get a fake player with the position and world set to the turtle's
	 * position and world
	 *
	 * @param turtle
	 *        Turtle getting the fake player
	 * @return A fake player with position of the turtle
	 */
	public static FakePlayer get(ITurtleAccess turtle) {
		if (player == null) {
			player = FakePlayerFactory.get((WorldServer) turtle.getWorld(), profile);
		}
		player.setPosition(turtle.getPosition().posX, turtle.getPosition().posY, turtle.getPosition().posZ);

		return player;
	}

}
