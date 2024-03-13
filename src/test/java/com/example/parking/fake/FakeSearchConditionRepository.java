package com.example.parking.fake;

import com.example.parking.domain.searchcondition.SearchCondition;
import com.example.parking.domain.searchcondition.SearchConditionRepository;
import java.util.Optional;

public class FakeSearchConditionRepository implements SearchConditionRepository {
    @Override
    public Optional<SearchCondition> findByMemberId(Long memberId) {
        return Optional.empty();
    }

    @Override
    public void save(SearchCondition searchCondition) {

    }
}
