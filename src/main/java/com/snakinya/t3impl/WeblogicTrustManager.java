package com.snakinya.t3impl;

import weblogic.security.SSL.TrustManager;

import java.security.cert.X509Certificate;

public class WeblogicTrustManager implements TrustManager {
    @Override
    public boolean certificateCallback(X509Certificate[] x509Certificates, int i) {
        return true;
    }
}