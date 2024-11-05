package de.morent.backend.mappers;

import de.morent.backend.dtos.address.AddressDTO;
import de.morent.backend.entities.Address;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {

    public static AddressDTO toDTO(Address address) {
        return new AddressDTO(
                address.getStreet(),
                address.getHouseNumber(),
                address.getZipCode(),
                address.getCity(),
                address.getCountry(),
                address.getCoordinates(),
                address.isRealUserAddress()
        );
    }
}
