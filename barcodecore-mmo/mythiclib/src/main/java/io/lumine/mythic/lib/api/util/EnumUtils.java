package io.lumine.mythic.lib.api.util;

import java.util.Optional;

public class EnumUtils {
    /**
     * @param enumClass The enum class to look in
     * @param key The value to lookup in the enum class.
     * @return An optional with the value of the lookup.
     * If the enum value did not exist it will return an empty optional
     */
    public static <T> Optional<T> getIfPresent(Class<T> enumClass, String key) {
        if(!enumClass.isEnum()) return Optional.empty();
        T enumValue = null;
        for(T constant : enumClass.getEnumConstants())
            if(((Enum<?>) constant).name().equals(key)) {
                enumValue = constant;
                break;
            }
        return Optional.ofNullable(enumValue);
    }
}
