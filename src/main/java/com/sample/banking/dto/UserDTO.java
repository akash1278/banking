package com.sample.banking.dto;

import com.sample.banking.entity.Account;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDTO {
    private Long userId;
    private String username;
    private List<Account> accounts;
}