package xinghom.com.timechallenger_static;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, AppUsageStatisticsFragment.newInstance())
                    .commit();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater(). inflate( R.menu.main_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case
                R.id.statisticsItem: Toast.makeText( this, "Statistics", Toast.LENGTH_SHORT). show();
                break;
            case
                R.id.challengeModeItem: Toast.makeText( this, "ChallengeMode", Toast.LENGTH_SHORT). show();
                Intent intent = new Intent(MainActivity.this, ChallengeMode.class);
                startActivity(intent);
                break;


            default:
        }
        return true;
    }
}

