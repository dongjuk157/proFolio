package com.ssafy.profolio.service.social;

import com.ssafy.profolio.web.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class GoogleOauth implements SocialOauth {

    @Value("${spring.sns.google.url}")
    private String GOOGLE_SNS_BASE_URL;
    @Value("${spring.sns.google.client.id}")
    private String GOOGLE_SNS_CLIENT_ID;
    @Value("${spring.sns.google.callback.url}")
    private String GOOGLE_SNS_CALLBACK_URL;
    @Value("${spring.sns.google.client.secret}")
    private String GOOGLE_SNS_CLIENT_SECRET;
    @Value("${spring.sns.google.token.url}")
    private String GOOGLE_SNS_TOKEN_BASE_URL;
    @Value("${spring.sns.google.user.url}")
    private String GOOGLE_SNS_USER_URL;

    @Override
    public String getOauthRedirectURL() {
        Map<String, Object> params = new HashMap<>();
        params.put("scope", "profile email openid");
        params.put("response_type", "code");
        params.put("client_id", GOOGLE_SNS_CLIENT_ID);
        params.put("redirect_uri", GOOGLE_SNS_CALLBACK_URL);

        String parameterString = params.entrySet().stream()
                .map(x -> x.getKey() + "=" + x.getValue())
                .collect(Collectors.joining("&"));

        return GOOGLE_SNS_BASE_URL + "?" + parameterString;
    }

    @Override
    public String requestAccessToken(String state, String code) {
        RestTemplate restTemplate = new RestTemplate();

        Map<String, Object> params = new HashMap<>();
        params.put("code", code);
        params.put("client_id", GOOGLE_SNS_CLIENT_ID);
        params.put("client_secret", GOOGLE_SNS_CLIENT_SECRET);
        params.put("redirect_uri", GOOGLE_SNS_CALLBACK_URL);
        params.put("grant_type", "authorization_code");

        ResponseEntity<String> responseEntity =
                restTemplate.postForEntity(GOOGLE_SNS_TOKEN_BASE_URL, params, String.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            return responseEntity.getBody();
        }
        return "?????? ????????? ?????? ?????? ??????";
    }

    @Override
    public String getAccessToken(String result) throws JSONException {
        JSONObject jObject = new JSONObject(result);
        String access_token = jObject.getString("access_token");
        //System.out.println(access_token);
        return access_token;
    }

    @Override
    public String getRefreshToken(String result) throws JSONException {
        return null;
    }

    @Override
    public String getUserInfo(String access_token) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> responseEntity =
                    restTemplate.getForEntity(GOOGLE_SNS_USER_URL + "/?access_token=" + access_token, String.class);

            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                return responseEntity.getBody();
            }
        } catch (HttpClientErrorException e) {
            log.info(">>>>>>>> getUserInfo GOOGLE API ?????? ?????? ??????");
            e.printStackTrace();
        } catch (Exception e) {
            log.info(">>>>>>>> GOOGLE API ??????");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public UserDto makeUserDto(String response) throws JSONException {
        JSONObject jObject = new JSONObject(response);

        String socailId = "GOOGLE-"+jObject.getString("id");

        String email = null;
        String picture = null;
        String name = null;

        if(!jObject.isNull("email"))  email = jObject.getString("email");
        if(!jObject.isNull("picture")) picture = jObject.getString("picture");
        if(!jObject.isNull("name")) name = jObject.getString("name");


        log.info(">>>>>GOOGLE social Id: " + socailId);

        UserDto result = new UserDto(socailId, email,name,"","",picture);
        return result;
    }
}