package com.parkingcomestrue.parking.api.favorite;

import com.parkingcomestrue.parking.application.favorite.FavoriteService;
import com.parkingcomestrue.parking.application.favorite.dto.FavoriteCreateRequest;
import com.parkingcomestrue.parking.application.favorite.dto.FavoriteDeleteRequest;
import com.parkingcomestrue.parking.application.member.dto.MemberId;
import com.parkingcomestrue.parking.config.argumentresolver.MemberAuth;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "즐겨찾기 컨트롤러")
@RequiredArgsConstructor
@RestController
public class FavoriteController {

    private final FavoriteService favoriteService;

    @Operation(summary = "즐겨찾기 등록", description = "즐겨찾기 등록")
    @PostMapping("/favorites")
    public ResponseEntity<Void> create(@RequestBody FavoriteCreateRequest favoriteCreateRequest,
                                       @Parameter(hidden = true) @MemberAuth MemberId memberId) {
        favoriteService.createFavorite(favoriteCreateRequest, memberId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "즐겨찾기 해제", description = "즐겨찾기 해제")
    @DeleteMapping("/favorites")
    public ResponseEntity<Void> delete(@RequestBody FavoriteDeleteRequest favoriteDeleteRequest,
                                       @Parameter(hidden = true) @MemberAuth MemberId memberId) {
        favoriteService.deleteFavorite(favoriteDeleteRequest, memberId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
