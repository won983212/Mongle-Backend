package com.rtsj.return_to_soju.model.entity;

import com.rtsj.return_to_soju.model.enums.Emotion;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DailySentence extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "sentence_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "calender_id")
    private Calender calender;
    private String sentence;
    @Enumerated(EnumType.STRING)
    private Emotion emotion;

    private String roomName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="kakao_id")
    private KakaoText kakaoText;

}