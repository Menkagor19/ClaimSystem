package com.example.claimsystem.controller

import com.example.claimsystem.model.GetPolicyDetailsResponse
import com.example.claimsystem.model.PolicyDetails
import jakarta.xml.soap.MessageFactory
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.ws.client.core.WebServiceTemplate
import org.springframework.ws.soap.SoapMessage
import org.springframework.ws.soap.client.SoapFaultClientException
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory
import spock.lang.Specification

@AutoConfigureMockMvc
@SpringBootTest
class ClaimSystemControllerTest extends Specification {

    @Autowired
    MockMvc mvc

    @SpringBean
    WebServiceTemplate webServiceTemplate = Mock()

    def "Test GET request and verify response data"() {
        given:
        def endpoint = '/policy-details/CLM-987654'
        webServiceTemplate.marshalSendAndReceive(_) >> getPolicyDetailsResponseMock()

        expect:
        mvc.perform(MockMvcRequestBuilders.get(endpoint))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(
                        '{\"policyHolderName\":\"ABCD\",' +
                                '\"policyNumber\":1234,' +
                                '\"coverageName\":\"EFGH\",' +
                                '\"coverageLimitInUSD\":2,' +
                                '\"deductibleInUSD\":3}'))
    }

    def "Test GET request when the response is null"() {
        given:
        def endpoint = '/policy-details/CLM-987654'
        webServiceTemplate.marshalSendAndReceive(_) >> null

        expect:
        mvc.perform(MockMvcRequestBuilders.get(endpoint))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andExpect(MockMvcResultMatchers.content().string("Policy detail response from SOAP is invalid"))
    }

    def "Test GET request when claim number is invalid"() {
        given:
        def endpoint = '/policy-details/CL-987654'

        SaajSoapMessageFactory saajSoapMessageFactory = new SaajSoapMessageFactory()
        saajSoapMessageFactory.setMessageFactory(MessageFactory.newInstance())
        SoapMessage soapMessage = saajSoapMessageFactory.createWebServiceMessage()


        webServiceTemplate.marshalSendAndReceive(_) >> {
            throw new SoapFaultClientException(soapMessage)
        }

        expect:
        mvc.perform(MockMvcRequestBuilders.get(endpoint))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Claim number is not valid"))
    }

    private GetPolicyDetailsResponse getPolicyDetailsResponseMock() {
        def policyDetails = new PolicyDetails()
        policyDetails.setPolicyHolderName("ABCD")
        policyDetails.setPolicyNumber(BigInteger.valueOf(1234))
        policyDetails.setCoverageName("EFGH")
        policyDetails.setCoverageLimitInUSD(BigInteger.valueOf(2))
        policyDetails.setDeductibleInUSD(BigInteger.valueOf(3))

        def mockPolicyDetailsResponse = new GetPolicyDetailsResponse()
        mockPolicyDetailsResponse.setPolicyDetails(policyDetails)
        mockPolicyDetailsResponse
    }
}
