package core.hashmaps;

import core.Pin;

import java.util.HashMap;

public class RaspberryHashMap {

    public final static String GND = "GND";
    public final static String PWR3 = "PWR3";
    public final static String PWR5 = "PWR5";
    public final static String GPIO = "GPIO";
//    public final static String I2C = "I2C";
//    public final static String SPI = "SPI";
    public final static String UART = "UART";
    public final static String EEPROM = "EEPROM";

    private HashMap<Integer, String[]> hashMap = new HashMap<>();

    public void createHashMap() {
        String[] pin1 = { PWR3 };
        String[] pin2 = { PWR5 };
        String[] pin3 = { GPIO, Pin.PIN_TYPE_I2C };
        String[] pin4 = { PWR5 };
        String[] pin5 = { GPIO, Pin.PIN_TYPE_I2C };
        String[] pin6 = { GND };
        String[] pin7 = { GPIO };
        String[] pin8 = { GPIO, UART };
        String[] pin9 = { GND };

        String[] pin10 = { GPIO, UART };
        String[] pin11 = { GPIO };
        String[] pin12 = { GPIO };
        String[] pin13 = { GPIO };
        String[] pin14 = { GND };
        String[] pin15 = { GPIO };
        String[] pin16 = { GPIO };
        String[] pin17 = { PWR3 };
        String[] pin18 = { GPIO };
        String[] pin19 = { GPIO, Pin.PIN_TYPE_SPI };

        String[] pin20 = { GND };
        String[] pin21 = { GPIO, Pin.PIN_TYPE_SPI };
        String[] pin22 = { GPIO };
        String[] pin23 = { GPIO, Pin.PIN_TYPE_SPI };
        String[] pin24 = { GPIO, Pin.PIN_TYPE_SPI };
        String[] pin25 = { GND };
        String[] pin26 = { GPIO, Pin.PIN_TYPE_SPI };
        String[] pin27 = { EEPROM };
        String[] pin28 = { EEPROM };
        String[] pin29 = { GPIO };

        String[] pin30 = { GND };
        String[] pin31 = { GPIO };
        String[] pin32 = { GPIO };
        String[] pin33 = { GPIO };
        String[] pin34 = { GND };
        String[] pin35 = { GPIO };
        String[] pin36 = { GPIO };
        String[] pin37 = { GPIO };
        String[] pin38 = { GPIO };
        String[] pin39 = { GND };

        String[] pin40 = { GPIO };

        hashMap.put(1, pin1);
        hashMap.put(2, pin2);
        hashMap.put(3, pin3);
        hashMap.put(4, pin4);
        hashMap.put(5, pin5);
        hashMap.put(6, pin6);
        hashMap.put(7, pin7);
        hashMap.put(8, pin8);
        hashMap.put(9, pin9);

        hashMap.put(10, pin10);
        hashMap.put(11, pin11);
        hashMap.put(12, pin12);
        hashMap.put(13, pin13);
        hashMap.put(14, pin14);
        hashMap.put(15, pin15);
        hashMap.put(16, pin16);
        hashMap.put(17, pin17);
        hashMap.put(18, pin18);
        hashMap.put(19, pin19);

        hashMap.put(20, pin20);
        hashMap.put(21, pin21);
        hashMap.put(22, pin22);
        hashMap.put(23, pin23);
        hashMap.put(24, pin24);
        hashMap.put(25, pin25);
        hashMap.put(26, pin26);
        hashMap.put(27, pin27);
        hashMap.put(28, pin28);
        hashMap.put(29, pin29);

        hashMap.put(30, pin30);
        hashMap.put(31, pin31);
        hashMap.put(32, pin32);
        hashMap.put(33, pin33);
        hashMap.put(34, pin34);
        hashMap.put(35, pin35);
        hashMap.put(36, pin36);
        hashMap.put(37, pin37);
        hashMap.put(38, pin38);
        hashMap.put(39, pin39);

        hashMap.put(40, pin40);
    }

    public String[] getValueByKey(int key) {
        return hashMap.get(key);
    }
}
