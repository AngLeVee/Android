package mobiletipster.leveep2.cis2237.com.leveep2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, TipsterActivity.class);
        startActivity(intent);
        finish();
    }
}
