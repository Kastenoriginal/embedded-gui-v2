package core.networking;

import core.Logger;
import core.Pin;
import core.Validations;
import layouts.EmbeddedLayout;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.*;

public class Networking {

    final static String DATE_FORMAT = "ddMMyyyyHHmmss";
    private final static int INITIAL_DELAY = 5;

    private ExecutorService connectionService;
    private ScheduledExecutorService scheduledService;
    private Logger logger;
    private NetworkingParams params;
    private Validations validations;
    private int refreshRate;
    private List<Pin> pins;

    public Networking() {
        connectionService = Executors.newSingleThreadExecutor();
        validations = new Validations(null);
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public synchronized boolean connect(String ipAddress) {
        try {
            params = connectionService.submit(new Connect(ipAddress, logger)).get();
        } catch (InterruptedException | ExecutionException e) {
            logger.log(e.toString());
        }
        return params.connected;
    }

    public synchronized boolean disconnect() {
        //already disconnected
        if (params != null) {
            try {
                connectionService.submit(new Disconnect(params, logger)).get();
                return params.connected;
            } catch (InterruptedException | ExecutionException e) {
                logger.log("Disconnecting without notifying server.");
                return false;
            }
        }
        return false;
    }

    public synchronized void toggleGpioPin(EmbeddedLayout callback, Pin pin) {
        try {
            String pinValue = "";
            connectionService.submit(new ToggleGpioPin(callback, getDateAndTime(), params, logger, pin, pinValue)).get();
        } catch (InterruptedException | ExecutionException e) {
            logger.log("Server did not responded on command.");
        }
    }

    public synchronized void sendValueToI2CPin(Pin pin, String address, String message) {
        try {
            connectionService.submit(new SendValueToI2CPin(getDateAndTime(), params, logger, pin, address, message)).get();
        } catch (InterruptedException | ExecutionException e) {
            logger.log("Server did not responded on command.");
        }
    }

    public synchronized void sendValueToSpiPin(Pin pin, String address, String message) {
        try {
            connectionService.submit(new SendValueToSpiPin(getDateAndTime(), params, logger, pin, address, message)).get();
        } catch (InterruptedException | ExecutionException e) {
            logger.log("Server did not responded on command.");
        }
    }

    public synchronized void startRequestPinStatus(EmbeddedLayout callback, int refreshRate, final List<Pin> pins) {
        this.refreshRate = refreshRate;
        this.pins = pins;
        scheduledService = Executors.newSingleThreadScheduledExecutor();
        final StartRequestPinStatus status = new StartRequestPinStatus(callback, params, logger, getDateAndTime(), pins);
        scheduledService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    status.call();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, INITIAL_DELAY, refreshRate, TimeUnit.MILLISECONDS);
    }

    public void cancelRequestPinStatus() {
        if (scheduledService != null) {
            scheduledService.shutdownNow();
        }
    }

    public synchronized void updateRequestRefreshRate(EmbeddedLayout callback, int refreshRate) {
        this.refreshRate = refreshRate;
        cancelRequestPinStatus();
        scheduledService = Executors.newSingleThreadScheduledExecutor();
        startRequestPinStatus(callback, refreshRate, this.pins);
    }

    public synchronized void updatePinsInRequestStatus(EmbeddedLayout callback, List<Pin> pins) {
        this.pins = pins;
        cancelRequestPinStatus();
        scheduledService = Executors.newSingleThreadScheduledExecutor();
        startRequestPinStatus(callback, this.refreshRate, pins);
    }

    public synchronized void sendMacro(EmbeddedLayout pinCallback, PopupDismiss popupCallback, List<String> commands) {
        connectionService.submit(new SendMacro(pinCallback, popupCallback, params, logger, commands));
    }

    private String getDateAndTime() {
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        Calendar calendar = Calendar.getInstance();
        return dateFormat.format(calendar.getTime());
    }
}
