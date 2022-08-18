
package com.rtsj.return_to_soju.controller;

import com.rtsj.return_to_soju.common.SuccessResponseResult;
import com.rtsj.return_to_soju.model.dto.request.KakaoMLDataSaveRequestDto;
import com.rtsj.return_to_soju.service.MLService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "ML Controller", description = "ML서버로부터 데이터 받아오는 api 컨트롤러")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MLController {

    private final MLService mlService;

    @Operation(summary = "ML 분석 후 카톡 데이터 받아오기", description = "ML서버가 카톡 데이터 처리를 마친 후 해당 api를 호출하여 데이터를 저장한다.")
    @PostMapping("/ml/kakao")
    public ResponseEntity<SuccessResponseResult> saveKakaoMLData(@RequestBody KakaoMLDataSaveRequestDto dto) {
        mlService.saveKakaoMLData(dto);

        return ResponseEntity.ok()
                .body(new SuccessResponseResult("성공"));
    }
}