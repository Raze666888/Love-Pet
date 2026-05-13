package com.javaPro.myProject.modules.message.controller;

import com.javaPro.myProject.common.controller.BaseController;
import com.javaPro.myProject.common.model.AjaxResult;
import com.javaPro.myProject.common.model.ListByPage;
import com.javaPro.myProject.modules.message.entity.Message;
import com.javaPro.myProject.modules.message.service.MessageService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 消息表(Message)表控制层
 *
 * @author
 * @since 19:04:32
 */
@RestController
@RequestMapping("message")
public class MessageController extends BaseController {
    /**
     * 服务对象
     */
    @Resource
    private MessageService messageService;

    /**
     * 分页查询
     *
     * @param message 筛选条件
     * @return 查询结果
     */
    @GetMapping
    public ListByPage queryByPage(Message message) {
        startPage();
        return getList(this.messageService.queryByPage(message));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("detail")
    public AjaxResult queryById(Integer id) {
        return AjaxResult.ok(this.messageService.queryById(id));
    }

    /**
     * 新增数据 (JSON格式)
     *
     * @param message 实体
     * @return 新增结果
     */
    @PostMapping
    public AjaxResult add(@RequestBody Message message) {
        return AjaxResult.ok(this.messageService.insert(message));
    }

    /**
     * 新增数据 (表单格式)
     *
     * @param content 消息内容
     * @param senderid 发送者ID
     * @param receiveid 接收者ID
     * @param status 状态
     * @return 新增结果
     */
    @PostMapping("/form")
    public AjaxResult addByForm(@RequestParam String content,
                               @RequestParam Integer senderid,
                               @RequestParam Integer receiveid,
                               @RequestParam(defaultValue = "0") String status) {
        Message message = new Message();
        message.setContent(content);
        message.setSenderid(senderid);
        message.setReceiveid(receiveid);
        message.setStatus(status);
        return AjaxResult.ok(this.messageService.insert(message));
    }

    /**
     * 编辑数据
     *
     * @param message 实体
     * @return 编辑结果
     */
    @PutMapping
    public AjaxResult edit(@RequestBody Message message) {
        return AjaxResult.ok(this.messageService.update(message));
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @DeleteMapping
    public AjaxResult deleteById(Integer id) {
        return AjaxResult.ok(this.messageService.deleteById(id));
    }

    /**
     * 获取聊天记录
     *
     * @param userId 当前用户ID
     * @param partnerId 聊天对象ID
     * @return 聊天记录
     */
    @GetMapping("/chat")
    public ListByPage getChatMessages(@RequestParam Integer userId, @RequestParam Integer partnerId) {
        try {
            return getList(this.messageService.getChatMessages(userId, partnerId));
        } catch (Exception e) {
            e.printStackTrace();
            return getList(500, "获取聊天记录失败: " + e.getMessage());
        }
    }

}

