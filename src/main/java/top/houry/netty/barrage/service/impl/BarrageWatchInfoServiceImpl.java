package top.houry.netty.barrage.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import top.houry.netty.barrage.consts.BarrageRedisKeyConst;
import top.houry.netty.barrage.service.IBarrageWatchInfoService;
import top.houry.netty.barrage.utils.BarrageRedisUtils;

/**
 * @Desc
 * @Author houruiyang
 * @Date 2022/2/5
 **/
@Slf4j
@Service
public class BarrageWatchInfoServiceImpl implements IBarrageWatchInfoService {

    @Override
    public void addOnlineWatchCount(String videoId) {
        BarrageRedisUtils.increment(BarrageRedisKeyConst.BARRAGE_ONLINE_POPULATION_KEY + videoId);
    }

    @Override
    public void subOnlineWatchCount(String videoId) {
        int onlineWatchCount = Integer.parseInt(BarrageRedisUtils.get(BarrageRedisKeyConst.BARRAGE_ONLINE_POPULATION_KEY + videoId));
        if(0 >= onlineWatchCount) {
            return;
        }
        BarrageRedisUtils.decrement(BarrageRedisKeyConst.BARRAGE_ONLINE_POPULATION_KEY + videoId);
    }

    @Override
    public void addTotalWatchCount(String videoId) {
        BarrageRedisUtils.increment(BarrageRedisKeyConst.BARRAGE_TOTAL_WATCH_KEY + videoId);
    }

    @Override
    public String getTotalWatchCount(String videoId) {
        return StringUtils.isBlank(BarrageRedisUtils.get(BarrageRedisKeyConst.BARRAGE_TOTAL_WATCH_KEY + videoId)) ? "0" : BarrageRedisUtils.get(BarrageRedisKeyConst.BARRAGE_TOTAL_WATCH_KEY + videoId);
    }

    @Override
    public String getTotalOnlineWatchCount(String videoId) {
        return StringUtils.isBlank(BarrageRedisUtils.get(BarrageRedisKeyConst.BARRAGE_ONLINE_POPULATION_KEY + videoId)) ? "0" : BarrageRedisUtils.get(BarrageRedisKeyConst.BARRAGE_ONLINE_POPULATION_KEY + videoId);
    }
}
