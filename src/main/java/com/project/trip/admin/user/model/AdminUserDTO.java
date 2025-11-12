package com.project.trip.admin.user.model;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AdminUserDTO {
    private int userId;
    private String nickname;
    private String realName;
    private String email;
    private Date regdate;
    private String status;
}

