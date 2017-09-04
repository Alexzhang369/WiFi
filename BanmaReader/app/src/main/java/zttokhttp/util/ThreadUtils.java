package zttokhttp.util;

import android.os.Handler;

/*
 * @创建者     Administrator
 * @创建时间   2015/8/7 10:25
 * @描述	      ${TODO}$
 *
 * @更新者     $Author$
 * @更新时间   $Date$
 * @更新描述   ${TODO}$
 */
public class ThreadUtils {
    /**
     * 子线程执行task
     */
    public static void runInThread(Runnable task) {
        new Thread(task).start();
    }

    /**
     * 创建一个主线程中handler
     */
    public static Handler mHandler = new Handler();

    /**
     * UI线程执行task
     */
    public static void runInUIThread(Runnable task) {
        mHandler.post(task);
    }
}
