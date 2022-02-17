package com.example.infinia.udhaar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.github.clans.fab.FloatingActionButton;

public class Reminderme extends ActionBarActivity {

    EditText message;
    FloatingActionButton mFab;
    String message1;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.message);
        toolbar = (Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        mFab = (FloatingActionButton) findViewById(R.id.btnAdd);
        //mFab.setDrawable(getResources().getDrawable(R.drawable.ic_content_send));
        Intent i = getIntent();
        String name=(String) i.getExtras().get("name");
        String balance=(String) i.getExtras().get("balance");
        Integer bal = Integer.parseInt(balance);
        message = (EditText) findViewById(R.id.message);
        if(bal>0)
        {
            message1="Hey "+name+",\n\nI just wanted to remind you that I currently owe you Rs. "+bal + ".00"+
                    "\nLet's settle up next time we see each other.\n\nThanks!\n<Your Name>";
            message.setText(message1);
        }
        else
        {
            message1="Hey "+name+",\n\nI just wanted to remind you that you currently owe me Rs. "+(-1*bal) + ".00"+
                    "\nLet's settle up next time we see each other.\n\nThanks!\n<Your Name>";
            message.setText(message1);
        }

        mFab.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent2 = new Intent();
                intent2.setAction(Intent.ACTION_SEND);
                intent2.setType("text/plain");
                intent2.putExtra(Intent.EXTRA_TEXT, message1 );
                startActivity(Intent.createChooser(intent2, "Share via"));

            }
        });
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id;
        id = item.getItemId();
        if(id==android.R.id.home){
            Intent i = new Intent(Reminderme.this,MainActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }
}
