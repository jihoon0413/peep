package com.example.peep.service;

import com.example.peep.config.jwt.JwtTokenProvider;
import com.example.peep.domain.Hashtag;
import com.example.peep.domain.Student;
import com.example.peep.domain.mapping.StudentHashtag;
import com.example.peep.dto.HashtagDto;
import com.example.peep.enumType.HashtagType;
import com.example.peep.repository.HashtagRepository;
import com.example.peep.repository.StudentHashtagRepository;
import com.example.peep.repository.StudentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class HashtagServiceTest {
    @Mock private HashtagRepository hashtagRepository;
    @Mock private StudentRepository studentRepository;
    @Mock private StudentHashtagRepository studentHashtagRepository;
    @Mock private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private HashtagService hashtagService;

    @DisplayName("getHashList - 해쉬태그 리스트 리턴")
    @Test
    void given_whenGetHashList_thenReturnAllHashtagList() {
        //Given
        List<Hashtag> hashtagList = new ArrayList<>();
        Hashtag hash1 = Hashtag.of("soccer", HashtagType.HOBBY);
        Hashtag hash2 = Hashtag.of("baseball", HashtagType.HOBBY);
        Hashtag hash3 = Hashtag.of("tennis", HashtagType.HOBBY);
        hashtagList.add(hash1);
        hashtagList.add(hash2);
        hashtagList.add(hash3);
        given(hashtagRepository.findAllByType(HashtagType.HOBBY)).willReturn(hashtagList);
        //When
        List<HashtagDto> dtoList = hashtagService.getHashList(HashtagType.HOBBY).getBody();
        //Then
        assertThat(dtoList.size()).isEqualTo(3);
    }


    @DisplayName("setMyHashtag - 나의 해쉬태그 설정")
    @Test
    void givenTokenAndHashtagDto_whenSetMyHashtag_thenSaveHashtag() {

        //Given
        Student std1 = Student.of("jihoon", "1234" ,"지훈", null, null, null, 3,1, "01012345678");
        HashtagDto hashDto1 = HashtagDto.of(1L, "soccer", HashtagType.HOBBY);
        HashtagDto hashDto2 = HashtagDto.of(2L, "baseball", HashtagType.HOBBY);
        HashtagDto hashDto3 = HashtagDto.of(3L, "tennis", HashtagType.HOBBY);
        List<HashtagDto> dtoList = new ArrayList<>();
        dtoList.add(hashDto1);
        dtoList.add(hashDto2);
        dtoList.add(hashDto3);
        Hashtag hash1 = Hashtag.of("soccer", HashtagType.HOBBY);
        Hashtag hash2 = Hashtag.of("baseball", HashtagType.HOBBY);
        Hashtag hash3 = Hashtag.of("tennis", HashtagType.HOBBY);
        given(jwtTokenProvider.getUserId("token")).willReturn("jihoon");
        given(studentRepository.findByUserId("jihoon")).willReturn(Optional.of(std1));
        given(hashtagRepository.findById(1L)).willReturn(Optional.of(hash1));
        given(hashtagRepository.findById(2L)).willReturn(Optional.of(hash2));
        given(hashtagRepository.findById(3L)).willReturn(Optional.of(hash3));
        //When
        hashtagService.setMyHashtag("token", dtoList);
        //Then
        ArgumentCaptor<StudentHashtag> captor = ArgumentCaptor.forClass(StudentHashtag.class);
        then(studentHashtagRepository).should(times(3)).save(captor.capture());

    }
    @DisplayName("getMyHasttag - 나의 해쉬태그 불러오기")
    @Test
    void givenToken_whenGetMyHashtag_thenReturnHashtagDtoList() {
        //Given
        Student std1 = Student.of("jihoon", "1234" ,"지훈", null, null, null, 3,1, "01012345678");
        Hashtag hash1 = Hashtag.of("soccer", HashtagType.HOBBY);
        Hashtag hash2 = Hashtag.of("baseball", HashtagType.HOBBY);
        Hashtag hash3 = Hashtag.of("tennis", HashtagType.HOBBY);
        StudentHashtag sh1 = StudentHashtag.of(std1,hash1);
        StudentHashtag sh2 = StudentHashtag.of(std1,hash2);
        StudentHashtag sh3 = StudentHashtag.of(std1,hash3);
        List<StudentHashtag> list = new ArrayList<>();
        list.add(sh1);
        list.add(sh2);
        list.add(sh3);
        given(jwtTokenProvider.getUserId("token")).willReturn("jihoon");
        given(studentHashtagRepository.findAllByStudentUserIdOrderByHashtagTypeAscHashtagContentAsc("jihoon")).willReturn(list);
        //When
        List<HashtagDto> result = hashtagService.getMyHashtag("token").getBody();
        //Then
        assertThat(result.size()).isEqualTo(3);
    }
    @DisplayName("deleteMyHashtag - 나의 해쉬태그 해제")
    @Test
    void givenTokenAndHashtagDto_whenDeleteMyHashtag_thenDeleteHashtag() {

        //Given
        HashtagDto hashDto1 = HashtagDto.of(1L, "soccer", HashtagType.HOBBY);
        HashtagDto hashDto2 = HashtagDto.of(2L, "baseball", HashtagType.HOBBY);
        HashtagDto hashDto3 = HashtagDto.of(3L, "tennis", HashtagType.HOBBY);
        List<HashtagDto> dtoList = List.of(hashDto1, hashDto2, hashDto3);
        given(jwtTokenProvider.getUserId("token")).willReturn("jihoon");

        //When
        hashtagService.deleteMyHashtag("token", dtoList);
        //Then
        then(jwtTokenProvider).should().getUserId("token");
        for (HashtagDto hashtagDto : dtoList) {
            then(studentHashtagRepository).should()
                    .deleteByStudentUserIdAndHashtagId("jihoon", hashtagDto.id());
        }
    }




}