package com.example.censimenti;

import static com.example.censimenti.AdapterComuni.refComuni;
import static com.example.censimenti.AdapterEdifici.refEdifici;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;

public class EdificiActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private AdapterEdifici adapterEdifici;
    FloatingActionButton addButton, esportaBtn;
    String keyCommessa, keyEdificio;
//    static DatabaseReference eRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edifici);

        recyclerView = findViewById(R.id.edificiRecycler);
        keyCommessa = getIntent().getStringExtra("key");
        refEdifici = refComuni.child(keyCommessa).child("Edifici");
//        eRef = FirebaseDatabase.getInstance().getReference("edificio");

//         Query query = eRef.child(keyCommessa).orderByChild("key").equalTo(keyCommessa);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(null);

        FirebaseRecyclerOptions<Edificio> options
                = new FirebaseRecyclerOptions.Builder<Edificio>()
                .setQuery(refEdifici, Edificio.class)
                .build();



        adapterEdifici = new AdapterEdifici(options);
        recyclerView.setAdapter(adapterEdifici);

        addButton = findViewById(R.id.addingBtn);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EdificiActivity.this, AggiungiEdificio.class);

                keyEdificio = refEdifici.push().getKey();
//                intent.putExtra("keyCommessa", keyCommessa);
                intent.putExtra("keyEdificio", keyEdificio);
                startActivity(intent);
            }
        });

        esportaBtn = findViewById(R.id.esportaBtn);
        esportaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent intent = new Intent("android.settings.MANAGE_APP_ALL_FILES_ACCESS_PERMISSION");
//                intent.setData(Uri.parse("package:" + getPackageName()));
//                startActivity(intent);


                esportaDati();
            }
        });


    }

    private void esportaDati() {
        try {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Dati");

            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Comune");
            headerRow.createCell(1).setCellValue("Edificio");
            headerRow.createCell(2).setCellValue("Piano");
            headerRow.createCell(3).setCellValue("Lampada");


//
            DatabaseReference db = refComuni.child(keyCommessa);
            db.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot comuneSnapshot : snapshot.getChildren()) {
                        String nomeComune = comuneSnapshot.child("nome").getValue(String.class);

                        for (DataSnapshot edificioSnapshot : comuneSnapshot.child("Edifici").getChildren()) {
                            String nomeEdificio = edificioSnapshot.child("name").getValue(String.class);

                            for (DataSnapshot planimetriaSnapshot : edificioSnapshot.child("Planimetrie").getChildren()) {
                                String nomePlanimetria = planimetriaSnapshot.child("name").getValue(String.class);

                                for (DataSnapshot lampadaSnapshot : planimetriaSnapshot.child("Lampade").getChildren()) {
                                    String attacco = lampadaSnapshot.child("attacco").getValue(String.class);

                                    Row row = sheet.createRow(sheet.getLastRowNum()+1);
                                    row.createCell(0).setCellValue(nomeComune);
                                    row.createCell(1).setCellValue(nomeEdificio);
                                    row.createCell(2).setCellValue(nomePlanimetria);
                                    row.createCell(3).setCellValue(attacco);
                                    Log.d("fatto", "fffffff");
                                }
                            }
                        }
                    }

                    try {
                        FileOutputStream fileOutputStream = new FileOutputStream(getExternalFilesDir(null) + "Dati000.xlsx");
                        workbook.write(fileOutputStream);
                        Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_SHORT).show();
                        fileOutputStream.close();
                        workbook.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "NO OK", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


//    private void esportaDati() {
//        Workbook workbook = new HSSFWorkbook();
//        Cell cell = null;
//
//        Sheet sheet = null;
//        sheet = workbook.createSheet("nome");
//
//        Row row = sheet.createRow(0);
//
//        cell= row.createCell(0);
//        cell.setCellValue("Nome");
//
//        cell= row.createCell(1);
//        cell.setCellValue("Numero");
//
//        File file = new File(getExternalFilesDir(null), "Dati.xls");
//        FileOutputStream outputStream = null;
//
//        try {
//            outputStream = new FileOutputStream(file);
//            workbook.write(outputStream);
//            Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_SHORT).show();
//        } catch (IOException e) {
//            e.printStackTrace();
//            Toast.makeText(getApplicationContext(), "NO OK", Toast.LENGTH_SHORT).show();
//            try {
//                outputStream.close();
//            } catch (IOException ex) {
//                ex.printStackTrace();
//            }
//        }
////        new ExportTask().execute();
//
//    }

    private class ExportTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            try {
//                Workbook workbook = new XSSFWorkbook();
//                Sheet sheet = workbook.createSheet("Dati");
//
//                Row headerRow = sheet.createRow(0);
//                headerRow.createCell(0).setCellValue("Comune");
//                headerRow.createCell(1).setCellValue("Edificio");
//                headerRow.createCell(2).setCellValue("Piano");
//                headerRow.createCell(3).setCellValue("Lampada");
//
//                try {
//                            FileOutputStream fileOutputStream = new FileOutputStream(getExternalFilesDir(Environment.DIRECTORY_ALARMS) + "Dati.xlsx");
//                            workbook.write(fileOutputStream);
//                            fileOutputStream.close();
//                            workbook.close();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                            Log.d("dcdc", ""+ e);
//                        }

//                refComuni.child(keyCommessa).addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        for (DataSnapshot comuneSnapshot : snapshot.getChildren()) {
//                            String nomeComune = comuneSnapshot.child("nome").getValue(String.class);
//
//                            for (DataSnapshot edificioSnapshot : comuneSnapshot.child("Edifici").getChildren()) {
//                                String nomeEdificio = edificioSnapshot.child("name").getValue(String.class);
//
//                                for (DataSnapshot planimetriaSnapshot : edificioSnapshot.child("Planimetrie").getChildren()) {
//                                    String nomePlanimetria = planimetriaSnapshot.child("name").getValue(String.class);
//
//                                    for (DataSnapshot lampadaSnapshot : planimetriaSnapshot.child("Lampade").getChildren()) {
//                                        String attacco = lampadaSnapshot.child("attacco").getValue(String.class);
//
//                                        Row row = sheet.createRow(sheet.getLastRowNum() + 1);
//                                        row.createCell(0).setCellValue(nomeComune);
//                                        row.createCell(1).setCellValue(nomeEdificio);
//                                        row.createCell(2).setCellValue(nomePlanimetria);
//                                        row.createCell(3).setCellValue(attacco);
//                                    }
//                                }
//                            }
//                        }
//
//                        try {
//                            FileOutputStream fileOutputStream = new FileOutputStream(getExternalFilesDir(null) + "/Dati.xlsx");
//                            workbook.write(fileOutputStream);
//                            fileOutputStream.close();
//                            workbook.close();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("dcdc", "2222----" + e);
            }
            return null;
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        adapterEdifici.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapterEdifici.startListening();
    }
}
