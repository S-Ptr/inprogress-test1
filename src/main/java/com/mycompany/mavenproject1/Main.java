/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject1;

import com.verisec.frejaeid.client.beans.general.SslSettings;
import com.verisec.frejaeid.client.client.api.AuthenticationClientApi;
import com.verisec.frejaeid.client.client.impl.AuthenticationClient;
import com.verisec.frejaeid.client.enums.FrejaEnvironment;
import com.verisec.frejaeid.client.exceptions.FrejaEidClientInternalException;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Stefan
 */
public class Main {
    public static int sabiranje (int a,int b) {
        return a + b;
    }
    public static void main(String[] args) {
        try {
            SslSettings sslSettings = SslSettings.create("src/main/resources/test.ks", "123123123", "bellim30.test.verisec.se/certificate.crt");
            AuthenticationClientApi authenticationClient = AuthenticationClient.create(sslSettings, FrejaEnvironment.TEST).build();
        } catch (FrejaEidClientInternalException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
