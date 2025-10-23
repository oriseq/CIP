package com.oriseq.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oriseq.dtm.dto.LoginUser;
import com.oriseq.dtm.dto.ProjectListDTO;
import com.oriseq.dtm.dto.ProjectPriceDTO;
import com.oriseq.dtm.dto.ProjectStatisticsDTO;
import com.oriseq.dtm.entity.Package;
import com.oriseq.dtm.entity.ProjectReferences;
import com.oriseq.dtm.vo.billStatistics.BillStatisticsRespVO;
import com.oriseq.dtm.vo.billStatistics.BillStatisticsVO;
import com.oriseq.dtm.vo.project.ProjectSearchVO;
import com.oriseq.mapper.PackageMapper;
import com.oriseq.mapper.ProjectReferencesMapper;
import com.oriseq.service.ProjectReferencesService;
import io.jsonwebtoken.lang.Maps;
import io.micrometer.common.util.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author huang
 * @description 针对表【project_references(项目引用表)】的数据库操作Service实现
 * @createDate 2024-11-11 14:57:06
 */
@Service
public class ProjectReferencesServiceImpl extends ServiceImpl<ProjectReferencesMapper, ProjectReferences>
        implements ProjectReferencesService {

    @Autowired
    private ProjectReferencesMapper projectReferencesMapper;
    @Autowired
    private PackageMapper packageMapper;

    /**
     * 在每一个年份分组内部去统计，相同的项目id做分组，统计为出送检量（相同id的数量）和费用（discountedPrice的数量）
     * 年：{
     * 项目id：{
     * 数据
     * }
     * }
     *
     * @param timeGroupMap
     * @return
     */
    @NotNull
    private static Map<String, Map<Long, BillStatisticsRespVO>> getStaticMapMap(Map<String, List<ProjectStatisticsDTO>> timeGroupMap) {
        Map<String, Map<Long, BillStatisticsRespVO>> timeProjectStats = timeGroupMap.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> entry.getKey(),
                        entry -> entry.getValue().stream()
                                .collect(Collectors.groupingBy(ProjectStatisticsDTO::getProjectId,
                                        Collectors.collectingAndThen(
                                                Collectors.toList(),
                                                list -> {
                                                    BillStatisticsRespVO stats = new BillStatisticsRespVO();
                                                    stats.setProjectName(list.get(0).getProjectName());
                                                    stats.setTotalNum(list.size());
                                                    stats.setPrice(list.get(0).getPrice());
                                                    stats.setTimeGroup(entry.getKey());
                                                    // 计算总价
                                                    stats.setTotalPrice(list.stream()
                                                            .map(ProjectStatisticsDTO::getDiscountedPrice) // 获取每个项目的折扣价格
                                                            .filter(Objects::nonNull)
                                                            .reduce(BigDecimal.ZERO, BigDecimal::add));
//                                                    JSONObject entries = JSONUtil.parseObj(stats);
                                                    // 3. 再基于前面相同的项目id做分组后，再对相同用户组id的项目分组（sampleUserGroupId），统计不同用户组的出送检量（相同id的数量）和费用（discountedPrice的数量）
                                                    // 统计出数据到entries，key：{num or price}groupName，value：统计结果
                                                    Map<Long, Map<String, Object>> userGroupStats = list.stream()
                                                            .collect(Collectors.groupingBy(
                                                                    dto -> dto.getSampleUserGroupId(), // 按用户组ID分组
                                                                    Collectors.collectingAndThen(
                                                                            Collectors.toList(),
                                                                            (List<ProjectStatisticsDTO> groupList) -> {
                                                                                Map<String, Object> statsEachGroup = new HashMap<>();
                                                                                statsEachGroup.put("num", groupList.size()); // 出送检量
                                                                                statsEachGroup.put("totalPrice", groupList.stream()
                                                                                        .map(ProjectStatisticsDTO::getDiscountedPrice)
                                                                                        .filter(Objects::nonNull)
                                                                                        .reduce(BigDecimal.ZERO, BigDecimal::add));
                                                                                statsEachGroup.put("settlementPrice", groupList.get(0).getDiscountedPrice());
                                                                                return statsEachGroup;
                                                                            }
                                                                    )
                                                            ));

                                                    // 将用户组统计数据存入 entries，使用 groupName 作为键的一部分
                                                    userGroupStats.forEach((userGroupId, statsEachGroup) -> {
                                                        BillStatisticsRespVO.GroupData groupData = new BillStatisticsRespVO.GroupData();
                                                        groupData.setTotalPrice((BigDecimal) statsEachGroup.get("totalPrice"));
                                                        groupData.setNum((Integer) statsEachGroup.get("num"));
                                                        groupData.setSettlementPrice((BigDecimal) statsEachGroup.get("settlementPrice"));
                                                        stats.getGroupDataMap().put(userGroupId, groupData);
                                                        // 用户组出送检量
//                                                        entries.put("num" + groupName, statsEachGroup.get("num"));
                                                        // 用户组费用
//                                                        entries.put("price" + groupName, statsEachGroup.get("price"));
                                                        // 结算单价
//                                                        entries.put("settlementPrice" + groupName, statsEachGroup.get("settlementPrice"));
                                                    });
                                                    return stats;
                                                }
                                        )
                                ))
                ));

        return timeProjectStats;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<ProjectListDTO> getProjects(LoginUser userInfo, ProjectSearchVO projectSearchVO) {
        QueryWrapper<Object> queryWrapper = new QueryWrapper<>();
        // 如果是超级管理员或内部组不用加条件，直接查询所有
        if (!userInfo.isInternalGroup() && !userInfo.isSuper()) {
            queryWrapper.eq("user_group_id", userInfo.getUserGroupId());
        }
        // 过滤项目引用ids
        if (projectSearchVO.getProjectIds() != null && !projectSearchVO.getProjectIds().isEmpty()) {
            queryWrapper.in("pr.project_id", projectSearchVO.getProjectIds());
        }
        if (StringUtils.isNotBlank(projectSearchVO.getUserGroupId())) {
            queryWrapper.eq("user_group_id", projectSearchVO.getUserGroupId());
        }
        Optional.ofNullable(projectSearchVO.getBelongingPackage()).ifPresent(packageId -> {
            Package aPackage = packageMapper.selectById(packageId);
            Long userGroupId = aPackage.getUserGroupId();
            if (StringUtils.isNotBlank(aPackage.getProjectIdList())) {
                queryWrapper.in("pm.id", aPackage.getProjectIdList().split(","));
                queryWrapper.eq("pr.user_group_id", userGroupId);
            }
        });
        List<ProjectListDTO> projectListDTOS = projectReferencesMapper.selectProjects(queryWrapper);
        // 非超级管理员去掉价格系数,和折后价格 可优化
        if (!userInfo.isSuper()) {
            projectListDTOS.forEach(projectListDTO -> {
                projectListDTO.setPriceCoefficient(null);
                projectListDTO.setDiscountedPrice(null);
            });
        }
        // 排序id升序
        List<ProjectListDTO> list = projectListDTOS.stream().sorted(Comparator.comparing(ProjectListDTO::getId)).toList();
        return list;
    }

    @Override
    public ProjectPriceDTO getSUMProjectPrice(QueryWrapper<Object> in, QueryWrapper<Object> ined, Long userGroupId) {
        return baseMapper.getSUMProjectPrice(in, ined, userGroupId);
    }

    @Override
    public List<JSONObject> staticProjectNumAndSpend(BillStatisticsVO billStatisticsVO, LoginUser userInfo) {
        /*
        构建返回格式
                [{
                    projectName:'促卵泡成熟激素',
                    timeGroup:'2025-01',
                    totalNum:10,
                    totalPrice:102.5,
                    price:23,
                    ...
                    // 命名规则 {数量or价格}{用户组名}
                    groupDataMap:{
                    "19": {
                        "num": 1,
                        "totalPrice": 141.75,
                        "settlementPrice": 141.75
                    },
                    "26": {
                        "num": 1,
                        "totalPrice": 95.63,
                        "settlementPrice": 95.63
                    }
                }
                },
                 ]
         */

        QueryWrapper<Object> queryWrapper = new QueryWrapper<>();
        // 条件查询
        if (!Objects.isNull(billStatisticsVO)) {
            queryWrapper.in(CollUtil.isNotEmpty(billStatisticsVO.getProjectIds()), "sp.project_id", billStatisticsVO.getProjectIds());
            if (billStatisticsVO.getCreationTime() != null && billStatisticsVO.getCreationTime().size() == 2) {
                queryWrapper.ge("sp.creation_time", billStatisticsVO.getCreationTime().get(0));
                queryWrapper.le("sp.creation_time", billStatisticsVO.getCreationTime().get(1));
            }
        }
        // 只查询非内部组数据
        queryWrapper.eq(true, "ug.is_internal_group", 0);
        // 用户组状态为启用的数据
        queryWrapper.eq(true, "ug.avail_status", 1);
        // 非内部组和超级管理员，只查询自己组本身数据
        if (!userInfo.isSuper() && !userInfo.getIsInternalGroup()) {
            queryWrapper.eq("s.sample_user_group_id", userInfo.getUserGroupId());
        }
        List<ProjectStatisticsDTO> projectNumAndSpend = baseMapper.getProjectNumAndSpend(queryWrapper);
        String timeGroup = Optional.ofNullable(billStatisticsVO).isPresent() ? billStatisticsVO.getTimeGroup() : null;
        Map<String, Map<Long, BillStatisticsRespVO>> projectStats = null;
        // 统计和格式化
        if ("y".equals(timeGroup)) {
            // 1.从creationTime按年份分组，比如同一个项目，出现不同的年份那就存在多个记录
            // 2.在每一个年份分组内部去统计，相同的项目id做分组，统计为出送检量（相同id的数量）和费用（discountedPrice的数量），这是总的数据。
            // 3.再基于前面相同的项目id做分组后，再对相同用户组id的项目分组（sampleUserGroupId），统计不同用户组的出送检量（相同id的数量）和费用（discountedPrice的数量）

            // 1. 从 creationTime 按年份分组
            Map<String, List<ProjectStatisticsDTO>> yearGroupMap = projectNumAndSpend.stream()
                    .collect(Collectors.groupingBy(dto -> dto.getCreationTime().format(DateTimeFormatter.ofPattern("yyyy"))));
            System.out.println(yearGroupMap);
            Map<String, Map<Long, BillStatisticsRespVO>> yearlyProjectStats = getStaticMapMap(yearGroupMap);
            projectStats = yearlyProjectStats;

        } else if ("m".equals(timeGroup)) {
            // 按年-月分组
            Map<String, List<ProjectStatisticsDTO>> yearMonthGroupMap = projectNumAndSpend.stream()
                    .collect(Collectors.groupingBy(dto -> dto.getCreationTime().format(DateTimeFormatter.ofPattern("yyyy-MM"))));
            Map<String, Map<Long, BillStatisticsRespVO>> yearMonthProjectStats = getStaticMapMap(yearMonthGroupMap);
            projectStats = yearMonthProjectStats;

        } else if ("d".equals(timeGroup)) {
            // 按年-月-日分组
            Map<String, List<ProjectStatisticsDTO>> yearMonthDayGroupMap = projectNumAndSpend.stream()
                    .collect(Collectors.groupingBy(dto -> dto.getCreationTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))));
            Map<String, Map<Long, BillStatisticsRespVO>> yearMonthDayProjectStats = getStaticMapMap(yearMonthDayGroupMap);
            projectStats = yearMonthDayProjectStats;


        } else {
            // 所有
            // 适配方法的使用，设置key值
            Map<String, List<ProjectStatisticsDTO>> listMap = Maps.of("所有", projectNumAndSpend).build();
            Map<String, Map<Long, BillStatisticsRespVO>> allProjectStats = getStaticMapMap(listMap);
            projectStats = allProjectStats;
        }
        // 对projectStats拆分
        List<BillStatisticsRespVO> billStatisticsRespVOS = new ArrayList<>(projectStats.values().stream().flatMap(map -> map.values().stream()).toList());


        // 新增一条数据，属于所有项目的汇总，项目名为“所有”，统计所有项目num，price，同时统计每个项目组名对应的num，price，有的就加上
        if (!billStatisticsRespVOS.isEmpty()) {
            BigDecimal totalNum = BigDecimal.ZERO;
            BigDecimal totalPrice = BigDecimal.ZERO;

            // 用于存储每个用户组的数量和价格
            Map<Long, BigDecimal> groupNumMap = new LinkedHashMap<>();
            Map<Long, BigDecimal> groupPriceMap = new LinkedHashMap<>();

            for (BillStatisticsRespVO jsonObject : billStatisticsRespVOS) {
                // 累加总数量和总价格
                totalNum = totalNum.add(BigDecimal.valueOf(jsonObject.getTotalNum()));
                totalPrice = totalPrice.add(jsonObject.getTotalPrice());

                // 统计每个用户组的数量和价格
                Map<Long, BillStatisticsRespVO.GroupData> groupDataMap = jsonObject.getGroupDataMap();
                for (Map.Entry<Long, BillStatisticsRespVO.GroupData> entry : groupDataMap.entrySet()) {
                    Long groupId = entry.getKey();
                    BillStatisticsRespVO.GroupData groupData = entry.getValue();
                    Integer num = groupData.getNum();
                    BigDecimal price = groupData.getTotalPrice();

                    // 更新数量和价格
                    groupNumMap.put(groupId, groupNumMap.getOrDefault(groupId, BigDecimal.ZERO).add(BigDecimal.valueOf(num)));
                    groupPriceMap.put(groupId, groupPriceMap.getOrDefault(groupId, BigDecimal.ZERO).add(price));
                }
            }
            BillStatisticsRespVO summary = new BillStatisticsRespVO();
            summary.setProjectName("所有");
            summary.setTimeGroup("所有");
            summary.setTotalNum(totalNum.intValue());
            summary.setTotalPrice(totalPrice);

            for (Map.Entry<Long, BigDecimal> entry : groupNumMap.entrySet()) {
                Long groupId = entry.getKey();
                Integer num = entry.getValue().intValue();
                BigDecimal price = groupPriceMap.get(groupId);

                BillStatisticsRespVO.GroupData groupData = new BillStatisticsRespVO.GroupData();
                groupData.setNum(num);
                groupData.setTotalPrice(price);

                summary.getGroupDataMap().put(groupId, groupData);
            }
            // 添加到结果列表中
            // 新增：生成时间维度下的“所有”汇总项
            List<BillStatisticsRespVO> timeGroupSummaries = generateTimeGroupSummary(projectStats, timeGroup);
            List<JSONObject> jsonObjectListAll = Stream.concat(
                    Stream.concat(Stream.of(summary), timeGroupSummaries.stream())
                    , billStatisticsRespVOS.stream()).map(billStatisticsRespVO -> JSONUtil.parseObj(billStatisticsRespVO)).toList();


            return jsonObjectListAll;

        }
        List<JSONObject> jsonObjectList = billStatisticsRespVOS.stream().map(billStatisticsRespVO -> JSONUtil.parseObj(billStatisticsRespVO)).toList();
        return jsonObjectList;

    }

    /**
     * 生成指定时间粒度的汇总项，如：2025-04-05-所有、2025-04-所有、2025-所有
     *
     * @param baseStats 原始分组数据
     * @param timeGroup 时间粒度 y/m/d
     * @return 汇总后的 BillStatisticsRespVO 列表
     */
    private List<BillStatisticsRespVO> generateTimeGroupSummary(@NotNull Map<String, Map<Long, BillStatisticsRespVO>> baseStats, String timeGroup) {
        List<BillStatisticsRespVO> summaries = new ArrayList<>();
        if (!("d".equals(timeGroup) || "m".equals(timeGroup) || "y".equals(timeGroup))) {
            return summaries;
        }
        for (Map.Entry<String, Map<Long, BillStatisticsRespVO>> entry : baseStats.entrySet()) {
            String timeKey = entry.getKey(); // 如 "2025", "2025-04", "2025-04-05"
            Map<Long, BillStatisticsRespVO> projectMap = entry.getValue();

            BillStatisticsRespVO summary = new BillStatisticsRespVO();
            summary.setProjectName(timeKey + "_所有");
            summary.setTimeGroup(timeKey);

            BigDecimal totalNum = BigDecimal.ZERO;
            BigDecimal totalPrice = BigDecimal.ZERO;

            Map<Long, BigDecimal> groupNumMap = new LinkedHashMap<>();
            Map<Long, BigDecimal> groupPriceMap = new LinkedHashMap<>();

            for (BillStatisticsRespVO vo : projectMap.values()) {
                totalNum = totalNum.add(BigDecimal.valueOf(vo.getTotalNum()));
                totalPrice = totalPrice.add(vo.getTotalPrice());

                for (Map.Entry<Long, BillStatisticsRespVO.GroupData> groupEntry : vo.getGroupDataMap().entrySet()) {
                    Long groupId = groupEntry.getKey();
                    BillStatisticsRespVO.GroupData data = groupEntry.getValue();

                    groupNumMap.put(groupId, groupNumMap.getOrDefault(groupId, BigDecimal.ZERO).add(BigDecimal.valueOf(data.getNum())));
                    groupPriceMap.put(groupId, groupPriceMap.getOrDefault(groupId, BigDecimal.ZERO).add(data.getTotalPrice()));
                }
            }

            summary.setTotalNum(totalNum.intValue());
            summary.setTotalPrice(totalPrice);

            for (Map.Entry<Long, BigDecimal> groupEntry : groupNumMap.entrySet()) {
                Long groupId = groupEntry.getKey();
                Integer num = groupEntry.getValue().intValue();
                BigDecimal price = groupPriceMap.get(groupId);

                BillStatisticsRespVO.GroupData groupData = new BillStatisticsRespVO.GroupData();
                groupData.setNum(num);
                groupData.setTotalPrice(price);
                summary.getGroupDataMap().put(groupId, groupData);
            }

            summaries.add(summary);
        }

        return summaries;
    }

}




