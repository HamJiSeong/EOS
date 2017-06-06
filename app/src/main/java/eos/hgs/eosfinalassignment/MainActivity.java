package eos.hgs.eosfinalassignment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;

public class MainActivity extends Activity {

    Fragment fragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FragmentManager fragmentManager = getFragmentManager();
        // FragmentTransaction transaction = fragmentManager.beginTransaction();
        fragment = fragmentManager.findFragmentById(R.id.list);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && fragment instanceof KeypadFragment && ((KeypadFragment)fragment).onBackKeyPressed())
            return true;
        else
            return super.onKeyDown(keyCode, event);
    }

    public interface BackKeyListener {
        public boolean onBackKeyPressed();
    }
}
