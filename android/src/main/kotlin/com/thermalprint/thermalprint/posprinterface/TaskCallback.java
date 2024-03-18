package com.thermalprint.thermalprint.posprinterface;

import net.posprinter.*;

import org.jetbrains.annotations.Nullable;

public interface TaskCallback {
    void OnSucceed();

    void OnFailed();
}
