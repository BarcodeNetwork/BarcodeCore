package io.lumine.mythic.lib.api.util;

public enum NBTTypeHelper {
    TAG_END(0),
    BYTE(1),
    BOOLEAN(1),
    SHORT(2),
    INT(3),
    LONG(4),
    FLOAT(5),
    DOUBLE(6),
    BYTE_ARRAY(7),
    STRING(8),
    LIST(9),
    COMPOUND(10),
    INT_ARRAY(11),
    LONG_ARRAY(12);

    private final int typeId;

    NBTTypeHelper(int id) {
        typeId = id;
    }

    public boolean is(int typeId) {
        return this.typeId == typeId;
    }

    public int getTypeId() {
        return typeId;
    }

    public static NBTTypeHelper getFrom(Class<?> clazz) {
        if(clazz.isAssignableFrom(Byte.class) ||
                clazz.isAssignableFrom(Boolean.class))
            return NBTTypeHelper.BYTE;
        else if(clazz.isAssignableFrom(Short.class))
            return NBTTypeHelper.SHORT;
        else if(clazz.isAssignableFrom(Integer.class))
            return NBTTypeHelper.INT;
        else if(clazz.isAssignableFrom(Long.class))
            return NBTTypeHelper.LONG;
        else if(clazz.isAssignableFrom(Double.class))
            return NBTTypeHelper.DOUBLE;
        else if(clazz.isAssignableFrom(Float.class))
            return NBTTypeHelper.FLOAT;
        else if(clazz.isAssignableFrom(String.class))
            return NBTTypeHelper.STRING;
        else
            return NBTTypeHelper.TAG_END;
    }
}
