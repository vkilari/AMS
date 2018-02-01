package com.mobisolutions.ams.maintenance;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.text.Line;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itextpdf.text.Anchor;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.mobisolutions.ams.BaseActivity;
import com.mobisolutions.ams.R;
import com.mobisolutions.ams.contacts.Contacts;
import com.mobisolutions.ams.utils.CommonUtils;

import org.json.JSONArray;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static com.amazonaws.mobileconnectors.pinpoint.internal.core.util.Preconditions.checkArgument;

/**
 * Created by vkilari on 12/5/17.
 */

public class GeneralMaintenanceActivity extends BaseActivity implements GetGeneralMaintenanceCount, RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    private static final String TAG = GeneralMaintenanceActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private List<MaintenanceItem> cartList;
    private List<Contacts> contactsList;
    private GeneralMaintenanceAdapter mAdapter;
    private IndividualMaintenanceAdapter mIndividualAdapter;
    private Context mCtx;
    private Activity mActivity;
    List<MaintenanceItem> items;
    EditText totalGenrlMnt;
    private TextView year, month, day;
    private SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
    private FrameLayout maintenance_layout, individual_maintenance_layout;
    private LinearLayout total_layout, send_sms_layout;
    private double totalMaintenanceAmount;
    double individualMaintenanceAmount;

    private static DecimalFormat decimalFormat = new DecimalFormat(".##");

    int contactsLength = 34;

    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
            Font.BOLD);
    private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.NORMAL, BaseColor.RED);
    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
            Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.BOLD);


    String[] defaultMaintainrows = {"Watchmen Salary", "Electricity Bill", "Diesel", "Elevator Mnt", "Manjeera+others", "Common Water"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_general_maintenance);


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        year = (TextView) findViewById(R.id.year_label);
        month = (TextView) findViewById(R.id.month_label);
        day = (TextView) findViewById(R.id.day_label);
        maintenance_layout = (FrameLayout) findViewById(R.id.maintenance_layout);
        individual_maintenance_layout = (FrameLayout) findViewById(R.id.individual_maintenance_layout);
        total_layout =  (LinearLayout) findViewById(R.id.total_layout);
        send_sms_layout =  (LinearLayout) findViewById(R.id.send_sms_layout);

        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        int currentYear = calendar.get(Calendar.YEAR);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        String currentMonth = month_date.format(calendar.getTime());

        maintenance_layout.setVisibility(View.VISIBLE);
        individual_maintenance_layout.setVisibility(View.GONE);

        total_layout.setVisibility(View.VISIBLE);
        send_sms_layout.setVisibility(View.GONE);


        Log.d("TAG", "::::::"+currentYear+""+currentDay+""+currentMonth);

        String suffix = getDayOfMonthSuffix(currentDay);

        year.setText("Year: "+currentYear);
        month.setText("Month: "+currentMonth);
        day.setText("Day: "+currentDay+""+suffix);

        cartList = new ArrayList<>();

        mCtx = this;
        mActivity = this;
        contactsList = new ArrayList<>();
        mAdapter = new GeneralMaintenanceAdapter(this, cartList);
        mAdapter.setCountListener(this);



        for (int i =0; i<contactsLength; i++) {

            Contacts contacts = new Contacts();
            contacts.setFlatNo(20+""+i);
            contacts.setName("Venkat"+i);
            contactsList.add(contacts);
        }


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);

        totalGenrlMnt = (EditText)findViewById(R.id.general_maintain_value);

        // adding item touch helper
        // only ItemTouchHelper.LEFT added to detect Right to Left swipe
        // if you want both Right -> Left and Left -> Right
        // add pass ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT as param
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);


        // making http call and fetching menu json
        prepareCart();


        ItemTouchHelper.SimpleCallback itemTouchHelperCallback1 = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.UP) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // Row is swiped from recycler view
                // remove it from adapter
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };


        mAdapter.setOnItemClickListener(new GeneralMaintenanceAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Log.d(TAG, "onItemClick position: " + position);

                showDialog(GeneralMaintenanceActivity.this, defaultMaintainrows[position], position);
            }

            @Override
            public void onItemLongClick(int position, View v) {
                Log.d(TAG, "onItemLongClick pos = " +position);
            }
        });


        // attaching the touch helper to recycler view
        new ItemTouchHelper(itemTouchHelperCallback1).attachToRecyclerView(recyclerView);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_general_maintenance);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showDialog(GeneralMaintenanceActivity.this);

            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.individual) {

            maintenance_layout.setVisibility(View.GONE);
            individual_maintenance_layout.setVisibility(View.VISIBLE);

            total_layout.setVisibility(View.GONE);
            send_sms_layout.setVisibility(View.VISIBLE);

            RecyclerView individual_maintenance_recycler_view = (RecyclerView) findViewById(R.id.individual_maintenance_recycler_view);

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
            individual_maintenance_recycler_view.setLayoutManager(mLayoutManager);
            individual_maintenance_recycler_view.setItemAnimator(new DefaultItemAnimator());
            individual_maintenance_recycler_view.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));


            individualMaintenanceAmount = totalMaintenanceAmount/contactsLength;



            mIndividualAdapter = new IndividualMaintenanceAdapter(this, contactsList, decimalFormat.format(individualMaintenanceAmount));

            individual_maintenance_recycler_view.setAdapter(mIndividualAdapter);


            return true;
        } if (id == R.id.general) {

            maintenance_layout.setVisibility(View.VISIBLE);
            individual_maintenance_layout.setVisibility(View.GONE);
            total_layout.setVisibility(View.VISIBLE);
            send_sms_layout.setVisibility(View.GONE);


            return true;
        }

        if (id == R.id.export) {

            Log.d("TAG", "-----PDF Export-------");

            createPdf();

            String root = Environment.getExternalStorageDirectory().toString();

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("application/pdf");
            shareIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { "abc@gmail.com" });
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "test PDF");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "test");
            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(root+"/test.pdf"));
            startActivity(shareIntent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void getTotal(int position, double total) {

        Log.d("TAG", "-----getTotal-------" +position+":::::::::::::::"+total);

        totalMaintenanceAmount = total;
        totalGenrlMnt.setText(""+total);
    }


    private void prepareCart() {


        items= new ArrayList<MaintenanceItem>();
        for (int i = 0; i<defaultMaintainrows.length; i++) {

            MaintenanceItem maintenanceItem = new MaintenanceItem();
            maintenanceItem.setName(defaultMaintainrows[i]);
            maintenanceItem.setDescription("Add Description");
            items.add(maintenanceItem);
        }
        cartList.addAll(items);

        mAdapter.notifyDataSetChanged();

    }

    /**
     * callback when recycler view is swiped
     * item will be removed on swiped
     * undo option will be provided in snackbar to restore the item
     */
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof GeneralMaintenanceAdapter.MyViewHolder) {
            // get the removed item name to display it in snack bar
            String name = cartList.get(viewHolder.getAdapterPosition()).getName();

            // backup of removed item for undo purpose
            final MaintenanceItem deletedItem = cartList.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            mAdapter.removeItem(viewHolder.getAdapterPosition());

            // showing snack bar with Undo option
//            Snackbar snackbar = Snackbar
//                    .make(coordinatorLayout, name + " removed from cart!", Snackbar.LENGTH_LONG);
//            snackbar.setAction("UNDO", new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    // undo is selected, restore the deleted item
//                    mAdapter.restoreItem(deletedItem, deletedIndex);
//                }
//            });
//            snackbar.setActionTextColor(Color.YELLOW);
//            snackbar.show();
        }
    }


    public void showDialog(Activity activity){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_single_button_edittext);

        final EditText text = (EditText) dialog.findViewById(R.id.add_record);
        final EditText description = (EditText) dialog.findViewById(R.id.add_description);

        Button dialogButton = (Button) dialog.findViewById(R.id.positive_button);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                CommonUtils.hideSoftKeyboard(mActivity);

                MaintenanceItem maintenanceItem = new MaintenanceItem();
                maintenanceItem.setName(text.getText().toString());
                maintenanceItem.setDescription(description.getText().toString());
                items.add(maintenanceItem);

                cartList.clear();
                cartList.addAll(items);

                mAdapter.notifyDataSetChanged();

            }
        });

        dialog.show();

    }

    public void showDialog(Activity activity, final String itemName, final int itemPosition){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_single_button_edittext);

        final EditText text = (EditText) dialog.findViewById(R.id.add_record);
        final EditText description = (EditText) dialog.findViewById(R.id.add_description);
        text.setText(itemName);
        description.setText("Add Description");

        Button dialogButton = (Button) dialog.findViewById(R.id.positive_button);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                CommonUtils.hideSoftKeyboard(mActivity);

                Log.d("TAG", "get Item:"+mAdapter.getItem(itemPosition)+" :::"+text.getText().toString());

                mAdapter.getItem(itemPosition).setName(text.getText().toString());
                mAdapter.getItem(itemPosition).setDescription(description.getText().toString());

                mAdapter.notifyDataSetChanged();

            }
        });

        dialog.show();

    }

    String getDayOfMonthSuffix(final int n) {
        checkArgument(n >= 1 && n <= 31, "illegal day of month: " + n);
        if (n >= 11 && n <= 13) {
            return "th";
        }
        switch (n % 10) {
            case 1:  return "st";
            case 2:  return "nd";
            case 3:  return "rd";
            default: return "th";
        }
    }

    public void createPdf() {

       // http://www.vogella.com/tutorials/JavaPDF/article.html

        try {
            String fname="test.pdf";
            String root = Environment.getExternalStorageDirectory().toString();
            File file = new File (root, fname);

            FileOutputStream  output=new FileOutputStream(file);


            Document document = new Document();
            PdfWriter.getInstance(document, output);
            document.open();
            addMetaData(document);
            addTitlePage(document);
            addContent(document);
            document.close();


        }catch (Exception e) {
            Log.e(TAG, "Exception while generating pdf::"+e.getMessage());
        }


    }


    private static void addMetaData(Document document) {
        document.addTitle("My first PDF");
        document.addSubject("Using iText");
        document.addKeywords("Java, PDF, iText");
        document.addAuthor("Lars Vogel");
        document.addCreator("Lars Vogel");
    }

    private static void addTitlePage(Document document)
            throws DocumentException {
        Paragraph preface = new Paragraph();
        // We add one empty line
        addEmptyLine(preface, 1);
        // Lets write a big header
        preface.add(new Paragraph("Title of the document", catFont));

        addEmptyLine(preface, 1);
        // Will create: Report generated by: _name, _date
        preface.add(new Paragraph(
                "Report generated by: " + System.getProperty("user.name") + ", " + new Date(), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                smallBold));
        addEmptyLine(preface, 3);
        preface.add(new Paragraph(
                "This document describes something which is very important ",
                smallBold));

        addEmptyLine(preface, 8);

        preface.add(new Paragraph(
                "This document is a preliminary version and not subject to your license agreement or any other agreement with vogella.com ;-).",
                redFont));

        document.add(preface);
        // Start a new page
        document.newPage();
    }

    private static void addContent(Document document) throws DocumentException {
        Anchor anchor = new Anchor("First Chapter", catFont);
        anchor.setName("First Chapter");

        // Second parameter is the number of the chapter
        Chapter catPart = new Chapter(new Paragraph(anchor), 1);

        Paragraph subPara = new Paragraph("Subcategory 1", subFont);
        Section subCatPart = catPart.addSection(subPara);
        subCatPart.add(new Paragraph("Hello"));

        subPara = new Paragraph("Subcategory 2", subFont);
        subCatPart = catPart.addSection(subPara);
        subCatPart.add(new Paragraph("Paragraph 1"));
        subCatPart.add(new Paragraph("Paragraph 2"));
        subCatPart.add(new Paragraph("Paragraph 3"));

        // add a list
        createList(subCatPart);
        Paragraph paragraph = new Paragraph();
        addEmptyLine(paragraph, 5);
        subCatPart.add(paragraph);

        // add a table
        createTable(subCatPart);

        // now add all this to the document
        document.add(catPart);

        // Next section
        anchor = new Anchor("Second Chapter", catFont);
        anchor.setName("Second Chapter");

        // Second parameter is the number of the chapter
        catPart = new Chapter(new Paragraph(anchor), 1);

        subPara = new Paragraph("Subcategory", subFont);
        subCatPart = catPart.addSection(subPara);
        subCatPart.add(new Paragraph("This is a very important message"));

        // now add all this to the document
        document.add(catPart);

    }

    private static void createTable(Section subCatPart)
            throws BadElementException {
        PdfPTable table = new PdfPTable(3);

        // t.setBorderColor(BaseColor.GRAY);
        // t.setPadding(4);
        // t.setSpacing(4);
        // t.setBorderWidth(1);

        PdfPCell c1 = new PdfPCell(new Phrase("Table Header 1"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Table Header 2"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Table Header 3"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        table.setHeaderRows(1);

        table.addCell("1.0");
        table.addCell("1.1");
        table.addCell("1.2");
        table.addCell("2.1");
        table.addCell("2.2");
        table.addCell("2.3");

        subCatPart.add(table);

    }


    private static void createList(Section subCatPart) {
        com.itextpdf.text.List list = new com.itextpdf.text.List(true, false, 10);
        list.add(new ListItem("First point"));
        list.add(new ListItem("Second point"));
        list.add(new ListItem("Third point"));
        subCatPart.add(list);
    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

}