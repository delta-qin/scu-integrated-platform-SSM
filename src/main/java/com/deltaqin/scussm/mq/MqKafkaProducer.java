//package com.deltaqin.scussm.mq;
//
//import com.alibaba.fastjson.JSONObject;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.stereotype.Component;
//
///**
// * @author deltaqin
// * @date 2021/6/24 上午12:25
// */
//@Component
//@Deprecated
//public class MqKafkaProducer {
//    @Autowired
//    private KafkaTemplate kafkaTemplate;
//
//    // 处理事件
//    public void fireEvent(Event event) {
//        // 将事件发布到指定的主题
//        kafkaTemplate.send(event.getTopic(), JSONObject.toJSONString(event));
//    }
//
//}
