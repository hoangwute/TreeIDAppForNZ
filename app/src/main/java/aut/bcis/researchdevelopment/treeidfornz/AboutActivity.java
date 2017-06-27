package aut.bcis.researchdevelopment.treeidfornz;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {
    private TextView txtEmail, txtExternalSource, txtExternalPicture;
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
        txtExternalSource = (TextView) findViewById(R.id.txtExternalSource);
        txtExternalPicture = (TextView) findViewById(R.id.txtExternalPicture);
        txtExternalSource.setText(Html.fromHtml("The Planlist.org <a href=\"http://www.theplantlist.org/\">http://www.theplantlist.org/</a><br>Wikipedia" +
                "<br>Terrain <a href=\"http://terrain.net.nz/\">terrain.net.nz</a><br>Moon, P. (2005). A Tohunga's Natural World. Plants, gardening and food. David Ling Pub..<br>" +
                "Landcare Research<br>NZPCN<br><a href=\"http://ngaitahu.iwi.nz/our_stories/good-oil-tough-old-titoki/\">ngaitahu.iwi.nz</a><br>Crowe, Andrew (1992). Which Native Tree? Penguin Books<br>" +
                "<a href=\"http://nzflora.info/\">http://nzflora.info/</a>"));
        txtExternalPicture.setText(Html.fromHtml("<i><b>Aristotelia serrata</b></i> leaf and flower<br>Credit: Stephenhartley CC BY-SA 4.0<br><br><i><b>Aristotelia serrata</b></i> habitus<br>Credit: Grapeman4 CC BY-SA 3.0<br><br><i><b>Melicytus macrophyllus</b></i> leaf<br>Credit:aunty CC BY-NC<br><br><i><b>M. macrophyllus</b></i> fruits" +
                "<br>Credit: jacqui-nz CC-BY-NC<br><br><i><b>Myorporum laetum</b></i> leaves and fruits<br>Credit: Júlio Reis CC BY-SA 3.0<br><br><i><b>Myoporum laetum</b></i> flower<br>Credit: Avenue CC BY-SA 3.0"));
        txtEmail.append(Html.fromHtml("\"<a href=\"mailto:nztreeapp@aut.ac.nz\">nztreeapp@aut.ac.nz</a>\""));
        txtExternalSource.setMovementMethod(LinkMovementMethod.getInstance());
        txtExternalPicture.setMovementMethod(LinkMovementMethod.getInstance());
        txtEmail.setMovementMethod(LinkMovementMethod.getInstance());
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
        else if(item.getItemId() == R.id.menuFavourite) {
            Intent intent = new Intent(AboutActivity.this, ListActivity.class);
            intent.putExtra("FromHomePage", "homepage");
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
