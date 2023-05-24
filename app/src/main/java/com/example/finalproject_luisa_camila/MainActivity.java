package com.example.finalproject_luisa_camila;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button buttonProductos, buttonClientes, buttonPedidos, buttonFacturas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonProductos = findViewById(R.id.buttonProductos);
        buttonClientes = findViewById(R.id.buttonClientes);
        buttonPedidos = findViewById(R.id.buttonPedidos);
        buttonFacturas = findViewById(R.id.buttonFacturas);


        buttonProductos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProductosActivity();
            }
        });

        buttonClientes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openClientesActivity();
            }
        });

        buttonPedidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPedidosActivity();
            }
        });

        buttonFacturas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFacturasActivity();
            }
        });
    }


    private void openProductosActivity() {
        Intent intent = new Intent(MainActivity.this, Producto.class);
        startActivity(intent);
    }

    private void openClientesActivity() {
        Intent intent = new Intent(MainActivity.this, Cliente.class);
        startActivity(intent);
    }

    private void openPedidosActivity() {
        Intent intent = new Intent(MainActivity.this, Pedido.class);
        startActivity(intent);
    }

    private void openFacturasActivity() {
        Intent intent1 = new Intent(MainActivity.this, Factura.class);
        startActivity(intent1);
    }


}
