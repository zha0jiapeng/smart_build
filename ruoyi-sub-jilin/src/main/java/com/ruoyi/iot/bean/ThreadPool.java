package com.ruoyi.iot.bean;

import cn.hutool.core.thread.ThreadUtil;

import java.util.concurrent.ExecutorService;

public class ThreadPool {
    public static ExecutorService executorService = ThreadUtil.newExecutor(50, 100, 450);
}