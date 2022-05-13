package top.houry.netty.barrage.service.impl;

import top.houry.netty.barrage.entity.BarrageMsgSensitive;
import top.houry.netty.barrage.dao.BarrageMsgSensitiveMapper;
import top.houry.netty.barrage.service.IBarrageMsgSensitiveService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 敏感词汇过滤和转义表 服务实现类
 * </p>
 *
 * @author houry
 * @since 2022-05-13
 */
@Service
public class BarrageMsgSensitiveServiceImpl extends ServiceImpl<BarrageMsgSensitiveMapper, BarrageMsgSensitive> implements IBarrageMsgSensitiveService {

}
