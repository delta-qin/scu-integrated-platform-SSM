package com.deltaqin.scussm.quartz;

import com.deltaqin.scussm.common.CommunityConstant;
import com.deltaqin.scussm.common.utils.RedisKeyUtil;
import com.deltaqin.scussm.entity.DiscussPost;
import com.deltaqin.scussm.service.DiscussPostService;
import com.deltaqin.scussm.service.ElasticsearchService;
import com.deltaqin.scussm.service.LikeService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author deltaqin
 * @date 2021/6/23 下午11:59
 */

public class PostScoreRefreshJob implements Job, CommunityConstant {

    private static final Logger logger = LoggerFactory.getLogger(PostScoreRefreshJob.class);

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private ElasticsearchService elasticsearchService;

    // 纪元
    private static final Date epoch;

    static {
        try{
            epoch = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2014-08-01 00:00:00");
        }catch (ParseException e){
            throw new RuntimeException("初始化论坛纪元失败!", e);
        }
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        String redisKey = RedisKeyUtil.getPostScoreKey();
        // 返回对绑定到给定键的集合值执行的操作。
        BoundSetOperations operations = redisTemplate.boundSetOps(redisKey);
        if(operations.size()==0){
            logger.info("[任务取消] 没有需要刷新的帖子!");
            return;
        }
        logger.info("[任务开始] 正在刷新帖子分数: " + operations.size());
        while (operations.size()>0) {
            // 集合的值，也即是帖子的 ID
            this.refresh((Integer) operations.pop());
        }
        logger.info("[任务结束] 帖子分数刷新完毕!");
    }

    private void refresh(int postId) {
        DiscussPost post = discussPostService.findDiscussPostById(postId);

        if (post == null) {
            logger.error("该帖子不存在: id = " + postId);
            return;
        }

        // 是否精华
        boolean wonderful = post.getStatus() == 1;
        // 评论数量
        int commentCount = post.getCommentCount();
        // 点赞数量
        long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_POST, postId);

        // 计算权重
        double w = (wonderful ? 75 : 0) + commentCount * 10 + likeCount * 2;
        // 分数 = 帖子权重 + 距离天数
        double score = Math.log10(Math.max(w, 1))
                + (post.getCreateTime().getTime() - epoch.getTime()) / (1000 * 3600 * 24);

        // 更新数据库和ES=========================================
        // 更新帖子分数
        discussPostService.updateScore(postId, score);
        // 同步搜索数据
        post.setScore(score);
        elasticsearchService.saveDiscussPost(post);
    }
}

