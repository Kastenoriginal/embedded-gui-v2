package layouts;

import core.AlertsImpl;
import core.Logger;
import core.Pin;
import core.Root;
import core.hashmaps.RaspberryHashMap;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.List;

public class RaspberryLayout implements EmbeddedLayout {

    private final static int GRID_PANE_POSITION_IN_ROOT_CHILDREN = 3;
    private final static int ELEMENTS_FOR_ROW_COUNT = 5;
    private final static int LAYOUT_MAX_COL_COUNT = 10;
    private final static int BUTTON_MIN_SIZE_WIDTH = 35;
    private final static int BUTTON_MIN_SIZE_HEIGHT = 35;
    private final static int PIN_COUNT = 40;
    private final static int IO_COMBO_BOX_PREF_WIDTH = 80;
    private final static int PIN_TYPE_COMBO_BOX_PREF_WIDTH = 110;
    private final static int ELEMENTS_COUNT = PIN_COUNT * ELEMENTS_FOR_ROW_COUNT;
    private final static String GPIO_OUTPUT = "OUT";
    private final static String GPIO_INPUT = "IN";
    private static final String BUTTON_FONT_SIZE = "-fx-font-size: 12";
    private static final String LAYOUT_CREATION_FAILED = "Creation of layout failed.";
    private static final String BUTTON_COLOR_RED = "-fx-background-color: #ff9999;-fx-text-fill: black;-fx-font-size: 9pt;";
    private static final String BUTTON_COLOR_GREEN = "-fx-background-color: #99ff99;-fx-text-fill: black;-fx-font-size: 9pt;";

    private Root root;
    private Logger logger;
    private GridPane gridPane;
    private AlertsImpl alerts;
    private ArrayList<CheckBox> checkBoxes;
    private ArrayList<ComboBox<String>> inputOutputComboBoxes;
    private ArrayList<ComboBox<String>> pinTypeComboBoxes;
    private ArrayList<Button> buttons;
    private ArrayList<Pin> pins;
    private int row = 1;
    private int column = 1;
    private int buttonId = 1;
    private int pinTypeComboBoxId = 1;
    private int inputOutputComboBoxId = 1;
    private int checkBoxId = 1;

    public void setMainApp(Root root, Logger logger) {
        this.root = root;
        this.logger = logger;
        init();
    }

    private void init() {
        gridPane = (GridPane) root.getRootLayout().getChildren().get(GRID_PANE_POSITION_IN_ROOT_CHILDREN);

        alerts = new AlertsImpl();

        checkBoxes = new ArrayList<>();
        inputOutputComboBoxes = new ArrayList<>();
        pinTypeComboBoxes = new ArrayList<>();
        buttons = new ArrayList<>();
        pins = new ArrayList<>();

        createLayout();
        disableGridElements(buttons, pinTypeComboBoxes, inputOutputComboBoxes, checkBoxes);
        for (CheckBox checkBox : checkBoxes) {
            toggleElementsEnabled(false, Integer.valueOf(checkBox.getId()) - 1);
        }
    }

    private void createLayout() {
        RaspberryHashMap piMap = new RaspberryHashMap();
        piMap.createHashMap();

        for (int i = 0; i < ELEMENTS_COUNT; i++) {
            if (column > LAYOUT_MAX_COL_COUNT) {
                column = 1;
                row++;
            }
            if (pinTypeComboBoxId > PIN_COUNT) {
                pinTypeComboBoxId = PIN_COUNT;
            }
            String[] pinTypes = piMap.getValueByKey(pinTypeComboBoxId);
            ObservableList<String> pinTypeOptions = FXCollections.observableArrayList(pinTypes);

            if (column == 1 || column == 10) {
                //reserved in case any element need to be added to the outer edge of layout
            } else if (column == 2 || column == 9) {
                addCheckBoxToLayout();
            } else if (column == 3 || column == 8) {
                addInputOutputComboBoxToLayout();
            } else if (column == 4 || column == 7) {
                addPinTypeComboBoxToLayout(pinTypeOptions);
            } else if (column == 5 || column == 6) {
                addButtonToLayout();
            } else {
                logger.log(LAYOUT_CREATION_FAILED);
                alerts.createErrorAlert(null, LAYOUT_CREATION_FAILED);
            }
            column++;
        }
    }

