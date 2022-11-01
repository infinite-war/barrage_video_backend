package top.houry.netty.barrage.utils;

import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @Desc 弹幕连接信息
 * @Author houruiyang
 * @Date 2022/1/22
 **/
public class BarrageConnectInfoUtils {


    /**
     * 维护了视频ID和目前观看视频的用户的通道
     * <视频id，正在观看的用户>一对多
     */
    public static final Map<String, CopyOnWriteArrayList<ChannelHandlerContext>> BASE_CONNECT_INFO_MAP = new ConcurrentHashMap<>(256);
    /**
     * <用户，视频id>一对一
     */
    public static final Map<ChannelHandlerContext, String> BASE_CHANNEL_VIDEO_MAP = new ConcurrentHashMap<>(256);

    /**
     * 根据视频ID获取对应的用户连接通道信息
     *
     * @param videoId 视频ID
     * @return 通道信息集合
     */
    public static List<ChannelHandlerContext> getChannelHandlerContextListByVideId(String videoId) {
        if (StringUtils.isBlank(videoId)) {
            return null;
        }
        List<ChannelHandlerContext> channelHandlerContexts = BASE_CONNECT_INFO_MAP.get(videoId);
        if (CollectionUtils.isEmpty(channelHandlerContexts)) {
            return null;
        }
        return channelHandlerContexts;
    }

    /**
     * 添加视频对应的通道信息到缓存中
     *
     * @param videoId 视频ID
     * @param context channel 上下文
     * @return 添加结果
     */
    public static boolean addChannelInfoToBaseMap(String videoId, ChannelHandlerContext context) {
        if (StringUtils.isBlank(videoId) || null == context || !context.channel().isActive()) {
            return false;
        }
        //一个视频可以同时被多个人观看
        List<ChannelHandlerContext> channelHandlerContexts = BASE_CONNECT_INFO_MAP.get(videoId);
        //假如当前打开的视频没有人观看，则新建相关的<视频id，正在观看的人数>一对多关系
        if (CollectionUtils.isEmpty(channelHandlerContexts)) {
            CopyOnWriteArrayList<ChannelHandlerContext> contexts = new CopyOnWriteArrayList<>();
            contexts.add(context);
            BASE_CONNECT_INFO_MAP.put(videoId, contexts);   //一个视频可以被多个人观看
            BASE_CHANNEL_VIDEO_MAP.put(context, videoId);   //一个用户打开一个视频
            return true;
        }
        //假如当前打开的视频有其他人再看，则在原有信息上加上<用户通道，视频id>的一对一关系
        BASE_CHANNEL_VIDEO_MAP.put(context, videoId);
        return channelHandlerContexts.add(context);
    }

    /**
     * 从缓存中移除掉用户的基础连接信息
     *
     * @param videoId 视频ID
     * @param context 通道信息
     * @return 移除结果
     */
    public static boolean removeChannelInfoFromBaseMap(String videoId, ChannelHandlerContext context) {
        if (StringUtils.isBlank(videoId) || null == context || !context.channel().isActive()) {
            return false;
        }
        List<ChannelHandlerContext> channelHandlerContexts = BASE_CONNECT_INFO_MAP.get(videoId);
        if (CollectionUtils.isEmpty(channelHandlerContexts)) {
            return true;
        }
        if (!channelHandlerContexts.contains(context)) {
            return true;
        }
        return channelHandlerContexts.remove(context);
    }

    /**
     * 根据ChannelHandlerContext移除缓存中的信息
     *
     * @param context ChannelHandlerContext
     * @return 移除结果
     */
    public static boolean removeChannelInfoByChannelHandlerContext(ChannelHandlerContext context) {
        String videId = BASE_CHANNEL_VIDEO_MAP.get(context);
        if (StringUtils.isBlank(videId)) {
            return true;
        }
        List<ChannelHandlerContext> contexts = getChannelHandlerContextListByVideId(videId);
        if (CollectionUtils.isEmpty(contexts)) {
            return true;
        }
        return contexts.remove(context);
    }

    /**
     * 根据上下文通道获取对应的视频ID
     *
     * @param context 上下文通道
     * @return 视频ID
     */
    public static String getVideoIdByChannelHandlerContext(ChannelHandlerContext context) {
        String videoId = BASE_CHANNEL_VIDEO_MAP.get(context);
        if (StringUtils.isBlank(videoId)) {
            return "";
        }
        return videoId;
    }


}
