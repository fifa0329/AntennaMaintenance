package com.station.activity;

import android.app.Application;

public class MyApplication extends Application { 

// �����˳���� ,����һ���˳�used to one-key exit

private static boolean isProgramExit = false; 

public void setExit(boolean exit) { 

isProgramExit = exit; 

} 

public boolean isExit() { 

return isProgramExit; 

} 

}