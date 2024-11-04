package com.act.Gakos.controller.address;

import com.act.Gakos.entity.CurrentAddress;
import com.act.Gakos.service.CurrentAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/addresses")
public class CurrentAddressController {

    @Autowired
    private CurrentAddressService currentAddressService;

    // Register a new address for a user
    @PostMapping("/register/{userId}")
    public ResponseEntity<CurrentAddress> registerAddress(
            @PathVariable Integer userId,
            @RequestBody CurrentAddress address) {
        CurrentAddress registeredAddress = currentAddressService.registerAddress(userId, address);
        return ResponseEntity.ok(registeredAddress);
    }

    // Delete an address by ID
    @DeleteMapping("/{addressId}")
    public ResponseEntity<String> deleteAddress(@PathVariable Integer addressId) {
        currentAddressService.deleteAddress(addressId);
        return ResponseEntity.ok("Address deleted successfully.");
    }

    // Get addresses for a user with pagination
    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<CurrentAddress>> getAddressesByUserId(
            @PathVariable Integer userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<CurrentAddress> addressPage = currentAddressService.getAddressesByUserId(userId, page, size);
        return ResponseEntity.ok(addressPage);
    }


    // Update an address by ID
    @PutMapping("/update/{userId}/{addressId}")
    public ResponseEntity<CurrentAddress> updateAddress(
            @PathVariable Integer userId,
            @PathVariable Integer addressId,
            @RequestBody CurrentAddress newAddressData) {
        CurrentAddress updatedAddress = currentAddressService.updateAddress(userId, addressId, newAddressData);
        return ResponseEntity.ok(updatedAddress);
    }
}
