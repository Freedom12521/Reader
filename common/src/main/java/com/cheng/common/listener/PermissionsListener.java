package com.cheng.common.listener;

import java.util.List;

public interface PermissionsListener {

    void onGranted();

    void onDenied(List<String> deniedPermissions);
}
