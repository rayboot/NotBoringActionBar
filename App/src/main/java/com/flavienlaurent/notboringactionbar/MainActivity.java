package com.flavienlaurent.notboringactionbar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startActivity(
                new Intent(MainActivity.this, NoBoringActionBarActivity.class));

    }

    public void onGoClick(View view)
    {
        startActivity(
                new Intent(MainActivity.this, NoBoringActionBarActivity.class));
    }
}
