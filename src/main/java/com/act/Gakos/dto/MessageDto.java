package com.act.Gakos.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Setter
@Getter
@ToString
@EqualsAndHashCode
public class MessageDto {

    private Long id;
    private String content;
    private Integer senderId;
    private Integer receiverId;
//    private String receiverName;

    public MessageDto(Long id, String content, Integer senderId, Integer receiverId) {
        this.id = id;
        this.content = content;
        this.senderId = senderId;
        this.receiverId = receiverId;
    }
}
