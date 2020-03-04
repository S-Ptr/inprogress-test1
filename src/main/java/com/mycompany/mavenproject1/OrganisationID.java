/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject1;

import com.verisec.frejaeid.client.beans.general.OrganisationId;
import com.verisec.frejaeid.client.beans.general.SslSettings;
import com.verisec.frejaeid.client.beans.general.SsnUserInfo;
import com.verisec.frejaeid.client.beans.organisationid.delete.DeleteOrganisationIdRequest;
import com.verisec.frejaeid.client.beans.organisationid.get.OrganisationIdResult;
import com.verisec.frejaeid.client.beans.organisationid.get.OrganisationIdResultRequest;
import com.verisec.frejaeid.client.beans.organisationid.init.InitiateAddOrganisationIdRequest;
import com.verisec.frejaeid.client.beans.sign.get.SignResult;
import com.verisec.frejaeid.client.beans.sign.get.SignResultRequest;
import com.verisec.frejaeid.client.beans.sign.init.DataToSign;
import com.verisec.frejaeid.client.beans.sign.init.InitiateSignRequest;
import com.verisec.frejaeid.client.beans.usermanagement.customidentifier.set.SetCustomIdentifierRequest;
import com.verisec.frejaeid.client.client.api.CustomIdentifierClientApi;
import com.verisec.frejaeid.client.client.api.OrganisationIdClientApi;
import com.verisec.frejaeid.client.client.api.SignClientApi;
import com.verisec.frejaeid.client.client.impl.CustomIdentifierClient;
import com.verisec.frejaeid.client.client.impl.OrganisationIdClient;
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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ivan
 */
public class OrganisationID {

    public static void setID() throws FrejaEidClientInternalException, FrejaEidException, FrejaEidClientPollingException {
        String organisationIdTitle = "Jmetter";
        String identifierName = "ID";
        String identifier = "inprogress";
        String relyingPartyId = "verisec_1";
        OrganisationId organisationId = OrganisationId.create(organisationIdTitle, identifierName, identifier);
        SsnUserInfo ssn = SsnUserInfo.create(Country.SWEDEN, "199110043172");

        SslSettings sslSettings = SslSettings.create("src/main/resources/relyingparty_keystore.p12", "123123123");

        OrganisationIdClientApi organisationIdClient = OrganisationIdClient.create(sslSettings, FrejaEnvironment.TEST).setTestModeCustomUrl("https://services-st.test.frejaeid.com").setTransactionContext(TransactionContext.PERSONAL).build();
        InitiateAddOrganisationIdRequest initiateAddOrganisationIdRequest = InitiateAddOrganisationIdRequest.createCustom()
                .setEmailAndOrganisationId("aleksandar.markovic@verisec.com", organisationId)
                .setMinRegistrationLevel(MinRegistrationLevel.EXTENDED)
                .setRelyingPartyId(relyingPartyId)
                .build();
        String transactionReference = organisationIdClient.initiateAdd(initiateAddOrganisationIdRequest);
        int maxWaitingTimeInSeconds = 60;
        
        OrganisationIdResult result = organisationIdClient.pollForResult(OrganisationIdResultRequest.create(transactionReference,relyingPartyId), maxWaitingTimeInSeconds);
    }
    
     public static void signTransactionORG () throws FrejaEidClientInternalException, FrejaEidException, FrejaEidClientPollingException {
        String relyingPartyId = "verisec_1";
        SslSettings sslSettings = SslSettings.create("src/main/resources/relyingparty_keystore.p12", "123123123");
        SignClientApi signClient = SignClient.create(sslSettings, FrejaEnvironment.TEST).setTestModeCustomUrl("https://services-st.test.frejaeid.com").setTransactionContext(TransactionContext.ORGANISATIONAL).build();

        SsnUserInfo ssn = SsnUserInfo.create(Country.SWEDEN, "199110043172");
        String dataToSignText = "Would you like to transfer 1000 EUR from account A to account B?";
        byte[] dataToSignBinaryData = "Binary data, not presented to the user.".getBytes(StandardCharsets.UTF_8);
        DataToSign dataToSign = DataToSign.create(dataToSignText, dataToSignBinaryData);

        AttributeToReturn[] attributes = {AttributeToReturn.BASIC_USER_INFO,
                                  AttributeToReturn.EMAIL_ADDRESS,
                                  AttributeToReturn.DATE_OF_BIRTH,
                                  AttributeToReturn.RELYING_PARTY_USER_ID,
                                  AttributeToReturn.SSN,
                                  AttributeToReturn.CUSTOM_IDENTIFIER,
                                  AttributeToReturn.ORGANISATION_ID_IDENTIFIER};
        InitiateSignRequest initSignRequest = InitiateSignRequest.createCustom()
                  .setEmail("aleksandar.markovic@verisec.com")
                  .setDataToSign(dataToSign)
                 // .setExpiry(expiry)
                  .setMinRegistrationLevel(MinRegistrationLevel.EXTENDED)
                  .setAttributesToReturn(attributes)
               //   .setPushNotification(pushNotification)
                  .setTitle("Potvrdi transakciju")
                  .setRelyingPartyId(relyingPartyId)
                  .build();
        String reference = signClient.initiate(initSignRequest);
        
        int maxWaitingTimeInSeconds = 60;
        
        SignResult result = signClient.pollForResult(SignResultRequest.create(reference,relyingPartyId), maxWaitingTimeInSeconds);
        System.out.println(result.getRequestedAttributes());
     }
     
