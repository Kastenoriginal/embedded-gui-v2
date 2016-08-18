package core;

public class Pin {

    public static final String PIN_TYPE_I2C = "I2C";
    public static final String PIN_TYPE_SPI = "SPI";
    public static final String PIN_TYPE_GPIO = "GPIO";
    private int pinId;
    private String ioType;
    private String pinType;

    public Pin(int pinId, String ioType, String pinType) {
        this.pinId = pinId;
        this.ioType = ioType;
        this.pinType = pinType;
    }

    public int getPinId() {
        return pinId;
    }

    public String getIoType() {
        return ioType.substring(0,1);
    }

    public String getPinType() {
        return pinType;
    }
}
