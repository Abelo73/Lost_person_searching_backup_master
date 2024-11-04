package com.act.Gakos.service;

import com.act.Gakos.entity.CurrentAddress;
import com.act.Gakos.entity.User;
import com.act.Gakos.repository.UserRepository;
import com.act.Gakos.repository.address.CurrentAddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CurrentAddressService {

    @Autowired
    private CurrentAddressRepository currentAddressRepository;

    @Autowired
    private UserRepository userRepository;

    // Check if user exists, then save the address
    public CurrentAddress registerAddress(Integer userId, CurrentAddress address) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            address.setUser(userOptional.get());
            return currentAddressRepository.save(address);
        } else {
            throw new IllegalArgumentException("User not found with id: " + userId);
        }
    }

    // Delete address by ID
    public void deleteAddress(Integer addressId) {
        if (currentAddressRepository.existsById(addressId)) {
            currentAddressRepository.deleteById(addressId);
        } else {
            throw new IllegalArgumentException("Address not found with id: " + addressId);
        }
    }

    // Get addresses by user ID with pagination
    public Page<CurrentAddress> getAddressesByUserId(Integer userId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return currentAddressRepository.findAllByUserId(userId, pageRequest);
    }


    // Update an existing address
    public CurrentAddress updateAddress(Integer userId, Integer addressId, CurrentAddress newAddressData) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User not found with id: " + userId);
        }

        Optional<CurrentAddress> addressOptional = currentAddressRepository.findById(addressId);
        if (addressOptional.isEmpty()) {
            throw new IllegalArgumentException("Address not found with id: " + addressId);
        }

        CurrentAddress existingAddress = addressOptional.get();

        // Update fields with new data
        existingAddress.setCountry(newAddressData.getCountry());
        existingAddress.setRegion(newAddressData.getRegion());
        existingAddress.setZone(newAddressData.getZone());
        existingAddress.setWoreda(newAddressData.getWoreda());
        existingAddress.setKebele(newAddressData.getKebele());
        existingAddress.setGot(newAddressData.getGot());
        existingAddress.setStartedYear(newAddressData.getStartedYear());
        existingAddress.setEndedYear(newAddressData.getEndedYear());
        existingAddress.setCalculatedDuration(newAddressData.getCalculatedDuration());
        existingAddress.setAge(newAddressData.getAge());
        existingAddress.setNotes(newAddressData.getNotes());

        return currentAddressRepository.save(existingAddress);
    }
}
