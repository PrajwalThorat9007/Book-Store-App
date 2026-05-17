package com.bookstore.modules.customer.service;

/*
 * This is the service layer class for the Customer module.
 * It contains all business logic for managing customer profiles and delivery addresses.
 * Key rules enforced here:
 *   - A customer profile is auto-created if it doesn't exist yet
 *   - The first address added is automatically set as default
 *   - Only one address can be the default at a time
 *   - Users can only update or delete their own addresses (ownership check)
 */

import com.bookstore.entity.Address;
import com.bookstore.entity.CustomerProfile;
import com.bookstore.entity.User;
import com.bookstore.modules.customer.dto.AddressRequest;
import com.bookstore.modules.customer.dto.AddressResponse;
import com.bookstore.modules.customer.dto.CustomerDetailsRequest;
import com.bookstore.modules.customer.dto.CustomerDetailsResponse;
import com.bookstore.modules.customer.repository.AddressRepository;
import com.bookstore.modules.customer.repository.CustomerProfileRepository;
import com.bookstore.modules.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CustomerService {

    private final CustomerProfileRepository customerProfileRepository;
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    // Constructor injection — Spring provides all three repository beans automatically
    public CustomerService(
            CustomerProfileRepository customerProfileRepository,
            AddressRepository addressRepository,
            UserRepository userRepository) {
        this.customerProfileRepository = customerProfileRepository;
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
    }

    // Returns the customer profile including all saved addresses — throws if profile not found
    @Transactional(readOnly = true)
    public CustomerDetailsResponse getCustomerDetails(Long userId) {
        CustomerProfile profile = customerProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Customer profile not found for user: " + userId));
        return mapToCustomerDetailsResponse(profile);
    }

    // Creates a new profile if one doesn't exist, then updates phone and preference notes
    public CustomerDetailsResponse updateCustomerDetails(Long userId, CustomerDetailsRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));

        // Auto-create a profile if this user doesn't have one yet
        CustomerProfile profile = customerProfileRepository.findByUserId(userId)
                .orElseGet(() -> {
                    CustomerProfile newProfile = new CustomerProfile();
                    newProfile.setUser(user);
                    return newProfile;
                });

        // Only update fields that are actually provided in the request
        if (request.getPhone() != null) {
            profile.setPhone(request.getPhone());
        }
        if (request.getPreferenceNotes() != null) {
            profile.setPreferenceNotes(request.getPreferenceNotes());
        }

        CustomerProfile saved = customerProfileRepository.save(profile);

        // If a default address is included in the request, save it as well
        if (request.getDefaultAddress() != null) {
            AddressRequest addrReq = request.getDefaultAddress();
            addrReq.setIsDefault(true);
            addAddress(userId, addrReq);
        }

        return mapToCustomerDetailsResponse(saved);
    }

    // Adds a new address — first address is always default, subsequent ones only if isDefault=true
    public AddressResponse addAddress(Long userId, AddressRequest request) {
        CustomerProfile profile = customerProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Customer profile not found for user: " + userId));

        List<Address> existing = addressRepository.findByCustomerProfileId(profile.getId());

        // First address is always default; otherwise respect the isDefault flag from the request
        boolean makeDefault = existing.isEmpty() || Boolean.TRUE.equals(request.getIsDefault());

        if (makeDefault) {
            // Clear the default flag from all existing addresses before setting the new one
            existing.forEach(a -> {
                a.setIsDefault(false);
                addressRepository.save(a);
            });
        }

        Address address = new Address();
        address.setCustomerProfile(profile);
        address.setLine1(request.getLine1());
        address.setLine2(request.getLine2());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setPincode(request.getPincode());
        address.setIsDefault(makeDefault);

        Address saved = addressRepository.save(address);
        return mapToAddressResponse(saved);
    }

    // Updates an existing address — verifies ownership before making any changes
    public AddressResponse updateAddress(Long userId, Long addressId, AddressRequest request) {
        CustomerProfile profile = customerProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Customer profile not found for user: " + userId));

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found: " + addressId));

        // Ownership check — prevent users from editing addresses that don't belong to them
        if (!address.getCustomerProfile().getId().equals(profile.getId())) {
            throw new RuntimeException("Address does not belong to this user");
        }

        address.setLine1(request.getLine1());
        address.setLine2(request.getLine2());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setPincode(request.getPincode());

        if (Boolean.TRUE.equals(request.getIsDefault())) {
            // Clear default from all other addresses before setting this one as default
            addressRepository.findByCustomerProfileId(profile.getId()).forEach(a -> {
                if (!a.getId().equals(addressId)) {
                    a.setIsDefault(false);
                    addressRepository.save(a);
                }
            });
            address.setIsDefault(true);
        }

        Address saved = addressRepository.save(address);
        return mapToAddressResponse(saved);
    }

    // Deletes an address — verifies ownership before deleting
    public void deleteAddress(Long userId, Long addressId) {
        CustomerProfile profile = customerProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Customer profile not found for user: " + userId));

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found: " + addressId));

        // Ownership check — users can only delete their own addresses
        if (!address.getCustomerProfile().getId().equals(profile.getId())) {
            throw new RuntimeException("Address does not belong to this user");
        }

        addressRepository.delete(address);
    }

    // Sets one address as default — clears the default flag from all other addresses first
    public void setDefaultAddress(Long userId, Long addressId) {
        CustomerProfile profile = customerProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Customer profile not found for user: " + userId));

        Address target = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found: " + addressId));

        // Ownership check — users can only set their own addresses as default
        if (!target.getCustomerProfile().getId().equals(profile.getId())) {
            throw new RuntimeException("Address does not belong to this user");
        }

        // Set isDefault=true only for the target address, false for all others
        addressRepository.findByCustomerProfileId(profile.getId()).forEach(a -> {
            a.setIsDefault(a.getId().equals(addressId));
            addressRepository.save(a);
        });
    }

    // Returns all addresses for a customer — used internally and can be exposed via a dedicated endpoint
    @Transactional(readOnly = true)
    public List<AddressResponse> getAllAddresses(Long userId) {
        CustomerProfile profile = customerProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Customer profile not found for user: " + userId));
        return addressRepository.findByCustomerProfileId(profile.getId())
                .stream()
                .map(this::mapToAddressResponse)
                .collect(Collectors.toList());
    }

    // Converts a CustomerProfile entity to a CustomerDetailsResponse DTO — includes all addresses
    private CustomerDetailsResponse mapToCustomerDetailsResponse(CustomerProfile profile) {
        List<Address> addresses = addressRepository.findByCustomerProfileId(profile.getId());

        CustomerDetailsResponse response = new CustomerDetailsResponse();
        response.setId(profile.getId());
        response.setUserId(profile.getUser().getId());
        response.setPhone(profile.getPhone());
        response.setPreferenceNotes(profile.getPreferenceNotes());
        response.setAddresses(addresses.stream().map(this::mapToAddressResponse).collect(Collectors.toList()));

        // Find and set the default address separately for easy access on the frontend
        addresses.stream()
                .filter(a -> Boolean.TRUE.equals(a.getIsDefault()))
                .findFirst()
                .ifPresent(a -> response.setDefaultAddress(mapToAddressResponse(a)));

        return response;
    }

    // Converts an Address entity to an AddressResponse DTO
    private AddressResponse mapToAddressResponse(Address address) {
        AddressResponse response = new AddressResponse();
        response.setId(address.getId());
        response.setLine1(address.getLine1());
        response.setLine2(address.getLine2());
        response.setCity(address.getCity());
        response.setState(address.getState());
        response.setPincode(address.getPincode());
        response.setIsDefault(address.getIsDefault());
        return response;
    }
}
