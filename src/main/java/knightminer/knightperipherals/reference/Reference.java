package knightminer.knightperipherals.reference;

public class Reference {
	
	// general
	public static final String MOD_ID = "knightperipherals";
	public static final String MOD_NAME = "KnightPeripherals";
	public static final String VERSION = "${version}";
	public static final String RESOURCE_LOCATION = MOD_ID;
	
	// proxies
	public static final String PACKAGE = "knightminer.knightperipherals.proxy.";
	public static final String CLIENT_PROXY_CLASS = PACKAGE + "ClientProxy";
	public static final String SERVER_PROXY_CLASS = PACKAGE + "CommonProxy";

	// legacy turtle IDs
	public static final int UPGRADE_LEGACY_CLAW = 190;
	public static final int UPGRADE_LEGACY_HAMMER = 191;
	public static final int UPGRADE_LEGACY_BOW = 192;
	public static final int UPGRADE_LEGACY_TNT = 193;
	
	// turtle IDs
	public static final String UPGRADE_CLAW = MOD_ID + ":turtle_claw";
	public static final String UPGRADE_HAMMER = MOD_ID + ":ex_nihilo_hammer";
	public static final String UPGRADE_BOW = MOD_ID + ":bow";
	public static final String UPGRADE_TNT = MOD_ID + ":tnt";

}