    private void disableGridElements(ArrayList<Button> buttons, ArrayList<ComboBox<String>> pinTypeComboBoxes,
                                     final ArrayList<ComboBox<String>> inputOutputComboBoxes, ArrayList<CheckBox> checkBoxes) {
        for (int i = 0; i < PIN_COUNT; i++) {
            String selectedItem = pinTypeComboBoxes.get(i).getSelectionModel().getSelectedItem();
            if (Pin.PWR5.equals(selectedItem) ||
                    Pin.PWR3.equals(selectedItem) ||
                    Pin.GND.equals(selectedItem) ||
                    Pin.EEPROM.equals(selectedItem)) {
                pinTypeComboBoxes.get(i).setDisable(true);
                inputOutputComboBoxes.get(i).setVisible(false);
                checkBoxes.get(i).setVisible(false);
                buttons.get(i).setDisable(true);
            }
        }
    }

    private void addCheckBoxToLayout() {
        final CheckBox checkBox = new CheckBox();
        checkBox.setId(String.valueOf(checkBoxId));
        checkBox.selectedProperty().addListener((observableValue, oldValue, newValue) -> {
            int checkBoxId1 = Integer.valueOf(checkBox.getId()) - 1;

            toggleElementsEnabled(newValue, checkBoxId1);

            String buttonText = buttons.get(checkBoxId1).getText().trim();
            String ioType = inputOutputComboBoxes.get(checkBoxId1).getSelectionModel().getSelectedItem();
            String pinType = pinTypeComboBoxes.get(checkBoxId1).getSelectionModel().getSelectedItem();

            Pin pin = new Pin(Integer.valueOf(buttonText), ioType, pinType);

            if (newValue) {
                addPin(pin);
            } else {
                removePin(pin);
            }

            if (pins != null || !pins.isEmpty()) {
                root.handleCheckBoxClick(pins);
            }
        });
        checkBoxes.add(checkBox);
        checkBoxId++;
        gridPane.add(checkBox, column, row);
    }

    private void addInputOutputComboBoxToLayout() {
        ObservableList<String> inputOutputOptions = FXCollections.observableArrayList(GPIO_OUTPUT, GPIO_INPUT);
        final ComboBox<String> inputOutputComboBox = new ComboBox<>(inputOutputOptions);
        inputOutputComboBox.setId(String.valueOf(inputOutputComboBoxId));
        inputOutputComboBox.setPrefWidth(IO_COMBO_BOX_PREF_WIDTH);
        inputOutputComboBox.getSelectionModel().selectFirst();
        inputOutputComboBoxes.add(inputOutputComboBox);
        inputOutputComboBoxId++;
        inputOutputComboBox.setOnAction(arg0 -> {
            Object currentlySelectedItem = inputOutputComboBox.getSelectionModel().getSelectedItem();
            int inputOutputComboBoxId1 = Integer.valueOf(inputOutputComboBox.getId()) - 1;

            if (GPIO_INPUT.equals(currentlySelectedItem)) {
                buttons.get(inputOutputComboBoxId1).setDisable(true);
            } else {
                buttons.get(inputOutputComboBoxId1).setDisable(false);
            }
        });
        gridPane.add(inputOutputComboBox, column, row);
    }

    private void addPinTypeComboBoxToLayout(ObservableList<String> pinTypeOptions) {
        final ComboBox<String> pinTypeComboBox = new ComboBox<>(pinTypeOptions);
        pinTypeComboBox.setId(String.valueOf(pinTypeComboBoxId));
        pinTypeComboBox.setPrefWidth(PIN_TYPE_COMBO_BOX_PREF_WIDTH);
        pinTypeComboBox.getSelectionModel().selectFirst();
        pinTypeComboBoxes.add(pinTypeComboBox);
        pinTypeComboBoxId++;
        EventHandler<ActionEvent> eventHandler = arg0 -> {
            int pinTypeComboBoxId1 = Integer.valueOf(pinTypeComboBox.getId()) - 1;
            ComboBox<String> currentRowComboBox = inputOutputComboBoxes.get(pinTypeComboBoxId1);
            Button currentRowButton = buttons.get(pinTypeComboBoxId1);
            CheckBox currentRowCheckBox = checkBoxes.get(pinTypeComboBoxId1);
            setElementsVisibility(pinTypeComboBox.getSelectionModel().getSelectedItem(), currentRowComboBox, currentRowButton, currentRowCheckBox.isSelected());
            synchronizePinElements(pinTypeComboBox);
        };
        pinTypeComboBox.setOnAction(eventHandler);
        gridPane.add(pinTypeComboBox, column, row);
    }

