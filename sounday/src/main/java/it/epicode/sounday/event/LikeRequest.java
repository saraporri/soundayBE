package it.epicode.sounday.event;

import lombok.Data;
@Data // Getter and Setter

public class LikeRequest { private Long userId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
