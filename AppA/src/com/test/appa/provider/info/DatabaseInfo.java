/*
 * Mobile Communication Company, LG ELECTRONICS INC., SEOUL, KOREA
 * Copyright(c) 2014 by LG Electronics Inc.
 *
 * All rights reserved. No part of this work may be reproduced, stored in a
 * retrieval system, or transmitted by any means without prior written
 * Permission of LG Electronics Inc.
 */
package com.test.appa.provider.info;

public class DatabaseInfo {
    public static final int DATABASE_VERSION = 101;

    public static final String DATABASE_NAME = "appa.db";

    public interface Tables {
        public static final String MODE_SETTING = "mode_setting";
    }

    public interface ModeSettingColumns {
        public static final String _ID = BaseColumns._ID;
        public static final String ON_OFF = "on_off";
        public static final String OPERATOR = "operator";
        public static final String EMAIL = "email";
    }

    public interface BaseColumns {
        public static final String _ID = "_id";
    }
}
