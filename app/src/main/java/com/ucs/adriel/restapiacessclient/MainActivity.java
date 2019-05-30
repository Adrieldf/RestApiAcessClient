package com.ucs.adriel.restapiacessclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.ucs.adriel.restapiacessclient.adapter.MtgAdapter;
import com.ucs.adriel.restapiacessclient.model.Card;
import com.ucs.adriel.restapiacessclient.model.Cards;
import com.ucs.adriel.restapiacessclient.mtgApi.MtgApiClient;
import com.ucs.adriel.restapiacessclient.mtgApi.MtgApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getMtgCards();
    }
    private void getMtgCards(){
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        MtgApiInterface apiService = MtgApiClient.getClient().create(MtgApiInterface.class);

        Call<Cards> call = apiService.getAllCards();
        call.enqueue(new Callback<Cards>() {
            @Override
            public void onResponse(Call<Cards> call, Response<Cards> response) {
                if(response != null)
                {
                    int statusCode = response.code();
                    List<Card> cards = response.body().getCards();
                    recyclerView.setAdapter(new MtgAdapter(cards));
                }
            }

            @Override
            public void onFailure(Call<Cards> call, Throwable t) {
                // Log error here since request failed
                Log.e("deu erro", t.toString());
            }
        });
    }
}
