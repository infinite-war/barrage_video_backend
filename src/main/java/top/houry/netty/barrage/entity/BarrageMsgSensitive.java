package top.houry.netty.barrage.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 敏感词汇过滤和转义表
 * </p>
 *
 * @author houry
 * @since 2022-05-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_barrage_msg_sensitive")
public class BarrageMsgSensitive implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Integer id;

    /**
     * 敏感词
     */
    private String sensitiveMsg;

    /**
     * 展示词
     */
    private String showMsg;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;


}
