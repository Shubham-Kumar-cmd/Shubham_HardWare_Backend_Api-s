package com.shubham.hardware.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class RoleDto {
    private String roleId;
    private String roleName;
}
