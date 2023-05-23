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

public class Pedido extends AppCompatActivity {
    private static final String DB_NAME = "YourDatabaseName";
    private static final int DB_VERSION = 1;
    private static final String TABLE_PEDIDO = "Pedido";
    private static final String TABLE_CLIENTE = "Cliente";

    private EditText editTextClienteId, editTextPedidoId, editTextDescripcionPedido, editTextDate;
    private Button buttonConsultarCliente, buttonCrearPedido, buttonActualizarPedido, buttonConsultarPedido, buttonEliminarPedido;

    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido);

        // Initialize views
        editTextClienteId = findViewById(R.id.editTextClienteId);
        editTextPedidoId = findViewById(R.id.editTextPedidoId);
        editTextDescripcionPedido = findViewById(R.id.editTextDescripcionPedido);
        editTextDate= findViewById(R.id.editTextDate);
        buttonConsultarCliente = findViewById(R.id.buttonConsultarCliente);
        buttonCrearPedido = findViewById(R.id.buttonCrearPedido);
        buttonActualizarPedido = findViewById(R.id.buttonActualizarPedido);
        buttonConsultarPedido = findViewById(R.id.buttonConsultarPedido);
        buttonEliminarPedido = findViewById(R.id.buttonEliminarPedido);

        // Create or open the database
        database = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);
        createTablePedido();
        createTableCliente();

        buttonConsultarCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consultarCliente();
            }
        });

        buttonCrearPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearPedido();
            }
        });

        buttonActualizarPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarPedido();
            }
        });

        buttonConsultarPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consultarPedido();
            }
        });

        buttonEliminarPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminarPedido();
            }
        });
    }

    private void createTablePedido(){
        database.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_PEDIDO + " (IdPedido INTEGER PRIMARY KEY, " +
                "IdCliente INTEGER, DescripcionPedido TEXT, FechaPedido TEXT, " +
                "FOREIGN KEY (IdCliente) REFERENCES Cliente(IdCliente) ON DELETE CASCADE ON UPDATE CASCADE)");
    }

    private void createTableCliente(){
        database.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_CLIENTE + " (IdCliente INTEGER PRIMARY KEY, " +
                "NombreCliente TEXT, DireccionCliente TEXT, TelefonoCliente TEXT)");

    }

    private void consultarCliente() {
        int clienteId = Integer.parseInt(editTextClienteId.getText().toString());

        // Perform the query to check if the client exists
        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_CLIENTE + " WHERE IdCliente = " + clienteId, null);

        if (cursor.moveToFirst()) {
            // Client exists, enable the buttons for creating and updating pedido
            buttonCrearPedido.setEnabled(true);
            buttonActualizarPedido.setEnabled(true);
            editTextPedidoId.setText("");
            editTextDescripcionPedido.setText("");
            editTextDate.setText("");

        } else {
            // Client does not exist, show an error message and disable the buttons
            buttonCrearPedido.setEnabled(false);
            buttonActualizarPedido.setEnabled(false);
            Toast.makeText(this, "No existe el cliente, debe crearlo", Toast.LENGTH_SHORT).show();
            editTextPedidoId.setText("");
            editTextClienteId.setText("");
            editTextDescripcionPedido.setText("");
            editTextDate.setText("");
        }

        cursor.close();

    }

    private void crearPedido() {
        String IdPedido = editTextPedidoId.getText().toString();
        String clienteId = editTextClienteId.getText().toString();
        String descripcionPedido = editTextDescripcionPedido.getText().toString();
        String fechaPedido = editTextDate.getText().toString();

        if (IdPedido.isEmpty() || clienteId.isEmpty() || descripcionPedido.isEmpty() || fechaPedido.isEmpty() ){
            Toast.makeText(this,"No pueden existir campos vacíos",Toast.LENGTH_LONG).show();

        }else{
            ContentValues values = new ContentValues();
            values.put("IdPedido", Integer.parseInt(IdPedido));
            values.put("IdCliente", Integer.parseInt(clienteId));
            values.put("DescripcionPedido", descripcionPedido);
            values.put("FechaPedido", fechaPedido);

            // Insert the pedido into the database
            long pedidoId = database.insert(TABLE_PEDIDO, null, values);

            if (pedidoId != -1) {
                Toast.makeText(this, "El pedido se ha creado exitosamente", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "No se pudo crear el pedido", Toast.LENGTH_LONG).show();
            }

            editTextPedidoId.setText("");
            editTextClienteId.setText("");
            editTextDescripcionPedido.setText("");
            editTextDate.setText("");

        }


    }

    private void actualizarPedido() {
        int pedidoId = Integer.parseInt(editTextPedidoId.getText().toString());
        String descripcionPedido = editTextDescripcionPedido.getText().toString();
        String FechaPedido = editTextDate.getText().toString();

        ContentValues values = new ContentValues();
        values.put("DescripcionPedido", descripcionPedido);
        values.put("FechaPedido", FechaPedido);

        // Update the pedido in the database
        int rowsAffected = database.update(TABLE_PEDIDO, values, "IdPedido = " + pedidoId, null);

        if (rowsAffected > 0) {
            Toast.makeText(this, "El pedido se actualizó exitosamente", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No se pudo actualizar el pedido", Toast.LENGTH_SHORT).show();
        }
        editTextPedidoId.setText("");
        editTextClienteId.setText("");
        editTextDescripcionPedido.setText("");
        editTextDate.setText("");
    }

    private void consultarPedido() {
        int pedidoId = Integer.parseInt(editTextPedidoId.getText().toString());

        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_PEDIDO + " WHERE IdPedido = " + pedidoId, null);

        if (cursor.moveToFirst()) {
            int IdPedido = cursor.getInt(0);
            int IdCliente = cursor.getInt(1);
            String descripcionPedido = cursor.getString(2);
            String FechaPedido = cursor.getString(3);
            editTextPedidoId.setText(Integer.toString(IdPedido));
            editTextClienteId.setText(Integer.toString(IdCliente));
            editTextDescripcionPedido.setText(descripcionPedido);
            editTextDate.setText(FechaPedido);
        } else {
            Toast.makeText(this, "No se pudo encontrar el pedido", Toast.LENGTH_SHORT).show();
            editTextPedidoId.setText("");
            editTextClienteId.setText("");
            editTextDescripcionPedido.setText("");
            editTextDate.setText("");
        }
        cursor.close();
    }

    private void eliminarPedido() {
        int pedidoId = Integer.parseInt(editTextPedidoId.getText().toString());

        int rowsAffected = database.delete(TABLE_PEDIDO, "IdPedido = " + pedidoId, null);

        if (rowsAffected > 0) {
            Toast.makeText(this, "El pedido se eliminó exitosamente", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No se pudo eliminar el pedido", Toast.LENGTH_SHORT).show();
        }
        editTextPedidoId.setText("");
        editTextClienteId.setText("");
        editTextDescripcionPedido.setText("");
        editTextDate.setText("");
    }

    protected void onDestroy() {
        super.onDestroy();
        database.close();
    }
}


