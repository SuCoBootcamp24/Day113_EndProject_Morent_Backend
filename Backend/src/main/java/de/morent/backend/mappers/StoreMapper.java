package de.morent.backend.mappers;

import de.morent.backend.dtos.store.StoreDTO;
import de.morent.backend.dtos.store.StoreShortDTO;
import de.morent.backend.entities.Booking;
import de.morent.backend.entities.Store;
import de.morent.backend.services.BookingService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;


@Component
public class StoreMapper {

    private BookingService bookingService;

    public StoreMapper(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    public StoreShortDTO toStoreShortDTO(Store store) {
        return new StoreShortDTO(
                store.getId(),
                store.getName(),
                AddressMapper.toDTO(store.getAddress()),
                0.0
        );
    }

    public List<StoreShortDTO> toListStoreShort (List<Store> stores) {
        return stores.stream()
               .map(this::toStoreShortDTO)
               .collect(Collectors.toList());
    }

    public StoreDTO toStoreDTO(Store store) {
        String name = store.getManager().getProfile().getFirstName() + " " + store.getManager().getProfile().getLastName();
        return new StoreDTO(
                store.getId(),
                store.getName(),
                AddressMapper.toDTO(store.getAddress()),
                store.getVehicleExemplars().size(),
                bookingService.getBookingsFromStore(store.getId()).size(),
                name
        );
    }
}
