package top.houry.netty.barrage.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import top.houry.netty.barrage.entity.BarrageMsg;
import top.houry.netty.barrage.dao.BarrageMsgMapper;
import top.houry.netty.barrage.service.IBarrageMsgService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author houry
 * @since 2022-02-03
 */
@Service
public class BarrageMsgServiceImpl extends ServiceImpl<BarrageMsgMapper, BarrageMsg> implements IBarrageMsgService {

    @Override
    public boolean saveBarrageMsg(BarrageMsg barrageMsg) {
        return save(barrageMsg);
    }

    @Override
    public List<BarrageMsg> getListByVideoId(String videoId) {
        Page<BarrageMsg> page = new Page<BarrageMsg>().setCurrent(1).setSize(80);
        return page(page, Wrappers.<BarrageMsg>lambdaQuery()
                .eq(BarrageMsg::getDelFlag, false)
                .eq(BarrageMsg::getVideoId, videoId)
                .orderByDesc(BarrageMsg::getCreateTime)).getRecords();
    }

    @Override
    public int getMsgCountByVideoId(String videoId) {
        return getListByVideoId(videoId).size();
    }
}
