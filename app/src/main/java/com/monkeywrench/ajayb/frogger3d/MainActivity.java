package com.monkeywrench.ajayb.frogger3d;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class MainActivity extends Activity {

    private GLSurfaceView glSurfaceView;
    private boolean isrendererset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityManager activityManager = (ActivityManager)getSystemService(ACTIVITY_SERVICE);
        ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();

        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        final boolean supportsgl2 = configurationInfo.reqGlEsVersion >= 0x20000 || isProbablyEmulator();

        if(supportsgl2){
            glSurfaceView = new GameView(this);

            if(isProbablyEmulator()){
                glSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
            }

            glSurfaceView.setEGLContextClientVersion(2);
            glSurfaceView.setRenderer(new GameRenderer(this));
            isrendererset = true;
            setContentView(glSurfaceView);
        }

        else{
            Toast.makeText(this, "This device does not support opengles 2.0", Toast.LENGTH_LONG).show();
        }
    }

    private boolean isProbablyEmulator(){
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1
                && (Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86"));
    }

    @Override
    protected void onPause(){
        super.onPause();

        if(isrendererset)
            glSurfaceView.onPause();
    }

    @Override
    protected void onResume(){
        super.onResume();

        if(isrendererset){
            glSurfaceView.onResume();
        }
    }

}
