package team.space.utils;

import org.kordamp.ikonli.javafx.FontIcon;

public class FontIconFactory {
    public enum ICON {
        DELETE,
        USER,
        USER_BOARD,
        LOCK,
        POWER,
        EDIT,
        ADD,
        ZOOM
    }

    public static FontIcon createFontIcon(ICON icon, int size) {
        var iconCode = getIconCode(icon);
        var fontIcon = new FontIcon(iconCode);

        fontIcon.setIconSize(size);

        return fontIcon;
    }

    private static String getIconCode(ICON icon) {
        String iconCode = switch (icon) {
            case DELETE -> "fltral-delete-24";
            case USER -> "fltfmz-person-16";
            case USER_BOARD -> "fltfmz-person-board-16";
            case LOCK -> "fltfal-lock-24";
            case POWER -> "fltfmz-power-24";
            case EDIT -> "fltfal-edit-16";
            case ADD -> "fltfal-add-24";
            case ZOOM -> "fltrmz-zoom-in-24";
        };

        return iconCode;
    }
}
