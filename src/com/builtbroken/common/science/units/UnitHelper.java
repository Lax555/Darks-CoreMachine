package com.builtbroken.common.science.units;

import java.util.ArrayList;
import java.util.List;

import com.builtbroken.common.Pair;

public class UnitHelper
{
    public static final float FEET_TO_METERS = 0.3048f;
    public static final float METERS_TO_FEET = 3.28084f;

    public static List<Character> numbers = new ArrayList<>();

    static
    {
        numbers.add('0');
        numbers.add('1');
        numbers.add('2');
        numbers.add('3');
        numbers.add('4');
        numbers.add('5');
        numbers.add('6');
        numbers.add('7');
        numbers.add('8');
        numbers.add('9');
    }

    public float convert(ImperialUnits a, MetricUnit b, float value)
    {
        return b.convert(MetricUnit.BASE, a.convert(ImperialUnits.foot, value) * FEET_TO_METERS);

    }

    public float convert(MetricUnit a, ImperialUnits b, float value)
    {
        return b.convert(ImperialUnits.foot, a.convert(MetricUnit.BASE, value) * METERS_TO_FEET);
    }

    public Pair<Object, Float> parseString(String input)
    {
        Pair<Object, Float> def = null;
        if (input != null && !input.isEmpty())
        {
            String editedString = input;
            char[] chars = input.toCharArray();
            String number = "";
            String units = "";
            int toPowerOf = 1;
            int timeTenToPowerOF = 1;

            //Get number first
            for (int i = 0; i < chars.length; i++)
            {
                char c = chars[i];
                if (numbers.contains(c))
                {
                    number += c;
                }
                else
                {
                    break;
                }
            }
            editedString.replaceAll("[0-9]", "");
            chars = editedString.toCharArray();
            //Check if number is being times by 10 to the power of something
            if (chars != null)
            {
                if (chars.length >= 5 && chars[0] == 'x' && chars[1] == '1' && chars[2] == '0' && chars[3] == '^' && numbers.contains(chars[4]))
                {
                    timeTenToPowerOF = Integer.parseInt("" + chars[4], 1);
                    editedString = editedString.substring(5);
                }
                else if (chars.length >= 2 && chars[0] == '^' && numbers.contains(chars[1]))
                {
                    toPowerOf = Integer.parseInt("" + chars[1], 1);
                    editedString = editedString.substring(2);
                }
            }

        }

        return def;
    }
}
