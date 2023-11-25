package com.example.eventbot.model.ids;

import com.example.eventbot.model.Task;
import com.example.eventbot.model.UserEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class WishTaskId implements Serializable {
    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task taskId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userId;

    public WishTaskId(Task taskId, UserEntity userId) {
        this.taskId = taskId;
        this.userId = userId;
    }
}
