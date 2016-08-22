package core.networking;

import core.Logger;
import core.Pin;
import core.ResponseParser;
import javafx.application.Platform;
import layouts.EmbeddedLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

class StartRequestPinStatus implements Callable<NetworkingParams> {

    private final static String PIN_REQUEST_CODE = "REQUEST:990";
    private final static String RESPONSE_SPLITTER = ";";
    private final static int MINIMUM_RESPONSE_LENGTH = 15;

    private EmbeddedLayout callback;
    private NetworkingParams params;
    private Logger logger;
    private String dateAndTime;
    private List<Pin> pinList;

    StartRequestPinStatus(EmbeddedLayout callback, NetworkingParams params, Logger logger, String dateAndTime, List<Pin> pinList) {
        this.callback = callback;
        this.params = params;
        this.logger = logger;
        this.dateAndTime = dateAndTime;
        this.pinList = pinList;
    }

    @Override
    public NetworkingParams call() throws Exception {
        String pinsToRequest = "";
        if (pinList != null || !pinList.isEmpty()) {
            for (Pin pin : pinList) {
                if (pinsToRequest.isEmpty()) {
                    pinsToRequest = String.valueOf(pin.getPinId());
                } else {
                    pinsToRequest = pinsToRequest + ";" + pin.getPinId();
                }
            }
        }
        String finalPinsToRequest = pinsToRequest;
        Platform.runLater(() -> logger.log("Requesting status for pins: " + finalPinsToRequest));
        params.out.println(dateAndTime + PIN_REQUEST_CODE + pinsToRequest);
        params.message = params.in.readLine();

        params = setColorOnPins();
        return params;
    }

    private NetworkingParams setColorOnPins() {
        String response = params.message;
        if (response == null || response.isEmpty() || !(response.contains("START") && response.contains("END"))) {
            return params;
        }
        String[] partialStatus = response.split(RESPONSE_SPLITTER);
        List<Pin> pins = new ArrayList<>();
        if (partialStatus != null) {
            for (String part : partialStatus) {
                if (!(part.equals("START") || part.equals("END")) && part.length() > MINIMUM_RESPONSE_LENGTH) {
                    ResponseParser parser = new ResponseParser(part);
                    Pin pin = new Pin(parser.getPinId(), parser.getIoType(), parser.getPinType());
                    String pinValueFromParser = parser.getValue();
                    if (pinValueFromParser.length() == 1) {
                        int intValue = Integer.valueOf(pinValueFromParser);
                        if (intValue == 0) {
                            pin.setValue(false);
                        } else {
                            pin.setValue(true);
                        }
                    } else {
                        // Not an GPIO pin instance
                        return params;
                    }
                    pins.add(pin);
                }
            }
        }
        Platform.runLater(() -> callback.setColorOnPins(pins));
        return params;
    }
}
