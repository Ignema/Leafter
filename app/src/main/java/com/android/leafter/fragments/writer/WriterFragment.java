package com.android.leafter.fragments.writer;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.leafter.R;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class WriterFragment extends Fragment {

    Button save;
    TextView title;
    EditText story;

    String filepath;


    public WriterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_writer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        save = requireView().findViewById(R.id.save_button);
        title = requireView().findViewById(R.id.writer_title);
        story = requireView().findViewById(R.id.textArea_story);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    filepath = Environment.getExternalStorageDirectory()+"/Leafter/"+ title.getText().toString() +".pdf";
                    save();
                } else {
                    requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }
            }
        });
    }

    void save(){
        File folder = new File(Environment.getExternalStorageDirectory() + File.separator + "Leafter");
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdirs();
        }

        if (success) {
            Document doc = new Document();
            try {
                PdfWriter.getInstance(doc, new FileOutputStream(filepath));
                doc.open();
                doc.addTitle(title.getText().toString());
                doc.add(new Paragraph(story.getText().toString()));
            } catch (DocumentException | FileNotFoundException e) {
                e.printStackTrace();
            }

//            try {
//                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(new File(filepath)));
//                outputStreamWriter.write(story.getText().toString());
//                outputStreamWriter.close();
//            }
//            catch (IOException e) {
//                Log.e("Exception", "File write failed: " + e.toString());
//            }

//                try {
//                    // Create new Book
//                    Book book = new Book();
//                    Metadata metadata = book.getMetadata();
//
//                    metadata.addTitle(title.getText().toString());
//                    metadata.addAuthor(new Author(android.os.Build.MODEL));
//
//                    // Add Chapter 1
//                    book.addSection(title.getText().toString(), new Resource(story.getText().toString()));
//
//                    EpubWriter epubWriter = new EpubWriter();
//                    epubWriter.write(book, new FileOutputStream(filepath));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }


            Log.d("TITLE", title.getText().toString());
            Log.d("STORY", story.getText().toString());
            Log.d("FILEPATH", filepath);

            Toast.makeText(getContext(),"Saved",Toast.LENGTH_SHORT).show();
            title.setText("");
            story.setText("");
            doc.close();
        } else {
            Toast.makeText(getContext(),"Can't create Leafter folder in internal storage",Toast.LENGTH_SHORT).show();
        }
    }

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    filepath = Environment.getExternalStorageDirectory()+"/Leafter/"+ title.getText().toString() +".pdf";
                    save();
                } else {
                    Toast.makeText(getContext(),"Can't save without permission!",Toast.LENGTH_SHORT).show();
                }
            });
}