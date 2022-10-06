package io.lumine.mythic.lib.api.util;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.utils.adventure.text.Component;
import io.lumine.utils.adventure.text.format.NamedTextColor;
import io.lumine.utils.adventure.text.format.TextDecoration;
import io.lumine.utils.adventure.text.minimessage.MiniMessage;
import io.lumine.utils.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.NotNull;

public class LegacyComponent {
    private static final LegacyComponentSerializer unusualSectionRGB = LegacyComponentSerializer.builder().character("§".toCharArray()[0]).hexCharacter('#').hexColors()
            .useUnusualXRepeatedCharacterHexFormat().build();

    @NotNull
    public static Component parse(String text) {
        Component component = MiniMessage.get().parse(NestedSerializer.serialize(unusualSectionRGB.
                deserialize(MythicLib.inst().parseColors(text))));

        if (component.decorations().get(TextDecoration.ITALIC) == TextDecoration.State.NOT_SET)
            component = component.decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE);

        return component.colorIfAbsent(NamedTextColor.WHITE);
    }
}
