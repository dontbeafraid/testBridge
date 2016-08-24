package com.test.appa.provider.info;

import android.net.Uri;

import com.test.appa.provider.info.DatabaseInfo.ModeSettingColumns;

public final class AppAContract {

    public static final String AUTHORITY = "com.test.appa";
    public static final Uri AUTHORITY_URI = Uri.parse("content://com.test.appa");

    public static final class ModeSetting implements ModeSettingColumns {
        public static final Uri CONTENT_URI;
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/mode_setting";

        static {
            CONTENT_URI = Uri.withAppendedPath(AppAContract.AUTHORITY_URI, "mode_setting");
        }

        private ModeSetting() { }
    }
    
    public static final String LIMIT_PARAM_KEY = "limit";
}
