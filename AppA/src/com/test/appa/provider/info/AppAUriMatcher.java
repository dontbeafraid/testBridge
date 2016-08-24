/*
 * Mobile Communication Company, LG ELECTRONICS INC., SEOUL, KOREA
 * Copyright(c) 2014 by LG Electronics Inc.
 *
 * All rights reserved. No part of this work may be reproduced, stored in a
 * retrieval system, or transmitted by any means without prior written
 * Permission of LG Electronics Inc.
 */
package com.test.appa.provider.info;

import android.content.UriMatcher;

public class AppAUriMatcher {
    public static final class Type {
        public static final int MODE_SETTING = 1000;
        public static final int MODE_SETTING_ID = 1001;
    }

    public void addURIs(final UriMatcher matcher) {
        matcher.addURI(AppAContract.AUTHORITY, "mode_setting", Type.MODE_SETTING);
        matcher.addURI(AppAContract.AUTHORITY, "mode_setting/#", Type.MODE_SETTING_ID);
    }
}
