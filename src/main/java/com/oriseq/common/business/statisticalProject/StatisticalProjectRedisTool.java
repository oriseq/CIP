package com.oriseq.common.business.statisticalProject;

import com.oriseq.dtm.entity.SampleProject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Description: 操作redis工具类
 *
 * @author hacah
 * @version 1.0
 * @date 2025/1/21 9:20
 */
@Component
public class StatisticalProjectRedisTool {


    private static final String KEY_PREFIX = "statisticalProject:group:";
    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 统计次数并记录到redis
     * <p>
     * statisticalProject:group:{groupId}  :   [{projectId}:1, {projectId2}:100,...]
     *
     * @param userGroupId
     * @param sampleProjects
     */
    public void statisticalProject(Long userGroupId, List<SampleProject> sampleProjects) {
        // 使用Redis的hash结构存储数据，键为group:{groupId}
        String redisKey = KEY_PREFIX + userGroupId;

        // 遍历sampleProjects列表，更新Redis中的计数
        for (SampleProject project : sampleProjects) {
            String projectKey = project.getProjectId().toString();

            // 检查Redis中是否存在该项目的计数，如果存在则加1，否则初始化为1
            redisTemplate.opsForHash().increment(redisKey, projectKey, 1);
        }
    }

    /**
     * 获取某个项目提交次数
     *
     * @param userGroupId
     * @param projectId
     * @return
     */
    public Long getProjectCount(Long userGroupId, Long projectId) {
        String redisKey = KEY_PREFIX + userGroupId;
        String projectKey = projectId.toString();
        Object count = redisTemplate.opsForHash().get(redisKey, projectKey);
        return count == null ? 0L : Long.parseLong(count.toString());
    }

    /**
     * 获取某个组的所有项目提交次数
     *
     * @param userGroupId
     * @return
     */
    public Map<Object, Object> getGroupProjectCount(Long userGroupId) {
        String redisKey = KEY_PREFIX + userGroupId;
        return redisTemplate.opsForHash().entries(redisKey);
    }
}
