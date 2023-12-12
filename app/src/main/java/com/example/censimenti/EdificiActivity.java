package com.example.censimenti;

import static com.example.censimenti.AdapterComuni.refComuni;
import static com.example.censimenti.AdapterEdifici.refEdifici;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class EdificiActivity extends AppCompatActivity {
    private RecyclerView recyclerView;

    private AdapterEdifici adapterEdifici;
    FloatingActionButton addButton, esportaBtn;
    String keyCommessa, keyEdificio;
    ImageView refresh;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edifici);

        recyclerView = findViewById(R.id.edificiRecycler);
        keyCommessa = getIntent().getStringExtra("key");
        refEdifici = refComuni.child(keyCommessa).child("Edifici");



        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(null);

        FirebaseRecyclerOptions<Edificio> options
                = new FirebaseRecyclerOptions.Builder<Edificio>()
                .setQuery(refEdifici, Edificio.class)
                .build();



        adapterEdifici = new AdapterEdifici(options);
        recyclerView.setAdapter(adapterEdifici);

        refresh = findViewById(R.id.refreshEdifici);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EdificiActivity.this, EdificiActivity.class);
                intent.putExtra("key", keyCommessa);
                startActivity(intent);
                finish();
            }
        });

        addButton = findViewById(R.id.addingBtn);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EdificiActivity.this, AggiungiEdificio.class);

                keyEdificio = refEdifici.push().getKey();
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
            headerRow.createCell(4).setCellValue("Numero");
            headerRow.createCell(5).setCellValue("Tipo");
            headerRow.createCell(6).setCellValue("Nome");
            headerRow.createCell(7).setCellValue("Potenza");
            headerRow.createCell(8).setCellValue("Sorgente");
            headerRow.createCell(9).setCellValue("Attacco");
            headerRow.createCell(10).setCellValue("Installazione");
            headerRow.createCell(11).setCellValue("Foto");

            Row headerRow1 = sheet1.createRow(0);
            headerRow1.createCell(0).setCellValue("Comune");
            headerRow1.createCell(1).setCellValue("Edificio");
            headerRow1.createCell(2).setCellValue("Piano");
            headerRow1.createCell(3).setCellValue("Locale");
            headerRow1.createCell(4).setCellValue("Note");
            headerRow1.createCell(5).setCellValue("Foto");

            refComuni.addListenerForSingleValueEvent(new ValueEventListener() {
                @RequiresApi(api = Build.VERSION_CODES.R)
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
                                        String foto = lampadaSnapshot.child("foto").getValue(String.class);
                                        Long id = lampadaSnapshot.child("id").getValue(Long.class);

                                        Row row = sheet.createRow(sheet.getLastRowNum()+1);
                                        row.createCell(0).setCellValue(nomeComune);
                                        row.createCell(1).setCellValue(nomeEdificio);
                                        row.createCell(2).setCellValue(nomePlanimetria);
                                        row.createCell(3).setCellValue(locale);
                                        row.createCell(4).setCellValue(id);
                                        row.createCell(5).setCellValue(tipo);
                                        row.createCell(6).setCellValue(nome);
                                        row.createCell(7).setCellValue(potenza);
                                        row.createCell(8).setCellValue(sorgente);
                                        row.createCell(9).setCellValue(attacco);
                                        row.createCell(10).setCellValue(installazione);
//                                        row.createCell(11).setCellValue(foto);
                                        Cell cell = row.createCell(11);
                                        cell.setCellValue(foto);
                                        CreationHelper createHelper = workbook.getCreationHelper();
                                        Hyperlink hyperlink = createHelper.createHyperlink(HyperlinkType.URL);
                                        hyperlink.setAddress(foto);

                                        // Applica il collegamento alla cella
                                        cell.setHyperlink(hyperlink);

                                        // Imposta il formato della cella come link
                                        CellStyle linkStyle = workbook.createCellStyle();
                                        Font font = workbook.createFont();
                                        font.setUnderline(Font.U_SINGLE);
                                        font.setColor(IndexedColors.BLUE.getIndex());
                                        linkStyle.setFont(font);
                                        cell.setCellStyle(linkStyle);



                                    }

                                    for (DataSnapshot localeSnapshot : planimetriaSnapshot.child("Locali").getChildren()) {
                                        String locale = localeSnapshot.child("nome").getValue(String.class);
                                        String note = localeSnapshot.child("note").getValue(String.class);
                                        String foto = localeSnapshot.child("foto").getValue(String.class);


                                        Row row1 = sheet1.createRow(sheet1.getLastRowNum()+1);
                                        row1.createCell(0).setCellValue(nomeComune);
                                        row1.createCell(1).setCellValue(nomeEdificio);
                                        row1.createCell(2).setCellValue(nomePlanimetria);
                                        row1.createCell(3).setCellValue(locale);
                                        row1.createCell(4).setCellValue(note);
                                        row1.createCell(5).setCellValue(foto);

                                    }
                                }
                            }
                        }
                    }

                    StorageManager storageManager = (StorageManager) getSystemService(STORAGE_SERVICE);
                    StorageVolume storageVolume = storageManager.getStorageVolumes().get(0);
//                    File file = null;

//                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                        File file = new File(getApplicationContext().getExternalFilesDir("Download"),  "DatiExcel.xlsx");
                        Log.d("azaz", ""+getApplicationContext().getExternalFilesDir("Download"));
//                    }

                    try {
                        FileOutputStream fileOutputStream = new FileOutputStream(file);
                        workbook.write(fileOutputStream);
                        fileOutputStream.close();
                        workbook.close();
                        Toast.makeText(getApplicationContext(), "File Creato Con Successo!", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        Log.e("azaz", ""+e.getMessage());
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
    protected void onRestart() {
        super.onRestart();
        FirebaseRecyclerOptions<Edificio> options
                = new FirebaseRecyclerOptions.Builder<Edificio>()
                .setQuery(refEdifici, Edificio.class)
                .build();
        adapterEdifici = new AdapterEdifici(options);
        recyclerView.setAdapter(adapterEdifici);

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
