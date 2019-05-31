package com.ucs.adriel.restapiacessclient;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ucs.adriel.restapiacessclient.adapter.MtgAdapter;
import com.ucs.adriel.restapiacessclient.model.Card;
import com.ucs.adriel.restapiacessclient.model.Cards;
import com.ucs.adriel.restapiacessclient.mtgApi.MtgApiClient;
import com.ucs.adriel.restapiacessclient.mtgApi.MtgApiInterface;
import com.ucs.adriel.restapiacessclient.sqlite.BDSQLiteHelper;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private BDSQLiteHelper bd;
    private int resultsCount = 0;
    private boolean dataIsLocal = false;
    List<Card> mainCards;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bd = new BDSQLiteHelper(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateUIStatus();
    }

    private void getMtgCards(String name){
        clearRecyclerView();
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        MtgApiInterface  apiService = MtgApiClient.getClient().create(MtgApiInterface.class);
        Call<Cards> call;
      /*  if(name != "")
            call = apiService.getCardsByName(name);
        else*/
            call = apiService.getAllCards();
        dataIsLocal = false;

        call.enqueue(new Callback<Cards>() {
            @Override
            public void onResponse(Call<Cards> call, Response<Cards> response) {
                if(response != null)
                {
                    int statusCode = response.code();
                    mainCards = response.body().getCards();
                    recyclerView.setAdapter(new MtgAdapter(mainCards));
                    resultsCount = mainCards.size();
                    showOnlineSearchCount();
                }
            }

            @Override
            public void onFailure(Call<Cards> call, Throwable t) {
                // Log error here since request failed
                Log.e("deu erro", t.toString());
            }
        });
        updateUIStatus();
    }
    private void showOnlineSearchCount()
    {
        updateUIStatus();
        Toast.makeText(this, resultsCount + " Resultados", Toast.LENGTH_SHORT).show();
    }
    private void showOnlineEditError()
    {
        Toast.makeText(this, " Só é possivel alterar dados locais", Toast.LENGTH_SHORT).show();
    }
    private void clearRecyclerView()
    {
        if(mainCards == null)
            return;
        mainCards.clear();
        final RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setAdapter(new MtgAdapter(mainCards));
    }
    public void onClickListItem(View view)
    {

    }
    public void onClickSearchLocal(View view)
    {
        clearRecyclerView();
        dataIsLocal = true;
         mainCards = bd.getAllCards();
        final RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MtgAdapter(mainCards));



        Toast.makeText(this, mainCards.size() + " Resultados", Toast.LENGTH_SHORT).show();
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                if(i == 4)//left
                {
                    if(dataIsLocal){
                    Intent intent = new Intent(MainActivity.this, editCards.class);
                    intent.putExtra("ID", mainCards.get(viewHolder.getAdapterPosition()).getId());
                    startActivity(intent);
                } else{
                        showOnlineEditError();
                    }

                }else//not left
                {
                    if(dataIsLocal) {
                        Intent intent = new Intent(MainActivity.this, editCards.class);
                        intent.putExtra("ID", mainCards.get(viewHolder.getAdapterPosition()).getId());
                        startActivity(intent);
                    }else{
                        showOnlineEditError();
                    }
                }
            }


        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        updateUIStatus();
    }

    public void onClickSearchOnline(View view)
    {
      /*  if(isInternetWorking())
        {
            getMtgCards("");
       }
        else {
            Toast.makeText(this, "Sem conexão com a internet", Toast.LENGTH_SHORT).show();
        }*/
        getMtgCards("");
    }

    public void onClickSaveData(View view)
    {updateUIStatus();
        if(dataIsLocal || mainCards == null) {
            Toast.makeText(this, "Consulte os dados online para poder salva-los", Toast.LENGTH_SHORT).show();
            return;
        }
        else
        {
            for (int i = 0; i < mainCards.size(); i++)
                bd.addCard(mainCards.get(i));
            Toast.makeText(this, "Os dados online foram salvos localmente", Toast.LENGTH_SHORT).show();
        }

    }

    public void onClickClearData(View view)
    {
        clearRecyclerView();
        List<Card> bdCards = bd.getAllCards();
        for (int i = 0; i < bdCards.size(); i++){
            bd.deleteCard(bdCards.get(i));
        }
        dataIsLocal = true;
        Toast.makeText(this, " Os dados locais foram excluídos", Toast.LENGTH_SHORT).show();
        updateUIStatus();
    }

    public void onClickSearch(View view)
    {
        if(!dataIsLocal || mainCards == null)
        {
           // TextView txtSearch = findViewById(R.id.txtSearch);
           // getMtgCards(String.valueOf(txtSearch.getText()).trim());
             Toast.makeText(this, "Consulte os dados locais para poder pesquisar", Toast.LENGTH_SHORT).show();

            return;
        }
        TextView txtSearch = findViewById(R.id.txtSearch);
        Card card = bd.getCardByName(String.valueOf(txtSearch.getText()).trim());
        mainCards.clear();
        if(card != null)
        {
            mainCards.add(card);
        }

        final RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MtgAdapter(mainCards));
        updateUIStatus();
    }

    public void updateUIStatus()
    {
        TextView lblCurrentData = findViewById(R.id.lblCurrentData);
        TextView lblCurrentAmount = findViewById(R.id.lblCurrentAmount);
        Button btnClearDB = findViewById(R.id.btnClearDB);
        Button btnSaveData = findViewById(R.id.btnSaveData);
        if(dataIsLocal){
            lblCurrentData.setText("Dados Atuais: LOCAL");
            btnClearDB.setEnabled(true);
            btnSaveData.setEnabled(false);
        }else
        {
            lblCurrentData.setText("Dados Atuais: ONLINE");
            btnClearDB.setEnabled(false);
            btnSaveData.setEnabled(true);
        }

        if(mainCards == null)
            lblCurrentAmount.setText("Total: 0");
        else
            lblCurrentAmount.setText("Total: " + mainCards.size());
    }

    public boolean isInternetWorking() {
        boolean success = false;
        try {
            URL url = new URL("https://google.com");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(10000);
            connection.connect();
            success = connection.getResponseCode() == 200;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return success;
    }
}
