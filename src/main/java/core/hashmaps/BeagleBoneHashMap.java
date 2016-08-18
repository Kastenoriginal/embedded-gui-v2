package core.hashmaps;

import core.Pin;

import java.util.HashMap;

public class BeagleBoneHashMap {

    public final static String GND = "GND";
    public final static String PWR3 = "PWR3";
    public final static String PWR5 = "PWR5";
    public final static String PWR_BTN = "PWR_BTN";
    public final static String SYS_RST = "SYS_RST";
    public final static String GPIO = "GPIO";
//    public final static String I2C = "I2C";
//    public final static String SPI = "SPI";
    public final static String PWM = "PWM";
    public final static String AI = "AI";
    public final static String UART = "UART";

    private HashMap<Integer, String[]> hashMap = new HashMap<>();

    public void createHashMap() {
        String[] pin1 = { GND };
        String[] pin2 = { GND };
        String[] pin3 = { PWR3 };
        String[] pin4 = { PWR3 };
        String[] pin5 = { PWR5 };
        String[] pin6 = { PWR5 };
        String[] pin7 = { PWR5 };
        String[] pin8 = { PWR5 };
        String[] pin9 = { PWR_BTN };

        String[] pin10 = { SYS_RST };
        String[] pin11 = { GPIO, UART };
        String[] pin12 = { GPIO };
        String[] pin13 = { GPIO, UART };
        String[] pin14 = { GPIO, PWM };
        String[] pin15 = { GPIO };
        String[] pin16 = { GPIO, PWM };
        String[] pin17 = { GPIO, Pin.PIN_TYPE_I2C, Pin.PIN_TYPE_SPI };
        String[] pin18 = { GPIO, Pin.PIN_TYPE_I2C, Pin.PIN_TYPE_SPI };
        String[] pin19 = { GPIO, Pin.PIN_TYPE_I2C, Pin.PIN_TYPE_SPI, UART };

        String[] pin20 = { GPIO, Pin.PIN_TYPE_I2C, Pin.PIN_TYPE_SPI, UART };
        String[] pin21 = { GPIO, Pin.PIN_TYPE_I2C, Pin.PIN_TYPE_SPI, UART, PWM };
        String[] pin22 = { GPIO, Pin.PIN_TYPE_I2C, Pin.PIN_TYPE_SPI, UART, PWM };
        String[] pin23 = { GPIO };
        String[] pin24 = { GPIO, Pin.PIN_TYPE_I2C, UART };
        String[] pin25 = { GPIO };
        String[] pin26 = { GPIO, Pin.PIN_TYPE_I2C, UART };
        String[] pin27 = { GPIO };
        String[] pin28 = { GPIO, Pin.PIN_TYPE_SPI, PWM };
        String[] pin29 = { GPIO, Pin.PIN_TYPE_SPI, PWM };

        String[] pin30 = { GPIO, Pin.PIN_TYPE_SPI };
        String[] pin31 = { GPIO, Pin.PIN_TYPE_SPI, PWM };
        String[] pin32 = { AI };
        String[] pin33 = { AI };
        String[] pin34 = { AI };
        String[] pin35 = { AI };
        String[] pin36 = { AI };
        String[] pin37 = { AI };
        String[] pin38 = { AI };
        String[] pin39 = { AI };

        String[] pin40 = { AI };
        String[] pin41 = { GPIO };
        String[] pin42 = { GPIO, Pin.PIN_TYPE_SPI, UART };
        String[] pin43 = { GND };
        String[] pin44 = { GND };
        String[] pin45 = { GND };
        String[] pin46 = { GND };
        String[] pin47 = { GND };
        String[] pin48 = { GND };
        String[] pin49 = { GPIO };

        String[] pin50 = { GPIO };
        String[] pin51 = { GPIO };
        String[] pin52 = { GPIO };
        String[] pin53 = { GPIO };
        String[] pin54 = { GPIO };
        String[] pin55 = { GPIO };
        String[] pin56 = { GPIO };
        String[] pin57 = { GPIO };
        String[] pin58 = { GPIO };
        String[] pin59 = { GPIO, PWM };

        String[] pin60 = { GPIO };
        String[] pin61 = { GPIO };
        String[] pin62 = { GPIO };
        String[] pin63 = { GPIO };
        String[] pin64 = { GPIO };
        String[] pin65 = { GPIO, PWM };
        String[] pin66 = { GPIO };
        String[] pin67 = { GPIO };
        String[] pin68 = { GPIO };
        String[] pin69 = { GPIO };

        String[] pin70 = { GPIO };
        String[] pin71 = { GPIO };
        String[] pin72 = { GPIO };
        String[] pin73 = { GPIO };
        String[] pin74 = { GPIO };
        String[] pin75 = { GPIO };
        String[] pin76 = { GPIO };
        String[] pin77 = { GPIO, UART };
        String[] pin78 = { GPIO, UART };
        String[] pin79 = { GPIO, UART };

        String[] pin80 = { GPIO, UART, PWM };
        String[] pin81 = { GPIO, UART };
        String[] pin82 = { GPIO, UART, PWM };
        String[] pin83 = { GPIO, UART };
        String[] pin84 = { GPIO, UART };
        String[] pin85 = { GPIO };
        String[] pin86 = { GPIO };
        String[] pin87 = { GPIO };
        String[] pin88 = { GPIO };
        String[] pin89 = { GPIO };

        String[] pin90 = { GPIO };
        String[] pin91 = { GPIO, PWM };
        String[] pin92 = { GPIO, PWM };

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
        hashMap.put(41, pin41);
        hashMap.put(42, pin42);
        hashMap.put(43, pin43);
        hashMap.put(44, pin44);
        hashMap.put(45, pin45);
        hashMap.put(46, pin46);
        hashMap.put(47, pin47);
        hashMap.put(48, pin48);
        hashMap.put(49, pin49);

        hashMap.put(50, pin50);
        hashMap.put(51, pin51);
        hashMap.put(52, pin52);
        hashMap.put(53, pin53);
        hashMap.put(54, pin54);
        hashMap.put(55, pin55);
        hashMap.put(56, pin56);
        hashMap.put(57, pin57);
        hashMap.put(58, pin58);
        hashMap.put(59, pin59);

        hashMap.put(60, pin60);
        hashMap.put(61, pin61);
        hashMap.put(62, pin62);
        hashMap.put(63, pin63);
        hashMap.put(64, pin64);
        hashMap.put(65, pin65);
        hashMap.put(66, pin66);
        hashMap.put(67, pin67);
        hashMap.put(68, pin68);
        hashMap.put(69, pin69);

        hashMap.put(70, pin70);
        hashMap.put(71, pin71);
        hashMap.put(72, pin72);
        hashMap.put(73, pin73);
        hashMap.put(74, pin74);
        hashMap.put(75, pin75);
        hashMap.put(76, pin76);
        hashMap.put(77, pin77);
        hashMap.put(78, pin78);
        hashMap.put(79, pin79);

        hashMap.put(80, pin80);
        hashMap.put(81, pin81);
        hashMap.put(82, pin82);
        hashMap.put(83, pin83);
        hashMap.put(84, pin84);
        hashMap.put(85, pin85);
        hashMap.put(86, pin86);
        hashMap.put(87, pin87);
        hashMap.put(88, pin88);
        hashMap.put(89, pin89);

        hashMap.put(90, pin90);
        hashMap.put(91, pin91);
        hashMap.put(92, pin92);
    }

    public String[] getValueByKey(int key) {
        return hashMap.get(key);
    }
}
