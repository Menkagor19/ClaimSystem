package com.example.claimsystem.controller;

import com.example.claimsystem.dto.PolicyDetailsDTO;
import com.example.claimsystem.service.ClaimSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClaimSystemController {

    @Autowired
    ClaimSystemService claimSystemService;

    public ClaimSystemController(ClaimSystemService claimSystemService){
        this.claimSystemService = claimSystemService;
    }

    @GetMapping("/policy-details/{claimNumber}")
    public PolicyDetailsDTO getPolicyDetails(@PathVariable String claimNumber) {
        return claimSystemService.getPolicyDetails(claimNumber);
    }
}
