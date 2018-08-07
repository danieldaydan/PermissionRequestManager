package vip.danielday.permissionmanager;

import android.content.Context;
import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;


/**
 * 使用说明，在application中 Thread.setDefaultUncaughtExceptionHandler( CustomExceptionHandler.getInstance.init()); 即可
 *
 * @Description : catch app errors ，捕获程序异常错误
 */

public class CustomExceptionHandler implements UncaughtExceptionHandler {


    private Thread.UncaughtExceptionHandler mUncaughtExceptionHandler;

    private static CustomExceptionHandler instance;

    private Context mContext;
    public static CustomExceptionHandler getInstance(Context context)
    {
        if (instance == null) {
            instance = new CustomExceptionHandler(context);
        }
        return instance;
    }

    private CustomExceptionHandler(Context context)
    {
        mContext = context;
        this.mUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
//        ex.printStackTrace();
        Log.d("CustomExceptionHandler",getErrorInfo(ex));
//        DebugLog.d("error", getErrorInfo(ex),true);


        if (mUncaughtExceptionHandler != null) {
            mUncaughtExceptionHandler.uncaughtException(thread, ex);
        }

        //程序退出
    }



    /**
     * 获取错误信息
     */
    private String getErrorInfo(Throwable ex) {
        Writer writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);
        ex.printStackTrace(pw);
        pw.close();
        String error = writer.toString();
        return error;
    }

}
