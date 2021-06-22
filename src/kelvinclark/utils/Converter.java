/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kelvinclark.utils;

import java.util.TreeMap;

/**
 *
 * @author Kelvin Clark Spátola
 */
public class Converter {
 private final static TreeMap<Integer, String> map = new TreeMap<Integer, String>();

  static {
    map.put(1000, "M");
    map.put(900, "CM");
    map.put(500, "D");
    map.put(400, "CD");
    map.put(100, "C");
    map.put(90, "XC");
    map.put(50, "L");
    map.put(40, "XL");
    map.put(10, "X");
    map.put(9, "IX");
    map.put(5, "V");
    map.put(4, "IV");
    map.put(1, "I");
  }

  public static final String toRoman(int number) {
    int baseNumber =  map.floorKey(number);
    
    if (number == baseNumber) return map.get(number);
    return map.get(baseNumber) + toRoman(number - baseNumber);
  }
}
