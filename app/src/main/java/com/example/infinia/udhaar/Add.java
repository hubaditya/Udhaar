package com.example.infinia.udhaar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.github.clans.fab.FloatingActionButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Add extends ActionBarActivity
{
    String name;
    String date;
    String location;
    String money;

    private RadioGroup amount;
    private RadioButton select;
    FloatingActionButton mFab;
    DatabaseHandler db;
    private Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_view);
        toolbar = (Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        mFab = (FloatingActionButton) findViewById(R.id.btnAdd);
        EditText etdate = (EditText)findViewById(R.id.txtDateAdd);
        etdate.setText(dateFormat.format(date));
        getSupportActionBar().setHomeButtonEnabled(true); //displaying home icon in the corner
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void onClickAdd(View v)
    {
        long id;
        db=new DatabaseHandler(this);
        name=((EditText)findViewById(R.id.txtNameAdd)).getText().toString();
        date=((EditText)findViewById(R.id.txtDateAdd)).getText().toString();
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
        if(name.length()==0 || date.length()==0 || location.length()==0 || money.length()==0)
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
        {
            Cursor c = db.getAllDetails();
            if(c.moveToFirst())
            {
                do
                    if(name.equals(c.getString(1)))
                    {
                        flag=1;
                        new MaterialDialog.Builder(this)
                                .title("Error")
                                .content("This name already exists")
                                .positiveText("Ok")
                                .positiveColorRes(R.color.primary)
                                .show();
                        break;
                    }
                while(c.moveToNext());
            }

            if(flag==0)
                new MaterialDialog.Builder(this)
                        .title("Done")
                        .content("Details Regsitered")
                        .positiveText("Ok")
                        .positiveColorRes(R.color.primary)
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                Intent i = new Intent(Add.this,MainActivity.class);
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
            Intent i = new Intent(Add.this,MainActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }
}
