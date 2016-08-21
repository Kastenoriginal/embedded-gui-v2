package core.networking;

import core.Logger;
import core.Pin;
import javafx.application.Platform;

import java.util.concurrent.Callable;

class ToggleGpioPin implements Callable<NetworkingParams> {

    private static final int TWO_DIGITS_VALUE = 10;

    private NetworkingParams params;
    private String dateAndTime;
    private Logger logger;
    private Pin pin;

    ToggleGpioPin(String dateAndTime, NetworkingParams params, Logger logger, Pin pin) {
        this.params = params;
        this.dateAndTime = dateAndTime;
        this.logger = logger;
        this.pin = pin;
    }

    @Override
    public NetworkingParams call() throws Exception {
        if (params != null) {
            if (pin.getPinId() < TWO_DIGITS_VALUE) {
                String message = dateAndTime + pin.getIoType() + pin.getPinType() + ":0" + pin.getPinId() + "1";
                Platform.runLater(() -> logger.log("Sending: " + message));
                params.out.println(message);
            } else {
                String message = dateAndTime + pin.getIoType() + pin.getPinType() + ":" + pin.getPinId() + "1";
                Platform.runLater(() -> logger.log("Sending: " + message));
                params.out.println(message);
            }
            // TODO: 21.8.2016  GPIO pin skusit poslat z BBB layoutu na neexistujuci pin - breakpoint ci zamrzne na out alebo in
            // TODO: 18.8.2016 test response
            String response = params.in.readLine();
            params.message = response;
            Platform.runLater(() -> logger.log(response));
        } else {
            Platform.runLater(() -> logger.log("Not connected. Cannot toggle GPIO pin."));
        }
        return params;
    }
}
