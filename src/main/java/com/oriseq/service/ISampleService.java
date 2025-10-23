package com.oriseq.service;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.oriseq.dtm.dto.LoginUser;
import com.oriseq.dtm.entity.Sample;
import com.oriseq.dtm.vo.*;
import com.oriseq.dtm.vo.sample.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Description: new java files header..
 *
 * @author hacah
 * @version 1.0
 * @date 2024/5/21 13:36
 */
public interface ISampleService extends IService<Sample> {

    /**
     * 提交检验单
     *
     * @param inspectionInfoVO
     * @param user
     * @return
     */
    Sample submitInspectionForm(InspectionInfoVO inspectionInfoVO, LoginUser user);

    /**
     * 查询送检信息
     *
     * @param user
     * @param sampleRequestVO
     * @return
     */
    IPage<SampleInfoVO> getSampleInfo(LoginUser user, SampleRequestVO sampleRequestVO);

    /**
     * ids删除样本和项目
     *
     * @param vo
     */
    void changeSamples(DeleteSampleVO vo);


    /**
     * 用户查询分租其他用户
     *
     * @param userInfo
     * @return
     */
    List<InspectionMissionUserVO> queryOtherGroupUsers(LoginUser userInfo);

    /**
     * 样本过户
     *
     * @param sampleTransferVO
     */
    void sampleTransfer(SampleTransferVO sampleTransferVO);

    /**
     * 样本代管
     *
     * @param sampleHostingVO
     */
    void sampleHosting(SampleHostingVO sampleHostingVO);

    /**
     * 取消代管
     *
     * @param username
     * @param noHostingVO
     */
    void noHosting(String username, NoHostingVO noHostingVO);

    /**
     * 保存导入样本信息
     *
     * @param dataSource
     * @param userInfo
     */
    void saveSampleInfo(List<SampleInfoSheetData.SampleInfo> dataSource, LoginUser userInfo);

    /**
     * 查询所有分租下用户，排除自己
     *
     * @param userInfo
     * @return
     */
    Collection<InspectionMissionUserVO> queryOtherGroupUsersCascader(LoginUser userInfo);

    /**
     * 修改样本数据
     *
     * @param sampleUpdateVO
     */
    void updateSample(SampleUpdateVO sampleUpdateVO);

    /**
     * 修改样本状态
     *
     * @param sampleId
     */
    void updateSampleStatus(Long sampleId);

    /**
     * 得到代管样本信息
     *
     * @param user
     * @param sampleRequestVO
     * @return
     */
    IPage<SampleInfoVO> getEscrowSampleInfo(LoginUser user, SampleRequestVO sampleRequestVO);

    JSONObject getLogisticsInformation(GetLogisticsInformationVO logisticsInformationVO);

    /**
     * 查询所有分租下用户
     *
     * @param userInfo
     * @return
     */
    Collection<InspectionMissionUserVO> queryGroupUsersCascader(LoginUser userInfo);

    /**
     * 修改样本项目状态（能多个）
     *
     * @param sampleProjectsVO
     */
    void changeSampleProjectsStatus(ChangeSampleProjectsVO sampleProjectsVO);


    /**
     * 查询样本数据，并包含项目。根据项目ID筛选样本数据
     * 该方法用于获取经过项目ID过滤后的样本页面数据，支持分页和条件查询
     *
     * @param page       分页对象，用于定义当前页和每页大小等分页信息
     * @param wrapper    查询条件包装器，用于封装查询条件
     * @param projectIds 项目ID列表，用于指定需要查询的项目范围
     * @return 返回一个分页对象，包含过滤后的样本数据
     */
    IPage<Sample> getSamplePageWithProjects(IPage<Sample> page, Wrapper<Object> wrapper, List<Long> projectIds);

    /**
     * 更新样本报告数据
     * 主要用于在系统中修改或更新与样本报告相关的信息
     *
     * @param sampleReportIdUpdateVO 一个封装了样本报告ID更新信息的对象
     */
    void updateSampleReportId(SampleReportIdUpdateVO sampleReportIdUpdateVO);

    /**
     * 获取导出样本信息
     * <p>
     * 主要用于数据导出功能，将筛选后的样本信息以列表的形式返回，以便进一步处理或导出至文件
     *
     * @param user            当前登录用户信息
     * @param sampleRequestVO 样本请求参数对象
     * @return 返回一个SampleInfoVO对象的列表，每个对象代表一个符合筛选条件的样本信息记录如果无符合条件的记录，则返回空列表
     */
    List<? extends Object> getExportSampleInfo(LoginUser user, SampleRequestVO sampleRequestVO);


    /**
     * 获取送检任务汇总  统计不同状态的数量统计不同状态的数量
     *
     * @param user            登录用户对象，包含用户的基本信息，如用户ID、角色等
     *                        该参数用于确定哪些检查任务与当前用户相关
     * @param sampleRequestVO
     * @return 返回一个Map对象
     */
    Map<Integer, Long> summaryOfInspectionTasks(LoginUser user, SampleRequestVO sampleRequestVO);

}
