package com.javaPro.myProject.modules.comment.controller;

import com.javaPro.myProject.common.controller.BaseController;
import com.javaPro.myProject.common.model.AjaxResult;
import com.javaPro.myProject.common.model.ListByPage;
import com.javaPro.myProject.modules.comment.entity.Comment;
import com.javaPro.myProject.modules.comment.service.CommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 公告评论(Comment)表控制层
 *
 * @author
 * @since 2025-10-08
 */
@RestController
@RequestMapping("comment")
public class CommentController extends BaseController {
    
    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);
    
    /**
     * 服务对象
     */
    @Resource
    private CommentService commentService;

    /**
     * 分页查询
     *
     * @param comment 筛选条件
     * @return 查询结果
     */
    @GetMapping("list")
    public ListByPage queryByPage(Comment comment) {
        startPage();
        return getList(this.commentService.queryByPage(comment));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("detail")
    public AjaxResult queryById(Integer id) {
        return AjaxResult.ok(this.commentService.queryById(id));
    }

    /**
     * 根据公告ID查询评论列表
     *
     * @param noticeId 公告ID
     * @param currentUserId 当前用户ID（可选）
     * @return 评论列表
     */
    @GetMapping("notice/{noticeId}")
    public AjaxResult queryByNoticeId(@PathVariable("noticeId") Integer noticeId,
                                     @RequestParam(value = "currentUserId", required = false) Integer currentUserId) {
        try {
            List<Comment> comments;
            if (currentUserId != null) {
                comments = this.commentService.getNoticeCommentsWithCurrentUser(noticeId, currentUserId);
            } else {
                comments = this.commentService.getNoticeComments(noticeId);
            }
            return AjaxResult.ok(comments);
        } catch (Exception e) {
            logger.error("查询公告评论失败", e);
            return AjaxResult.error("查询评论失败");
        }
    }

    /**
     * 根据用户ID查询评论列表
     *
     * @param userId 用户ID
     * @return 评论列表
     */
    @GetMapping("user/{userId}")
    public AjaxResult queryByUserId(@PathVariable("userId") Integer userId) {
        try {
            List<Comment> comments = this.commentService.queryByUserId(userId);
            return AjaxResult.ok(comments);
        } catch (Exception e) {
            logger.error("查询用户评论失败", e);
            return AjaxResult.error("查询评论失败");
        }
    }

    /**
     * 统计指定公告的评论数量
     *
     * @param noticeId 公告ID
     * @return 评论数量
     */
    @GetMapping("count/{noticeId}")
    public AjaxResult countByNoticeId(@PathVariable("noticeId") Integer noticeId) {
        try {
            int count = this.commentService.countByNoticeId(noticeId);
            return AjaxResult.ok(count);
        } catch (Exception e) {
            logger.error("统计评论数量失败", e);
            return AjaxResult.error("统计失败");
        }
    }

    /**
     * 新增评论
     *
     * @param comment 实体
     * @return 新增结果
     */
    @PostMapping("add")
    public AjaxResult add(@RequestBody Comment comment) {
        try {
            // 基本验证
            if (comment.getNoticeId() == null) {
                return AjaxResult.error("公告ID不能为空");
            }
            if (comment.getUserId() == null) {
                return AjaxResult.error("用户ID不能为空");
            }
            if (comment.getContent() == null || comment.getContent().trim().isEmpty()) {
                return AjaxResult.error("评论内容不能为空");
            }
            if (comment.getContent().length() > 500) {
                return AjaxResult.error("评论内容不能超过500字");
            }

            Comment result = this.commentService.insert(comment);
            return AjaxResult.ok("评论发表成功", result);
        } catch (Exception e) {
            logger.error("发表评论失败", e);
            return AjaxResult.error("发表评论失败");
        }
    }

    /**
     * 发表评论（简化接口）
     *
     * @param noticeId 公告ID
     * @param userId 用户ID
     * @param content 评论内容
     * @return 新增结果
     */
    @PostMapping("submit")
    public AjaxResult submitComment(@RequestParam Integer noticeId,
                                   @RequestParam Integer userId,
                                   @RequestParam String content) {
        try {
            // 基本验证
            if (noticeId == null) {
                return AjaxResult.error("公告ID不能为空");
            }
            if (userId == null) {
                return AjaxResult.error("用户ID不能为空");
            }
            if (content == null || content.trim().isEmpty()) {
                return AjaxResult.error("评论内容不能为空");
            }
            if (content.length() > 500) {
                return AjaxResult.error("评论内容不能超过500字");
            }

            Comment result = this.commentService.addComment(noticeId, userId, content);
            return AjaxResult.ok("评论发表成功", result);
        } catch (Exception e) {
            logger.error("发表评论失败", e);
            return AjaxResult.error("发表评论失败");
        }
    }

    /**
     * 回复评论
     *
     * @param comment 评论对象
     * @return 新增结果
     */
    @PostMapping("reply")
    public AjaxResult replyComment(@RequestBody Comment comment) {
        try {
            // 基本验证
            if (comment.getNoticeId() == null) {
                return AjaxResult.error("公告ID不能为空");
            }
            if (comment.getUserId() == null) {
                return AjaxResult.error("用户ID不能为空");
            }
            if (comment.getParentId() == null) {
                return AjaxResult.error("父评论ID不能为空");
            }
            if (comment.getContent() == null || comment.getContent().trim().isEmpty()) {
                return AjaxResult.error("回复内容不能为空");
            }
            if (comment.getContent().length() > 500) {
                return AjaxResult.error("回复内容不能超过500字");
            }

            Comment result = this.commentService.replyComment(comment.getNoticeId(), comment.getUserId(), comment.getParentId(), comment.getContent());
            return AjaxResult.ok("回复发表成功", result);
        } catch (Exception e) {
            logger.error("发表回复失败", e);
            return AjaxResult.error("发表回复失败");
        }
    }

    /**
     * 根据公告ID获取评论列表（包含回复）
     *
     * @param noticeId 公告ID
     * @param currentUserId 当前用户ID（可选）
     * @return 评论树形结构
     */
    @GetMapping("tree/{noticeId}")
    public AjaxResult getCommentTree(@PathVariable("noticeId") Integer noticeId,
                                   @RequestParam(value = "currentUserId", required = false) Integer currentUserId) {
        try {
            List<Comment> comments;
            if (currentUserId != null) {
                comments = this.commentService.getCommentTreeWithCurrentUser(noticeId, currentUserId);
            } else {
                comments = this.commentService.getCommentTree(noticeId);
            }
            return AjaxResult.ok(comments);
        } catch (Exception e) {
            logger.error("获取评论树失败", e);
            return AjaxResult.error("获取评论失败");
        }
    }

    /**
     * 获取评论树（支持展开收起）
     *
     * @param noticeId 公告ID
     * @param currentUserId 当前用户ID（可选）
     * @param expandLimit 展开回复的数量限制，默认2条
     * @return 评论树形结构
     */
    @GetMapping("tree-expand/{noticeId}")
    public AjaxResult getCommentTreeWithExpand(@PathVariable("noticeId") Integer noticeId,
                                             @RequestParam(value = "currentUserId", required = false) Integer currentUserId,
                                             @RequestParam(value = "expandLimit", defaultValue = "2") Integer expandLimit) {
        try {
            List<Comment> comments = this.commentService.getCommentTreeWithExpandLimit(noticeId, currentUserId, expandLimit);
            return AjaxResult.ok(comments);
        } catch (Exception e) {
            logger.error("获取评论树失败", e);
            return AjaxResult.error("获取评论失败");
        }
    }

    /**
     * 获取评论的回复（分页）
     *
     * @param parentId 父评论ID
     * @param offset 偏移量
     * @param limit 限制数量
     * @param currentUserId 当前用户ID（可选）
     * @return 回复列表
     */
    @GetMapping("replies/{parentId}")
    public AjaxResult getCommentReplies(@PathVariable("parentId") Integer parentId,
                                      @RequestParam(value = "offset", defaultValue = "0") Integer offset,
                                      @RequestParam(value = "limit", defaultValue = "10") Integer limit,
                                      @RequestParam(value = "currentUserId", required = false) Integer currentUserId) {
        try {
            List<Comment> replies = this.commentService.getCommentRepliesWithLimit(parentId, offset, limit);

            // 如果提供了当前用户ID，标记当前用户的回复
            if (currentUserId != null) {
                for (Comment reply : replies) {
                    reply.setIsCurrentUser(currentUserId.equals(reply.getUserId()));
                }
            }

            return AjaxResult.ok(replies);
        } catch (Exception e) {
            logger.error("获取回复失败", e);
            return AjaxResult.error("获取回复失败");
        }
    }

    /**
     * 统计评论的回复数量
     *
     * @param parentId 父评论ID
     * @return 回复数量
     */
    @GetMapping("replies-count/{parentId}")
    public AjaxResult countReplies(@PathVariable("parentId") Integer parentId) {
        try {
            int count = this.commentService.countReplies(parentId);
            return AjaxResult.ok(count);
        } catch (Exception e) {
            logger.error("统计回复数量失败", e);
            return AjaxResult.error("统计失败");
        }
    }

    /**
     * 编辑数据
     *
     * @param comment 实体
     * @return 编辑结果
     */
    @PutMapping("edit")
    public AjaxResult edit(@RequestBody Comment comment) {
        try {
            if (comment.getId() == null) {
                return AjaxResult.error("评论ID不能为空");
            }
            if (comment.getContent() == null || comment.getContent().trim().isEmpty()) {
                return AjaxResult.error("评论内容不能为空");
            }
            if (comment.getContent().length() > 500) {
                return AjaxResult.error("评论内容不能超过500字");
            }

            Comment result = this.commentService.update(comment);
            return AjaxResult.ok("评论修改成功", result);
        } catch (Exception e) {
            logger.error("修改评论失败", e);
            return AjaxResult.error("修改评论失败");
        }
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @DeleteMapping("del")
    public AjaxResult deleteById(Integer id) {
        try {
            if (id == null) {
                return AjaxResult.error("评论ID不能为空");
            }

            boolean success = this.commentService.softDeleteById(id);
            if (success) {
                return AjaxResult.ok("评论删除成功");
            } else {
                return AjaxResult.error("评论删除失败");
            }
        } catch (Exception e) {
            logger.error("删除评论失败", e);
            return AjaxResult.error("删除评论失败");
        }
    }
}
