package rip.skyland.carly.util;

public class WoolColor {

    public static byte getWoolColor(CC color) {
        int c;

        switch(color) {
            case GOLD:
            case ORANGE:
                c = 1;
                break;

            case DARK_PURPLE:
            case PURPLE:
                c = 2;
                break;

            case BLUE:
            case AQUA:
                c = 3;
                break;

            case YELLOW:
                c = 4;
                break;

            case GREEN:
                c = 5;
                break;

            case PINK:
            case LIGHT_PURPLE:
                c = 6;
                break;

            case DARK_GRAY:
                c = 7;
                break;

            case GRAY:
                c = 8;
                break;

            case DARK_AQUA:
                c = 9;
                break;

            case DARK_BLUE:
                c = 11;
                break;

            case DARK_GREEN:
                c = 13;
                break;

            case RED:
            case DARK_RED:
                c = 14;
                break;

            case BLACK:
                c = 15;
                break;

            default:
                c = 0;
        }

        return (byte) c;
    }
}
