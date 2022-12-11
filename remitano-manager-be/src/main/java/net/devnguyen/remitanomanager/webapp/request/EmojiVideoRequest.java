package net.devnguyen.remitanomanager.webapp.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.devnguyen.remitanomanager.enums.EmojiType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmojiVideoRequest {
    private EmojiType emojiType;
    private String videoId;
}
