package com.javaPro.myProject.modules.sysuser.controller;

import com.javaPro.myProject.common.controller.BaseController;
import com.javaPro.myProject.common.model.AjaxResult;
import com.javaPro.myProject.common.model.ListByPage;
import com.javaPro.myProject.modules.sysuser.entity.Sysuser;
import com.javaPro.myProject.modules.sysuser.service.SysuserService;
import com.javaPro.myProject.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * 用户表(Sysuser)表控制层
 *
 * @author
 * @since  17:36:57
 */
@RestController
@RequestMapping("sysuser")
public class SysuserController extends BaseController {
    /**
     * 服务对象
     */
    @Resource
    private SysuserService sysuserService;

    @Autowired
    private FileUploadService fileUploadService;
    /**
     * 分页查询
     *
     * @param sysuser 筛选条件

     * @return 查询结果
     */
    @GetMapping
    public ListByPage queryByPage(Sysuser sysuser) {
        startPage();
        return getList(this.sysuserService.queryByPage(sysuser));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("detail")
    public AjaxResult queryById(Integer id) {
        return AjaxResult.ok(this.sysuserService.queryById(id));
    }

    /**
     * 新增数据
     *
     * @param sysuser 实体
     * @return 新增结果
     */
    @PostMapping
    public AjaxResult add(@RequestBody Sysuser sysuser) {
        return AjaxResult.ok(this.sysuserService.insert(sysuser));
    }

    /**
     * 编辑数据
     *
     * @param sysuser 实体
     * @return 编辑结果
     */
    @PutMapping
    public AjaxResult edit(@RequestBody Sysuser sysuser) {
        return AjaxResult.ok(this.sysuserService.update(sysuser));
    }
    @PostMapping("editUserPerson")
    public AjaxResult editUserPerson(MultipartFile file, Sysuser sysuser) {
        try {
            // 处理文件上传
            if (file != null && !file.isEmpty()) {
                try {
                    String ossUrl = fileUploadService.uploadFile(file);
                    sysuser.setImg(ossUrl);
                } catch (Exception fileUploadException) {
                    // 如果文件上传失败，记录错误但不阻止用户信息更新
                    System.err.println("文件上传失败，但继续更新用户信息: " + fileUploadException.getMessage());
                    // 可以选择保留原有头像或设置默认头像
                    // sysuser.setImg(null); // 如果想清空头像
                }
            }

            // 更新用户信息
            Sysuser updatedUser = this.sysuserService.update(sysuser);
            return AjaxResult.ok(updatedUser);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error("个人信息更新失败: " + e.getMessage());
        }
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @DeleteMapping
    public AjaxResult deleteById(Integer id) {
        return AjaxResult.ok(this.sysuserService.deleteById(id));
    }

}

