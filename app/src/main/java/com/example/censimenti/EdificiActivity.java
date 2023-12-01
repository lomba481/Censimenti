package com.example.censimenti;

import static com.example.censimenti.AdapterComuni.refComuni;
import static com.example.censimenti.AdapterEdifici.refEdifici;
import static com.example.censimenti.ComuniActivity.storageComuni;

import android.content.Intent;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
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
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class EdificiActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    static StorageReference storageEdifici;
    private AdapterEdifici adapterEdifici;
    FloatingActionButton addButton, esportaBtn;
    String keyCommessa, keyEdificio;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edifici);

        recyclerView = findViewById(R.id.edificiRecycler);
        keyCommessa = getIntent().getStringExtra("key");
        refEdifici = refComuni.child(keyCommessa).child("Edifici");
        storageEdifici = storageComuni.child(keyCommessa).child("Edifici");
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

                Toast.makeText(getApplicationContext(), "Sto esportando in formato .xslx", Toast.LENGTH_LONG).show();
                esportaDati();
            }
        });


    }

    private void esportaDati() {

        try {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Lampade");
            Sheet sheet1 = workbook.createSheet("Locali");

            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Comune");
            headerRow.createCell(1).setCellValue("Edificio");
            headerRow.createCell(2).setCellValue("Piano");
            headerRow.createCell(3).setCellValue("Locale");
            headerRow.createCell(4).setCellValue("Tipo");
            headerRow.createCell(5).setCellValue("Nome");
            headerRow.createCell(6).setCellValue("Potenza");
            headerRow.createCell(7).setCellValue("Sorgente");
            headerRow.createCell(8).setCellValue("Attacco");
            headerRow.createCell(9).setCellValue("Installazione");

            Row headerRow1 = sheet1.createRow(0);
            headerRow1.createCell(0).setCellValue("Comune");
            headerRow1.createCell(1).setCellValue("Edificio");
            headerRow1.createCell(2).setCellValue("Piano");
            headerRow1.createCell(3).setCellValue("Locale");
            headerRow1.createCell(4).setCellValue("Note");

            refComuni.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot comuneSnapshot : snapshot.getChildren()) {
                        String chiave = comuneSnapshot.getKey();

                        if (chiave.equals(keyCommessa)) {
                            String nomeComune = comuneSnapshot.child("nome").getValue(String.class);

                            for (DataSnapshot edificioSnapshot : comuneSnapshot.child("Edifici").getChildren()) {
                                String nomeEdificio = edificioSnapshot.child("nome").getValue(String.class);

                                for (DataSnapshot planimetriaSnapshot : edificioSnapshot.child("Planimetrie").getChildren()) {
                                    String nomePlanimetria = planimetriaSnapshot.child("nome").getValue(String.class);

                                    for (DataSnapshot lampadaSnapshot : planimetriaSnapshot.child("Lampade").getChildren()) {
                                        String locale = lampadaSnapshot.child("locale").getValue(String.class);
                                        String tipo = lampadaSnapshot.child("tipo").getValue(String.class);
                                        String nome = lampadaSnapshot.child("nome").getValue(String.class);
                                        String potenza = lampadaSnapshot.child("potenza").getValue(String.class);
                                        String sorgente = lampadaSnapshot.child("sorgente").getValue(String.class);
                                        String attacco = lampadaSnapshot.child("attacco").getValue(String.class);
                                        String installazione = lampadaSnapshot.child("installazione").getValue(String.class);

                                        Row row = sheet.createRow(sheet.getLastRowNum()+1);
                                        row.createCell(0).setCellValue(nomeComune);
                                        row.createCell(1).setCellValue(nomeEdificio);
                                        row.createCell(2).setCellValue(nomePlanimetria);
                                        row.createCell(3).setCellValue(locale);
                                        row.createCell(4).setCellValue(tipo);
                                        row.createCell(5).setCellValue(nome);
                                        row.createCell(6).setCellValue(potenza);
                                        row.createCell(7).setCellValue(sorgente);
                                        row.createCell(8).setCellValue(attacco);
                                        row.createCell(9).setCellValue(installazione);

                                    }

                                    for (DataSnapshot localeSnapshot : planimetriaSnapshot.child("Locali").getChildren()) {
                                        String locale = localeSnapshot.child("nome").getValue(String.class);
                                        String note = localeSnapshot.child("note").getValue(String.class);


                                        Row row1 = sheet1.createRow(sheet1.getLastRowNum()+1);
                                        row1.createCell(0).setCellValue(nomeComune);
                                        row1.createCell(1).setCellValue(nomeEdificio);
                                        row1.createCell(2).setCellValue(nomePlanimetria);
                                        row1.createCell(3).setCellValue(locale);
                                        row1.createCell(4).setCellValue(note);
                                    }
                                }
                            }
                        }
                    }

                    StorageManager storageManager = (StorageManager) getSystemService(STORAGE_SERVICE);
                    StorageVolume storageVolume = storageManager.getStorageVolumes().get(0);
                    File file = null;

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                        file = new File(storageVolume.getDirectory().getPath() + "/Download/DatiExcel.xlsx");
                    }

                    try {
                        FileOutputStream fileOutputStream = new FileOutputStream(file);
                        workbook.write(fileOutputStream);
                        fileOutputStream.close();
                        workbook.close();
                        Toast.makeText(getApplicationContext(), "File Creato Con Successo", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Creazione File Fallita", Toast.LENGTH_SHORT).show();
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
