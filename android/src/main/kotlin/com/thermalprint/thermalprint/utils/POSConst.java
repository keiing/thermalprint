package com.thermalprint.thermalprint.utils;


public class POSConst {

    /// 左对齐
    public static final int ALIGNMENT_LEFT = 0;

    /// 剧中
    public static final int ALIGNMENT_CENTER = 1;

    /// 右对齐
    public static final int ALIGNMENT_RIGHT = 2;

    public static final int FNT_DEFAULT = 0;
    public static final int FNT_FONTB = 1;
    public static final int FNT_BOLD = 8;
    public static final int FNT_REVERSE = 16;
    public static final int FNT_UNDERLINE = 128;
    public static final int FNT_UNDERLINE2 = 256;

    /// 字体宽度与高度
    public static final int TXT_1WIDTH = 0;
    public static final int TXT_2WIDTH = 16;
    public static final int TXT_3WIDTH = 32;
    public static final int TXT_4WIDTH = 48;
    public static final int TXT_5WIDTH = 64;
    public static final int TXT_6WIDTH = 80;
    public static final int TXT_7WIDTH = 96;
    public static final int TXT_8WIDTH = 112;
    public static final int TXT_1HEIGHT = 0;
    public static final int TXT_2HEIGHT = 1;
    public static final int TXT_3HEIGHT = 2;
    public static final int TXT_4HEIGHT = 3;
    public static final int TXT_5HEIGHT = 4;
    public static final int TXT_6HEIGHT = 5;
    public static final int TXT_7HEIGHT = 6;
    public static final int TXT_8HEIGHT = 7;
    public static final int BMP_NORMAL = 0;
    public static final int BMP_WIDTH_DOUBLE = 1;
    public static final int BMP_HEIGHT_DOUBLE = 2;
    public static final int BMP_WIDTH_HEIGHT_DOUBLE = 3;
    public static final int BCS_UPCA = 65;
    public static final int BCS_UPCE = 66;
    public static final int BCS_EAN8 = 68;
    public static final int BCS_EAN13 = 67;
    public static final int BCS_JAN8 = 68;
    public static final int BCS_JAN13 = 67;
    public static final int BCS_ITF = 70;
    public static final int BCS_Codabar = 71;
    public static final int BCS_Code39 = 69;
    public static final int BCS_Code93 = 72;
    public static final int BCS_Code128 = 73;
    public static final int HRI_TEXT_NONE = 0;
    public static final int HRI_TEXT_ABOVE = 1;
    public static final int HRI_TEXT_BELOW = 2;
    public static final int HRI_TEXT_BOTH = 3;
    public static final int QRCODE_EC_LEVEL_L = 48;
    public static final int QRCODE_EC_LEVEL_M = 49;
    public static final int QRCODE_EC_LEVEL_Q = 50;
    public static final int QRCODE_EC_LEVEL_H = 51;
    public static final int CUT_ALL = 0;
    public static final int CUT_HALF = 1;
    public static final int DEVICE_58 = 1;
    public static final int DEVICE_80 = 2;
    public static final int DEVICE_76 = 3;
    public static final int PIN_TWO = 0;
    public static final int PIN_FIVE = 1;
    public static final int STS_UNKNOWN = -1;
    public static final int STS_NORMAL = 0;
    public static final int STS_COVEROPEN = 16;
    public static final int STS_PRESS_FEED = 8;
    public static final int STS_PAPEREMPTY = 32;
    public static final int STS_PRINTER_ERR = 64;
    public static final int STS_CASH_OPEN = 0;
    public static final int STS_CASH_CLOSE = 1;
    public static final int STS_TYPE_PRINT = 1;
    public static final int STS_TYPE_OFFLINE = 2;
    public static final int STS_TYPE_ERR = 3;
    public static final int STS_TYPE_PAPER = 4;
    public static final int CONNECT_SUCCESS = 1;
    public static final int CONNECT_FAIL = 2;
    public static final int SEND_FAIL = 3;
    public static final int CONNECT_INTERRUPT = 4;
    public static final int USB_ATTACHED = 5;
    public static final int USB_DETACHED = 6;
    public static final int BLUETOOTH_INTERRUPT = 7;
    public static final int DIRECTION_LEFT_TOP = 0;
    public static final int DIRECTION_LEFT_BOTTOM = 1;
    public static final int DIRECTION_RIGHT_BOTTOM = 2;
    public static final int DIRECTION_RIGHT_TOP = 3;
    public static final int SINGLE_DENSITY_8 = 0;
    public static final int DOUBLE_DENSITY_8 = 1;
    public static final int SINGLE_DENSITY_24 = 32;
    public static final int DOUBLE_DENSITY_24 = 33;
    public static final int SPACE_DEFAULT = -1;
    public static final int CODE_PAGE_PC437 = 0;
    public static final int CODE_PAGE_KATAKANA = 1;
    public static final int CODE_PAGE_PC850 = 2;
    public static final int CODE_PAGE_PC860 = 3;
    public static final int CODE_PAGE_PC863 = 4;
    public static final int CODE_PAGE_PC865 = 5;
    public static final int CODE_PAGE_WEST_EUROPE = 6;
    public static final int CODE_PAGE_GREEK = 7;
    public static final int CODE_PAGE_HEBREW = 8;
    public static final int CODE_PAGE_EAST_EUROPE = 9;
    public static final int CODE_PAGE_IRAN = 10;
    public static final int CODE_PAGE_WPC1252 = 16;
    public static final int CODE_PAGE_PC866 = 17;
    public static final int CODE_PAGE_PC852 = 18;
    public static final int CODE_PAGE_PC858 = 19;
    public static final int FONT_STANDARD = 0;
    public static final int FONT_COMPRESS = 1;
    public static final int STS_CONNECT = 1;
    public static final int STS_DISCONNECT = 0;
    public static final byte ENCRYPT_NULL = 0;
    public static final byte ENCRYPT_WEP64 = 1;
    public static final byte ENCRYPT_WEP128 = 2;
    public static final byte ENCRYPT_WPA_AES_PSK = 3;
    public static final byte ENCRYPT_WPA_TKIP_PSK = 4;
    public static final byte ENCRYPT_WPA_TKIP_AES_PSK = 5;
    public static final byte ENCRYPT_WPA2_AES_PSK = 6;
    public static final byte ENCRYPT_WPA2_TKIP = 7;
    public static final byte ENCRYPT_WPA2_TKIP_AES_PSK = 8;
    public static final byte ENCRYPT_WPA_WPA2_MixedMode = 9;

    public POSConst() {
    }
}
