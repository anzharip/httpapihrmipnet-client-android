package com.anzhari.hrmipnetmobile.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.webkit.MimeTypeMap;

import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import okhttp3.ResponseBody;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class Helper {

    public static void showSettingsDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", (dialog, which) -> {
            dialog.cancel();
            openSettings(context);
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();

    }

    public static void openSettings(Context context) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        intent.setData(uri);
        ((Activity) context).startActivityForResult(intent, 101);
    }

    public static String encodeFileToBase64(File file){
        try {
            byte[] byteFileArray = FileUtils.readFileToByteArray(file);
            if (byteFileArray.length > 0) {
                return Base64.encodeToString(byteFileArray, Base64.NO_WRAP);
            }
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String decodeFileFromBase64(String base64String, String fileName){
        try {
            File file = new File(Environment.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS) +"/"+ fileName);
            byte[] base64 = Base64.decode(base64String, Base64.NO_WRAP);
            FileOutputStream fos = new FileOutputStream(file, false);
            fos.write(base64);
            fos.flush();
            fos.close();
            return "success";
        } catch (IOException e) {
            e.printStackTrace();
            return "failed";
        }
    }

    public static String getErrorMessage(ResponseBody responseBody) {
        try {
            JSONObject jsonObject = new JSONObject(responseBody.string());
            return jsonObject.getString("message");
        } catch (Exception e) {
            return e.getMessage();
        }
    }

}
