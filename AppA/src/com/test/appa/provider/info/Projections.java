package com.test.appa.provider.info;

import com.test.appa.provider.info.AppAContract.ModeSetting;

public class Projections {
    public interface Projection {
        public static ProjectionMap MODE_SETTING_COLUMNS = ProjectionMap.builder()
                .add(ModeSetting.ON_OFF)
                .add(ModeSetting.OPERATOR)
                .add(ModeSetting.EMAIL)
                .build();
        
        public static final ProjectionMap MODE_SETTING_PROJECTION_MAP = ProjectionMap.builder()
                .add(ModeSetting._ID)
                .addAll(MODE_SETTING_COLUMNS)
                .build();
    }
}
