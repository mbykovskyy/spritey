/**
 * This source file is part of Spritey - the sprite sheet creator.
 * 
 * Copyright 2010 Maksym Bykovskyy.
 * 
 * Spritey is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * Spritey is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * Spritey. If not, see <http://www.gnu.org/licenses/>.
 */
package spritey.rcp.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * A utility class for striping illegal characters from a string.
 */
public class IllegalCharacterStripper {

    private Map<Character, String> charToStr;

    public IllegalCharacterStripper() {
        charToStr = new HashMap<Character, String>();
        charToStr.put(' ', "_space_");
        charToStr.put('!', "_exclamation_");
        charToStr.put('\"', "_quotation");
        charToStr.put('£', "_pound_");
        charToStr.put('$', "_dollar_");
        charToStr.put('%', "_percentage_");
        charToStr.put('^', "_caret_");
        charToStr.put('&', "_ampersand_");
        charToStr.put('*', "_asterisk_");
        charToStr.put('(', "_leftbracket_");
        charToStr.put(')', "_rightbracket_");
        charToStr.put('_', "_");
        charToStr.put('+', "_plus_");
        charToStr.put('-', "-");
        charToStr.put('=', "_equals_");
        charToStr.put('[', "_leftsquarebracket_");
        charToStr.put(']', "_rightsquarebracket_");
        charToStr.put('{', "_leftcurlybracket_");
        charToStr.put('}', "_rightcurlybracket_");
        charToStr.put(';', "_semicolon_");
        charToStr.put('\'', "_apostrophe_");
        charToStr.put('#', "_hash_");
        charToStr.put(':', "_colon_");
        charToStr.put('@', "_at_");
        charToStr.put('~', "_tilde_");
        charToStr.put(',', "_comma_");
        charToStr.put('.', "_dot_");
        charToStr.put('/', "_forwardslash_");
        charToStr.put('<', "_lessthan_");
        charToStr.put('>', "_greaterthan_");
        charToStr.put('?', "_question_");
        charToStr.put('\\', "_backslash_");
        charToStr.put('|', "_bar_");
        charToStr.put('a', "a");
        charToStr.put('b', "b");
        charToStr.put('c', "c");
        charToStr.put('d', "d");
        charToStr.put('e', "e");
        charToStr.put('f', "f");
        charToStr.put('g', "g");
        charToStr.put('h', "h");
        charToStr.put('i', "i");
        charToStr.put('j', "j");
        charToStr.put('k', "k");
        charToStr.put('l', "l");
        charToStr.put('m', "m");
        charToStr.put('n', "n");
        charToStr.put('o', "o");
        charToStr.put('p', "p");
        charToStr.put('q', "q");
        charToStr.put('r', "r");
        charToStr.put('s', "s");
        charToStr.put('t', "t");
        charToStr.put('u', "u");
        charToStr.put('v', "v");
        charToStr.put('w', "w");
        charToStr.put('x', "x");
        charToStr.put('y', "y");
        charToStr.put('z', "z");
        charToStr.put('A', "A");
        charToStr.put('B', "B");
        charToStr.put('C', "C");
        charToStr.put('D', "D");
        charToStr.put('E', "E");
        charToStr.put('F', "F");
        charToStr.put('G', "G");
        charToStr.put('H', "H");
        charToStr.put('I', "I");
        charToStr.put('J', "J");
        charToStr.put('K', "K");
        charToStr.put('L', "L");
        charToStr.put('M', "M");
        charToStr.put('N', "N");
        charToStr.put('O', "O");
        charToStr.put('P', "P");
        charToStr.put('Q', "Q");
        charToStr.put('R', "R");
        charToStr.put('S', "S");
        charToStr.put('T', "T");
        charToStr.put('U', "U");
        charToStr.put('V', "V");
        charToStr.put('W', "W");
        charToStr.put('X', "X");
        charToStr.put('Y', "Y");
        charToStr.put('Z', "Z");
        charToStr.put('0', "0");
        charToStr.put('1', "1");
        charToStr.put('2', "2");
        charToStr.put('3', "3");
        charToStr.put('4', "4");
        charToStr.put('5', "5");
        charToStr.put('6', "6");
        charToStr.put('7', "7");
        charToStr.put('8', "8");
        charToStr.put('9', "9");
    }

    /**
     * Replaces illegal characters with equivalent words. For example, #(hash)
     * character will be replaced with '_hash_' string.
     * 
     * @param str
     *        the string to process.
     * @return a new string with illegal characters stripped.
     */
    public String strip(String str) {
        StringBuilder builder = new StringBuilder();

        for (char ch : str.toCharArray()) {
            String s = charToStr.get(ch);
            if (null != s) {
                builder.append(s);
            }
        }
        return builder.toString();
    }

}
