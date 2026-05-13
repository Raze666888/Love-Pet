package com.javaPro.myProject.modules.comment.service.impl;

import com.javaPro.myProject.modules.comment.dao.CommentDao;
import com.javaPro.myProject.modules.comment.entity.Comment;
import com.javaPro.myProject.modules.comment.service.CommentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 公告评论(Comment)表服务实现类
 *
 * @author
 * @since 2025-10-08
 */
@Service("commentService")
public class CommentServiceImpl implements CommentService {
    
    @Resource
    private CommentDao commentDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public Comment queryById(Integer id) {
        return this.commentDao.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param comment 筛选条件
     * @return 查询结果
     */
    @Override
    public List<Comment> queryByPage(Comment comment) {
        return this.commentDao.queryAllByLimit(comment);
    }

    /**
     * 根据公告ID查询评论列表
     *
     * @param noticeId 公告ID
     * @return 评论列表
     */
    @Override
    public List<Comment> queryByNoticeId(Integer noticeId) {
        return this.commentDao.queryByNoticeId(noticeId);
    }

    /**
     * 根据用户ID查询评论列表
     *
     * @param userId 用户ID
     * @return 评论列表
     */
    @Override
    public List<Comment> queryByUserId(Integer userId) {
        return this.commentDao.queryByUserId(userId);
    }

    /**
     * 统计指定公告的评论数量
     *
     * @param noticeId 公告ID
     * @return 评论数量
     */
    @Override
    public int countByNoticeId(Integer noticeId) {
        return this.commentDao.countByNoticeId(noticeId);
    }

    /**
     * 新增数据
     *
     * @param comment 实例对象
     * @return 实例对象
     */
    @Override
    public Comment insert(Comment comment) {
        // 设置默认值
        if (comment.getCreateTime() == null) {
            comment.setCreateTime(new Date());
        }
        if (comment.getUpdateTime() == null) {
            comment.setUpdateTime(new Date());
        }
        if (comment.getStatus() == null) {
            comment.setStatus("1");
        }
        
        this.commentDao.insert(comment);
        return comment;
    }

    /**
     * 修改数据
     *
     * @param comment 实例对象
     * @return 实例对象
     */
    @Override
    public Comment update(Comment comment) {
        comment.setUpdateTime(new Date());
        this.commentDao.update(comment);
        return this.queryById(comment.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.commentDao.deleteById(id) > 0;
    }

    /**
     * 软删除评论
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean softDeleteById(Integer id) {
        return this.commentDao.softDeleteById(id) > 0;
    }

    /**
     * 根据公告ID删除所有评论
     *
     * @param noticeId 公告ID
     * @return 是否成功
     */
    @Override
    public boolean deleteByNoticeId(Integer noticeId) {
        return this.commentDao.deleteByNoticeId(noticeId) > 0;
    }

    /**
     * 发表评论（业务方法）
     *
     * @param noticeId 公告ID
     * @param userId 用户ID
     * @param content 评论内容
     * @return 评论对象
     */
    @Override
    public Comment addComment(Integer noticeId, Integer userId, String content) {
        Comment comment = new Comment();
        comment.setNoticeId(noticeId);
        comment.setUserId(userId);
        comment.setContent(content);
        comment.setCreateTime(new Date());
        comment.setUpdateTime(new Date());
        comment.setStatus("1");
        
        return this.insert(comment);
    }

    /**
     * 获取公告的评论列表（包含用户信息和格式化时间）
     *
     * @param noticeId 公告ID
     * @return 评论列表
     */
    @Override
    public List<Comment> getNoticeComments(Integer noticeId) {
        List<Comment> comments = this.commentDao.queryByNoticeId(noticeId);
        
        // 格式化时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (Comment comment : comments) {
            if (comment.getCreateTime() != null) {
                comment.setCreateTimeFormatted(sdf.format(comment.getCreateTime()));
            }
            // 如果用户名为空，设置默认值
            if (comment.getUsername() == null || comment.getUsername().trim().isEmpty()) {
                comment.setUsername("匿名用户");
            }
        }
        
        return comments;
    }

    /**
     * 获取公告的评论列表（包含用户信息和格式化时间，以及当前用户标识）
     *
     * @param noticeId 公告ID
     * @param currentUserId 当前用户ID
     * @return 评论列表
     */
    @Override
    public List<Comment> getNoticeCommentsWithCurrentUser(Integer noticeId, Integer currentUserId) {
        List<Comment> comments = this.getNoticeComments(noticeId);

        // 标记当前用户的评论
        for (Comment comment : comments) {
            comment.setIsCurrentUser(currentUserId != null && currentUserId.equals(comment.getUserId()));
        }

        return comments;
    }

    /**
     * 回复评论
     *
     * @param noticeId 公告ID
     * @param userId 用户ID
     * @param parentId 父评论ID
     * @param content 回复内容
     * @return 回复对象
     */
    @Override
    public Comment replyComment(Integer noticeId, Integer userId, Integer parentId, String content) {
        Comment reply = new Comment();
        reply.setNoticeId(noticeId);
        reply.setUserId(userId);
        reply.setParentId(parentId);
        reply.setContent(content);
        reply.setCreateTime(new Date());
        reply.setUpdateTime(new Date());
        reply.setStatus("1");

        return this.insert(reply);
    }

    /**
     * 获取评论树形结构（顶级评论及其回复）
     *
     * @param noticeId 公告ID
     * @return 评论树
     */
    @Override
    public List<Comment> getCommentTree(Integer noticeId) {
        // 获取顶级评论（没有父评论的评论）
        List<Comment> topComments = this.commentDao.queryTopLevelComments(noticeId);

        // 格式化时间并处理用户信息
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (Comment comment : topComments) {
            if (comment.getCreateTime() != null) {
                comment.setCreateTimeFormatted(sdf.format(comment.getCreateTime()));
            }
            if (comment.getUsername() == null || comment.getUsername().trim().isEmpty()) {
                comment.setUsername("匿名用户");
            }

            // 获取该评论的回复
            List<Comment> replies = this.getCommentReplies(comment.getId());
            comment.setReplies(replies);
        }

        return topComments;
    }

    /**
     * 获取评论树形结构（顶级评论及其回复，包含当前用户标识）
     *
     * @param noticeId 公告ID
     * @param currentUserId 当前用户ID
     * @return 评论树
     */
    @Override
    public List<Comment> getCommentTreeWithCurrentUser(Integer noticeId, Integer currentUserId) {
        List<Comment> comments = this.getCommentTree(noticeId);

        // 递归标记当前用户的评论和回复
        this.markCurrentUserInTree(comments, currentUserId);

        return comments;
    }

    /**
     * 获取评论的所有回复（支持多级回复）
     *
     * @param parentId 父评论ID
     * @return 回复列表
     */
    @Override
    public List<Comment> getCommentReplies(Integer parentId) {
        List<Comment> replies = this.commentDao.queryAllChildComments(parentId);

        // 格式化时间并处理用户信息
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (Comment reply : replies) {
            if (reply.getCreateTime() != null) {
                reply.setCreateTimeFormatted(sdf.format(reply.getCreateTime()));
            }
            if (reply.getUsername() == null || reply.getUsername().trim().isEmpty()) {
                reply.setUsername("匿名用户");
            }
            // 设置回复对象用户名
            if (reply.getReplyToUser() == null || reply.getReplyToUser().trim().isEmpty()) {
                reply.setReplyToUser("原评论者");
            }
        }

        return replies;
    }

    /**
     * 获取评论的回复（分页）
     *
     * @param parentId 父评论ID
     * @param offset 偏移量
     * @param limit 限制数量
     * @return 回复列表
     */
    @Override
    public List<Comment> getCommentRepliesWithLimit(Integer parentId, Integer offset, Integer limit) {
        List<Comment> replies = this.commentDao.queryByParentIdWithLimit(parentId, offset, limit);

        // 格式化时间和处理用户信息
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (Comment reply : replies) {
            if (reply.getCreateTime() != null) {
                reply.setCreateTimeFormatted(sdf.format(reply.getCreateTime()));
            }
            if (reply.getUsername() == null || reply.getUsername().trim().isEmpty()) {
                reply.setUsername("匿名用户");
            }
            // 设置回复对象用户名
            if (reply.getReplyToUser() == null || reply.getReplyToUser().trim().isEmpty()) {
                reply.setReplyToUser("原评论者");
            }

            // 获取该回复的子回复数量
            int childReplyCount = this.commentDao.countRepliesByParentId(reply.getId());
            reply.setReplyCount(childReplyCount);
        }

        return replies;
    }

    /**
     * 统计评论的回复数量（包括所有子回复）
     *
     * @param parentId 父评论ID
     * @return 回复数量
     */
    @Override
    public int countReplies(Integer parentId) {
        // 获取所有子回复
        List<Comment> allReplies = this.commentDao.queryAllChildComments(parentId);
        return allReplies.size();
    }

    /**
     * 获取评论树形结构（支持展开收起）
     *
     * @param noticeId 公告ID
     * @param currentUserId 当前用户ID（可选）
     * @param expandReplyLimit 展开回复的数量限制
     * @return 评论树
     */
    @Override
    public List<Comment> getCommentTreeWithExpandLimit(Integer noticeId, Integer currentUserId, Integer expandReplyLimit) {
        // 获取顶级评论（没有父评论的评论）
        List<Comment> topComments = this.commentDao.queryTopLevelComments(noticeId);

        // 格式化时间并处理用户信息
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (Comment comment : topComments) {
            if (comment.getCreateTime() != null) {
                comment.setCreateTimeFormatted(sdf.format(comment.getCreateTime()));
            }
            if (comment.getUsername() == null || comment.getUsername().trim().isEmpty()) {
                comment.setUsername("匿名用户");
            }

            // 统计回复数量
            int replyCount = this.commentDao.countRepliesByParentId(comment.getId());
            comment.setReplyCount(replyCount);

            // 获取前几条回复
            List<Comment> replies = null;
            if (replyCount > 0) {
                replies = this.getCommentRepliesWithLimit(comment.getId(), 0, expandReplyLimit);
            }
            comment.setReplies(replies);
        }

        // 如果提供了当前用户ID，标记当前用户的评论
        if (currentUserId != null) {
            this.markCurrentUserInTree(topComments, currentUserId);
        }

        return topComments;
    }

    /**
     * 递归标记评论树中的当前用户评论
     *
     * @param comments 评论列表
     * @param currentUserId 当前用户ID
     */
    private void markCurrentUserInTree(List<Comment> comments, Integer currentUserId) {
        if (comments == null || currentUserId == null) return;

        for (Comment comment : comments) {
            comment.setIsCurrentUser(currentUserId.equals(comment.getUserId()));

            // 递归处理回复
            if (comment.getReplies() != null && !comment.getReplies().isEmpty()) {
                this.markCurrentUserInTree(comment.getReplies(), currentUserId);
            }
        }
    }
}
