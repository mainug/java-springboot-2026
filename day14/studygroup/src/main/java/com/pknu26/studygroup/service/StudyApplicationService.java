package com.pknu26.studygroup.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.pknu26.studygroup.dto.StudyApplication;
import com.pknu26.studygroup.dto.StudyPost;
import com.pknu26.studygroup.mapper.StudyApplicationMapper;
import com.pknu26.studygroup.mapper.StudyPostMapper;
import com.pknu26.studygroup.validation.StudyApplicationForm;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudyApplicationService {

    private final StudyApplicationMapper studyApplicationMapper;
    private final StudyPostMapper studyPostMapper; // 스터디 신청하려면 스터디 게시글이 필요

    public void apply(StudyApplicationForm form) {

        // 해당 스터디 포스트 찾음
        StudyPost post = this.studyPostMapper.findById(form.getPostId());

        if (post == null) { // 글이 없으면
            throw new IllegalArgumentException("게시글이 없습니다.");
        }

        if (post.getUserId().equals(form.getUserId())) { // 자기가 작성한 스터디 포스트에는 직접 신청할 수 없음
            throw new IllegalArgumentException("본인 글에는 신청할 수 없습니다.");
        }

        if (!"OPEN".equals(post.getStatus())) { // 스터디 포스트 상태가 OPEN이 아니면 신청 불가
            throw new IllegalArgumentException("마감된 스터디입니다.");
        }

        int exists = studyApplicationMapper.countByPostIdAndUserId(form.getPostId(), form.getUserId());
        if (exists > 0) { // 신청을 했는데 다시 할 수 없음
            throw new IllegalArgumentException("이미 신청한 스터디입니다.");
        }

        int approvedCount = studyApplicationMapper.countApprovedByPostId(form.getPostId());
        if (approvedCount >= post.getMaxMembers()) { // 게시글에 정해진 최대 인원을 넘어서 신청할 수 없음
            throw new IllegalArgumentException("모집 인원이 마감되었습니다.");
        }

        StudyApplication studyApplication = new StudyApplication();
        studyApplication.setPostId(form.getPostId());
        studyApplication.setUserId(form.getUserId());
        studyApplication.setMessage(form.getMessage());

        // 스터디 신청 목록
        this.studyApplicationMapper.insertApplication(studyApplication);
    }

    // 스터디 포스트 총 신청 목록
    public List<StudyApplication> getApplicationListByPostId(Long postId) {
        return studyApplicationMapper.findByPostId(postId);
    }

    // 사용자별 스터디 신청 목록
    public List<StudyApplication> getMyApplicationList(Long userId) {
        return studyApplicationMapper.findByUserId(userId);
    }

    // 스터디 신청 한 건 조회
    public StudyApplication getApplication(Long applicationId) {
        return studyApplicationMapper.findById(applicationId);
    }

    // 신청 승인 로직
    public void approve(Long applicationId) {
        studyApplicationMapper.updateStatus(applicationId, "APPROVED");
    }

    // 신청 거부 로직
    public void reject(Long applicationId) {
        studyApplicationMapper.updateStatus(applicationId, "REJECTED");
    }
}