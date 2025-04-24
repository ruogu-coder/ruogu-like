package com.ruogu.thumb.constant;

import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;

/**
 * @Author code_zhang
 * @Date 2025/4/21 22:56
 */
public class RedisLuaScriptConstant {

    /**
     * 点赞 Lua 脚本
     * KEYS[1]       -- 临时计数键
     * KEYS[2]       -- 用户点赞状态键
     * ARGV[1]       -- 用户 ID
     * ARGV[2]       -- 博客 ID
     * ARGV[3]       -- 点赞时间
     * 返回:
     * -1: 已点赞
     * 1: 操作成功
     */
    public static final RedisScript<Long> THUMB_SCRIPT = new DefaultRedisScript<>("""
             local tempThumbKey = KEYS[1]       -- 临时计数键（如 thumb:temp:{timeSlice}） \s
             local userThumbKey = KEYS[2]       -- 用户点赞状态键（如 thumb:{userId}） \s
             local userId = ARGV[1]             -- 用户 ID \s
             local blogId = ARGV[2]             -- 博客 ID \s
             local currentTime = ARGV[3]        -- 点赞时间
              \s
             -- 1. 检查是否已点赞（避免重复操作） \s
             if redis.call('HEXISTS', userThumbKey, blogId) == 1 then \s
                 return -1  -- 已点赞，返回 -1 表示失败 \s
             end \s
              \s
             -- 2. 获取旧值（不存在则默认为 0）  .. 拼接字符串
             local hashKey = userId .. ':' .. blogId \s
             local oldValue = redis.call('HGET', tempThumbKey, hashKey)
             if oldValue == false then
                 oldValue = '{"type":0,"time":""}'
             end
            
            -- 3. 解析旧值
            local oldData = cjson.decode(oldValue)
            local oldType = oldData.type
            local oldTime = oldData.time
            
            -- 4. 计算新值
            local newType = 1
            local newValue = '{"type":' .. newType .. ',"time":' .. currentTime .. '}'
            
            -- 5. 原子性更新
            redis.call('HSET', tempThumbKey, hashKey, newValue)
            redis.call('HSET', userThumbKey, blogId, 1)
            
            -- 6. 返回成功结果
            return 1
            \s""", Long.class);

    /**
     * 取消点赞 Lua 脚本
     * 参数同上
     * 返回：
     * -1: 未点赞
     * 1: 操作成功
     */
    public static final RedisScript<Long> UNTHUMB_SCRIPT = new DefaultRedisScript<>("""
             local tempThumbKey = KEYS[1]      -- 临时计数键（如 thumb:temp:{timeSlice}） \s
             local userThumbKey = KEYS[2]      -- 用户点赞状态键（如 thumb:{userId}） \s
             local userId = ARGV[1]            -- 用户 ID \s
             local blogId = ARGV[2]            -- 博客 ID \s
             local currentTime = ARGV[3]        -- 点赞时间
              \s
             -- 1. 检查用户是否已点赞（若未点赞，直接返回失败） \s
             if redis.call('HEXISTS', userThumbKey, blogId) ~= 1 then \s
                 return -1  -- 未点赞，返回 -1 表示失败 \s
             end \s
              \s
             -- 2. 获取当前临时计数（若不存在则默认为 0） \s
             local hashKey = userId .. ':' .. blogId \s
             local oldValue = redis.call('HGET', tempThumbKey, hashKey)
             if oldValue == false then
                 oldValue = '{"type":0,"time":""}'
             end
            
            -- 3. 解析旧值
            local oldData = cjson.decode(oldValue)
            local oldType = oldData.type
            local oldTime = oldData.time
            
             local oldNumber = oldType \s
              \s
             -- 3. 计算新值并更新 \s
             local newNumber = oldNumber - 1 \s
             local newValue = '{"type":' .. newNumber .. ',"time":'.. currentTime ..'}'
              \s
             -- 4. 原子性操作：更新临时计数 + 删除用户点赞标记 \s
             redis.call('HSET', tempThumbKey, hashKey, newValue) \s
             redis.call('HDEL', userThumbKey, blogId) \s
              \s
             return 1  -- 返回 1 表示成功 \s
            \s""", Long.class);


    /**
     * 点赞 Lua 脚本
     * KEYS[1]       -- 用户点赞状态键
     * ARGV[1]       -- 博客 ID
     * 返回:
     * -1: 已点赞
     * 1: 操作成功
     */
    public static final RedisScript<Long> THUMB_SCRIPT_MQ = new DefaultRedisScript<>("""  
                    local userThumbKey = KEYS[1]
                    local blogId = ARGV[1]
            
                    -- 判断是否已经点赞
                    if redis.call("HEXISTS", userThumbKey, blogId) == 1 then
                        return -1
                    end
            
                    -- 添加点赞记录
                    redis.call("HSET", userThumbKey, blogId, 1)
                    return 1
            """, Long.class);

    /**
     * 取消点赞 Lua 脚本
     * KEYS[1]       -- 用户点赞状态键
     * ARGV[1]       -- 博客 ID
     * 返回:
     * -1: 已点赞
     * 1: 操作成功
     */
    public static final RedisScript<Long> UN_THUMB_SCRIPT_MQ = new DefaultRedisScript<>("""  
            local userThumbKey = KEYS[1]
            local blogId = ARGV[1]
            
            -- 判断是否已点赞
            if redis.call("HEXISTS", userThumbKey, blogId) == 0 then
                return -1
            end
            
            -- 删除点赞记录
            redis.call("HDEL", userThumbKey, blogId)
            return 1
            """, Long.class);

}
