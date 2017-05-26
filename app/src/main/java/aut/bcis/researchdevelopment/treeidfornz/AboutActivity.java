package aut.bcis.researchdevelopment.treeidfornz;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {
    private TextView txtEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        addControls();
    }

    private void addControls() {
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        TextView txtBarTitle = (TextView) myToolbar.findViewById(R.id.toolbar_title);
        txtBarTitle.setText("About the project");
        txtBarTitle.setTextColor(Color.WHITE);
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(),R.drawable.icon_menuu); // change tool bar icon
        myToolbar.setOverflowIcon(drawable);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        txtEmail = (TextView) findViewById(R.id.txtEmail);
        txtEmail.append(Html.fromHtml("<a href=\"mailto:nztreeapp@aut.ac.nz\">nztreeapp@aut.ac.nz</a>"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menuHome) {
            Intent intent = new Intent(AboutActivity.this, MainActivity.class);
            startActivity(intent);
        }
        else if(item.getItemId() == R.id.menuList) {
            Intent intent = new Intent(AboutActivity.this, ListActivity.class);
            startActivity(intent);
        }
        else if(item.getItemId() == R.id.menuIdentification) {
            Intent intent = new Intent(AboutActivity.this, IdentificationActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