    private void addButtonToLayout() {
        final Button button = new Button();
        button.setId(String.valueOf(buttonId));
        button.setUserData('0');
        button.setStyle(BUTTON_FONT_SIZE);
        button.setMinSize(BUTTON_MIN_SIZE_WIDTH, BUTTON_MIN_SIZE_HEIGHT);
        button.setText(String.valueOf(buttonId));
        buttons.add(button);
        buttonId++;
        button.setOnAction(arg0 -> {
            int buttonId1 = Integer.valueOf(button.getId()) - 1;

            String buttonText = button.getText().trim();
            String pinType = pinTypeComboBoxes.get(buttonId1).getSelectionModel().getSelectedItem();
            String ioType = inputOutputComboBoxes.get(buttonId1).getSelectionModel().getSelectedItem();

            Pin pin = new Pin(Integer.valueOf(buttonText), ioType, pinType);
            root.handlePinButtonClick(pin);
        });
        gridPane.add(button, column, row);
    }

    private void toggleElementsEnabled(boolean newValue, int checkBoxId) {
        if (newValue) {
            enableRowElements(checkBoxId);
        } else {
            disableRowElements(checkBoxId);
        }

        setElementsVisibility(pinTypeComboBoxes.get(checkBoxId).getSelectionModel().getSelectedItem(),
                inputOutputComboBoxes.get(checkBoxId), buttons.get(checkBoxId), newValue);
    }

    private void enableRowElements(int checkBoxId) {
        ComboBox currentPinTypeComboBox = pinTypeComboBoxes.get(checkBoxId);
        currentPinTypeComboBox.setDisable(false);
        inputOutputComboBoxes.get(checkBoxId).setDisable(false);
        buttons.get(checkBoxId).setDisable(false);
        if (Pin.I2C.equals(currentPinTypeComboBox.getSelectionModel().getSelectedItem())) {
            for (ComboBox<String> pinTypeComboBox : pinTypeComboBoxes) {
                if (Pin.I2C.equals(pinTypeComboBox.getSelectionModel().getSelectedItem())) {
                    int comboBoxId = Integer.valueOf(pinTypeComboBox.getId()) - 1;
                    checkBoxes.get(comboBoxId).setSelected(true);
                }
            }
        } else if (Pin.SPI.equals(currentPinTypeComboBox.getSelectionModel().getSelectedItem())) {
            for (ComboBox<String> pinTypeComboBox : pinTypeComboBoxes) {
                if (Pin.SPI.equals(pinTypeComboBox.getSelectionModel().getSelectedItem())) {
                    int comboBoxId = Integer.valueOf(pinTypeComboBox.getId()) - 1;
                    checkBoxes.get(comboBoxId).setSelected(true);
                }
            }
        }
    }

    private void disableRowElements(int checkBoxId) {
        ComboBox currentPinTypeComboBox = pinTypeComboBoxes.get(checkBoxId);
        inputOutputComboBoxes.get(checkBoxId).setDisable(true);
        currentPinTypeComboBox.setDisable(true);
        buttons.get(checkBoxId).setDisable(true);
        if (Pin.I2C.equals(currentPinTypeComboBox.getSelectionModel().getSelectedItem())) {
            for (ComboBox<String> pinTypeComboBox : pinTypeComboBoxes) {
                if (Pin.I2C.equals(pinTypeComboBox.getSelectionModel().getSelectedItem())) {
                    int comboBoxId = Integer.valueOf(pinTypeComboBox.getId()) - 1;
                    checkBoxes.get(comboBoxId).setSelected(false);
                }
            }
        } else if (Pin.SPI.equals(currentPinTypeComboBox.getSelectionModel().getSelectedItem())) {
            for (ComboBox<String> pinTypeComboBox : pinTypeComboBoxes) {
                if (Pin.SPI.equals(pinTypeComboBox.getSelectionModel().getSelectedItem())) {
                    int comboBoxId = Integer.valueOf(pinTypeComboBox.getId()) - 1;
                    checkBoxes.get(comboBoxId).setSelected(false);
                }
            }
        }
    }