     public static void signTransactionPersonal() throws FrejaEidClientInternalException, FrejaEidException, FrejaEidClientPollingException {
        String relyingPartyId = "verisec_1";
        SslSettings sslSettings = SslSettings.create("src/main/resources/relyingparty_keystore.p12", "123123123");
        SignClientApi signClient = SignClient.create(sslSettings, FrejaEnvironment.TEST).setTestModeCustomUrl("https://services-st.test.frejaeid.com").setTransactionContext(TransactionContext.PERSONAL).build();

        SsnUserInfo ssn = SsnUserInfo.create(Country.SWEDEN, "199110043172");
        String dataToSignText = "Would you like to transfer 1000 EUR from account A to account B?";
        byte[] dataToSignBinaryData = "Binary data, not presented to the user.".getBytes(StandardCharsets.UTF_8);
        DataToSign dataToSign = DataToSign.create(dataToSignText, dataToSignBinaryData);

        AttributeToReturn[] attributes = {AttributeToReturn.BASIC_USER_INFO,
                                  AttributeToReturn.EMAIL_ADDRESS,
                                  AttributeToReturn.DATE_OF_BIRTH,
                                  AttributeToReturn.RELYING_PARTY_USER_ID,
                                  AttributeToReturn.SSN,
                                  AttributeToReturn.CUSTOM_IDENTIFIER,
                                  AttributeToReturn.ORGANISATION_ID_IDENTIFIER};
        InitiateSignRequest initSignRequest = InitiateSignRequest.createCustom()
                  .setEmail("aleksandar.markovic@verisec.com")
                  .setDataToSign(dataToSign)
                 // .setExpiry(expiry)
                  .setMinRegistrationLevel(MinRegistrationLevel.EXTENDED)
                  .setAttributesToReturn(attributes)
               //   .setPushNotification(pushNotification)
                  .setTitle("Potvrdi transakciju")
                  .setRelyingPartyId(relyingPartyId)
                  .build();
        String reference = signClient.initiate(initSignRequest);
        
        int maxWaitingTimeInSeconds = 60;
        
        SignResult result = signClient.pollForResult(SignResultRequest.create(reference,relyingPartyId), maxWaitingTimeInSeconds);
        System.out.println(result.getRequestedAttributes());
     }
     
     public static void delOrgId() throws FrejaEidClientInternalException, FrejaEidException{
         
        SsnUserInfo ssn = SsnUserInfo.create(Country.SWEDEN, "199110043172");
        String identifier = "inprogress";
        SslSettings sslSettings = SslSettings.create("src/main/resources/relyingparty_keystore.p12", "123123123");

        OrganisationIdClientApi organisationIdClient = OrganisationIdClient.create(sslSettings, FrejaEnvironment.TEST).setTestModeCustomUrl("https://services-st.test.frejaeid.com").setTransactionContext(TransactionContext.PERSONAL).build();
        DeleteOrganisationIdRequest deleteOrganisationIdRequest = DeleteOrganisationIdRequest.create(identifier, "verisec_1");
        organisationIdClient.delete(deleteOrganisationIdRequest);
     
     }

    public static void main(String[] args) {
        try {
           // setID();
          // signTransactionORG();
          // signTransactionPersonal();
          delOrgId();
        } catch (FrejaEidClientInternalException ex) {
            Logger.getLogger(OrganisationID.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FrejaEidException ex) {
            Logger.getLogger(OrganisationID.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
