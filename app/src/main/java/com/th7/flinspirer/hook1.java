package com.th7.flinspirer;

import android.util.Log;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class hook1 implements IXposedHookLoadPackage {
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        if (loadPackageParam.packageName.equals("com.android.launcher3")) {
            Log.i("flinspirer", "报告！已打入领创内部！");
            XposedHelpers.findAndHookMethod(ClassLoader.class, "loadClass", String.class, new XC_MethodHook() {
                /* access modifiers changed from: protected */
                public void afterHookedMethod(MethodHookParam param) throws Throwable {
                    Class clazz = (Class) param.getResult();
                    String clazzName = clazz.getName();
                    StringBuilder sb = new StringBuilder();
                    sb.append("LoadClass: ");
                    sb.append(clazzName);
                    //android.app.*
                    if (clazzName.contains("android.app")) {
                        Method[] mds = clazz.getDeclaredMethods();
                        for (int i = 0; i < mds.length; i++) {
                            final Method md = mds[i];
                            int mod = mds[i].getModifiers();
                            if (!Modifier.isAbstract(mod) && !Modifier.isNative(mod) && !Modifier.isInterface(mod)) {
                                XposedBridge.hookMethod(mds[i], new XC_MethodHook() {
                                    /* access modifiers changed from: protected */
                                    public void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                        if (md.getName().contains("complexParameterFunc")) {
                                            for (Object obj : param.args) {
                                            }
                                        }
                                        StringBuilder sb = new StringBuilder();
                                        sb.append("Hook Method: ");
                                        sb.append(md.toString());
                                    }
                                });
                            }
                        }
                    }
                }
            });
        }
    }
}

