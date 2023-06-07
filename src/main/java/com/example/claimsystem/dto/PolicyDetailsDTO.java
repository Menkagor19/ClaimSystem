package com.example.claimsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
public class PolicyDetailsDTO {
    private String policyHolderName;
    private BigInteger policyNumber;
    private String coverageName;
    private BigInteger coverageLimitInUSD;
    private BigInteger deductibleInUSD;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PolicyDetailsDTO that = (PolicyDetailsDTO) o;
        return policyHolderName.equals(that.policyHolderName) && policyNumber.equals(that.policyNumber) && coverageName.equals(that.coverageName) && coverageLimitInUSD.equals(that.coverageLimitInUSD) && deductibleInUSD.equals(that.deductibleInUSD);
    }

    @Override
    public int hashCode() {
        return Objects.hash(policyHolderName, policyNumber, coverageName, coverageLimitInUSD, deductibleInUSD);
    }
}
