package com.vin7x.fitnow;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SqlHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "fitness_trtacker.db";
    private static final int DB_VERSION = 2;
    private static SqlHelper INSTANCE;

    static SqlHelper getInstance(Context context) {
        if (INSTANCE == null)
            INSTANCE = new SqlHelper(context);
        return INSTANCE;
    }

    private SqlHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE calc (id INTEGER primary key, type_calc TEXT, res DECIMAL, created_date DATETIME)"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("TESTE1", "on Upgrade disparado");
    }

    List<Register> getRegistersBy(String type) {
        List<Register> registers = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM calc WHERE type_calc = ?", new String[]{type}); //Usa-se interrogação para se referir a uma variável.

        try {
            //Verificar se o cursor está no perimeiro índice
            if (cursor.moveToFirst()){
                do{
                    Register register = new Register();

                    register.type = cursor.getString(cursor.getColumnIndex("type_calc"));
                    register.response = cursor.getDouble(cursor.getColumnIndex("res"));
                    register.createdDate = cursor.getString(cursor.getColumnIndex("created_date"));
                    registers.add(register);

                }while (cursor.moveToNext());
            }

        } catch (Exception e) {
            Log.e("SQLite", e.getMessage(), e);
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
        }
        return registers;
    }

    long addItem(String type, double response) {
        SQLiteDatabase db = getWritableDatabase();
        long calcId = 0;
        try {
            db.beginTransaction();

            ContentValues values = new ContentValues();
            values.put("type_calc", type);
            values.put("res", response);

            SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss", new Locale("pt", "BR"));
            String now = sdf.format(new Date());

            values.put("created_date", now);
            calcId = db.insertOrThrow("calc", null, values);

            db.setTransactionSuccessful();
            //INSERT INTO (ID,)

        } catch (Exception e) {
            Log.e("SQLite", e.getMessage(), e);
        } finally {
            if (db.isOpen())
                db.endTransaction();
        }
        return calcId;
    }

    long updateItem(String type, double response, int id){
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        long calcId = 0;

        try {
            ContentValues values = new ContentValues();
            values.put("type_calc", type);
            values.put("res", response);

            String now = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss", new Locale("pt", "BR"))
                    .format(Calendar.getInstance().getTime());

            values.put("created_date", now);

            calcId = db.update("calc", values, "id = ? and type_calc = ?", new String[]{String.valueOf(id), type});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("SQLite", e.getMessage(), e);
        } finally {
            db.endTransaction();
        }
        return calcId;
    }

    long removeItem(String type, int id){
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        long calcId = 0;

        try{
            // Passamos o whereClause para verificar o registro pelo ID e TYPE_CALC
            calcId = db.delete("calc", "id = ? and type_calc = ?", new String[]{String.valueOf(id), type});
            db.setTransactionSuccessful();
        } catch (Exception e){
            Log.e("SQLite", e.getMessage(), e);
        }finally {
            db.endTransaction();
        }
        return calcId;
    }
}
