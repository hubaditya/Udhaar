package com.example.infinia.udhaar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.github.clans.fab.FloatingActionButton;


public class Edit extends ActionBarActivity
{
    String name;
    String date;
    String location;
    String money;

    private RadioGroup amount;
    private RadioButton select;
    FloatingActionButton mFab;
    DatabaseHandler db;
    Typeface tf;
    EditText a,b,c,d;
    private Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_view);
        toolbar = (Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        mFab = (FloatingActionButton) findViewById(R.id.btnEdit);
        //mFab.setDrawable(getResources().getDrawable(R.drawable.ic_action_tick));
        EditText etdate = (EditText)findViewById(R.id.txtDateAdd);
        etdate.setText(dateFormat.format(date));
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void onClickEdit(View view)
    {
        long id;
        db=new DatabaseHandler(this);
        name= getIntent().getStringExtra("name").toString();
        a=(EditText)findViewById(R.id.txtDateAdd);
        a.setTypeface(tf);
        date=a.getText().toString();
        location=((EditText)findViewById(R.id.txtLocationAdd)).getText().toString();
        amount = (RadioGroup) findViewById (R.id.radioGroup1);
        int selected = amount.getCheckedRadioButtonId();
        select = (RadioButton) findViewById(selected);
        money=((EditText)findViewById(R.id.txtMoneyAdd)).getText().toString();
        int flag=fun();
        if(flag==0)
        {
            int amount=Integer.parseInt(money);

            if(select.getText().equals("Debit"))
            { id = db.addDetails(new Details(name,date,location,amount,0)); }

            else id = db.addDetails(new Details(name,date,location,0,amount));
        }
    }


    public int fun()
    {
        int flag=0;
        if(date.length()==0 || location.length()==0 || money.length()==0)
        {
            flag=1;
            new MaterialDialog.Builder(this)
                    .title("Error")
                    .content("Please fill all entries")
                    .positiveText("Ok")
                    .positiveColorRes(R.color.primary)
                    .show();
        }
        else
        { new MaterialDialog.Builder(this)
                .title("Done")
                .content("Details updated")
                .positiveText("Ok")
                .positiveColorRes(R.color.primary)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        Intent i = new Intent(Edit.this,MainActivity.class);
                        startActivity(i);
                    }
                }).show();
        }


        return flag;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id;
        id = item.getItemId();
        if(id==android.R.id.home){
            Intent i = new Intent(Edit.this,MainActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

}
