package dark.api;

import cpw.mods.fml.common.Loader;

public enum PowerSystems
{
	INDUSTRIALCRAFT("IC2"),
	MEKANISM("Mekanism"),
	BUILDCRAFT("BuildCraft|Energy");

	public String id;
	public static final PowerSystems[] UE_SUPPORTED_SYSTEMS = new PowerSystems[] { INDUSTRIALCRAFT, MEKANISM, BUILDCRAFT };

	private PowerSystems(String id)
	{
		this.id = id;
	}

	private static boolean init = false;
	private static Boolean[] loaded;

	/** Checks to see if something can run powerless based on mods loaded
	 *
	 * @param optional - power system that the device can use
	 * @return true if free power is to be generated */
	public static boolean runPowerLess(PowerSystems... optional)
	{
		for (int i = 0; i < optional.length; i++)
		{
			if (isPowerSystemLoaded(optional[i], false))
			{
				return false;
			}
		}
		return true;
	}

	/** Check to see if one of the mods listed in the PowerSystem enum is loaded */
	public static boolean isPowerSystemLoaded(PowerSystems power, boolean force)
	{
		if (!init || force)
		{
			loaded = new Boolean[PowerSystems.values().length];
			for (int i = 0; i < PowerSystems.values().length; i++)
			{
				loaded[i] = Loader.isModLoaded(PowerSystems.values()[i].id);
			}
			init = true;
		}
		return power != null && loaded[power.ordinal()];
	}
}