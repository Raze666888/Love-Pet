package com.javaPro.myProject.modules.message.service.impl;

import com.javaPro.myProject.modules.message.dao.MessageDao;
import com.javaPro.myProject.modules.message.entity.Message;
import com.javaPro.myProject.modules.message.service.MessageService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 消息表(Message)表服务实现类
 *
 * @author
 * @since 19:04:32
 */
@Service("messageService")
public class MessageServiceImpl implements MessageService {
    @Resource
    private MessageDao messageDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public Message queryById(Integer id) {
        return this.messageDao.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param message 筛选条件
     * @return 查询结果
     */
    @Override
    public List<Message> queryByPage(Message message) {
        return this.messageDao.queryAllByLimit(message);
    }

    /**
     * 新增数据
     *
     * @param message 实例对象
     * @return 实例对象
     */
    @Override
    public Message insert(Message message) {
        this.messageDao.insert(message);
        return message;
    }

    /**
     * 修改数据
     *
     * @param message 实例对象
     * @return 实例对象
     */
    @Override
    public Message update(Message message) {
        this.messageDao.update(message);
        return this.queryById(message.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.messageDao.deleteById(id) > 0;
    }

    /**
     * 获取聊天记录
     *
     * @param userId 当前用户ID
     * @param partnerId 聊天对象ID
     * @return 聊天记录列表
     */
    @Override
    public List<Message> getChatMessages(Integer userId, Integer partnerId) {
        return this.messageDao.getChatMessages(userId, partnerId);
    }
}
