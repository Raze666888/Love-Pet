package com.javaPro.myProject.modules.comment.service;

import com.javaPro.myProject.modules.comment.entity.Comment;

import java.util.List;

/**
 * 公告评论(Comment)表服务接口
 *
 * @author
 * @since 2025-10-08
 */
public interface CommentService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Comment queryById(Integer id);

    /**
     * 分页查询
     *
     * @param comment 筛选条件
     * @return 查询结果
     */
    List<Comment> queryByPage(Comment comment);

    /**
     * 根据公告ID查询评论列表
     *
     * @param noticeId 公告ID
     * @return 评论列表
     */
    List<Comment> queryByNoticeId(Integer noticeId);

    /**
     * 根据用户ID查询评论列表
     *
     * @param userId 用户ID
     * @return 评论列表
     */
    List<Comment> queryByUserId(Integer userId);

    /**
     * 统计指定公告的评论数量
     *
     * @param noticeId 公告ID
     * @return 评论数量
     */
    int countByNoticeId(Integer noticeId);

    /**
     * 新增数据
     *
     * @param comment 实例对象
     * @return 实例对象
     */
    Comment insert(Comment comment);

    /**
     * 修改数据
     *
     * @param comment 实例对象
     * @return 实例对象
     */
    Comment update(Comment comment);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

    /**
     * 软删除评论
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean softDeleteById(Integer id);

    /**
     * 根据公告ID删除所有评论
     *
     * @param noticeId 公告ID
     * @return 是否成功
     */
    boolean deleteByNoticeId(Integer noticeId);

    /**
     * 发表评论（业务方法）
     *
     * @param noticeId 公告ID
     * @param userId 用户ID
     * @param content 评论内容
     * @return 评论对象
     */
    Comment addComment(Integer noticeId, Integer userId, String content);

    /**
     * 获取公告的评论列表（包含用户信息和格式化时间）
     *
     * @param noticeId 公告ID
     * @return 评论列表
     */
    List<Comment> getNoticeComments(Integer noticeId);

    /**
     * 获取公告的评论列表（包含用户信息和格式化时间，以及当前用户标识）
     *
     * @param noticeId 公告ID
     * @param currentUserId 当前用户ID
     * @return 评论列表
     */
    List<Comment> getNoticeCommentsWithCurrentUser(Integer noticeId, Integer currentUserId);

    /**
     * 回复评论
     *
     * @param noticeId 公告ID
     * @param userId 用户ID
     * @param parentId 父评论ID
     * @param content 回复内容
     * @return 回复对象
     */
    Comment replyComment(Integer noticeId, Integer userId, Integer parentId, String content);

    /**
     * 获取评论树形结构（顶级评论及其回复）
     *
     * @param noticeId 公告ID
     * @return 评论树
     */
    List<Comment> getCommentTree(Integer noticeId);

    /**
     * 获取评论树形结构（顶级评论及其回复，包含当前用户标识）
     *
     * @param noticeId 公告ID
     * @param currentUserId 当前用户ID
     * @return 评论树
     */
    List<Comment> getCommentTreeWithCurrentUser(Integer noticeId, Integer currentUserId);

    /**
     * 获取评论的所有回复
     *
     * @param parentId 父评论ID
     * @return 回复列表
     */
    List<Comment> getCommentReplies(Integer parentId);

    /**
     * 获取评论的回复（分页）
     *
     * @param parentId 父评论ID
     * @param offset 偏移量
     * @param limit 限制数量
     * @return 回复列表
     */
    List<Comment> getCommentRepliesWithLimit(Integer parentId, Integer offset, Integer limit);

    /**
     * 统计评论的回复数量
     *
     * @param parentId 父评论ID
     * @return 回复数量
     */
    int countReplies(Integer parentId);

    /**
     * 获取评论树形结构（支持展开收起）
     *
     * @param noticeId 公告ID
     * @param currentUserId 当前用户ID（可选）
     * @param expandReplyLimit 展开回复的数量限制
     * @return 评论树
     */
    List<Comment> getCommentTreeWithExpandLimit(Integer noticeId, Integer currentUserId, Integer expandReplyLimit);
}
