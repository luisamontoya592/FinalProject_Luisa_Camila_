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

public class Producto extends AppCompatActivity {
    private static final String DB_NAME= "YourDatabaseName";
    private static final int DB_VERSION= 1;
    private static final String TABLE_PRODUCTO= "Producto";

    private EditText editTextProductId, editTextProductName, editTextProductValue, editTextProductManufacturer;
    private Button buttonInsertProduct, buttonUpdateProduct, buttonDeleteProduct, buttonQueryProduct;

    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto);


        editTextProductId = findViewById(R.id.editTextProductId);
        editTextProductName = findViewById(R.id.editTextProductName);
        editTextProductValue = findViewById(R.id.editTextProductValue);
        editTextProductManufacturer = findViewById(R.id.editTextProductManufacturer);
        buttonInsertProduct = findViewById(R.id.buttonInsertProduct);
        buttonUpdateProduct = findViewById(R.id.buttonUpdateProduct);
        buttonDeleteProduct = findViewById(R.id.buttonDeleteProduct);
        buttonQueryProduct = findViewById(R.id.buttonQueryProduct);

        database = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);
        createTableProducto();

        buttonInsertProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertProducto();
            }
        });

        buttonUpdateProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProducto();
            }
        });

        buttonDeleteProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteProducto();
            }
        });

        buttonQueryProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryProducto();
            }
        });
    }

    private void createTableProducto() {
        database.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_PRODUCTO + " (IdProducto INTEGER PRIMARY KEY, " +
                "NombreProducto TEXT, ValorProducto REAL, FabricanteProducto TEXT)");
    }

    private void insertProducto() {
        int id = Integer.parseInt(editTextProductId.getText().toString());
        String name = editTextProductName.getText().toString();
        float value = Float.parseFloat(editTextProductValue.getText().toString());
        String manufacturer = editTextProductManufacturer.getText().toString();

        ContentValues values = new ContentValues();
        values.put("IdProducto", id);
        values.put("NombreProducto", name);
        values.put("ValorProducto", value);
        values.put("FabricanteProducto", manufacturer);

        long result = database.insert(TABLE_PRODUCTO, null, values);

        if (result == -1) {
            showToast("No se pudo insertar el producto");
        } else {
            showToast("Producto insertado exitosamente");
            clearFields();
        }
    }

    private void updateProducto() {
        int id = Integer.parseInt(editTextProductId.getText().toString());
        String name = editTextProductName.getText().toString();
        float value = Float.parseFloat(editTextProductValue.getText().toString());
        String manufacturer = editTextProductManufacturer.getText().toString();

        ContentValues values = new ContentValues();
        values.put("NombreProducto", name);
        values.put("ValorProducto", value);
        values.put("FabricanteProducto", manufacturer);

        int result = database.update(TABLE_PRODUCTO, values, "IdProducto=?", new String[]{String.valueOf(id)});

        if (result > 0) {
            showToast("Se actualizó exitosamente el producto");
            clearFields();
        } else {
            showToast("No se pudo actualizar el producto");
        }
    }

    private void deleteProducto() {
        int id = Integer.parseInt(editTextProductId.getText().toString());

        int result = database.delete(TABLE_PRODUCTO, "IdProducto=?", new String[]{String.valueOf(id)});

        if (result > 0) {
            showToast("Se eliminó el producto exitosamente");
            clearFields();
        } else {
            showToast("No se pudo eliminar el producto");
        }
    }

    private void queryProducto() {
        int id = Integer.parseInt(editTextProductId.getText().toString());

        Cursor cursor = database.query(TABLE_PRODUCTO, null, "IdProducto=?", new String[]{String.valueOf(id)}, null, null, null);

        if (cursor.moveToFirst()) {
            String name = cursor.getString(1);
            float value = cursor.getFloat(2);
            String manufacturer = cursor.getString(3);

            editTextProductName.setText(name);
            editTextProductValue.setText(String.valueOf(value));
            editTextProductManufacturer.setText(manufacturer);
        } else {
            showToast("No se encontró algún producto");
        }

        cursor.close();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void clearFields() {
        editTextProductId.setText("");
        editTextProductName.setText("");
        editTextProductValue.setText("");
        editTextProductManufacturer.setText("");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        database.close();
    }
}
