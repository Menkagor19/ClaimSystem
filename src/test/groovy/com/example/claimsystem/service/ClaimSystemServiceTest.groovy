package com.example.claimsystem.service

import com.example.claimsystem.dto.PolicyDetailsDTO
import com.example.claimsystem.exception.ResponseIsNullorNotCastableException
import com.example.claimsystem.model.GetPolicyDetailsResponse
import com.example.claimsystem.model.PolicyDetails
import org.springframework.ws.client.core.WebServiceTemplate
import spock.lang.Specification

class ClaimSystemServiceTest extends Specification {

    def "Should return policy details when claim number is given"() {
        given:
        def claimNumber = "CLM-987654"
        def mockWebServiceTemplate = Mock(WebServiceTemplate)
        def claimSystemService = new ClaimSystemService(mockWebServiceTemplate)
        def expectedDto = new PolicyDetailsDTO("User 1", BigInteger.valueOf(1), "Coverage 1", BigInteger.valueOf(1000), BigInteger.valueOf(50))
        GetPolicyDetailsResponse mockPolicyDetailsResponse = getPolicyDetailsResponseMock()

        and:
        mockWebServiceTemplate.marshalSendAndReceive(_) >> mockPolicyDetailsResponse

        when:
        def expectedPolicyDetails = claimSystemService.getPolicyDetails(claimNumber)

        then:
        expectedPolicyDetails == expectedDto
    }

    private GetPolicyDetailsResponse getPolicyDetailsResponseMock() {
        def policyDetails = new PolicyDetails()
        policyDetails.setPolicyHolderName("User 1")
        policyDetails.setPolicyNumber(BigInteger.valueOf(1))
        policyDetails.setCoverageName("Coverage 1")
        policyDetails.setCoverageLimitInUSD(BigInteger.valueOf(1000))
        policyDetails.setDeductibleInUSD(BigInteger.valueOf(50))

        def mockPolicyDetailsResponse = new GetPolicyDetailsResponse()
        mockPolicyDetailsResponse.setPolicyDetails(policyDetails)
        mockPolicyDetailsResponse
    }

    def "Should throw an exception when SOAP service response is not type of GetPolicyDetailsResponse"(){
        given:
        def claimNumber = "CLM-987654"
        def mockWebServiceTemplate = Mock(WebServiceTemplate)
        def claimSystemService = new ClaimSystemService(mockWebServiceTemplate)
        String mockPolicyDetailsResponse = "response"

        and:
        mockWebServiceTemplate.marshalSendAndReceive(_) >> mockPolicyDetailsResponse

        when:
        claimSystemService.getPolicyDetails(claimNumber)

        then:
        thrown(ResponseIsNullorNotCastableException)
    }

    def "Should throw an exception when SOAP service response is null"(){
        given:
        def claimNumber = "CLM-987654"
        def mockWebServiceTemplate = Mock(WebServiceTemplate)
        def claimSystemService = new ClaimSystemService(mockWebServiceTemplate)
        Object mockPolicyDetailsResponse = null

        and:
        mockWebServiceTemplate.marshalSendAndReceive(_) >> mockPolicyDetailsResponse

        when:
        claimSystemService.getPolicyDetails(claimNumber)

        then:
        thrown(ResponseIsNullorNotCastableException)
    }

}
