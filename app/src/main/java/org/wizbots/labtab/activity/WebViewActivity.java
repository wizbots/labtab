package org.wizbots.labtab.activity;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.github.barteksc.pdfviewer.listener.OnRenderListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.shockwave.pdfium.PdfDocument;

import org.wizbots.labtab.LabTabConstants.Screens;
import org.wizbots.labtab.R;
import org.wizbots.labtab.customview.LabTabHeaderLayout;

import java.io.File;
import java.util.List;

public class WebViewActivity extends AppCompatActivity implements OnPageChangeListener, OnLoadCompleteListener,
        OnPageErrorListener, OnRenderListener {

    private static final String TAG = WebViewActivity.class.getSimpleName();

    private LabTabHeaderLayout labTabHeaderLayout;
    private Toolbar toolbar;
    private FrameLayout mProgressBar;
    PDFView pdfView;
    Integer pageNumber = 0;
    String fileName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binder_web_view);
//        initView();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mProgressBar = (FrameLayout) findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.VISIBLE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent intent = getIntent();

        fileName = intent.getStringExtra("path");
        //retrieve filename from intent
        pdfView = (PDFView) findViewById(R.id.pdfView);
        pdfView.setBackgroundColor(Color.LTGRAY);

        if (intent.getStringExtra(Screens.FROM_SCREEN).equalsIgnoreCase(Screens.ROSTER_DETAILS)) {
            displayFromFilePath(fileName);
            String rosterTitle = fileName.substring(fileName.indexOf("LabTabPdf/")+10);
            setTitle(rosterTitle);
            toolbar.setTitle(rosterTitle);
        } else {
            displayFromAsset(fileName);
            setTitle(fileName);
            toolbar.setTitle(fileName);
        }
    }


    private void displayFromAsset(String assetFileName) {
       String filePath = "pdfs/"+ assetFileName + ".pdf";

        pdfView.fromAsset(filePath)
                .defaultPage(pageNumber)
                .onPageChange(this)
                .enableAnnotationRendering(true)
                .onLoad(this)
                .scrollHandle(new DefaultScrollHandle(this))
                .spacing(10) // in dp
                .onPageError(this)
                .onRender(this)
                .load();
    }

    private void displayFromFilePath(String assetFileName) {
        String filePath = assetFileName + ".pdf";


        pdfView.fromFile(new File(filePath))
                .defaultPage(pageNumber)
                .onPageChange(this)
                .enableAnnotationRendering(true)
                .onLoad(this)
                .scrollHandle(new DefaultScrollHandle(this))
                .spacing(10) // in dp
                .onPageError(this)
                .onRender(this)
                .load();
    }


    @Override
    public void onPageChanged(int page, int pageCount) {
        pageNumber = page;
        setTitle(String.format("%s %s / %s", fileName, page + 1, pageCount));
    }


    @Override
    public void loadComplete(int nbPages) {
        PdfDocument.Meta meta = pdfView.getDocumentMeta();
        Log.e(TAG, "title = " + meta.getTitle());
        Log.e(TAG, "author = " + meta.getAuthor());
        Log.e(TAG, "subject = " + meta.getSubject());
        Log.e(TAG, "keywords = " + meta.getKeywords());
        Log.e(TAG, "creator = " + meta.getCreator());
        Log.e(TAG, "producer = " + meta.getProducer());
        Log.e(TAG, "creationDate = " + meta.getCreationDate());
        Log.e(TAG, "modDate = " + meta.getModDate());

        printBookmarksTree(pdfView.getTableOfContents(), "-");

    }

    public void printBookmarksTree(List<PdfDocument.Bookmark> tree, String sep) {
        for (PdfDocument.Bookmark b : tree) {

            Log.e(TAG, String.format("%s %s, p %d", sep, b.getTitle(), b.getPageIdx()));

            if (b.hasChildren()) {
                printBookmarksTree(b.getChildren(), sep + "-");
            }
        }
    }


    @Override
    public void onPageError(int page, Throwable t) {
        Log.e(TAG, "Cannot load page " + page);
    }

    public void initView() {
        toolbar = (Toolbar) findViewById(R.id.tool_bar_lab_tab);
        labTabHeaderLayout = (LabTabHeaderLayout) toolbar.findViewById(R.id.lab_tab_header_layout);
        labTabHeaderLayout.getDynamicTextViewCustom().setText(R.string.BINDER);
        labTabHeaderLayout.getMenuImageView().setVisibility(View.VISIBLE);
        labTabHeaderLayout.getMenuImageView().setImageResource(R.drawable.ic_menu);
        labTabHeaderLayout.getSyncImageView().setImageResource(R.drawable.ic_synced);
    }

    @Override
    public void onInitiallyRendered(int nbPages, float pageWidth, float pageHeight) {
        mProgressBar.setVisibility(View.GONE);
    }
}