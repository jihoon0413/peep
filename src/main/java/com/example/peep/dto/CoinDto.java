package com.example.peep.dto;

import com.example.peep.domain.Coin;

public record CoinDto(
        int myCoin
) {
    public static CoinDto of(int myCoin) {
        return new CoinDto(myCoin);
    }

    public CoinDto from(Coin coin) {
        return CoinDto.of(coin.getMyCoin());
    }

}
