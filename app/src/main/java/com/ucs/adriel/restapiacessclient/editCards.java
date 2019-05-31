package com.ucs.adriel.restapiacessclient;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.support.v7.app.AlertDialog;

import com.ucs.adriel.restapiacessclient.model.Card;
import com.ucs.adriel.restapiacessclient.sqlite.BDSQLiteHelper;

public class editCards extends AppCompatActivity {

    private BDSQLiteHelper bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_cards);

        Intent intent = getIntent();
        final String id = intent.getStringExtra("ID");
        bd = new BDSQLiteHelper(this);
        Card card = bd.getCard(Integer.valueOf(id));

        final EditText name = findViewById(R.id.txtCardName);
        final EditText type = findViewById(R.id.txtType);
        final EditText text = findViewById(R.id.txtText);
        name.setText(card.getName());
        type.setText(card.getType());
        text.setText(card.getText());

        final Button alterar = findViewById(R.id.btnSalvar);
        alterar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Card card = new Card();
                card.setId(String.valueOf(id));
                card.setName(name.getText().toString());
                card.setType(type.getText().toString());
                card.setText(text.getText().toString());
                bd.updateCard(card);
                Intent intent = new Intent(editCards.this, MainActivity.class);
                startActivity(intent);
            }
        });

        final Button remover = findViewById(R.id.btnExcluir);
        remover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(editCards.this)
                        .setTitle("Excluir")
                        .setMessage("Deseja mesmo excluir?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        Card card = new Card();
                                        card.setId(String.valueOf(id));
                                        bd.deleteCard(card);
                                        Intent intent = new Intent(editCards.this, MainActivity.class);
                                        startActivity(intent);
                                    }
                                })
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });

    }
}
