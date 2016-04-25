package example.phanmemquanlythuchi;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class DoiTien extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doitien);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.doi_tien, menu);
        return true;
    }

}
