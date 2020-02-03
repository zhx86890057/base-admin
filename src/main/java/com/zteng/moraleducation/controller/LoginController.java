package com.zteng.moraleducation.controller;


import com.zteng.moraleducation.common.CommonException;
import com.zteng.moraleducation.common.CommonResult;
import com.zteng.moraleducation.common.ResultCode;
import com.zteng.moraleducation.pojo.vo.UserVO;
import com.zteng.moraleducation.service.ISysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/login")
@Api(tags = "LoginEndpoint", description = "系统登录相关接口")
public class LoginController {

    @Autowired
    @Qualifier("consumerTokenServices")
    ConsumerTokenServices consumerTokenServices;
    @Autowired
    private TokenEndpoint tokenEndpoint;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private RestTemplate restTemplate;
    @Value("${server.port}")
    private String port;
    /**
     * 登录
     *
     * @param username
     * @param password
     * @return
     */
    @PostMapping("/loginPwd")
    public CommonResult loginPwd(String username, String password) {
        try {
            Authentication principal = new UsernamePasswordAuthenticationToken("auth-server","123456",null);
            Map<String, String> param = new LinkedHashMap<>();
            param.put("grant_type", "password");
            param.put("username", username);
            param.put("password", password);
            param.put("scope", "read");
            ResponseEntity<OAuth2AccessToken> responseEntity =  tokenEndpoint.postAccessToken(principal,param);
            OAuth2AccessToken token  = responseEntity.getBody();
            return CommonResult.success(token);
        }catch (InvalidGrantException ex){
            ex.printStackTrace();
            if("Bad credentials".equals(ex.getMessage())){
                return CommonResult.failed(ResultCode.LOGIN_ERROR);
            }else if("User is disabled".equals(ex.getMessage())){
                return CommonResult.failed(ResultCode.USER_DISABLED);
            }
        } catch (HttpRequestMethodNotSupportedException e) {
            e.printStackTrace();
        }
        return CommonResult.failed(ResultCode.LOGIN_ERROR);
    }

    @PostMapping("clientLogin")
    public CommonResult<OAuth2AccessToken> loginByClient(String clientId, String clientSecret) {
        try {
            MultiValueMap<String, String> params= new LinkedMultiValueMap<>();
            params.add("grant_type", "client_credentials");
            params.add("client_id", clientId);
            params.add("client_secret", clientSecret);
            params.add("scope", "read");
            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity(params, new HttpHeaders());
            OAuth2AccessToken token = restTemplate.postForObject("http://localhost:" + port + "/oauth/token", requestEntity, OAuth2AccessToken.class);
            return CommonResult.success(token);
        }catch (Exception ex){
            ex.printStackTrace();
            throw new CommonException(ResultCode.LOGIN_ERROR);
        }
    }

    /**
     * 获取用户信息
     * @param user
     * @return
     */
    @PostMapping("/info")
    @ApiOperation(value = "获取当前登录用户信息")
    public CommonResult<UserVO> getUserInfo(Principal user){
        UserVO userVO = sysUserService.getUserInfo(user.getName());
        return CommonResult.success(userVO);
    }

    /**
     * 退出登录
     * @return
     */
    @PostMapping("/logout")
    public CommonResult logout(Principal user, HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        String access_token = null;
        if (StringUtils.isNotBlank(header)) {
            access_token = header.substring(OAuth2AccessToken.BEARER_TYPE.length() + 1);
            if (consumerTokenServices.revokeToken(access_token)){
                sysUserService.logout(user.getName());
            }
        }
        return CommonResult.success(null);
    }
}
