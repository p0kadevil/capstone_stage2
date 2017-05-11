package com.p0kadevil.diabetesdiary.ui;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.p0kadevil.diabetesdiary.R;
import com.p0kadevil.diabetesdiary.db.DiabetesDbContract;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;


public class HomeFragment extends Fragment {

    final private int REQUEST_CODE_ASK_PERMISSIONS = 88;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        AdView mAdView = (AdView) view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        view.findViewById(R.id.btn_newEntry).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment newFragment = new NewEntryFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fl_container, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        view.findViewById(R.id.btn_diary).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment newFragment = new DiaryFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fl_container, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        view.findViewById(R.id.btn_export).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //To create the PDF, the tutorial at
                //https://www.androidtutorialpoint.com/basics/android-pdf-creator-tutorial/
                //was used to get an idea of it
                //--> The PDF itself, I have to generate, because in the Tutorial it is only a simple text
                try
                {
                    createPdfAndShow();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), getString(R.string.error_pdf), Toast.LENGTH_LONG).show();
                }
            }
        });

        view.findViewById(R.id.fab_prefs).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PrefFragment newFragment = new PrefFragment();
                android.app.FragmentTransaction transaction = getActivity().getFragmentManager().beginTransaction();
                transaction.replace(R.id.fl_container, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        ((AppCompatActivity)getActivity()).getSupportActionBar().
                    setTitle(getString(R.string.app_name));
    }

    private void createPdfAndShow() throws Exception
    {
        int hasWriteStoragePermission = ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (hasWriteStoragePermission != PackageManager.PERMISSION_GRANTED)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_CONTACTS)) {
                    showAlertWithOKCancel(getString(R.string.need_access_external_storage),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                REQUEST_CODE_ASK_PERMISSIONS);
                                    }
                                }
                            });
                    return;
                }

                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_ASK_PERMISSIONS);
            }
        }
        else
        {
            createPdf();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:

                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    try
                    {
                        createPdfAndShow();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
                else
                {
                    Toast.makeText(getActivity(), getString(R.string.error_pdf), Toast.LENGTH_SHORT)
                            .show();
                }

                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void showAlertWithOKCancel(String message, DialogInterface.OnClickListener okListener)
    {
        new AlertDialog.Builder(getActivity())
                .setMessage(message)
                .setPositiveButton(getString(R.string.ok), okListener)
                .setNegativeButton(getString(R.string.cancel), null)
                .create()
                .show();
    }

    private void showAlertWithOK(String message)
    {
        new AlertDialog.Builder(getActivity())
                .setMessage(message)
                .setPositiveButton(getString(R.string.ok), null)
                .create()
                .show();
    }

    private void createPdf() throws Exception
    {
        File docsFolder = new File(Environment.getExternalStorageDirectory() + "/Documents");

        if (!docsFolder.exists())
        {
            docsFolder.mkdir();
        }

        File pdfFile = new File(docsFolder.getAbsolutePath(), "diabetes_diary.pdf");

        if(pdfFile.exists())
        {
            pdfFile.delete();
        }

        OutputStream output = new FileOutputStream(pdfFile);
        Document document = new Document();
        PdfWriter.getInstance(document, output);
        document.open();

        Cursor cursor = DiabetesDbContract.getAllEntries(getContext());

        while(cursor.moveToNext())
        {
            String dateTime = cursor.getString(cursor.getColumnIndex(DiabetesDbContract.DiaryEntry.COLUMN_NAME_DATE));
            String be = cursor.getString(cursor.getColumnIndex(DiabetesDbContract.DiaryEntry.COLUMN_NAME_BE));
            String blood = cursor.getString(cursor.getColumnIndex(DiabetesDbContract.DiaryEntry.COLUMN_NAME_BLOOD));
            String insulin = cursor.getString(cursor.getColumnIndex(DiabetesDbContract.DiaryEntry.COLUMN_NAME_INSULIN));

            document.add(new Paragraph(
                    dateTime + ", " + getString(R.string.be) + ": " + be +
                            ", " + getString(R.string.blood_value) + ": " + blood +
                            ", " + getString(R.string.insulin) + ": " + insulin
            ));
        }

        cursor.close();
        document.close();
        previewPdf(pdfFile);
    }

    private void previewPdf(File pdfFile)
    {
        PackageManager packageManager = getActivity().getPackageManager();
        Intent testIntent = new Intent(Intent.ACTION_VIEW);
        testIntent.setType("application/pdf");
        List list = packageManager.queryIntentActivities(testIntent, PackageManager.MATCH_DEFAULT_ONLY);

        if (list.size() > 0)
        {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            Uri uri = Uri.fromFile(pdfFile);
            intent.setDataAndType(uri, "application/pdf");
            startActivity(intent);

            Toast.makeText(getActivity(), getString(R.string.export_success), Toast.LENGTH_LONG).show();
        }
        else
        {
            showAlertWithOK(getString(R.string.export_success_need_pdf_viewer));
        }
    }
}
