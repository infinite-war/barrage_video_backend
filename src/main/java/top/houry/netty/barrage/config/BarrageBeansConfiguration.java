package top.houry.netty.barrage.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import top.houry.netty.barrage.utils.BarrageRedisUtils;

@Component
public class BarrageBeansConfiguration {

    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    public void setSetStringRedisTemplate(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Bean
    public void initRedisUtil() {
        BarrageRedisUtils.setStringRedisTemplate(stringRedisTemplate);
    }
}
