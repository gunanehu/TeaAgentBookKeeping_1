package com.teaagent.ui.report;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class FileUtil {

    static String TAG = "FileUtil";

    public static void saveFile(Context context, String filename, Object object) {
        try (ObjectOutput out = new ObjectOutputStream(new FileOutputStream(new File(context.getFilesDir(), "") + File.separator + filename))) {
            out.writeObject(object);
        } catch (IOException ioe) {
            Log.e(TAG, "Exception while saving file" + ioe);
        }
    }

    public static void deleteFile(Context context, String fileName) {
        File file = new File(new File(context.getFilesDir(), "") + File.separator + fileName);
        file.delete();
    }

    public static Object readFile(Context context, String filename) throws ClassNotFoundException {
        Object object = null;
        try {
            ObjectInputStream input = new ObjectInputStream(new FileInputStream(new File(context.getFilesDir(), "") + File.separator + filename));
            input.close();
            object = input.readObject();
        } catch (FileNotFoundException e) {
            Log.e(TAG, "File not found", e);
        } catch (IOException ioe) {
            Log.e(TAG, "Error while reading file", ioe);
        }
        return object;
    }

    public static void createFile(Context context, String fileName, String data) {
        try (Writer out = new OutputStreamWriter(new FileOutputStream(fileName), StandardCharsets.UTF_8)) {
            out.write(data);
        } catch (IOException e) {
            Log.e(TAG, "Could not create file ", e);
            Toast.makeText(context, "Request failed: " + e.toString(),
                    Toast.LENGTH_LONG).show();
        }
    }

    public static boolean sendMail(Context context, File file, String subject, String extraText) {
        boolean isSuccessful = false;
//        Uri uriForFile = FileProvider.getUriForFile(context, context.getPackageName()  + File.separator , file);
        Uri uriForFile = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);

        String mailTo = "";

        final Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("message/rfc822");

        emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{mailTo});
        emailIntent.putExtra(Intent.EXTRA_TEXT, extraText);
        emailIntent.putExtra(Intent.EXTRA_STREAM, uriForFile);

        final PackageManager pm = context.getPackageManager();
        final List<ResolveInfo> matches = pm.queryIntentActivities(emailIntent, 0);
        ResolveInfo best = null;
        for (final ResolveInfo info : matches) {
            if (info.activityInfo.packageName.endsWith(".gm") ||
                    info.activityInfo.name.toLowerCase().contains("gmail")) {
                best = info;
            }
        }
        if (best != null) {
            emailIntent.setClassName(best.activityInfo.packageName, best.activityInfo.name);
        }

        try {
            context.startActivity(emailIntent);
            isSuccessful = true;
        } catch (ActivityNotFoundException ex) {
            Log.e(TAG, "No email client installed ", ex);
        }
        return isSuccessful;
    }
}
