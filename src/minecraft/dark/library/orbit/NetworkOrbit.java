package dark.library.orbit;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import dark.library.helpers.Pair;

import net.minecraft.entity.Entity;
import universalelectricity.core.vector.Vector3;

/**
 * Designed to be used by flying Entities to create an orbit pattern around a central point
 * 
 * @author DarkGuardsman
 * 
 */
public class NetworkOrbit
{
	/* DEFINED IDEAL NUMBER OF OBJECTS IN ORBIT */
	public static int minObjects = 3;
	public static int maxObjects = 20;

	/* CURRENT RADIUS OF THE CIRCLE */
	float orbitRadius;

	/* CHANGE IN ROTATION OF THE CIRCLE X Y Z */
	Vector3 rotationChange = new Vector3(0, 0, 0);

	/* OBJECTS IN ORBIT <Entity, Change in radius> */
	HashMap<IOrbitingEntity, Integer> orbitMemeber = new HashMap<IOrbitingEntity, Integer>();

	/**
	 * @param entities - entities to add to this orbit when its created < Entity, Radius change>
	 */
	public NetworkOrbit(HashMap<IOrbitingEntity, Integer> entities)
	{
		if (entities != null)
		{
			this.orbitMemeber.putAll(entities);
		}
	}

	/**
	 * Called by the host of the orbit too see if this orbit can continue
	 */
	public boolean canOrbitExist()
	{
		int members = this.getOrbitMemebers().size();
		if (members > maxObjects || members < minObjects)
		{
			return false;
		}
		return true;
	}

	/**
	 * Gets the list of Entities in this orbit
	 */
	public HashMap<IOrbitingEntity, Integer> getOrbitMemebers()
	{
		if (this.orbitMemeber == null)
		{
			this.orbitMemeber = new HashMap<IOrbitingEntity, Integer>();
		}
		return this.orbitMemeber;
	}

	/**
	 * Increase/changes the rotation angles of the orbit
	 * 
	 * @param vec - rotation change stored as a vector3
	 * @param increase - add the vec rotation to current rotation
	 */
	public void changeRotation(Vector3 vec, boolean increase)
	{
		Vector3 preRotation = rotationChange.clone();
		this.rotationChange = vec;
		if (increase)
		{
			this.rotationChange.add(preRotation);
		}
	}

	/**
	 * Get the rotation change of the orbit
	 */
	public Vector3 getRotation()
	{
		if (this.rotationChange == null)
		{
			this.rotationChange = new Vector3(0, 0, 0);
		}
		return this.rotationChange;
	}

	/**
	 * Ideal minimal radius needed for the number of objects
	 */
	public float getMinRadius()
	{
		float width = 0;
		Iterator<Entry<IOrbitingEntity, Integer>> it = this.getOrbitMemebers().entrySet().iterator();
		while (it.hasNext())
		{
			IOrbitingEntity entity = it.next().getKey();
			if (entity instanceof Entity)
			{
				width += ((Entity) entity).width;
			}
		}
		width = width / this.getOrbitMemebers().size();
		return ((width + (width / 2)) * this.getOrbitMemebers().size());
	}

	/**
	 * @param pos - position in the orbit
	 * @return offset distance from orbit center
	 * 
	 * Note this only gives the offset from the orbit point. This needs to be used to in combo with
	 * something else to get the orbit point. Then add the result of this to get the pos of the
	 * object
	 * 
	 * http://en.wikipedia.org/wiki/Spherical_coordinates
	 */
	public Vector3 getOrbitOffset(int pos)
	{
		float minRadius = this.getMinRadius();
		if (this.orbitRadius < minRadius)
		{
			this.orbitRadius = minRadius;
		}
		float spacing = this.orbitRadius / this.getOrbitMemebers().size();

		double x = this.orbitRadius * Math.cos((spacing * pos) + this.getRotation().y);
		double z = this.orbitRadius * Math.sin((spacing * pos) + this.getRotation().y);
		return null;
	}	
}