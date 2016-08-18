package core;

import com.sun.istack.internal.NotNull;
import javafx.scene.control.Alert;

interface Alerts {

    void createErrorAlert(String header, @NotNull String content);

    void createInfoAlert(String header, @NotNull String content);

    void createWarningAlert(String header, @NotNull String content);

    Alert createConfirmationAlert(String header, @NotNull String content);

}
