package com.exercise.utils.util;

public class IpAddress {

	static public String ipv4Int2Str(int addr) {
        StringBuffer ipBuf = new StringBuffer();
        ipBuf.append(addr  & 0xff).append('.').
            append((addr >>>= 8) & 0xff).append('.').
            append((addr >>>= 8) & 0xff).append('.').
            append((addr >>>= 8) & 0xff);
        
        return ipBuf.toString();
    }
}
