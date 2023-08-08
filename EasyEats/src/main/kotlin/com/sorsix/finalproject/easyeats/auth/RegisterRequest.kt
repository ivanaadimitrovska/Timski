package com.sorsix.finalproject.easyeats.auth

import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor
import org.springframework.web.multipart.MultipartFile

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
class RegisterRequest(

    val firstName: String,

    val lastName: String,

    val email: String,

    val username: String,

    val password: String,

) {}