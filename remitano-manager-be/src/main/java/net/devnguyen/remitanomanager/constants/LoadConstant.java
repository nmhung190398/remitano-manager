package net.devnguyen.remitanomanager.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class LoadConstant {

    public static Integer BLOCK_TIME_VIDEO_SIZE;

    @Value("${remitano.block-time-video-size}")
    private Integer blockTimeVideoSize;

    @PostConstruct
    public void init() {
        BLOCK_TIME_VIDEO_SIZE = blockTimeVideoSize;
    }


}
