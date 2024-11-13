package com.act.Gakos.controller.address;

import com.act.Gakos.service.AuthenticationService;
import com.act.Gakos.service.address.AddressDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/address")
public class AddressDataController {

    @Autowired
    private AddressDataService addressDataService;



    @Autowired
    AuthenticationService authenticationService;

    @GetMapping("/count")
    public ResponseEntity<Map<String, Long >> addressDataCount(){
        Map<String , Long> counts = addressDataService.getAddressDataCount();
        return ResponseEntity.ok(counts);
    }

    @GetMapping("/login/count")
    public Map<String, Long> getLoginAttemptCounts() {
        return authenticationService.getLoginAttemptCounts();
    }
}
