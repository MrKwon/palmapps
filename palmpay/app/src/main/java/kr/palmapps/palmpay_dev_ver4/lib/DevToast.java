package kr.palmapps.palmpay_dev_ver4.lib;

import android.content.Context;
import android.widget.Toast;

/**
 * Dev version 에서만 토스트 출력
 * 편리한 토스트 사용을 위한 Dev ver용 토스트 메시지
 */
public class DevToast {
    private static boolean enabled = true; // BuildConfig.DEBUG;


    public static void s(Context context, int id) {
        if (!enabled) return;

        Toast.makeText(context, id, Toast.LENGTH_SHORT).show();
    }

    public static void s(Context context, String string) {
        if (!enabled) return;

        Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
    }

    public static void l(Context context, int id) {
        if (!enabled) return;

        Toast.makeText(context, id, Toast.LENGTH_LONG).show();
    }

    public static void l(Context context, String string) {
        if (!enabled) return;

        Toast.makeText(context, string, Toast.LENGTH_LONG).show();
    }
}


