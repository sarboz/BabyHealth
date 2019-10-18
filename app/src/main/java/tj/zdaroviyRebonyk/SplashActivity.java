package tj.zdaroviyRebonyk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SplashActivity extends Activity {
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        pref = PreferenceManager.getDefaultSharedPreferences(getApplication());

        new Thread(new Runnable() {
            @Override
            public void run() {

               // ncAppRating.showRateDialog(getApplication());
                if (!pref.getBoolean("first", false)) {
                    copyFolder("Imagess");
                    copyFolder("Texts");
                    copyFolder("Imagesb");
                    copyFolder("Textb");
                    copyFolder("Imagesk");
                    copyFolder("Textk");
                    pref.edit().putBoolean("first", true).apply();
                }

            }
        }).start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        }, 3000);
    }


    private void copyFile(InputStream in, OutputStream out) {
        byte[] buffer = new byte[1024];
        int read;
        try {
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void copyFolder(String name) {
        AssetManager assetManager = getAssets();
        String[] files = null;

        try {
            files = assetManager.list(name);
        } catch (IOException e) {
            Log.e("ERROR", "Failed to get asset file list.", e);
        }
        for (String filename : files) {
            InputStream in = null;
            OutputStream out = null;
            File folder = new File(getFilesDir() + "/" + name);
            boolean success = true;
            if (!folder.exists()) {
                success = folder.mkdir();
            }
            if (success) {
                try {
                    in = assetManager.open(name + "/" + filename);
                    out = new FileOutputStream(getFilesDir() + "/" + name + "/" + filename);
                    copyFile(in, out);
                    in.close();
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    Log.e("ERROR", "Failed to copy asset file: " + filename, e);
                }
            }
        }
    }
}
