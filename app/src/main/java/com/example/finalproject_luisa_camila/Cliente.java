package com.example.finalproject_luisa_camila;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Cliente extends AppCompatActivity {
    private static final String DB_NAME= "YourDatabaseName";
    private static final int DB_VERSION= 1;
    private static final String TABLE_CLIENTE= "Cliente";

    private EditText editTextClientId, editTextClientName, editTextClientAddress, editTextClientPhone;
    private Button buttonInsertClient, buttonUpdateClient, buttonDeleteClient, buttonQueryClient;

    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente);

        editTextClientId = findViewById(R.id.editTextClientId);
        editTextClientName = findViewById(R.id.editTextClientName);
        editTextClientAddress = findViewById(R.id.editTextClientAddress);
        editTextClientPhone = findViewById(R.id.editTextClientPhone);
        buttonInsertClient = findViewById(R.id.buttonInsertClient);
        buttonUpdateClient = findViewById(R.id.buttonUpdateClient);
        buttonDeleteClient = findViewById(R.id.buttonDeleteClient);
        buttonQueryClient = findViewById(R.id.buttonQueryClient);

        database = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);
        createTableCliente();

        buttonInsertClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertCliente();
            }
        });

        buttonUpdateClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCliente();
            }
        });

        buttonDeleteClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCliente();
            }
        });

        buttonQueryClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryCliente();
            }
        });
    }

    private void createTableCliente() {
        database.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_CLIENTE + " (IdCliente INTEGER PRIMARY KEY, " +
                "NombreCliente TEXT, DireccionCliente TEXT, TelefonoCliente TEXT)");
    }

    private void insertCliente() {
        int id = Integer.parseInt(editTextClientId.getText().toString());
        String name = editTextClientName.getText().toString();
        String address = editTextClientAddress.getText().toString();
        String phone = editTextClientPhone.getText().toString();

        ContentValues values = new ContentValues();
        values.put("IdCliente", id);
        values.put("NombreCliente", name);
        values.put("DireccionCliente", address);
        values.put("TelefonoCliente", phone);

        long result = database.insert(TABLE_CLIENTE, null, values);

        if (result == -1) {
            showToast("No se pudo insertar el cliente");
        } else {
            showToast("El cliente se insertó exitosamente");
            clearFields();
        }
    }

    private void updateCliente() {
        int id = Integer.parseInt(editTextClientId.getText().toString());
        String name = editTextClientName.getText().toString();
        String address = editTextClientAddress.getText().toString();
        String phone = editTextClientPhone.getText().toString();

        ContentValues values = new ContentValues();
        values.put("NombreCliente", name);
        values.put("DireccionCliente", address);
        values.put("TelefonoCliente", phone);

        int result = database.update(TABLE_CLIENTE, values, "IdCliente=?", new String[]{String.valueOf(id)});

        if (result > 0) {
            showToast("El cliente se actualizó exitosamente");
            clearFields();
        } else {
            showToast("No se pudo actualizar el cliente");
        }
    }

    private void deleteCliente() {
        int id = Integer.parseInt(editTextClientId.getText().toString());

        int result = database.delete(TABLE_CLIENTE, "IdCliente=?", new String[]{String.valueOf(id)});

        if (result > 0) {
            showToast("El cliente se eliminó exitosamente");
            clearFields();
        } else {
            showToast("No se pudo eliminar el cliente");
        }
    }

    private void queryCliente() {
        int id = Integer.parseInt(editTextClientId.getText().toString());

        Cursor cursor = database.query(TABLE_CLIENTE, null, "IdCliente=?", new String[]{String.valueOf(id)}, null, null, null);

        if (cursor.moveToFirst()) {
            String name = cursor.getString(1);
            String address = cursor.getString(2);
            String phone = cursor.getString(3);

            editTextClientName.setText(name);
            editTextClientAddress.setText(address);
            editTextClientPhone.setText(phone);
        } else {
            showToast("No se encontró el cliente");
        }

        cursor.close();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void clearFields() {
        editTextClientId.setText("");
        editTextClientName.setText("");
        editTextClientAddress.setText("");
        editTextClientPhone.setText("");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        database.close();
    }
}
