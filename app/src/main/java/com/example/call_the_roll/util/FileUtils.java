package com.example.call_the_roll.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

/**
 * Created by 廖智涌 on 2017/5/19.
 */


public class FileUtils {
    public static String getPath(Context context, Uri uri) {

        //获取Uri中的scheme字符串部分
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = { "_data" };
            Cursor cursor = null;

            try {
                cursor = context.getContentResolver().query(uri, projection,null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it
            }
        }

        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }
}
