package backend.baba.diary.controller;

import backend.baba.diary.dto.request.DiaryCreateRequest;
import backend.baba.diary.dto.response.DiaryResponse;
import backend.baba.diary.service.DiaryService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/diaries")
@Tag(name = "Diary", description = "문화생활 기록 API")
public class DiaryController {

    private  final DiaryService diaryService;
    @PostMapping("/{member_id}")
    @Operation(summary = "문화생활 기록 추가", description = "사용자가 문화생활을 기록합니다.")
    public ResponseEntity<DiaryResponse> createDiary(@PathVariable("member_id") Long id, @Valid @RequestBody DiaryCreateRequest request){
        DiaryResponse response=diaryService.createDiary(id, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
