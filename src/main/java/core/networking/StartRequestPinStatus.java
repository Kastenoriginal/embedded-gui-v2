package core.networking;

import core.Logger;
import core.Pin;
import layouts.EmbeddedLayout;

import java.util.List;
import java.util.concurrent.Callable;

class StartRequestPinStatus implements Callable<NetworkingParams> {

    private final static String PIN_REQUEST_CODE = "REQUEST:990";

    private Logger logger;
    private NetworkingParams params;
    private String dateAndTime;
    private List<Pin> pinList;
    private EmbeddedLayout callback;

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
        logger.log("Scheduling at fixed rate with pins: " + pinsToRequest);
        // TODO: 16.8.2016 Uncomment to test with server and response
        params.out.println(dateAndTime + PIN_REQUEST_CODE + pinsToRequest);
        params.message = params.in.readLine();
        logger.log("GOT RESPONSE FROM SERVER: " + params.message);
        // TODO: 19.8.2016 START;tocomazaujima;tocomazaujima2;END
        // TODO: 19.8.2016 foreach pin new List<Pin> setnut kazdemu hodnotu a ID
        // TODO: 20.8.2016 az potom mozem do callbacku vratit pinlist ked ich setnem - tieto pins pridu ako parameter rovno do layout metody na update
        callback.updatePinsStatus(pinList);
        return params;
    }
}
