package com.sulsul.api.essay;

import com.sulsul.api.comment.Comment;
import com.sulsul.api.comment.CommentRepository;
import com.sulsul.api.common.type.EssayState;
import com.sulsul.api.common.type.UType;
import com.sulsul.api.essay.dto.request.CreateEssayRequest;
import com.sulsul.api.essay.dto.request.RejectRequest;
import com.sulsul.api.essay.dto.response.*;
import com.sulsul.api.exception.essay.EssayNotFoundException;
import com.sulsul.api.exception.essay.InvalidEssayStateChangeException;
import com.sulsul.api.exception.essay.InvalidEssayStateException;
import com.sulsul.api.exception.file.FileNotFoundException;
import com.sulsul.api.exception.review.ReviewNotFoundException;
import com.sulsul.api.exception.user.TeacherNotFoundException;
import com.sulsul.api.file.File;
import com.sulsul.api.file.FileRepository;
import com.sulsul.api.review.Review;
import com.sulsul.api.review.ReviewRepository;
import com.sulsul.api.teacherprofile.TeacherProfile;
import com.sulsul.api.teacherprofile.TeacherProfileRepository;
import com.sulsul.api.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EssayService {

    private final EssayRepository essayRepository;
    private final FileRepository fileRepository;
    private final CommentRepository commentRepository;
    private final ReviewRepository reviewRepository;
    private final TeacherProfileRepository teacherProfileRepository;

    @Transactional
    public Essay createEssay(Long profileId, User student, CreateEssayRequest request) {
        // profileId에 해당하는 강사프로필 조회
        TeacherProfile profile = teacherProfileRepository.findById(profileId)
                .orElseThrow(() -> new TeacherNotFoundException(profileId));
        // 강사프로필에 해당하는 강사 조회
        User teacher = profile.getTeacher();
        Essay essay = request.toEntity(student, teacher); // Essay 엔티티 생성
        return essayRepository.save(essay); // Essay 엔티티 저장
    }

    /**
     * (deprecated) essayId에 해당하는 Essay 엔티티를 조회
     */
    @Transactional(readOnly = true)
    public Essay getEssayById(Long essayId) {
        return essayRepository.findById(essayId)
                .orElseThrow(() -> new EssayNotFoundException(essayId));
    }

    /**
     * essayId와 essayState에 해당하는 Essay 엔티티를 조회
     * essayState가 일치하지 않는 경우 InvalidEssayStateException 발생
     */
    @Transactional(readOnly = true)
    public Essay getEssayByIdAndEssyState(Long essayId, EssayState essayState) {
        Essay essay = essayRepository.findById(essayId)
                .orElseThrow(() -> new EssayNotFoundException(essayId));

        if (!essay.checkEssayState(essayState)) {
            throw new InvalidEssayStateException(essayId);
        }

        return essay;
    }

    @Transactional(readOnly = true)
    public List<Essay> getEssaysByUser(User user, EssayState essayState) {
        UType userType = user.getUserType();
        Long userId = user.getId();
        if (userType.equals(UType.TEACHER)) {
            // 강사인 경우: 강사에게 요청된 첨삭글 목록 조회
            return essayRepository.findAllByTeacherIdAndEssayState(userId, essayState);
        }
        // 학생인 경우: 학생이 요청한 첨삭글 목록 조회
        return essayRepository.findAllByStudentIdAndEssayState(userId, essayState);
    }

    private String getStudentFilePath(Long essayId, Long studentId) {
        File file = fileRepository.getStudentEssayFile(essayId, studentId)
                .orElseThrow(() -> new FileNotFoundException());
        return file.getFilePath();
    }

    @Transactional(readOnly = true)
    public RequestEssayResponse getEssayRequest(Long essayId) {
        // essayId에 해당하는 첨삭 조회
        Essay essay = getEssayByIdAndEssyState(essayId, EssayState.REQUEST);
        Long studentId = essay.getStudent().getId();
        // 학생이 올린 첨삭파일 조회
        String filePath = getStudentFilePath(essayId, studentId); // 첨삭파일이 위치한 s3 경로
        // 첨삭요청 정보와 파일경로 반환
        return new RequestEssayResponse(essay, filePath);
    }

    @Transactional(readOnly = true)
    public RejectedEssayResponse getEssayReject(Long essayId) {
        // essayId에 해당하는 첨삭 조회
        Essay essay = getEssayByIdAndEssyState(essayId, EssayState.REJECT);
        Long studentId = essay.getStudent().getId();
        // 학생이 올린 첨삭파일 조회
        String filePath = getStudentFilePath(essayId, studentId); // 첨삭파일이 위치한 s3 경로
        // 첨삭요청 정보와 파일경로 반환
        return new RejectedEssayResponse(essay, filePath);
    }

    private String getTeacherFilePath(Long essayId, Long teacherId) {
        Optional<File> teacherFile = fileRepository.getTeacherEssayFile(essayId, teacherId);
        String teacherFilePath = ""; // 강사가 아직 첨삭파일을 업로드하지 않은 경우
        if (teacherFile.isPresent()) { // 강사가 첨삭파일을 업로드한 경우
            teacherFilePath = teacherFile.get().getFilePath(); // 강사가 올린 첨삭파일의 s3 경로
        }
        return teacherFilePath;
    }

    @Transactional(readOnly = true)
    public ProceedEssayResponse getProceedEssay(Long essayId) {
        // essayId에 해당하는 첨삭 조회
        Essay essay = getEssayByIdAndEssyState(essayId, EssayState.PROCEED);
        Long studentId = essay.getStudent().getId();
        // 학생이 올린 첨삭파일 조회
        String studentFilePath = getStudentFilePath(essayId, studentId); // 학생이 올린 첨삭파일의 s3 경로
        // 강사가 올린 첨삭파일 조회
        Long teacherId = essay.getTeacher().getId();
        String teacherFilePath = getTeacherFilePath(essayId, teacherId);
        // 첨삭에 작성된 모든 댓글 조회
        List<Comment> comments = commentRepository.findAllByEssayId(essayId);
        // 진행중인 첨삭 Response 반환
        return new ProceedEssayResponse(essay, studentFilePath, teacherFilePath, comments);
    }

    @Transactional(readOnly = true)
    public CompletedEssayResponse getCompleteEssay(Long essayId) {
        // essayId에 해당하는 첨삭 조회
        Essay essay = getEssayByIdAndEssyState(essayId, EssayState.COMPLETE);
        Long studentId = essay.getStudent().getId();
        // 학생이 올린 첨삭파일 조회
        String studentFilePath = getStudentFilePath(essayId, studentId); // 학생이 올린 첨삭파일의 s3 경로
        // 강사가 올린 첨삭파일 조회
        Long teacherId = essay.getTeacher().getId();
        String teacherFilePath = getTeacherFilePath(essayId, teacherId);
        // 첨삭에 작성된 모든 댓글 조회
        List<Comment> comments = commentRepository.findAllByEssayId(essayId);
        // 리뷰가 작성되지 않은 경우
        return new CompletedEssayResponse(essay, studentFilePath, teacherFilePath, comments);
    }

    @Transactional(readOnly = true)
    public ReviewedEssayResponse getReviewedEssay(Long essayId) {
        // essayId에 해당하는 첨삭 조회
        Essay essay = getEssayByIdAndEssyState(essayId, EssayState.COMPLETE);
        Long studentId = essay.getStudent().getId();
        // 학생이 올린 첨삭파일 조회
        String studentFilePath = getStudentFilePath(essayId, studentId); // 학생이 올린 첨삭파일의 s3 경로
        // 강사가 올린 첨삭파일 조회
        Long teacherId = essay.getTeacher().getId();
        String teacherFilePath = getTeacherFilePath(essayId, teacherId);
        // 첨삭에 작성된 모든 댓글 조회
        List<Comment> comments = commentRepository.findAllByEssayId(essayId);
        // 첨삭에 작성된 리뷰 조회
        Review review = reviewRepository.findByEssayId(essayId)
                .orElseThrow(() -> new ReviewNotFoundException(essayId));
        return new ReviewedEssayResponse(essay, studentFilePath, teacherFilePath, comments, review);
    }

    @Transactional
    public Essay acceptEssay(Long essayId) {
        Essay essay = essayRepository.findById(essayId)
                .orElseThrow(() -> new EssayNotFoundException(essayId));

        // 첨삭요청 상태에서만 진행상태로 변경할 수 있도록 강제
        if (!essay.checkEssayState(EssayState.REQUEST)) {
            throw new InvalidEssayStateChangeException(essayId);
        }

        // 첨삭진행 상태로 변경
        essay.updateEssayState(EssayState.PROCEED);
        return essayRepository.save(essay);
    }

    @Transactional
    public Essay rejectEssay(Long essayId, RejectRequest rejectRequest) {
        Essay essay = essayRepository.findById(essayId)
                .orElseThrow(() -> new EssayNotFoundException(essayId));

        // 첨삭요청 상태에서만 거절상태로 변경할 수 있도록 강제
        if (!essay.checkEssayState(EssayState.REQUEST)) {
            throw new InvalidEssayStateChangeException(essayId);
        }

        // 첨삭거절 상태로 변경
        essay.updateEssayState(EssayState.REJECT);
        essay.updateRejectDetail(rejectRequest.getRejectDetail());
        return essayRepository.save(essay);
    }

    @Transactional
    public Essay completeEssay(Long essayId) {
        Essay essay = essayRepository.findById(essayId)
                .orElseThrow(() -> new EssayNotFoundException(essayId));

        // 첨삭진행 상태에서만 첨삭완료 상태로 변경할 수 있도록 강제
        if (!essay.checkEssayState(EssayState.PROCEED)) {
            throw new InvalidEssayStateChangeException(essayId);
        }

        // 첨삭완료 상태로 변경
        essay.updateEssayState(EssayState.COMPLETE);
        return essayRepository.save(essay);
    }
}