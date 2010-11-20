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
package spritey.core.validator;

/**
 * Validates that the value contains only valid characters.
 */
public class CharacterValidator extends AbstractValidator {

    // Error codes (each code has to be unique across the whole application)
    public static final int INVALID = 50;
    public static final int ILLEGAL_CHARACTER = 51;

    // Messages
    private static final String M_INVALID = "Object is not of type String or string is null.";
    private static final String M_ILLEGAL_CHARACTER = "' character is illegal.";

    private String legalCharacters;

    /**
     * Constructor
     * 
     * @param legalCharacters
     *        a string of legal characters.
     */
    public CharacterValidator(String legalCharacters) {
        this.legalCharacters = legalCharacters;
    }

    @Override
    public boolean isValid(Object value) {
        if (value instanceof String) {
            for (char ch : ((String) value).toCharArray()) {
                if (-1 == legalCharacters.indexOf(ch)) {
                    setErrorCode(ILLEGAL_CHARACTER);
                    setMessage("'" + ch + M_ILLEGAL_CHARACTER);
                    return false;
                }
            }
            return true;
        }
        setErrorCode(INVALID);
        setMessage(M_INVALID);
        return false;
    }

}
