package com.example.eventbot.model;

import com.example.eventbot.model.ids.WishTaskId;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "wish_task")
@NoArgsConstructor
public class WishTask {
    @EmbeddedId
    private WishTaskId id;

    @Column(name = "ordinal")
    private Integer ordinal;

    public WishTask(WishTaskId id, Integer ordinal) {
        this.id = id;
        this.ordinal = ordinal;
    }
}
