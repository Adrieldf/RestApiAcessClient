package com.ucs.adriel.restapiacessclient.mtgApi;

import com.ucs.adriel.restapiacessclient.model.Cards;

import retrofit2.Call;
import retrofit2.http.GET;

public interface MtgApiInterface {
    @GET("cards")
    Call<Cards> getAllCards();
   // @GET("cards/{cardId}")
   // Call<Cards> getCards(@Path("cardId") int cardId);
}
