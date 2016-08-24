/*
 * Mobile Communication Company, LG ELECTRONICS INC., SEOUL, KOREA
 * Copyright(c) 2014 by LG Electronics Inc.
 *
 * All rights reserved. No part of this work may be reproduced, stored in a
 * retrieval system, or transmitted by any means without prior written
 * Permission of LG Electronics Inc.
 */
package com.test.appa.provider;

import android.content.Context;

public class Reader {
    protected Context mContext;
    AppADatabaseHelper mDbHelper;

    public Reader(Context context, AppADatabaseHelper helper) {
        initialize(context, helper);
    }

    private void initialize(Context context, AppADatabaseHelper helper) {
        mContext = context;
        mDbHelper = helper;
    }
}
