// 파일 경로: src/main/java/com/trip/admin/model/suspendedUserDTO.java
package com.project.trip.admin.user.model;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class suspendedUserDTO {
    private int userId;
    private int memsuspendedId;
    private String nickname;
    private String suspendedReason;
    private Date suspendedStartDate;
    private Date suspendedEndDate;
}