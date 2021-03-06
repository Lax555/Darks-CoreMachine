package dark.core.prefab.helpers;

import java.util.Random;

import universalelectricity.core.vector.Vector3;

public class MathHelper extends net.minecraft.util.MathHelper
{
    /** Generates an array of random numbers
     * 
     * @param random - random instance to be used
     * @param maxNumber - max size of the int to use
     * @param arraySize - length of the array
     * @return array of random numbers */
    public static int[] generateRandomIntArray(Random random, int maxNumber, int arraySize)
    {
        return MathHelper.generateRandomIntArray(random, 0, maxNumber, arraySize);
    }

    /** Generates an array of random numbers
     * 
     * @param random - random instance to be used
     * @param minNumber - smallest random Integer to use. Warning can lead to longer than normal
     * delay in returns
     * @param maxNumber - max size of the int to use
     * @param arraySize - length of the array
     * @return array of random numbers */
    public static int[] generateRandomIntArray(Random random, int minNumber, int maxNumber, int arraySize)
    {
        int[] array = new int[arraySize];
        for (int i = 0; i < array.length; i++)
        {
            int number = random.nextInt(maxNumber);
            if (minNumber != 0)
            {
                while (number < minNumber)
                {
                    number = random.nextInt(maxNumber);
                }
            }
            array[i] = number;
        }
        return array;
    }

    /** @param vec - vector3 that is on the sphere
     * @return new Vector3(radius, inclination, azimuth) */
    public static Vector3 vecToSphereAngles(Vector3 vec)
    {
        double radius = Math.sqrt((vec.x * vec.x) + (vec.y * vec.y) + (vec.z * vec.z));
        double inclination = Math.acos(vec.z / radius);
        double azimuth = Math.atan(vec.y / vec.z);
        return new Vector3(radius, inclination, azimuth);
    }

    /** Turns radius and sphere cords into a vector3
     * 
     * @param radius - sphere radius
     * @param inclination -
     * @param azimuth
     * @return Vector3(x,y,z) */
    public static Vector3 sphereAnglesToVec(Double radius, Double inclination, Double azimuth)
    {
        double x = radius * Math.sin(inclination) * Math.cos(azimuth);
        double y = radius * Math.sin(inclination) * Math.sin(azimuth);
        double z = radius * Math.cos(inclination);

        return new Vector3(x, y, z);
    }

    /** Clamps the angles to a min max by adding or subtracting the min max. This way it maintanes
     * the change in angle in the chance it goes out of bounds */
    public static float clampAngle(float var, float min, float max)
    {
        while (var < min)
        {
            var += min;
        }
        while (var > max)
        {
            var -= max;
        }
        return var;
    }

    /** Clamps an angle to 360 degree circle */
    public static float clampAngleTo360(float var)
    {
        return MathHelper.clampAngle(var, 0, 360);
    }

    /** Find the shortest delta change to the angle goal from the current angle */
    public static float shortestAngleTo360(float angle, float angleGoal)
    {
        angle = clampAngleTo360(angle);
        angleGoal = clampAngleTo360(angleGoal);

        if (angle == angleGoal)
        {
            return 0;
        }
        else if (angle > angleGoal)
        {
            return angleGoal - angle;
        }
        else
        {
            return angle - angleGoal;
        }
    }
}
