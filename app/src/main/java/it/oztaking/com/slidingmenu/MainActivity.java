package it.oztaking.com.slidingmenu;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import it.oztaking.com.slidingmenu.ui.SlideMenu;

public class MainActivity extends Activity implements View.OnClickListener{

    private SlideMenu sm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉标题 此处已经失效了
//        requestWindowFeature(Window.FEATURE_NO_TITLE);

       // getSupportActionBar().hide();
       // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        sm = (SlideMenu) findViewById(R.id.sm);
        findViewById(R.id.ib_back).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        sm.switchState();
    }
}
