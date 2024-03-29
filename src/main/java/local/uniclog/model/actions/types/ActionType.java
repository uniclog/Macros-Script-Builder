package local.uniclog.model.actions.types;

import local.uniclog.model.actions.ActionsInterface;
import local.uniclog.model.actions.impl.*;

import java.util.Arrays;

/**
 * click_l - (x, y, up/down/click, count, period, sleepAfter)
 * click_r - (x, y, up/down/click, count, period, sleepAfter)
 * sleep - (time)
 * log - (x, y, color, alert)
 * <p>
 * key - buttons ?
 * <p>
 * default - console log
 */
public enum ActionType {
    MOUSE_CLICK("MOUSE_CLICK"),
    SLEEP("SLEEP"),
    WHILE_BRAKE_BY_COLOR("WHILE_BRAKE_BY_COLOR"),
    WHILE("WHILE"),
    END("END"),
    KEY_PRESS("KEY_PRESS"),

    DEFAULT("DEFAULT");

    private final String value;

    ActionType(String value) {
        this.value = value;
    }

    public static ActionType getType(String msg) {
        return Arrays.stream(ActionType.values())
                .filter(it -> it.value.equalsIgnoreCase(msg))
                .findFirst()
                .orElse(DEFAULT);
    }

    public String getStringValue() {
        return value;
    }

    public ActionsInterface getAction() {
        return switch (this) {
            case MOUSE_CLICK -> new MouseClick();
            case WHILE_BRAKE_BY_COLOR -> new ActionWhileBrakeByColor();
            case SLEEP -> new Sleep();
            case WHILE -> new ActionWhile();
            case END -> new ActionEnd();
            case KEY_PRESS -> new ActionKeyPress();
            default -> new Default();
        };
    }
}
