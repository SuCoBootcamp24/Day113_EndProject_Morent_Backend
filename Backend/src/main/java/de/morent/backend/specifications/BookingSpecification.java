package de.morent.backend.specifications;

import de.morent.backend.entities.Booking;
import org.springframework.data.jpa.domain.Specification;

public class BookingSpecification {
    public static Specification<Booking> bookingNumberLike(String bookingNumber) {
        return (root, query, criteriaBuilder) -> {
            if (bookingNumber == null || bookingNumber.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("bookingNumber")), "%" + bookingNumber.toLowerCase() + "%");
        };
    }

    public static Specification<Booking> storeIdLike(Long storeId) {
        return (root, query, criteriaBuilder) -> {
            if (storeId != null ||  storeId == 0) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.join("pickUpLocation").get("id")), "%" + storeId + "%");
        };
    }

    public static Specification<Booking> firstNameLike(String firstName) {
        return (root, query, criteriaBuilder) -> {
            if (firstName == null || firstName.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")), "%" + firstName.toLowerCase() + "%");
        };
    }

    public static Specification<Booking> lastNameLike(String lastName) {
        return (root, query, criteriaBuilder) -> {
            if (lastName == null || lastName.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")), "%" + lastName.toLowerCase() + "%");
        };
    }


}
