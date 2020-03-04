/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject1;

import com.verisec.frejaeid.client.beans.general.RequestedAttributes;
import com.verisec.frejaeid.client.beans.general.SslSettings;
import com.verisec.frejaeid.client.beans.general.SsnUserInfo;
import com.verisec.frejaeid.client.beans.sign.get.SignResult;
import com.verisec.frejaeid.client.beans.sign.get.SignResultRequest;
import com.verisec.frejaeid.client.beans.sign.init.DataToSign;
import com.verisec.frejaeid.client.beans.sign.init.InitiateSignRequest;
import com.verisec.frejaeid.client.client.api.SignClientApi;
import com.verisec.frejaeid.client.client.impl.SignClient;
import com.verisec.frejaeid.client.enums.AttributeToReturn;
import com.verisec.frejaeid.client.enums.Country;
import com.verisec.frejaeid.client.enums.FrejaEnvironment;
import com.verisec.frejaeid.client.enums.MinRegistrationLevel;
import com.verisec.frejaeid.client.enums.TransactionContext;
import com.verisec.frejaeid.client.exceptions.FrejaEidClientInternalException;
import com.verisec.frejaeid.client.exceptions.FrejaEidClientPollingException;
import com.verisec.frejaeid.client.exceptions.FrejaEidException;
import java.nio.charset.StandardCharsets;


/**
 *
 * @author Stefan
 */
public class Main {

    public static int sabiranje(int a, int b) {
        return a + b;
    }

    public static void main(String[] args) throws FrejaEidClientInternalException, FrejaEidException, FrejaEidClientPollingException {
        
      /*  SslSettings sslSettings = SslSettings.create("src/main/resources/relyingparty_keystore.p12", "123123123");
        AuthenticationClientApi authenticationClient = AuthenticationClient.create(sslSettings, FrejaEnvironment.TEST).setTestModeCustomUrl("https://services-st.test.frejaeid.com").setTransactionContext(TransactionContext.PERSONAL).build();

        InitiateAuthenticationRequest request = InitiateAuthenticationRequest.createCustom()
                .setEmail("aleksandar.markovic@verisec.com")
                .setMinRegistrationLevel(MinRegistrationLevel.EXTENDED)
                .setRelyingPartyId("verisec_1")
                .build();

        String reference = authenticationClient.initiate(request);
        int maxWaitingTimeInSeconds = 30;

        AuthenticationResult result = authenticationClient.pollForResult(AuthenticationResultRequest.create(reference,"verisec_1"), maxWaitingTimeInSeconds);
        System.out.println(result);
*/      SslSettings sslSettings = SslSettings.create("src/main/resources/relyingparty_keystore.p12", "123123123");
        SignClientApi signClient = SignClient.create(sslSettings, FrejaEnvironment.TEST).setTestModeCustomUrl("https://services-st.test.frejaeid.com").setTransactionContext(TransactionContext.PERSONAL).build();
        SsnUserInfo ssn = SsnUserInfo.create(Country.SWEDEN, "199110043172");
        String dataToSignText = "Would you like to transfer 1000 EUR from account A to account B?";
        byte[] dataToSignBinaryData = "Binary data, not presented to the user.".getBytes(StandardCharsets.UTF_8);
        DataToSign dataToSign = DataToSign.create(dataToSignText, dataToSignBinaryData);
        AttributeToReturn[] attributes = {AttributeToReturn.BASIC_USER_INFO,
                                  AttributeToReturn.EMAIL_ADDRESS,
                                  AttributeToReturn.DATE_OF_BIRTH,
                                  AttributeToReturn.RELYING_PARTY_USER_ID,
                                  AttributeToReturn.SSN};
        InitiateSignRequest initSignRequest = InitiateSignRequest.createCustom()
                  .setEmail("aleksandar.markovic@verisec.com")
                  .setDataToSign(dataToSign)
                 // .setExpiry(expiry)
                  .setMinRegistrationLevel(MinRegistrationLevel.EXTENDED)
                  .setAttributesToReturn(attributes)
               //   .setPushNotification(pushNotification)
                  .setTitle("Potvrdi transakciju")
                  .setRelyingPartyId("verisec_1")
                  .build();
        String reference = signClient.initiate(initSignRequest);
        
        int maxWaitingTimeInSeconds = 60;
        
        SignResult result = signClient.pollForResult(SignResultRequest.create(reference, "verisec_1"), maxWaitingTimeInSeconds);
        System.out.println(result.getRequestedAttributes());
    }
}
