package com.ucs.adriel.restapiacessclient.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ucs.adriel.restapiacessclient.model.Card;

import java.util.ArrayList;

public class BDSQLiteHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "MtgCards";
    private static final String TABELA_CARDS = "cards";
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String MANACOST = "manacost";
    private static final String TYPE = "type";
    private static final String RARITY = "rarity";
    private static final String CSET = "cset";
    private static final String TEXT = "text";
    private static final String POWER = "power";
    private static final String TOUGHNESS = "toughness";
    private static final String[] COLUNAS = {ID, NAME,MANACOST,TYPE,RARITY,CSET,TEXT,POWER, TOUGHNESS};

    public BDSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE cards (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT," +
                "manacost TEXT," +
                "type TEXT," +
                "rarity TEXT," +
                "cset TEXT," +
                "text TEXT," +
                "power TEXT," +
                "toughness TEXT)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS cards");
        this.onCreate(db);
    }

    public void addCard(Card card) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME, card.getName());
        values.put(MANACOST, card.getManaCost());
        values.put(TYPE, card.getType());
        values.put(RARITY, card.getRarity());
        values.put(CSET, card.getSet());
        values.put(TEXT, card.getText());
        values.put(POWER, card.getPower());
        values.put(TOUGHNESS, card.getToughness());
        db.insert(TABELA_CARDS, null, values);
        db.close();
    }

    public Card getCard(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABELA_CARDS, // a. tabela
                COLUNAS, // b. colunas
                " id = ?", // c. colunas para comparar
                new String[]{String.valueOf(id)}, // d. parâmetros
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit
        if (cursor == null) {
            return null;
        } else {
            cursor.moveToFirst();
            Card card = cursorToCard(cursor);
            return card;
        }
    }
    public Card getCardByName(String name) {
      SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABELA_CARDS, // a. tabela
                COLUNAS, // b. colunas
                " name = ?", // c. colunas para comparar
                new String[]{name}, // d. parâmetros
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit
        if (cursor == null) {
            return null;
        } else {
            if(cursor.moveToFirst())
            {
                Card card = cursorToCard(cursor);
                return card;
            }
            else {
                return null;
            }
        }
    }

    private Card cursorToCard(Cursor cursor) {
        Card card = new Card();
        card.setId(cursor.getString(0));
        card.setName(cursor.getString(1));
        card.setManaCost(cursor.getString(2));
        card.setType(cursor.getString(3));
        card.setRarity(cursor.getString(4));
        card.setSet(cursor.getString(5));
        card.setText(cursor.getString(6));
        card.setPower(cursor.getString(7));
        card.setToughness(cursor.getString(8));
        return card;
    }

    public ArrayList<Card> getAllCards() {
        ArrayList<Card> listaCards = new ArrayList<Card>();
        String query = "SELECT * FROM " + TABELA_CARDS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                Card card = cursorToCard(cursor);
                listaCards.add(card);
            } while (cursor.moveToNext());
        }
        return listaCards;
    }

    public int updateCard(Card card) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME, card.getName());
        values.put(MANACOST, card.getManaCost());
        values.put(TYPE, card.getType());
        values.put(RARITY, card.getRarity());
        values.put(CSET, card.getSet());
        values.put(TEXT, card.getText());
        values.put(POWER, card.getPower());
        values.put(TOUGHNESS, card.getToughness());
        int i = db.update(TABELA_CARDS, //tabela
                values, // valores
                ID + " = ?", // colunas para comparar
                new String[]
                        {card.getId()}); //parâmetros
        db.close();
        return i; // número de linhas modificadas
    }

    public int deleteCard(Card card) {
        SQLiteDatabase db = this.getWritableDatabase();
        int i = db.delete(TABELA_CARDS, //tabela
                ID + " = ?", // colunas para comparar
                new String[]
                        {card.getId()});
        db.close();
        return i; // número de linhas excluídas
    }
}
