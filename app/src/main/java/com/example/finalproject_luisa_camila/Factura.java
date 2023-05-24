package com.example.finalproject_luisa_camila;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Factura extends AppCompatActivity {
    private static final String DB_NAME = "YourDatabaseName";
    private static final int DB_VERSION = 1;
    private static final String TABLE_PEDIDO= "Pedido";
    private static final String TABLE_CLIENTE= "Cliente";
    private static final String TABLE_PRODUCTO= "Producto";
    private static final String TABLE_FACTURA= "Factura";

    private EditText editTextClienteID, editTextPedidoID, editTextFacturaID, editTextValorFactura, editTextDateFactura, editTextProductoID;
    private Button buttonConsultarClientePedido, buttonCrearFactura, buttonActualizarFactura, buttonConsultarFactura, buttonEliminarFactura;

    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_factura);

        editTextClienteID = findViewById(R.id.editTextClienteID);
        editTextPedidoID = findViewById(R.id.editTextPedidoID);
        editTextFacturaID = findViewById(R.id.editTextFacturaID);
        editTextValorFactura= findViewById(R.id.editTextValorFactura);
        editTextDateFactura= findViewById(R.id.editTextDateFactura);
        editTextProductoID= findViewById(R.id.editTextProductoID);

        buttonConsultarClientePedido = findViewById(R.id.buttonConsultarClientePedido);
        buttonCrearFactura = findViewById(R.id.buttonCrearFactura);
        buttonActualizarFactura = findViewById(R.id.buttonActualizarFactura);
        buttonConsultarFactura = findViewById(R.id.buttonConsultarFactura);
        buttonEliminarFactura = findViewById(R.id.buttonEliminarFactura);

        database = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);
        createTablePedido();
        createTableCliente();
        createTableProducto();
        createTableFactura();


        buttonConsultarClientePedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consultarClientePedido();
            }
        });

        buttonCrearFactura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearFactura();
            }
        });

        buttonActualizarFactura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarFactura();
            }
        });

        buttonConsultarFactura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consultarFactura();
            }
        });

        buttonEliminarFactura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminarFactura();
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

    private void createTableProducto(){
        database.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_PRODUCTO + " (IdProducto int primary key, " +
                "NombreProducto text, ValorProducto real, FabricanteProducto text)");
    }

    private void createTableFactura(){
        database.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_FACTURA + " (IdFactura int primary key, " +
                "IdCliente int, IdProducto int, IdPedido int,ValorFactura real, FechaFactura text," +
                "Foreign key (IdCliente) references Cliente(IdCliente) ON DELETE CASCADE ON UPDATE CASCADE, " +
                "Foreign key (IdProducto) references Producto(IdProducto)ON DELETE CASCADE ON UPDATE CASCADE," +
                "Foreign key (IdPedido) references Pedido(IdPedido) ON DELETE CASCADE ON UPDATE CASCADE)");
    }

    private void consultarClientePedido() {
        int clienteId = Integer.parseInt(editTextClienteID.getText().toString());
        int PedidoId = Integer.parseInt(editTextPedidoID.getText().toString());
        int ProductoId = Integer.parseInt(editTextProductoID.getText().toString());

        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_CLIENTE + " WHERE IdCliente = " + clienteId, null);
        Cursor cursor1 = database.rawQuery("SELECT * FROM " + TABLE_PEDIDO + " WHERE IdPedido = " + PedidoId, null);
        Cursor cursor2 = database.rawQuery("SELECT * FROM " + TABLE_PRODUCTO + " WHERE IdProducto = " + ProductoId, null);

        if (cursor.moveToFirst() && cursor1.moveToFirst() && cursor2.moveToFirst()  ) {
            buttonCrearFactura.setEnabled(true);
            buttonActualizarFactura.setEnabled(true);
            editTextFacturaID.setText("");
            editTextValorFactura.setText("");
            editTextDateFactura.setText("");

        } else {
            buttonCrearFactura.setEnabled(false);
            buttonActualizarFactura.setEnabled(false);
            Toast.makeText(this, "No existe cliente, pedido o producto. Debe crearse primero", Toast.LENGTH_LONG).show();
            editTextClienteID.setText("");
            editTextPedidoID.setText("");
            editTextProductoID.setText("");
            editTextFacturaID.setText("");
            editTextValorFactura.setText("");
            editTextDateFactura.setText("");
        }
        cursor.close();
        cursor1.close();
        cursor2.close();

    }

    private void crearFactura() {
        String clienteId = editTextClienteID.getText().toString();
        String IdPedido = editTextPedidoID.getText().toString();
        String IdProducto = editTextProductoID.getText().toString();
        String IdFactura = editTextFacturaID.getText().toString();
        String ValorFactura = editTextValorFactura.getText().toString();
        String FechaFactura = editTextDateFactura.getText().toString();

        if (IdPedido.isEmpty() || clienteId.isEmpty() || IdFactura.isEmpty() || ValorFactura.isEmpty() || FechaFactura.isEmpty() || IdProducto.isEmpty()){
            Toast.makeText(this,"No pueden existir campos vacíos",Toast.LENGTH_LONG).show();
        }else{
            ContentValues values = new ContentValues();
            values.put("IdFactura", Integer.parseInt(IdFactura));
            values.put("IdCliente", Integer.parseInt(clienteId));
            values.put("IdProducto", Integer.parseInt(IdProducto));
            values.put("IdPedido", Integer.parseInt(IdPedido));
            values.put("ValorFactura", Float.parseFloat(ValorFactura));
            values.put("FechaFactura", FechaFactura);

            long FacturaId = database.insert(TABLE_FACTURA, null, values);

            if (FacturaId != -1) {
                Toast.makeText(this, "La factura se ha creado exitosamente", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "No se pudo crear la factura", Toast.LENGTH_LONG).show();
            }

            editTextClienteID.setText("");
            editTextPedidoID.setText("");
            editTextProductoID.setText("");
            editTextFacturaID.setText("");
            editTextValorFactura.setText("");
            editTextDateFactura.setText("");
        }

    }

    private void actualizarFactura() {
        int FacturaId = Integer.parseInt(editTextFacturaID.getText().toString());
        String ValorFactura = editTextValorFactura.getText().toString();
        String FechaFactura = editTextDateFactura.getText().toString();

        ContentValues values = new ContentValues();
        values.put("ValorFactura", Float.parseFloat(ValorFactura));
        values.put("FechaFactura", FechaFactura);

        int rowsAffected = database.update(TABLE_FACTURA, values, "IdFactura = " + FacturaId, null);

        if (rowsAffected > 0) {
            Toast.makeText(this, "La factura se actualizó exitosamente", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "No se pudo actualizar la factura", Toast.LENGTH_LONG).show();
        }
        editTextClienteID.setText("");
        editTextPedidoID.setText("");
        editTextProductoID.setText("");
        editTextFacturaID.setText("");
        editTextValorFactura.setText("");
        editTextDateFactura.setText("");
    }

    private void consultarFactura() {
        int FacturaId = Integer.parseInt(editTextFacturaID.getText().toString());

        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_FACTURA + " WHERE IdFactura = " + FacturaId, null);

        if (cursor.moveToFirst()) {
            int IdFactura = cursor.getInt(0);
            int IdCliente = cursor.getInt(1);
            int IdProducto = cursor.getInt(2);
            int IdPedido = cursor.getInt(3);
            float ValorFactura = cursor.getFloat(4);
            String FechaFactura = cursor.getString(5);

            editTextClienteID.setText(Integer.toString(IdCliente));
            editTextPedidoID.setText(Integer.toString(IdPedido));
            editTextProductoID.setText(Integer.toString(IdProducto));
            editTextFacturaID.setText(Integer.toString(IdFactura));
            editTextValorFactura.setText(String.valueOf(ValorFactura));
            editTextDateFactura.setText(FechaFactura);

        } else {
            Toast.makeText(this, "No se pudo encontrar la factura", Toast.LENGTH_SHORT).show();
            editTextClienteID.setText("");
            editTextPedidoID.setText("");
            editTextProductoID.setText("");
            editTextFacturaID.setText("");
            editTextValorFactura.setText("");
            editTextDateFactura.setText("");
        }
        cursor.close();
    }

    private void eliminarFactura() {
        int FacturaId = Integer.parseInt(editTextFacturaID.getText().toString());

        int rowsAffected = database.delete(TABLE_FACTURA, "IdFactura = " + FacturaId, null);

        if (rowsAffected > 0) {
            Toast.makeText(this, "La factura se eliminó exitosamente", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "No se pudo eliminar la factura", Toast.LENGTH_LONG).show();
        }
        editTextClienteID.setText("");
        editTextPedidoID.setText("");
        editTextProductoID.setText("");
        editTextFacturaID.setText("");
        editTextValorFactura.setText("");
        editTextDateFactura.setText("");
    }

    protected void onDestroy() {
        super.onDestroy();
        database.close();
    }
}