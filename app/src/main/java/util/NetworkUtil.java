package util;

import android.util.Log;


import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * Utility class for fetching <em>IPAddress</em>, and <em>MACAddress</em> for Android devices without relying on
 * Android's <em>WifiManager</em>, <em>Context</em>, etc. <br/>
 * <p/>
 * Courtesy:
 * 1. <a href= "http://stackoverflow.com/questions/6064510/how-to-get-ip-address-of-the-device/13007325#13007325">
 * A Stack-Overflow post by user 'whome'</a> <br/>
 */
public final class NetworkUtil {

    /**
     * Static Logger instance for logging.
     */
//    private static final Logger LOG = LoggerFactory.getLogger(NetworkUtil.class);
    /**
     * Constant used for getMACAddress( )
     */
    private static final String INTERFACE_WLAN = "wlan0";
    /**
     * Constant used for getMACAddress( )
     */
    private static final String INTERFACE_ETHERNET = "eth0";
    /**
     * Empty string as a constant.
     */
    private static final String EMPTY_STRING = "";

    /**
     * Preventing this class from having a public default constructor, since it is a Utility class.
     */
    private NetworkUtil() {
    }

    /**
     * Returns MAC address of the given interface name.
     *
     * @param interfaceName eth0, wlan0 or NULL=use first interface
     * @return mac address or empty string
     */
    public static String getMACAddress(String interfaceName) {
        Log.i("getMACAddress({})", interfaceName);
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                if (interfaceName != null && !intf.getName().equalsIgnoreCase(interfaceName)) {
                    continue;
                }
                byte[] mac = intf.getHardwareAddress();
                if (mac == null) {
                    return EMPTY_STRING;
                }
                StringBuilder buf = new StringBuilder();
                for (byte b : mac) {
                    buf.append(String.format("%02X", b));
                }
                return buf.toString();
            }
        } catch (SocketException ex) {
            Log.e("Exception while fetching mac address for well known name : ", ex.getMessage());
        }
        return EMPTY_STRING;
    }

    /**
     * Returns the MACAddress by giving priority to WLAN interface...
     *
     * @return MAC Address using the WLAN interface. If not available, checks the Ethernet interface.
     * Else, returns the empty string.
     */
    public static String fetchMACAddress() {
        Log.i("fetchMACAddress()","");
        String result = getMACAddress(INTERFACE_WLAN);
        if (result.isEmpty()) {
            result = getMACAddress(INTERFACE_ETHERNET);
        }
        return result;
    }

    /**
     * Method to retrieve the IPv4 address.<p/>
     *
     * @return The IPv4 address if available; {@code null} otherwise.
     */
    public static String getIPv4Address() throws SocketException {
        String result = null;
        Inet4Address internetAddress = null;

        for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
            NetworkInterface intf = en.nextElement();
            for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                InetAddress inetAddress = enumIpAddr.nextElement();
                Log.i("Found address: {}", inetAddress.toString());
                if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                    internetAddress = (Inet4Address) inetAddress;
                }
            }
        }

        if (internetAddress != null) {
            result = internetAddress.getHostAddress();
        }

        return result;
    }
}