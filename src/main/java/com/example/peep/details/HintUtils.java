package com.example.peep.details;

import com.example.peep.domain.Student;
import com.example.peep.domain.enumType.HashtagType;
import com.example.peep.domain.mapping.StudentHashtag;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.Set;

@Component
public class HintUtils {
    final String[] initials = {
            "ㄱ", "ㄲ", "ㄴ", "ㄷ", "ㄸ",
            "ㄹ", "ㅁ", "ㅂ", "ㅃ", "ㅅ",
            "ㅆ", "ㅇ", "ㅈ", "ㅉ", "ㅊ",
            "ㅋ", "ㅌ", "ㅍ", "ㅎ"
    };


    public String getHint(Student student, int hintNum) {

        Random random = new Random();

        int num = random.nextInt(hintNum);

        switch (num) {
            case 0: {
                return "grade : " + student.getGrade();
            }
            case 1: {
                return "myClass : " + student.getMyClass();
            }
            case 2: {
                char name = student.getName().charAt(0);
                int cho = ((name - 0xAC00) / 28 / 21);
                return "Initial consonant of the first letter  : " + initials[cho];
            }
            case 3: {
                char name = student.getName().charAt(1);
                int cho = ((name - 0xAC00) / 28 / 21);
                return "Initial consonant of the second letter  : " + initials[cho];
            }
            case 4: {
                char name = student.getName().charAt(2);
                int cho = ((name - 0xAC00) / 28 / 21);
                return "Initial consonant of the third letter  : " + initials[cho];
            }
            case 5: {
                Set<StudentHashtag> hashtags = student.getHashtags();
                for (StudentHashtag hashtag : hashtags) {
                    if (hashtag.getHashtag().getType() == HashtagType.CHARACTER) {
                        return "One of the character hashtag : " + hashtag.getHashtag().getContent();
                    }
                }
                return "There is no CHARACTER hashtag";
            }
            case 6: {
                Set<StudentHashtag> hashtags = student.getHashtags();
                for (StudentHashtag hashtag : hashtags) {
                    if (hashtag.getHashtag().getType() == HashtagType.HOBBY) {
                        return "One of the hobby hashtag : " + hashtag.getHashtag().getContent();
                    }
                }
                return "There is no Hobby hashtag";
            }
            case 7: {
                return "Writer school : " + student.getSchool().getSchoolName();
            }
        }
        return null;

        /*
        공통질문(커뮤니티 안에서)
        0. 학년
        1. 반
        2. 첫번쨰 초성
        3. 2번쨰 초성
        4. 3번쨰 초성
        5. 성격 해쉬태그 1개보기
        6. 취미 해쉬태그 1개보기
         */



    }


}
