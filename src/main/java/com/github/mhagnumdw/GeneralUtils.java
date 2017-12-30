package com.github.mhagnumdw;

/**
 * Utility for general use.
 */
public class GeneralUtils {

    private GeneralUtils() {
        // This class is not instantiable
    }

    /**
     * Format a String by changing the {} by their position in the varargs.
     *
     * <pre>
     * System.out.println(format("Eu tenho {} anos e moro em {}.", 5, "Fortaleza"));
     * Output: Eu tenho 5 anos e moro em Fortaleza.
     * </pre>
     *
     * @param format
     *            format
     * @param arguments
     *            values of {}
     *
     * @return A formatted string
     */
    public static String format(String format, Object... arguments) {
        return String.format(format.replace("{}", "%s"), arguments);
    }

    /**
     * <p>
     * Checks if a CharSequence is not empty (""), not null and not whitespace only.
     * </p>
     *
     * <pre>
     * StringUtils.isNotBlank(null)      = false
     * StringUtils.isNotBlank("")        = false
     * StringUtils.isNotBlank(" ")       = false
     * StringUtils.isNotBlank("bob")     = true
     * StringUtils.isNotBlank("  bob  ") = true
     * </pre>
     *
     * @param cs
     *            the CharSequence to check, may be null
     * 
     * @return {@code true} if the CharSequence is not empty and not null and not whitespace
     */
    public static boolean isNotBlank(final CharSequence cs) {
        return !isBlank(cs);
    }

    /**
     * <p>
     * Checks if a CharSequence is whitespace, empty ("") or null.
     * </p>
     *
     * <pre>
     * StringUtils.isBlank(null)      = true
     * StringUtils.isBlank("")        = true
     * StringUtils.isBlank(" ")       = true
     * StringUtils.isBlank("bob")     = false
     * StringUtils.isBlank("  bob  ") = false
     * </pre>
     *
     * @param cs
     *            the CharSequence to check, may be null
     * 
     * @return {@code true} if the CharSequence is null, empty or whitespace
     */
    public static boolean isBlank(final CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (Character.isWhitespace(cs.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }

}
