package com.example.parking.fake;

import com.example.parking.domain.favorite.Favorite;
import com.example.parking.domain.favorite.FavoriteRepository;
import com.example.parking.domain.member.Member;
import com.example.parking.domain.parking.ParkingId;
import com.example.parking.support.Association;
import java.util.List;

public class FakeFavoriteRepository implements FavoriteRepository {
    @Override
    public Favorite save(Favorite favorite) {
        return null;
    }

    @Override
    public void deleteByMemberIdAndParkingId(Association<Member> memberId, Association<ParkingId> parkingId) {

    }

    @Override
    public List<Favorite> findByMemberId(Association<Member> memberId) {
        return null;
    }
}
