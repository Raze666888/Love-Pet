package com.javaPro.myProject.modules.company.controller;

import com.javaPro.myProject.common.controller.BaseController;
import com.javaPro.myProject.common.model.AjaxResult;
import com.javaPro.myProject.common.model.ListByPage;
import com.javaPro.myProject.modules.company.entity.Company;
import com.javaPro.myProject.modules.company.service.CompanyService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 销售商表(Company)表控制层
 *
 * @author
 * @since 07:49:21
 */
@RestController
@RequestMapping("company")
public class CompanyController extends BaseController {
    /**
     * 服务对象
     */
    @Resource
    private CompanyService companyService;

    /**
     * 分页查询
     *
     * @param company 筛选条件
     * @return 查询结果
     */
    @GetMapping
    public ListByPage queryByPage(Company company) {
        startPage();
        return getList(this.companyService.queryByPage(company));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("detail")
    public AjaxResult queryById(Integer id) {
        return AjaxResult.ok(this.companyService.queryById(id));
    }

    /**
     * 新增数据
     *
     * @param company 实体
     * @return 新增结果
     */
    @PostMapping
    public AjaxResult add(@RequestBody Company company) {
        return AjaxResult.ok(this.companyService.insert(company));
    }

    /**
     * 编辑数据
     *
     * @param company 实体
     * @return 编辑结果
     */
    @PutMapping
    public AjaxResult edit(@RequestBody Company company) {
        return AjaxResult.ok(this.companyService.update(company));
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @DeleteMapping
    public AjaxResult deleteById(Integer id) {
        return AjaxResult.ok(this.companyService.deleteById(id));
    }

    /**
     * 根据创建者ID查询服务商信息
     *
     * @param createid 创建者ID（用户ID）
     * @return 服务商信息
     */
    @GetMapping("byCreateId")
    public AjaxResult queryByCreateId(Integer createid) {
        return AjaxResult.ok(this.companyService.queryByCreateId(createid));
    }

}

