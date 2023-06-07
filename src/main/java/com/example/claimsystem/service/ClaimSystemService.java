package com.example.claimsystem.service;

import com.example.claimsystem.dto.PolicyDetailsDTO;
import com.example.claimsystem.exception.RequestIsInvalid;
import com.example.claimsystem.exception.ResponseIsNullorNotCastableException;
import com.example.claimsystem.model.GetPolicyDetailsRequest;
import com.example.claimsystem.model.GetPolicyDetailsResponse;
import com.example.claimsystem.model.PolicyDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.client.SoapFaultClientException;

@Service
public class ClaimSystemService {
    private static final String POLICY_DETAIL_RESPONSE_IS_INVALID = "Policy detail response from SOAP is invalid";
    private static final String CLAIM_NUMBER_IS_NOT_VALID = "Claim number is not valid";
    private final WebServiceTemplate webServiceTemplate;

    @Autowired
    public ClaimSystemService(WebServiceTemplate webServiceTemplate) {
        this.webServiceTemplate = webServiceTemplate;
    }

    public PolicyDetailsDTO getPolicyDetails(String claimNumber) {
        GetPolicyDetailsRequest getPolicyDetailsRequest = getGetPolicyDetailsRequest(claimNumber);
        Object soapClientResponse = soapClient(getPolicyDetailsRequest);
        if(!(soapClientResponse instanceof GetPolicyDetailsResponse getPolicyDetailsResponse)){

            throw new ResponseIsNullorNotCastableException(POLICY_DETAIL_RESPONSE_IS_INVALID);
        }

        return getPolicyDetailsDTO(getPolicyDetailsResponse);
    }

    private static GetPolicyDetailsRequest getGetPolicyDetailsRequest(String policyDetails) {
        GetPolicyDetailsRequest getPolicyDetailsRequest = new GetPolicyDetailsRequest();
        getPolicyDetailsRequest.setClaimNumber(policyDetails);
        return getPolicyDetailsRequest;
    }

    private static PolicyDetailsDTO getPolicyDetailsDTO(GetPolicyDetailsResponse getPolicyDetailsResponse) {
        PolicyDetails policyDetails = getPolicyDetailsResponse.getPolicyDetails();
        return new PolicyDetailsDTO(policyDetails.getPolicyHolderName(),
                policyDetails.getPolicyNumber(),
                policyDetails.getCoverageName(),
                policyDetails.getCoverageLimitInUSD(),
                policyDetails.getDeductibleInUSD());
    }

    private Object soapClient(Object requestObject) {
        try {
            return webServiceTemplate.marshalSendAndReceive(requestObject);
        }catch (SoapFaultClientException e){
            throw new RequestIsInvalid(CLAIM_NUMBER_IS_NOT_VALID);
        }
    }
}
