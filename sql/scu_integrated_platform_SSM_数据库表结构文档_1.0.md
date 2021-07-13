# 数据库设计文档

**数据库名：** scu_integrated_platform_SSM

**文档版本：** 1.0

**文档描述：** 数据库表结构文档

| 表名                  | 说明       |
| :---: | :---: |
| [comment](#comment) |  |
| [discuss_post](#discuss_post) |  |
| [login_ticket](#login_ticket) |  |
| [message](#message) |  |
| [QRTZ_BLOB_TRIGGERS](#QRTZ_BLOB_TRIGGERS) |  |
| [QRTZ_CALENDARS](#QRTZ_CALENDARS) |  |
| [QRTZ_CRON_TRIGGERS](#QRTZ_CRON_TRIGGERS) |  |
| [QRTZ_FIRED_TRIGGERS](#QRTZ_FIRED_TRIGGERS) |  |
| [QRTZ_JOB_DETAILS](#QRTZ_JOB_DETAILS) |  |
| [QRTZ_LOCKS](#QRTZ_LOCKS) |  |
| [QRTZ_PAUSED_TRIGGER_GRPS](#QRTZ_PAUSED_TRIGGER_GRPS) |  |
| [QRTZ_SCHEDULER_STATE](#QRTZ_SCHEDULER_STATE) |  |
| [QRTZ_SIMPLE_TRIGGERS](#QRTZ_SIMPLE_TRIGGERS) |  |
| [QRTZ_SIMPROP_TRIGGERS](#QRTZ_SIMPROP_TRIGGERS) |  |
| [QRTZ_TRIGGERS](#QRTZ_TRIGGERS) |  |
| [user](#user) |  |

**表名：** <a id="comment">comment</a>

**说明：** 

**数据列：**

| 序号 | 名称 | 数据类型 |  长度  | 小数位 | 允许空值 | 主键 | 默认值 | 说明 |
| :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
|  1   | id |   int   | 10 |   0    |    N     |  Y   |       |   |
|  2   | user_id |   int   | 10 |   0    |    Y     |  N   |       |   |
|  3   | entity_type |   int   | 10 |   0    |    Y     |  N   |       |   |
|  4   | entity_id |   int   | 10 |   0    |    Y     |  N   |       |   |
|  5   | target_id |   int   | 10 |   0    |    Y     |  N   |       |   |
|  6   | content |   text   | 65535 |   0    |    Y     |  N   |       |   |
|  7   | status |   int   | 10 |   0    |    Y     |  N   |       |   |
|  8   | create_time |   timestamp   | 19 |   0    |    Y     |  N   |       |   |

**表名：** <a id="discuss_post">discuss_post</a>

**说明：** 

**数据列：**

| 序号 | 名称 | 数据类型 |  长度  | 小数位 | 允许空值 | 主键 | 默认值 | 说明 |
| :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
|  1   | id |   int   | 10 |   0    |    N     |  Y   |       |   |
|  2   | user_id |   varchar   | 45 |   0    |    Y     |  N   |       |   |
|  3   | title |   varchar   | 100 |   0    |    Y     |  N   |       |   |
|  4   | content |   text   | 65535 |   0    |    Y     |  N   |       |   |
|  5   | type |   int   | 10 |   0    |    Y     |  N   |       | 0-普通;1-置顶;  |
|  6   | status |   int   | 10 |   0    |    Y     |  N   |       | 0-正常;1-精华;2-拉黑;  |
|  7   | create_time |   timestamp   | 19 |   0    |    Y     |  N   |       |   |
|  8   | comment_count |   int   | 10 |   0    |    Y     |  N   |       |   |
|  9   | score |   double   | 23 |   0    |    Y     |  N   |       |   |

**表名：** <a id="login_ticket">login_ticket</a>

**说明：** 

**数据列：**

| 序号 | 名称 | 数据类型 |  长度  | 小数位 | 允许空值 | 主键 | 默认值 | 说明 |
| :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
|  1   | id |   int   | 10 |   0    |    N     |  Y   |       |   |
|  2   | user_id |   int   | 10 |   0    |    N     |  N   |       |   |
|  3   | ticket |   varchar   | 45 |   0    |    N     |  N   |       |   |
|  4   | status |   int   | 10 |   0    |    Y     |  N   |   0    | 0-有效;1-无效;  |
|  5   | expired |   timestamp   | 19 |   0    |    N     |  N   |   CURRENT_TIMESTAMP    |   |

**表名：** <a id="message">message</a>

**说明：** 

**数据列：**

| 序号 | 名称 | 数据类型 |  长度  | 小数位 | 允许空值 | 主键 | 默认值 | 说明 |
| :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
|  1   | id |   int   | 10 |   0    |    N     |  Y   |       |   |
|  2   | from_id |   int   | 10 |   0    |    Y     |  N   |       |   |
|  3   | to_id |   int   | 10 |   0    |    Y     |  N   |       |   |
|  4   | conversation_id |   varchar   | 45 |   0    |    N     |  N   |       |   |
|  5   | content |   text   | 65535 |   0    |    Y     |  N   |       |   |
|  6   | status |   int   | 10 |   0    |    Y     |  N   |       | 0-未读;1-已读;2-删除;  |
|  7   | create_time |   timestamp   | 19 |   0    |    Y     |  N   |       |   |

**表名：** <a id="QRTZ_BLOB_TRIGGERS">QRTZ_BLOB_TRIGGERS</a>

**说明：** 

**数据列：**

| 序号 | 名称 | 数据类型 |  长度  | 小数位 | 允许空值 | 主键 | 默认值 | 说明 |
| :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
|  1   | SCHED_NAME |   varchar   | 120 |   0    |    N     |  Y   |       |   |
|  2   | TRIGGER_NAME |   varchar   | 190 |   0    |    N     |  Y   |       |   |
|  3   | TRIGGER_GROUP |   varchar   | 190 |   0    |    N     |  Y   |       |   |
|  4   | BLOB_DATA |   blob   | 65535 |   0    |    Y     |  N   |       |   |

**表名：** <a id="QRTZ_CALENDARS">QRTZ_CALENDARS</a>

**说明：** 

**数据列：**

| 序号 | 名称 | 数据类型 |  长度  | 小数位 | 允许空值 | 主键 | 默认值 | 说明 |
| :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
|  1   | SCHED_NAME |   varchar   | 120 |   0    |    N     |  Y   |       |   |
|  2   | CALENDAR_NAME |   varchar   | 190 |   0    |    N     |  Y   |       |   |
|  3   | CALENDAR |   blob   | 65535 |   0    |    N     |  N   |       |   |

**表名：** <a id="QRTZ_CRON_TRIGGERS">QRTZ_CRON_TRIGGERS</a>

**说明：** 

**数据列：**

| 序号 | 名称 | 数据类型 |  长度  | 小数位 | 允许空值 | 主键 | 默认值 | 说明 |
| :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
|  1   | SCHED_NAME |   varchar   | 120 |   0    |    N     |  Y   |       |   |
|  2   | TRIGGER_NAME |   varchar   | 190 |   0    |    N     |  Y   |       |   |
|  3   | TRIGGER_GROUP |   varchar   | 190 |   0    |    N     |  Y   |       |   |
|  4   | CRON_EXPRESSION |   varchar   | 120 |   0    |    N     |  N   |       |   |
|  5   | TIME_ZONE_ID |   varchar   | 80 |   0    |    Y     |  N   |       |   |

**表名：** <a id="QRTZ_FIRED_TRIGGERS">QRTZ_FIRED_TRIGGERS</a>

**说明：** 

**数据列：**

| 序号 | 名称 | 数据类型 |  长度  | 小数位 | 允许空值 | 主键 | 默认值 | 说明 |
| :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
|  1   | SCHED_NAME |   varchar   | 120 |   0    |    N     |  Y   |       |   |
|  2   | ENTRY_ID |   varchar   | 95 |   0    |    N     |  Y   |       |   |
|  3   | TRIGGER_NAME |   varchar   | 190 |   0    |    N     |  N   |       |   |
|  4   | TRIGGER_GROUP |   varchar   | 190 |   0    |    N     |  N   |       |   |
|  5   | INSTANCE_NAME |   varchar   | 190 |   0    |    N     |  N   |       |   |
|  6   | FIRED_TIME |   bigint   | 20 |   0    |    N     |  N   |       |   |
|  7   | SCHED_TIME |   bigint   | 20 |   0    |    N     |  N   |       |   |
|  8   | PRIORITY |   int   | 10 |   0    |    N     |  N   |       |   |
|  9   | STATE |   varchar   | 16 |   0    |    N     |  N   |       |   |
|  10   | JOB_NAME |   varchar   | 190 |   0    |    Y     |  N   |       |   |
|  11   | JOB_GROUP |   varchar   | 190 |   0    |    Y     |  N   |       |   |
|  12   | IS_NONCONCURRENT |   varchar   | 1 |   0    |    Y     |  N   |       |   |
|  13   | REQUESTS_RECOVERY |   varchar   | 1 |   0    |    Y     |  N   |       |   |

**表名：** <a id="QRTZ_JOB_DETAILS">QRTZ_JOB_DETAILS</a>

**说明：** 

**数据列：**

| 序号 | 名称 | 数据类型 |  长度  | 小数位 | 允许空值 | 主键 | 默认值 | 说明 |
| :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
|  1   | SCHED_NAME |   varchar   | 120 |   0    |    N     |  Y   |       |   |
|  2   | JOB_NAME |   varchar   | 190 |   0    |    N     |  Y   |       |   |
|  3   | JOB_GROUP |   varchar   | 190 |   0    |    N     |  Y   |       |   |
|  4   | DESCRIPTION |   varchar   | 250 |   0    |    Y     |  N   |       |   |
|  5   | JOB_CLASS_NAME |   varchar   | 250 |   0    |    N     |  N   |       |   |
|  6   | IS_DURABLE |   varchar   | 1 |   0    |    N     |  N   |       |   |
|  7   | IS_NONCONCURRENT |   varchar   | 1 |   0    |    N     |  N   |       |   |
|  8   | IS_UPDATE_DATA |   varchar   | 1 |   0    |    N     |  N   |       |   |
|  9   | REQUESTS_RECOVERY |   varchar   | 1 |   0    |    N     |  N   |       |   |
|  10   | JOB_DATA |   blob   | 65535 |   0    |    Y     |  N   |       |   |

**表名：** <a id="QRTZ_LOCKS">QRTZ_LOCKS</a>

**说明：** 

**数据列：**

| 序号 | 名称 | 数据类型 |  长度  | 小数位 | 允许空值 | 主键 | 默认值 | 说明 |
| :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
|  1   | SCHED_NAME |   varchar   | 120 |   0    |    N     |  Y   |       |   |
|  2   | LOCK_NAME |   varchar   | 40 |   0    |    N     |  Y   |       |   |

**表名：** <a id="QRTZ_PAUSED_TRIGGER_GRPS">QRTZ_PAUSED_TRIGGER_GRPS</a>

**说明：** 

**数据列：**

| 序号 | 名称 | 数据类型 |  长度  | 小数位 | 允许空值 | 主键 | 默认值 | 说明 |
| :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
|  1   | SCHED_NAME |   varchar   | 120 |   0    |    N     |  Y   |       |   |
|  2   | TRIGGER_GROUP |   varchar   | 190 |   0    |    N     |  Y   |       |   |

**表名：** <a id="QRTZ_SCHEDULER_STATE">QRTZ_SCHEDULER_STATE</a>

**说明：** 

**数据列：**

| 序号 | 名称 | 数据类型 |  长度  | 小数位 | 允许空值 | 主键 | 默认值 | 说明 |
| :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
|  1   | SCHED_NAME |   varchar   | 120 |   0    |    N     |  Y   |       |   |
|  2   | INSTANCE_NAME |   varchar   | 190 |   0    |    N     |  Y   |       |   |
|  3   | LAST_CHECKIN_TIME |   bigint   | 20 |   0    |    N     |  N   |       |   |
|  4   | CHECKIN_INTERVAL |   bigint   | 20 |   0    |    N     |  N   |       |   |

**表名：** <a id="QRTZ_SIMPLE_TRIGGERS">QRTZ_SIMPLE_TRIGGERS</a>

**说明：** 

**数据列：**

| 序号 | 名称 | 数据类型 |  长度  | 小数位 | 允许空值 | 主键 | 默认值 | 说明 |
| :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
|  1   | SCHED_NAME |   varchar   | 120 |   0    |    N     |  Y   |       |   |
|  2   | TRIGGER_NAME |   varchar   | 190 |   0    |    N     |  Y   |       |   |
|  3   | TRIGGER_GROUP |   varchar   | 190 |   0    |    N     |  Y   |       |   |
|  4   | REPEAT_COUNT |   bigint   | 20 |   0    |    N     |  N   |       |   |
|  5   | REPEAT_INTERVAL |   bigint   | 20 |   0    |    N     |  N   |       |   |
|  6   | TIMES_TRIGGERED |   bigint   | 20 |   0    |    N     |  N   |       |   |

**表名：** <a id="QRTZ_SIMPROP_TRIGGERS">QRTZ_SIMPROP_TRIGGERS</a>

**说明：** 

**数据列：**

| 序号 | 名称 | 数据类型 |  长度  | 小数位 | 允许空值 | 主键 | 默认值 | 说明 |
| :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
|  1   | SCHED_NAME |   varchar   | 120 |   0    |    N     |  Y   |       |   |
|  2   | TRIGGER_NAME |   varchar   | 190 |   0    |    N     |  Y   |       |   |
|  3   | TRIGGER_GROUP |   varchar   | 190 |   0    |    N     |  Y   |       |   |
|  4   | STR_PROP_1 |   varchar   | 512 |   0    |    Y     |  N   |       |   |
|  5   | STR_PROP_2 |   varchar   | 512 |   0    |    Y     |  N   |       |   |
|  6   | STR_PROP_3 |   varchar   | 512 |   0    |    Y     |  N   |       |   |
|  7   | INT_PROP_1 |   int   | 10 |   0    |    Y     |  N   |       |   |
|  8   | INT_PROP_2 |   int   | 10 |   0    |    Y     |  N   |       |   |
|  9   | LONG_PROP_1 |   bigint   | 20 |   0    |    Y     |  N   |       |   |
|  10   | LONG_PROP_2 |   bigint   | 20 |   0    |    Y     |  N   |       |   |
|  11   | DEC_PROP_1 |   decimal   | 14 |   4    |    Y     |  N   |       |   |
|  12   | DEC_PROP_2 |   decimal   | 14 |   4    |    Y     |  N   |       |   |
|  13   | BOOL_PROP_1 |   varchar   | 1 |   0    |    Y     |  N   |       |   |
|  14   | BOOL_PROP_2 |   varchar   | 1 |   0    |    Y     |  N   |       |   |

**表名：** <a id="QRTZ_TRIGGERS">QRTZ_TRIGGERS</a>

**说明：** 

**数据列：**

| 序号 | 名称 | 数据类型 |  长度  | 小数位 | 允许空值 | 主键 | 默认值 | 说明 |
| :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
|  1   | SCHED_NAME |   varchar   | 120 |   0    |    N     |  Y   |       |   |
|  2   | TRIGGER_NAME |   varchar   | 190 |   0    |    N     |  Y   |       |   |
|  3   | TRIGGER_GROUP |   varchar   | 190 |   0    |    N     |  Y   |       |   |
|  4   | JOB_NAME |   varchar   | 190 |   0    |    N     |  N   |       |   |
|  5   | JOB_GROUP |   varchar   | 190 |   0    |    N     |  N   |       |   |
|  6   | DESCRIPTION |   varchar   | 250 |   0    |    Y     |  N   |       |   |
|  7   | NEXT_FIRE_TIME |   bigint   | 20 |   0    |    Y     |  N   |       |   |
|  8   | PREV_FIRE_TIME |   bigint   | 20 |   0    |    Y     |  N   |       |   |
|  9   | PRIORITY |   int   | 10 |   0    |    Y     |  N   |       |   |
|  10   | TRIGGER_STATE |   varchar   | 16 |   0    |    N     |  N   |       |   |
|  11   | TRIGGER_TYPE |   varchar   | 8 |   0    |    N     |  N   |       |   |
|  12   | START_TIME |   bigint   | 20 |   0    |    N     |  N   |       |   |
|  13   | END_TIME |   bigint   | 20 |   0    |    Y     |  N   |       |   |
|  14   | CALENDAR_NAME |   varchar   | 190 |   0    |    Y     |  N   |       |   |
|  15   | MISFIRE_INSTR |   smallint   | 6 |   0    |    Y     |  N   |       |   |
|  16   | JOB_DATA |   blob   | 65535 |   0    |    Y     |  N   |       |   |

**表名：** <a id="user">user</a>

**说明：** 

**数据列：**

| 序号 | 名称 | 数据类型 |  长度  | 小数位 | 允许空值 | 主键 | 默认值 | 说明 |
| :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
|  1   | id |   int   | 10 |   0    |    N     |  Y   |       |   |
|  2   | username |   varchar   | 50 |   0    |    Y     |  N   |       |   |
|  3   | password |   varchar   | 50 |   0    |    Y     |  N   |       |   |
|  4   | salt |   varchar   | 50 |   0    |    Y     |  N   |       |   |
|  5   | email |   varchar   | 100 |   0    |    Y     |  N   |       |   |
|  6   | type |   int   | 10 |   0    |    Y     |  N   |       | 0-普通用户;1-超级管理员;2-版主;  |
|  7   | status |   int   | 10 |   0    |    Y     |  N   |       | 0-未激活;1-已激活;  |
|  8   | activation_code |   varchar   | 100 |   0    |    Y     |  N   |       |   |
|  9   | header_url |   varchar   | 200 |   0    |    Y     |  N   |       |   |
|  10   | create_time |   timestamp   | 19 |   0    |    Y     |  N   |       |   |
