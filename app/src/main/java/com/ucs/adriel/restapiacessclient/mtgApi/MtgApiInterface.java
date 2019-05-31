package com.ucs.adriel.restapiacessclient.mtgApi;

import com.ucs.adriel.restapiacessclient.model.Cards;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface MtgApiInterface {
    @GET("cards")
    Call<Cards> getAllCards();

    @GET("cards?name={cardId}")
    Call<Cards> getCardsByName(@Path("cardId") String cardId);
}
