package local.uniclog.utils;

import static local.uniclog.utils.ConfigConstants.TEMPLATE_UTILITY_CLASS;

public class DataUtils {
    private DataUtils() {
        throw new IllegalStateException(TEMPLATE_UTILITY_CLASS);
    }

    /**
     * Get Integer value from String
     *
     * @param value        string value
     * @param defaultValue default
     * @return Integer
     */
    public static Integer getInteger(String value, int defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ignore) {
            return defaultValue;
        }
    }

    /**
     * Get Long value from String
     *
     * @param value        string value
     * @param defaultValue default
     * @return Long
     */
    public static Long getLong(String value, long defaultValue) {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException ignore) {
            return defaultValue;
        }
    }

    /**
     * Get Boolean value from String
     *
     * @param value        string value
     * @param defaultValue default
     * @return Long
     */
    public static Boolean getBoolean(String value, boolean defaultValue) {
        try {
            return Boolean.parseBoolean(value);
        } catch (NumberFormatException ignore) {
            return defaultValue;
        }
    }
}
