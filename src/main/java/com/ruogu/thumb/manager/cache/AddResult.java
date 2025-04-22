package com.ruogu.thumb.manager.cache;

/**
 * @param expelledKey 被挤出的 key
 * @param hotKey    当前 key 是否进入 TopK
 * @param currentKey  当前操作的 key
 * @Author ruogu
 * @Date 2025/4/22 22:15
 */
public record AddResult(String expelledKey, boolean hotKey, String currentKey) {

}