package com.ruogu.thumb.manager.cache;

import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 * @Author ruogu
 * @Date 2025/4/22 22:06
 */
public interface TopK {
    AddResult add(String key, int increment);

    List<Item> list();

    BlockingQueue<Item> expelled();

    void fading();

    long total();
}

