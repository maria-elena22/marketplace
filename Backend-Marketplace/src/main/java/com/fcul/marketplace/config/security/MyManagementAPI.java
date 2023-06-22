package com.fcul.marketplace.config.security;

import com.auth0.client.auth.AuthAPI;
import com.auth0.client.mgmt.ManagementAPI;
import com.auth0.exception.Auth0Exception;
import com.auth0.json.auth.TokenHolder;
import com.auth0.net.TokenRequest;
import org.springframework.scheduling.annotation.Scheduled;

public class MyManagementAPI {

    private static MyManagementAPI myManagementAPI;
    private final ManagementAPI managementAPI;
    public String clientId = "Uu8BC7ukA156VWaizAF1L0z2005Q3rz1";
    public String domain = "dev-gud3donfssuoxz2a.eu.auth0.com";
    public String secret = "3K2llZRnKU1Bj3Mgoy4tzDUQnpWrg4FJ29dxD2fSRfCFBDv1Ii55_ns2jrLjtXQZ";

    private MyManagementAPI() throws Auth0Exception {
        AuthAPI authAPI = AuthAPI.newBuilder(domain, clientId, secret).build();
        TokenRequest tokenRequest = authAPI.requestToken("https://" + domain + "/api/v2/");
        TokenHolder holder = tokenRequest.execute().getBody();
        String accessToken = holder.getAccessToken();
        this.managementAPI = ManagementAPI.newBuilder(domain, accessToken).build();
    }

    public static MyManagementAPI getInstance() throws Auth0Exception {
        if (myManagementAPI == null) {
            myManagementAPI = new MyManagementAPI();
        }
        return myManagementAPI;
    }

    @Scheduled(fixedRate = 7200000) // runs every 2 hours (in milliseconds)
    public void recreateManagementAPI() throws Auth0Exception {
        myManagementAPI = new MyManagementAPI();
    }

    public ManagementAPI getManagementAPI() {
        return managementAPI;
    }
}