    private void setElementsVisibility(String pinTypeComboBox, ComboBox<String> inputOutputComboBox, Button button, boolean checkBoxSelected) {
        if (checkBoxSelected) {
            switch (pinTypeComboBox) {
                case Pin.I2C:
                    inputOutputComboBox.setVisible(false);
                    button.setDisable(false);
                    break;
                case Pin.SPI:
                    inputOutputComboBox.setVisible(false);
                    button.setDisable(false);
                    break;
                case Pin.UART:
                    inputOutputComboBox.setVisible(false);
                    button.setDisable(true);
                    break;
                default:
                    inputOutputComboBox.setVisible(true);
                    if (GPIO_OUTPUT.equals(inputOutputComboBox.getSelectionModel().getSelectedItem())) {
                        button.setDisable(false);
                    } else {
                        button.setDisable(true);
                    }
                    break;
            }
        }
    }

    private void synchronizePinElements(ComboBox<String> pinTypeComboBox) {
        String pinTypeSelectedItem = pinTypeComboBox.getSelectionModel().getSelectedItem();
        if (Pin.I2C.equals(pinTypeSelectedItem)) {
            for (ComboBox<String> comboBox : pinTypeComboBoxes) {
                if (comboBox.getItems().contains(Pin.I2C)) {
                    comboBox.getSelectionModel().select(Pin.I2C);
                    checkBoxes.get(Integer.valueOf(comboBox.getId()) - 1).setSelected(true);
                }
            }
        } else if (Pin.SPI.equals(pinTypeSelectedItem) && pinTypeComboBox.getItems().contains(Pin.SPI)) {
            for (ComboBox<String> comboBox : pinTypeComboBoxes) {
                if (comboBox.getItems().contains(Pin.SPI)) {
                    comboBox.getSelectionModel().select(Pin.SPI);
                    checkBoxes.get(Integer.valueOf(comboBox.getId()) - 1).setSelected(true);
                }
            }
        } else if (Pin.GPIO.equals(pinTypeSelectedItem)) {
            if (pinTypeComboBox.getItems().contains(Pin.I2C)) {
                for (ComboBox<String> comboBox : pinTypeComboBoxes) {
                    if (comboBox.getItems().contains(Pin.I2C)) {
                        comboBox.getSelectionModel().select(Pin.GPIO);
                    }
                }
            }
            if (pinTypeComboBox.getItems().contains(Pin.SPI)) {
                for (ComboBox<String> comboBox : pinTypeComboBoxes) {
                    if (comboBox.getItems().contains(Pin.SPI)) {
                        comboBox.getSelectionModel().select(Pin.GPIO);
                    }
                }
            }
        }
    }

    private void addPin(Pin pin) {
        if (!pins.contains(pin)) {
            pins.add(pin);
        }
    }

    private void removePin(Pin pinToRemove) {
        for (Pin pin : pins) {
            if (pin.getPinId() == pinToRemove.getPinId()) {
                pins.remove(pin);
                return;
            }
        }
    }

    @Override
    public List<Pin> getCheckedPins() {
        return pins;
    }

    @Override
    public void updatePinsStatus(List<Pin> pins) {

        String randomColor = Math.random() > 0.5 ? BUTTON_COLOR_RED : BUTTON_COLOR_GREEN;

        // TODO: 19.8.2016 kuknut eclipse metodu setUiFromResponse a podla toho co pride zo servera poriesit metodu ify atd...
        for (Pin pin : pins) {
            int pinId = pin.getPinId() - 1;
            if (pin.isValue()) {
                buttons.get(pinId).setStyle(randomColor);
            } else {
                buttons.get(pinId).setStyle(randomColor);
            }

        }
    }
}
