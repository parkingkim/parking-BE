package com.parkingcomestrue.common.domain.searchcondition;

import com.parkingcomestrue.common.domain.member.Member;
import com.parkingcomestrue.common.domain.parking.OperationType;
import com.parkingcomestrue.common.domain.parking.ParkingType;
import com.parkingcomestrue.common.domain.parking.PayType;
import com.parkingcomestrue.common.infra.converter.AssociationConverter;
import com.parkingcomestrue.common.infra.converter.FeeTypeConverter;
import com.parkingcomestrue.common.infra.converter.OperationTypeConverter;
import com.parkingcomestrue.common.infra.converter.ParkingTypeConverter;
import com.parkingcomestrue.common.infra.converter.PayTypeConverter;
import com.parkingcomestrue.common.support.Association;
import jakarta.persistence.Convert;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = @Index(name = "search_condition_ux_member_id", columnList = "member_id", unique = true))
public class SearchCondition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Convert(converter = AssociationConverter.class)
    private Association<Member> memberId;

    @Convert(converter = OperationTypeConverter.class)
    private Set<OperationType> operationTypes;

    @Convert(converter = ParkingTypeConverter.class)
    private Set<ParkingType> parkingTypes;

    @Convert(converter = FeeTypeConverter.class)
    private Set<FeeType> feeTypes;

    @Convert(converter = PayTypeConverter.class)
    private Set<PayType> payTypes;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    @Embedded
    private Hours hours;

    public SearchCondition(Association<Member> memberId, Set<OperationType> operationTypes,
                           Set<ParkingType> parkingTypes,
                           Set<FeeType> feeTypes, Set<PayType> payTypes, Priority priority, Hours hours) {
        this.memberId = memberId;
        this.operationTypes = operationTypes;
        this.parkingTypes = parkingTypes;
        this.feeTypes = feeTypes;
        this.payTypes = payTypes;
        this.priority = priority;
        this.hours = hours;
    }

    public void update(SearchCondition updated) {
        this.operationTypes = updated.operationTypes;
        this.parkingTypes = updated.parkingTypes;
        this.feeTypes = updated.feeTypes;
        this.payTypes = updated.payTypes;
        this.priority = updated.priority;
        this.hours = updated.hours;
    }
}
