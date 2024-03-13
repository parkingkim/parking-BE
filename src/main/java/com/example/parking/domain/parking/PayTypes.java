package com.example.parking.domain.parking;

import jakarta.persistence.Embeddable;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@Embeddable
public class PayTypes {

    public static final PayTypes DEFAULT = new PayTypes(PayType.NO_INFO.getDescription());
    private static final String DELIMITER = ", ";

    private String description;

    private PayTypes(String description) {
        this.description = description;
    }

    public static PayTypes from(Collection<PayType> payTypes) {
        if (payTypes.contains(PayType.NO_INFO)) {
            return DEFAULT;
        }

        return new PayTypes(payTypes.stream()
                .map(PayType::getDescription)
                .sorted()
                .collect(Collectors.joining(DELIMITER))
        );
    }

    public boolean contains(List<PayType> memberPayTypes) {
        return memberPayTypes.stream()
                .anyMatch(payType -> this.description.contains(payType.getDescription()));
    }
}
