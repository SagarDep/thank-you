package protect.thank_you;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

import com.google.common.collect.ImmutableMap;

import java.util.Calendar;
import java.util.Map;

public class MainActivity extends AppCompatActivity
{
    private static final String TAG = "ThankYou";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if(id == R.id.action_about)
        {
            displayAboutDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void displayAboutDialog()
    {
        final Map<String, String> USED_LIBRARIES = ImmutableMap.of
        (
            "Guava", "https://github.com/google/guava"
        );
        final Map<String, String> USED_ASSETS = ImmutableMap.of
        (
            "Love by Hysen Drogu", "https://thenounproject.com/term/love/103874"
        );

        StringBuilder libs = new StringBuilder().append("<ul>");
        for (Map.Entry<String, String> entry : USED_LIBRARIES.entrySet())
        {
            libs.append("<li><a href=\"").append(entry.getValue()).append("\">").append(entry.getKey()).append("</a></li>");
        }
        libs.append("</ul>");

        StringBuilder resources = new StringBuilder().append("<ul>");
        for (Map.Entry<String, String> entry : USED_ASSETS.entrySet())
        {
            resources.append("<li><a href=\"").append(entry.getValue()).append("\">").append(entry.getKey()).append("</a></li>");
        }
        resources.append("</ul>");

        String appName = getString(R.string.app_name);
        int year = Calendar.getInstance().get(Calendar.YEAR);

        String version = "?";
        try
        {
            PackageInfo pi = getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pi.versionName;
        }
        catch (PackageManager.NameNotFoundException e)
        {
            Log.w(TAG, "Package name not found", e);
        }

        WebView wv = new WebView(this);
        String html =
                "<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" />" +
                        "<img src=\"file:///android_res/mipmap/ic_launcher.png\" alt=\"" + appName + "\"/>" +
                        "<h1>" +
                        String.format(getString(R.string.about_title_fmt),
                                "<a href=\"" + getString(R.string.app_webpage_url)) + "\">" +
                        appName +
                        "</a>" +
                        "</h1><p>" +
                        appName +
                        " " +
                        String.format(getString(R.string.debug_version_fmt), version) +
                        "</p><p>" +
                        String.format(getString(R.string.app_revision_fmt),
                                "<a href=\"" + getString(R.string.app_revision_url) + "\">" +
                                        getString(R.string.app_revision_url) +
                                        "</a>") +
                        "</p><hr/><p>" +
                        String.format(getString(R.string.app_copyright_fmt), year) +
                        "</p><hr/><p>" +
                        getString(R.string.app_license) +
                        "</p><hr/><p>" +
                        String.format(getString(R.string.app_libraries), appName, libs.toString()) +
                        "</p><hr/><p>" +
                        String.format(getString(R.string.app_resources), appName, resources.toString());

        wv.loadDataWithBaseURL("file:///android_res/drawable/", html, "text/html", "utf-8", null);
        new AlertDialog.Builder(this)
                .setView(wv)
                .setCancelable(true)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                    }
                })
                .show();
    }
}
