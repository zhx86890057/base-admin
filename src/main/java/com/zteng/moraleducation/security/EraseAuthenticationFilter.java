/*
 * Copyright 2006-2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package com.zteng.moraleducation.security;


import com.google.common.collect.Lists;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 登录的url去掉header的Authorization里面的内容
 * 带Authorization会判断token是否过期
 * 前端无法去除
 */
public class EraseAuthenticationFilter implements Filter {

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException,
            ServletException {
        final HttpServletRequest request = (HttpServletRequest) req;
        final HttpServletResponse response = (HttpServletResponse) res;

        List<String> filterUrl = Lists.newArrayList("/login/sendCode", "/login/phoneLogin", "/login/checkUser", "/login/teacherLogin");
        for (String url : filterUrl) {
            RequestMatcher requestMatcher = new AntPathRequestMatcher(url);
            if(requestMatcher.matches(request)){
                HeaderMapRequestWrapper requestWrapper = new HeaderMapRequestWrapper(request);
                requestWrapper.addHeader("Authorization","");
                chain.doFilter(requestWrapper, response);
                return;
            }
        }

        chain.doFilter(request, response);
    }


}
