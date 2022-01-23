package com.example.hackathon;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;

public class MainActivity extends AppCompatActivity
{
    SQLiteDatabase mydatabase;
    private CodeScanner mCodeScanner;
    private TextView txtName , txtEmail,txtDet;
    Cursor resultSet;
    String name="Default";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        mydatabase = openOrCreateDatabase("hackathon.db",MODE_PRIVATE,null);

        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS HACKATHON(NAME VARCHAR,EMAIL VARCHAR,ID INTEGER PRIMARY KEY);");
        mydatabase.execSQL("INSERT INTO HACKATHON VALUES('VIGNESH','vichu@gmail.com',25);");
        mydatabase.execSQL("INSERT INTO HACKATHON VALUES('CHAITRA','chaitra@gmail.com',29);");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        txtName = findViewById(R.id.textName);
        txtEmail = findViewById(R.id.textEmail);
        txtDet = findViewById(R.id.textDetails);
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback()
        {
            @Override
            public void onDecoded(@NonNull final Result result)
            {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        try
                        {
                            String sql = result.getText();

                            resultSet = mydatabase.rawQuery("Select * from HACKATHON WHERE ID = " + sql ,null);

                            resultSet.moveToFirst();
                            name = resultSet.getString(0);
                            String email = resultSet.getString(1);
                            txtName.setText("NAME : " + name);
                            txtEmail.setText("EMAIL : " + email);
                        }
                        catch(Exception e)
                        {
                                txtDet.setText("INVALID ENTRY");
                                txtName.setText("");
                                txtEmail.setText("");

                        }
                    }
                });
            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
                txtDet.setText("STUDENT DETAILS ");
                txtEmail.setText("EMAIL : ");
                txtName.setText("NAME : ");
            }
        });
    }
}