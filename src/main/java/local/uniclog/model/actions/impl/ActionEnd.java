package local.uniclog.model.actions.impl;

import local.uniclog.model.actions.ActionsInterface;
import local.uniclog.model.actions.types.ActionType;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import static java.lang.String.format;

@Slf4j
public class ActionEnd implements ActionsInterface {
    @Override
    public void execute(String... args) throws InterruptedException {
        log.debug("{}", this);
    }

    @Override
    public ActionsInterface fieldInjection(Map<String, String> args) {
        return this;
    }

    @Override
    public String toString() {
        return format("%s", getType().name());
    }

    @Override
    public ActionType getType() {
        return ActionType.END;
    }
}
