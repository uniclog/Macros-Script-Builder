package local.uniclog.services;

import local.uniclog.model.ActionContainer;
import local.uniclog.model.ActionType;
import local.uniclog.model.ActionsInterface;
import local.uniclog.model.actions.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyMap;
import static java.util.stream.Collectors.toMap;

@Slf4j
@Data
public class ActionService {
    private ActionContainer container = new ActionContainer();

    public ActionService addAction(ActionsInterface instruction) {
        container.add(instruction);
        return this;
    }

    public ActionService clear() {
        container.clear();
        return this;
    }

    public void setConfiguration(List<String> actionLines) {
        container.clear();
        actionLines.forEach(line -> {
                    ActionType type = getActionType(line);
                    if (type == ActionType.DEFAULT) {
                        return;
                    }
                    Map<String, String> map = getActionMap(line);
                    ActionsInterface action = getAction(type, map);
                    container.add(action);
                }
        );
        container.add(getAction(ActionType.END, null));
        log.debug("{}", container);
    }

    private ActionsInterface getAction(ActionType actionType, Map<String, String> map) {
        ActionsInterface action = switch (actionType) {
            case MOUSE_CLICK -> new MouseClick();
            case WHILE_BRAKE_BY_COLOR -> new ActionWhileBrakeByColor();
            case SLEEP -> new Sleep();
            case WHILE -> new ActionWhile();
            case END -> new ActionEnd();
            case KEY_PRESS -> new ActionKeyPress();

            default -> new Default();
        };
        return action.fieldInjection(map);
    }

    private ActionType getActionType(String line) {
        return Arrays.stream(ActionType.values())
                .filter(it -> line.startsWith(it.name()))
                .findFirst()
                .orElse(ActionType.DEFAULT);
    }

    private Map<String, String> getActionMap(String line) {
        try {
            String map = line.substring(line.indexOf('[') + 1, line.indexOf(']'));
            return Arrays.stream(map.split(","))
                    .map(param -> param.split("="))
                    .collect(toMap(it -> it[0], it -> it[1]));
        } catch (IndexOutOfBoundsException e) {
            log.error("ActionMap is empty - {}", line);
            return emptyMap();
        }
    }
}