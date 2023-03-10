package com.example.mbtiboard.service;

import com.example.mbtiboard.dto.*;
import com.example.mbtiboard.entity.User;
import com.example.mbtiboard.entity.UserRoleEnum;
import com.example.mbtiboard.jwt.JwtUtil;
import com.example.mbtiboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final JwtUtil jwtUtil;

    private final PasswordEncoder passwordEncoder;

    // ADMIN_TOKEN
    @Value("${admin.secret.token}")
    private String ADMIN_TOKEN;

    String rePw;

    //회원가입
    @Transactional
    public MsgResponseDto signup(SignupRequestDto signupRequestDto) {
        String username = signupRequestDto.getUsername();
        String password = signupRequestDto.getPassword();
        String email = signupRequestDto.getEmail();
        String mbti = signupRequestDto.getUserMbti();

        //아이디유효성검사
        if (Pattern.matches( "^[a-z0-9]*$",username)){
            if(username.length() < 4 || username.length() > 10){
                throw new IllegalArgumentException("아이디 길이를 4자 이상 10자 이하로 해주세요");
            }

            //중복확인
            Optional<User> found = userRepository.findByUsername(username);
            if (found.isPresent()){
                throw new IllegalArgumentException("중복된 ID입니다.");

            }
        }else{
            throw new IllegalArgumentException("아이디를 알파벳소문자 또는 숫자로만 구성해주세요");
        }

        //비밀번호유효성검사
        if (Pattern.matches( "^.(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$",password)){
            if(password.length() < 8 || password.length() > 15){
                throw new IllegalArgumentException("비밀번호 길이를 8자 이상 15자 이하로 해주세요");
            }
        } else {
            throw new IllegalArgumentException("비밀번호에 알파벳대소문자, 숫자, 특수문자가 포함되게 해주세요");
        }
        // 사용자 ROLE 확인
        UserRoleEnum role = UserRoleEnum.USER;
        if (signupRequestDto.isAdmin()) {
            if (!signupRequestDto.getAdminToken().equals(ADMIN_TOKEN)) {
                throw new IllegalArgumentException("관리자 토근값이 일치하지 않습니다.");
            }
            role = UserRoleEnum.ADMIN;
        }
        //pw암호화
        password = passwordEncoder.encode(password);

        User user = new User(username, password, email, mbti, role);
        userRepository.save(user);

        return new MsgResponseDto("회원가입 성공", HttpStatus.OK.value());
    }


    @Transactional(readOnly = true)
    public MsgResponseDto login(LoginRequestDto loginRequestDto, HttpServletResponse response) {
        String username = loginRequestDto.getUsername();
        String password = loginRequestDto.getPassword();
        //사용자확인
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("등록된 아이디가 아닙니다.")

        );

        if(!passwordEncoder.matches(password, user.getPassword())){

            throw new IllegalArgumentException("비밀번호를 확인해주세요");

        }

        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getUsername(), user.getRole()));

        return new MsgResponseDto("로그인성공", HttpStatus.OK.value());
    }

    @Transactional
    public String findId(FindIdDto findIdDto) {
        String email = findIdDto.getEmail();
        String mbti = findIdDto.getMbti();

        User user = userRepository.findByEmailAndUserMbti(email, mbti).orElseThrow(
                () -> new IllegalArgumentException("해당 아이디를 찾을 수 없습니다.")
        );
        return user.getUsername();
    }

//    @Transactional
//    public MsgResponseDto findPw(FindPwDto findPwDto) {
//        String username = findPwDto.getUsername();
//        String email = findPwDto.getEmail();
//        String mbti = findPwDto.getMbti();
//
//
//        User rePw = userRepository.findByUsernameAndAndEmailAndMbti(username, email, mbti).orElseThrow(
//                () -> new IllegalArgumentException("해당 아이디를 찾을 수 없습니다.")
//        );
//    }
}