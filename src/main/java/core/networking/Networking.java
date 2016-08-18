package core.networking;

import core.Logger;
import core.Pin;
import core.Validations;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.*;

public class Networking {

    private final static String DATE_FORMAT = "ddMMyyyyHHmmss";
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

    public boolean connect(String ipAddress) {
        try {
            params = connectionService.submit(new Connect(ipAddress, logger)).get();
        } catch (InterruptedException | ExecutionException e) {
            logger.log(e.toString());
        }
        return params.connected;
    }

    public synchronized void disconnect() {
        try {
            connectionService.submit(new Disconnect(params, logger)).get();
        } catch (InterruptedException | ExecutionException e) {
            logger.log(e.toString());
        }
    }

    public synchronized void ToggleGpioPin(Pin pin) {
        try {
            connectionService.submit(new ToggleGpioPin(getDateAndTime(), params, logger, pin)).get();
        } catch (InterruptedException | ExecutionException e) {
            logger.log(e.toString());
        }
    }

    public synchronized void sendValueToI2CPin(Pin pin, String address, String message) {
        address = "0x" + address;

        try {
            connectionService.submit(new SendValueToI2CPin(getDateAndTime(), params, logger, pin, address, message)).get();
        } catch (InterruptedException | ExecutionException e) {
            logger.log(e.toString());
        }
    }

    public synchronized void sendValueToSpiPin(Pin pin, String address, String message) {
        address = "0x" + address;

        try {
            connectionService.submit(new SendValueToSpiPin(getDateAndTime(), params, logger, pin, address, message)).get();
        } catch (InterruptedException | ExecutionException e) {
            logger.log(e.toString());
        }
    }

    public synchronized void startRequestPinStatus(int refreshRate, final List<Pin> pins) {
        this.refreshRate = refreshRate;
        this.pins = pins;
        // TODO: 15.8.2016 ScheduledFuture bude asi topka ale treba testovat so serverom
        scheduledService = Executors.newSingleThreadScheduledExecutor();
        final StartRequestPinStatus status = new StartRequestPinStatus(logger, getDateAndTime(), pins);
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

    public synchronized void updateRequestRefreshRate(int refreshRate) {
        this.refreshRate = refreshRate;
        cancelRequestPinStatus();
        scheduledService = Executors.newSingleThreadScheduledExecutor();
        startRequestPinStatus(refreshRate, this.pins);
    }

    public synchronized void updatePinsInRequestStatus(List<Pin> pins) {
        this.pins = pins;
        cancelRequestPinStatus();
        scheduledService = Executors.newSingleThreadScheduledExecutor();
        startRequestPinStatus(this.refreshRate, pins);
    }

    public synchronized void sendMacro(String address, List<String> commands) {
        // TODO: 18.8.2016 send macro implementuje callable na response
        // TODO: 19.8.2016 to je stary flow novy je ze sa explicitne volaju metody ktore asi budu implementovat callable
        for (String command : commands) {
            command = command.substring(0, command.lastIndexOf(";"));
            logger.log("Sending: " + command);
            sendMacroCommand(address, command);
        }
        logger.log("Macro sent.");
    }

    private void sendMacroCommand(String address, String command) {
        if (validations.isOnlyDigitString(command)) {
            sleepThread(connectionService, Integer.valueOf(command));
        } else {
            int pinId;
            String hexaCommand;

            if (command.startsWith("GPIO")) {
                pinId = Integer.valueOf(command.substring(5, 7));
                Pin pin = new Pin(pinId, "O", "GPIO");
                ToggleGpioPin(pin);
            } else if (command.startsWith("I2C")) {
                pinId = Integer.valueOf(command.substring(4, 6));
                hexaCommand = command.substring(6);
                Pin pin = new Pin(pinId, "O", "I2C");
                sendValueToI2CPin(pin, address, hexaCommand);
            } else if (command.startsWith("SPI")) {
                pinId = Integer.valueOf(command.substring(4, 6));
                hexaCommand = command.substring(6);
                Pin pin = new Pin(pinId, "O", "SPI");
                sendValueToSpiPin(pin, address, hexaCommand);
            } else {
                logger.log("Not supported command in macro.");
            }
        }
    }

    private void sleepThread(ExecutorService service, int sleepTimeMillis) {
        logger.log("Sleeping thread for " + sleepTimeMillis + " seconds.");
        service.submit(new SleepThread(sleepTimeMillis));
    }

    private String getDateAndTime() {
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        Calendar calendar = Calendar.getInstance();
        return dateFormat.format(calendar.getTime());
    }
}
