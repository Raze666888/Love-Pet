package com.javaPro.myProject.modules.comment.dao;

import com.javaPro.myProject.modules.comment.entity.Comment;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 公告评论(Comment)表数据库访问层
 *
 * @author
 * @since 2025-10-08
 */
public interface CommentDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Comment queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param comment 查询条件
     * @return 对象列表
     */
    List<Comment> queryAllByLimit(Comment comment);

    /**
     * 根据公告ID查询评论列表（包含用户信息）
     *
     * @param noticeId 公告ID
     * @return 评论列表
     */
    List<Comment> queryByNoticeId(@Param("noticeId") Integer noticeId);

    /**
     * 根据用户ID查询评论列表
     *
     * @param userId 用户ID
     * @return 评论列表
     */
    List<Comment> queryByUserId(@Param("userId") Integer userId);

    /**
     * 统计指定公告的评论数量
     *
     * @param noticeId 公告ID
     * @return 评论数量
     */
    int countByNoticeId(@Param("noticeId") Integer noticeId);

    /**
     * 新增数据
     *
     * @param comment 实例对象
     * @return 影响行数
     */
    int insert(Comment comment);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<Comment> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<Comment> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<Comment> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<Comment> entities);

    /**
     * 修改数据
     *
     * @param comment 实例对象
     * @return 影响行数
     */
    int update(Comment comment);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

    /**
     * 软删除评论（将状态设为0）
     *
     * @param id 主键
     * @return 影响行数
     */
    int softDeleteById(Integer id);

    /**
     * 根据公告ID删除所有评论
     *
     * @param noticeId 公告ID
     * @return 影响行数
     */
    int deleteByNoticeId(@Param("noticeId") Integer noticeId);

    /**
     * 查询顶级评论（没有父评论的评论）
     *
     * @param noticeId 公告ID
     * @return 顶级评论列表
     */
    List<Comment> queryTopLevelComments(@Param("noticeId") Integer noticeId);

    /**
     * 根据父评论ID查询回复
     *
     * @param parentId 父评论ID
     * @return 回复列表
     */
    List<Comment> queryByParentId(@Param("parentId") Integer parentId);

    /**
     * 根据父评论ID查询回复（分页）
     *
     * @param parentId 父评论ID
     * @param offset 偏移量
     * @param limit 限制数量
     * @return 回复列表
     */
    List<Comment> queryByParentIdWithLimit(@Param("parentId") Integer parentId,
                                          @Param("offset") Integer offset,
                                          @Param("limit") Integer limit);

    /**
     * 统计父评论的回复数量
     *
     * @param parentId 父评论ID
     * @return 回复数量
     */
    int countRepliesByParentId(@Param("parentId") Integer parentId);

    /**
     * 递归查询所有子评论（包括多级回复）
     *
     * @param parentId 父评论ID
     * @return 所有子评论列表
     */
    List<Comment> queryAllChildComments(@Param("parentId") Integer parentId);
}
