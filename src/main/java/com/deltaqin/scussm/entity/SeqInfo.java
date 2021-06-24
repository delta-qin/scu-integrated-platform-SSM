package com.deltaqin.scussm.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * @author deltaqin
 * @date 2021/6/24 下午6:39
 */
@Data
@Document(collection = "sequence")
public class SeqInfo {

    @Id
    private String id;

    // 集合的名字
    @Field
    private String colName;

    //序列值
    @Field
    private int seqId;
}
