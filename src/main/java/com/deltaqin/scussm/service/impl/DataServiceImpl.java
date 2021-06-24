package com.deltaqin.scussm.service.impl;

import com.deltaqin.scussm.common.utils.RedisKeyUtil;
import com.deltaqin.scussm.service.DataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author deltaqin
 * @date 2021/6/24 上午7:26
 */
@Service
@Slf4j
public class DataServiceImpl implements DataService {

    @Autowired
    private RedisTemplate redisTemplate;

    private SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");

    //将指定的IP计入UV
    @Override
    public void recordUV(String ip){
        String redisKey = RedisKeyUtil.getUVKey(df.format(new Date()));
        redisTemplate.opsForHyperLogLog().add(redisKey,ip);
    }

    // 统计指定日期范围内的UV
    @Override
    public long calculateUV(Date start, Date end){
        if(start == null || end ==null){
            throw new IllegalArgumentException("参数不能为空！");
        }
        // 整理该日期范围内的key
        List<String> keyList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);
        while (!calendar.getTime().after(end)){
            String key = RedisKeyUtil.getUVKey(df.format(calendar.getTime()));
            // 每日对应的key存下来
            keyList.add(key);
            // 加一天
            calendar.add(Calendar.DATE,1);
        }
        // 合并这些数据对应的新的key
        String redisKey = RedisKeyUtil.getUVKey(df.format(start),df.format(end));
        // union 就是合并。加起来，bitmap使用的是OR运算
        // 所有的key组成的数组对应的值合并，也就是redisKey 指的是对应的日期区间存放了对应的所有的IP（去重的）
        redisTemplate.opsForHyperLogLog().union(redisKey,keyList.toArray());
        // 返回统计的结果，返回日期区间内的独立访客
        return redisTemplate.opsForHyperLogLog().size(redisKey);
    }

    // 将指定用户计入DAU
    @Override
    public void recordDAU(int userId){
        String redisKey = RedisKeyUtil.getDAUKey(df.format(new Date()));
        // 日活用户，对应的ID设置为true即可
        redisTemplate.opsForValue().setBit(redisKey,userId,true);
    }

    // 统计指定日期范围内的DAU
    @Override
    public long calculateDAU(Date start, Date end) {
        if (start == null || end == null) {
            throw new IllegalArgumentException("参数不能为空!");
        }

        // 整理该日期范围内的key（同上）
        List<byte[]> keyList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);
        while (!calendar.getTime().after(end)) {
            String key = RedisKeyUtil.getDAUKey(df.format(calendar.getTime()));
            keyList.add(key.getBytes());
            calendar.add(Calendar.DATE, 1);
        }

        // 进行OR运算
        return (long) redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                String redisKey = RedisKeyUtil.getDAUKey(df.format(start), df.format(end));
                connection.bitOp(RedisStringCommands.BitOperation.OR,
                        redisKey.getBytes(),
                        // 转换为一个二维的byte数组
                        keyList.toArray(new byte[0][0]));

                // 计算是true的个数，就是对应时间间隔里登陆过的
                return connection.bitCount(redisKey.getBytes());
            }
        });
    }
}
