package vip.danielday.permissionrequest;

import android.app.Fragment;
import android.content.Context;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import vip.danielday.permissionrequest.annotation.PermissionCanceled;
import vip.danielday.permissionrequest.annotation.PermissionDenied;
import vip.danielday.permissionrequest.annotation.PermissionGranted;
import vip.danielday.permissionrequest.interf.IPermission;


/**
 * Created by daidan on 2018/4/26.
 */

public class PermissionRequestManager {

    private static final String TAG = "PermissionRequestManager";

    public static void PermissionRequest(final Object object, String[] permissions, int requestCode) {
        Context context = null;
        if (object instanceof Context) {
            context = (Context) object;
        } else if (object instanceof Fragment) {
            context = ((Fragment) object).getActivity();
        } else if (object instanceof android.support.v4.app.Fragment) {
            context = ((android.support.v4.app.Fragment) object).getActivity();
        }
        if (context == null || permissions == null) return;

        PermissionRequestActivity.PermissionRequest(context, permissions, requestCode, new IPermission() {
            @Override
            public void PermissionGranted(int requestCode) {
                invokeByAnnotation(object, new Object[]{requestCode}, PermissionGranted.class);
            }

            @Override
            public void PermissionDenied(int requestCode, List<String> denyList) {
                invokeByAnnotation(object, new Object[]{requestCode, denyList}, PermissionDenied.class);
            }

            @Override
            public void PermissionCanceled(int requestCode) {
                invokeByAnnotation(object, new Object[]{requestCode}, PermissionCanceled.class);
            }
        });
    }

    public static void invokeByAnnotation(final Object object, Object[] args, Class annotation) {
        if (object == null) return;
        Class<?> cls = object.getClass();
        Method[] methods = cls.getDeclaredMethods();
        if (methods == null || methods.length == 0) return;
        for (Method method : methods) {
            //过滤不含自定义注解PermissionCanceled的方法
            boolean isHasAnnotation = method.isAnnotationPresent(annotation);
            if (isHasAnnotation) {
                method.setAccessible(true);
                //获取方法类型
                Class<?>[] types = method.getParameterTypes();
                if (types == null || types.length != args.length) return;
                //获取方法上的注解
                if (method.getAnnotation(annotation) == null) return;
                try {
                    method.invoke(object, args);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
