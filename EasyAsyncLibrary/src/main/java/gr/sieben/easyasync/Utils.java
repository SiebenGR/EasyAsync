package gr.sieben.easyasync;

/**
 * Utility class that has convenience methods
 */
class Utils { //future use

    /**
     * Gets whether the current android phone is greater than Honeycomb or not
     * @return true if higher, false otherwise
     */
    public static boolean isGreaterThanHoneycomb() {
        return android.os.Build.VERSION.SDK_INT>=android.os.Build.VERSION_CODES.HONEYCOMB;
    }
}
