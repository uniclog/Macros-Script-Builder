package local.uniclog.model.actions.impl;

import local.uniclog.model.actions.ActionsInterface;
import local.uniclog.model.actions.types.ActionType;
import local.uniclog.model.actions.types.EventStateType;
import local.uniclog.utils.DataUtils;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.awt.event.KeyEvent;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static java.lang.String.format;

@Slf4j
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActionKeyPress implements ActionsInterface {
    @Builder.Default
    private String text = "";
    @Builder.Default
    private Integer keyCode = null;
    @Builder.Default
    private Long sleepAfter = 0L;
    @Builder.Default
    private EventStateType eventStateType = EventStateType.PRESS;

    @SneakyThrows
    @Override
    public void execute(String... args) throws InterruptedException {
        log.debug("{}", this);
        actionPressText(text);
        actionPressKey(keyCode);
        TimeUnit.MILLISECONDS.sleep(sleepAfter);
    }

    private void actionPressText(String keys) {
        for (char c : keys.toCharArray()) {
            int code = KeyEvent.getExtendedKeyCodeForChar(c);
            if (!Objects.equals(KeyEvent.CHAR_UNDEFINED, code)) {
                actionPressKey(code);
            }
        }
    }

    private void actionPressKey(Integer code) {
        try {
            if (Objects.nonNull(code)) {
                if (eventStateType.equals(EventStateType.DOWN) || eventStateType.equals(EventStateType.PRESS)) {
                    User32.INSTANCE.keybd_event(code.byteValue(), (byte) 0x00, 0, 0);
                }
                if (eventStateType.equals(EventStateType.UP) || eventStateType.equals(EventStateType.PRESS)) {
                    User32.INSTANCE.keybd_event(code.byteValue(), (byte) 0x00, 0x0002, 0);
                }
            }
        } catch (IllegalArgumentException ex) {
            log.error(ex.getMessage());
        }
    }

    @Override
    public ActionsInterface fieldInjection(Map<String, String> args) {
        if (Objects.nonNull(args))
            args.forEach(this::setFieldValue);
        return this;
    }

    private void setFieldValue(String key, String value) {
        switch (key) {
            case "text" -> setText(value);
            case "keyCode" -> setKeyCode(DataUtils.getInteger(value, 0));
            case "sleepAfter" -> setSleepAfter(DataUtils.getLong(value, 0));
            case "state" -> setEventStateType(EventStateType.getType(value));
            default -> log.debug("Field: {}, not set: {}", key, value);
        }
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();
        sb.append(format("%s [", getType().name()));
        if (!text.isBlank()) sb.append(format("text=%s", text));
        if (Objects.nonNull(keyCode)) {
            if (!text.isBlank()) sb.append(", ");
            sb.append(format("keyCode=%s", keyCode));
        }
        if (sleepAfter != 0) sb.append(format(", sleepAfter=%d", sleepAfter));
        sb.append(format(", state=%s", eventStateType));
        sb.append("]");
        return sb.toString();
    }

    @Override
    public ActionType getType() {
        return ActionType.KEY_PRESS;
    }
}
