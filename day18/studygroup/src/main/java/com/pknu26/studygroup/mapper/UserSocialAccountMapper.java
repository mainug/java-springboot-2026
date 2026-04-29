package com.pknu26.studygroup.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.pknu26.studygroup.dto.UserSocialAccount;

@Mapper
public interface UserSocialAccountMapper {

    UserSocialAccount findByProviderAndProviderUserId(
            String provider,
            String providerUserId
    );

    void insertSocialAccount(UserSocialAccount socialAccount);
    
}