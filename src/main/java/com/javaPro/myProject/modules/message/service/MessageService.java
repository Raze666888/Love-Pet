package com.javaPro.myProject.modules.message.service;

import com.javaPro.myProject.modules.message.entity.Message;

import java.util.List;

/**
 * 消息表(Message)表服务接口
 *
 * @author
 * @since 19:04:32
 */
public interface MessageService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Message queryById(Integer id);

    /**
     * 分页查询
     *
     * @param message 筛选条件
     * @return 查询结果
     */
    List<Message> queryByPage(Message message);

    /**
     * 新增数据
     *
     * @param message 实例对象
     * @return 实例对象
     */
    Message insert(Message message);

    /**
     * 修改数据
     *
     * @param message 实例对象
     * @return 实例对象
     */
    Message update(Message message);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

    /**
     * 获取聊天记录
     *
     * @param userId 当前用户ID
     * @param partnerId 聊天对象ID
     * @return 聊天记录列表
     */
    List<Message> getChatMessages(Integer userId, Integer partnerId);

}
