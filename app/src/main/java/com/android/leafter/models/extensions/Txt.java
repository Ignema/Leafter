package com.android.leafter.models.extensions;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.android.leafter.models.Book;
import com.android.leafter.models.Metadata;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Txt extends Book {
    private final List<String> l = new ArrayList<>();

    private final Map<String,String> toc = new LinkedHashMap<>();

    public Txt(Context context) {
        super(context);
    }

    @Override
    protected void load() throws IOException {
        if (!getFile().exists() || !getFile().canRead()) {
            throw new FileNotFoundException(getFile() + " doesn't exist or not readable");
        }
        File outFile = getBookFile();

        if (!outFile.exists()) {

            try (BufferedReader reader = new BufferedReader(new FileReader(getFile()))) {
                try (Writer out = new FileWriter(outFile)) {
                    StringBuilder para = new StringBuilder(4096);
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (line.matches("^\\s*$")) {
                            para.append(System.lineSeparator());
                            para.append(System.lineSeparator());
                            out.write(para.toString());
                            para.delete(0, para.length());
                        } else {
                            para.append(line);
                            if (!line.matches(".*\\s+$")) {
                                para.append(" ");
                            }
                            //if (line.matches("[.?!\"]\\s*$")) {
                            //    para.append(System.lineSeparator());
                            //}
                        }
                    }
                }
            }
        }

    }


    private File getBookFile() {
        return new File(getThisBookDir(), getFile().getName());
    }

    @Override
    public Map<String, String> getToc() {
        return toc;
    }

    @Override
    protected Metadata getMetaData() throws IOException {
        Metadata metadata = new Metadata();

        metadata.setFilename(getFile().getPath());
        metadata.setAuthor(android.os.Build.MODEL);
        metadata.setTitle(getFile().getName().substring(0, getFile().getName().length() - 4));

        return metadata;
    }

    @Override
    protected List<String> getSectionIds() {
        l.add("1");
        return l;
    }

    @Override
    protected Uri getUriForSectionID(String id) {
        return Uri.fromFile(getBookFile());
    }

    protected ReadPoint locateReadPoint(String section) {
        ReadPoint readPoint = new ReadPoint();
        readPoint.setId("1");
        readPoint.setPoint(Uri.parse(section));
        return readPoint;
    }



//    @Override
//    protected Uri getUriForSection(String section) {
//        return Uri.fromFile(getBookFile());
//    }
//
//    @Override
//    protected String getSectionIDForSection(String section) {
//        return "1";
//    }



}
