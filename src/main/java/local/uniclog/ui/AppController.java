package local.uniclog.ui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import local.uniclog.model.ActionType;
import local.uniclog.model.MouseButtonType;
import local.uniclog.model.actions.MouseClick;
import local.uniclog.services.ActionProcessExecuteService;
import local.uniclog.services.FileServiceWrapper;
import local.uniclog.services.JnaKeyHookService;
import local.uniclog.services.MouseServiceWrapper;
import local.uniclog.utils.DataUtils;
import lombok.extern.slf4j.Slf4j;

import static local.uniclog.model.MouseButtonType.BUTTON_L;

@Slf4j
public class AppController {
    private static final String GUI_BUTTON_RED = "gui-button-red";
    private static final String GUI_BUTTON_GREEN = "gui-button-green";
    @FXML
    private Button onRunActionButton;
    @FXML
    private ChoiceBox<MouseButtonType> setMouseActionChoiceBox;
    @FXML
    private TextField setMouseActionCountTextField;
    @FXML
    private TextField setMouseActionPeriodTextField;
    @FXML
    private TextField setMouseActionSleepAfterTextField;
    @FXML
    private Button setMouseActionReaderButton;
    @FXML
    private Pane setMousePane;
    @FXML
    private TextArea textAreaConsole;
    @FXML
    private ToggleButton exit;
    @FXML
    private ChoiceBox<ActionType> setActionChoiceBox;

    private boolean initializeHookListener = true;
    private boolean initializeRunExecute = false;

    // Main Controls Block ============================================
    public void onExit() {
        System.exit(0);
    }

    public void onMin() {
        Stage stage = (Stage) exit.getScene().getWindow();
        stage.setIconified(true);
    }
    // ================================================================

    public void initialize() {
        log.debug("App Controller init");

        setActionChoiceBox.getItems().setAll(ActionType.values());
        setMouseActionChoiceBox.getItems().setAll(MouseButtonType.values());
        setMouseActionChoiceBox.setValue(BUTTON_L);

        setActionChoiceBox.getSelectionModel()
                .selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                    setMousePane.setVisible(false);
                    if (newValue.equals(ActionType.MOUSE_CLICK))
                        setMousePane.setVisible(true);
                });
    }

    /**
     * Button: Load configuration
     */
    public void onLoad() {
        textAreaConsole.setText(FileServiceWrapper.read());
    }

    /**
     * Button: Save configuration to file
     */
    public void onSave() {
        FileServiceWrapper.write(textAreaConsole.getText());
    }

    /**
     * Button: Read Coordinates
     */
    public void setMouseActionReaderAction() {
        if (initializeHookListener) {
            setMouseActionReaderButton.setText("Stop Action Read");
            setMouseActionReaderButton.getStyleClass().removeAll();
            setMouseActionReaderButton.getStyleClass().add(GUI_BUTTON_RED);
        } else {
            setMouseActionReaderButton.setText("Start Action Read");
            setMouseActionReaderButton.getStyleClass().removeAll(GUI_BUTTON_RED);
            setMouseActionReaderButton.getStyleClass().add(GUI_BUTTON_GREEN);
        }

        JnaKeyHookService jnaKeyHookService = new JnaKeyHookService();
        jnaKeyHookService.initialize(initializeHookListener, this::setMouseInfo);
        initializeHookListener = !initializeHookListener;
    }

    /**
     * Add mouse info to TextArea Console
     *
     * @param ignore ignore
     */
    public void setMouseInfo(Boolean ignore) {
        MouseClick action = MouseClick.builder()
                .action(setMouseActionChoiceBox.getValue())
                .point(MouseServiceWrapper.getMousePointer())
                .count(DataUtils.getInteger(setMouseActionCountTextField.getText(), 0))
                .period(DataUtils.getLong(setMouseActionPeriodTextField.getText(), 0L))
                .sleepAfter(DataUtils.getLong(setMouseActionSleepAfterTextField.getText(), 0L))
                .build();
        Platform.runLater(() -> textAreaConsole
                .setText(textAreaConsole.getText()
                        + "\n"
                        + action.toString())
        );
    }

    public void onRunAction() {
        onRunActionComplete(initializeRunExecute);
        ActionProcessExecuteService actionProcessExecuteService = new ActionProcessExecuteService();
        actionProcessExecuteService.initialize(initializeRunExecute, textAreaConsole.getText(), this::onRunActionComplete);
    }

    public void onRunActionComplete(Boolean complete) {
        if (complete.equals(false)) {
            Platform.runLater(() -> {
                onRunActionButton.setText("Stop");
                onRunActionButton.getStyleClass().add(GUI_BUTTON_RED);
            });
            initializeRunExecute = true;
        } else {
            Platform.runLater(() -> {
                onRunActionButton.setText("Run");
                onRunActionButton.getStyleClass().removeAll(GUI_BUTTON_RED);
                onRunActionButton.getStyleClass().add(GUI_BUTTON_GREEN);
            });
            initializeRunExecute = false;
        }
    }
}