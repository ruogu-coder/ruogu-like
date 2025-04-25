package com.ruogu.thumb;

import cn.hutool.core.util.RandomUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruogu.thumb.common.exception.enums.GlobalErrorCodeConstants;
import com.ruogu.thumb.common.pojo.CommonResult;
import com.ruogu.thumb.model.dto.user.UserLoginReqDTO;
import com.ruogu.thumb.model.dto.user.UserRegisterReqDTO;
import com.ruogu.thumb.model.entity.User;
import com.ruogu.thumb.service.UserService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/***
 *@author ruogu
 *@create 2025/4/25 12:57
 **/
@SpringBootTest
@AutoConfigureMockMvc
class RuoguLikeApplicationTest {

    @Resource
    private UserService userService;

    @Resource
    private MockMvc mockMvc;

    @Test
    void addUser() throws Exception {
        int initCount = 5000;
        Set<String> hashSet = new HashSet<>(5000);
        for (int i = 0; i < initCount; i++) {
            // 生成随机用户账号
            String userAccount = RandomUtil.randomString(8);
            while (hashSet.contains(userAccount)){
                userAccount = RandomUtil.randomString(8);
            }
            hashSet.add(userAccount);
            // 固定密码和确认密码
            String userPassword = "123456789";

            // 构建请求体
            UserRegisterReqDTO userRegisterReqDTO = new UserRegisterReqDTO();
            userRegisterReqDTO.setUserAccount(userAccount);
            userRegisterReqDTO.setUserPassword(userPassword);
            userRegisterReqDTO.setCheckPassword(userPassword);

            // 将请求体转换为 JSON 字符串
            String requestBody = new ObjectMapper().writeValueAsString(userRegisterReqDTO);

            // 构建 MockHttpServletRequestBuilder
            MockHttpServletRequestBuilder requestBuilder = post("/user/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody);

            // 执行请求并获取响应
            ResultActions resultActions = mockMvc.perform(requestBuilder);

            // 解析响应体
            MvcResult mvcResult = resultActions.andReturn();
            String responseBody = mvcResult.getResponse().getContentAsString();
            CommonResult<Long> commonResult = new ObjectMapper().readValue(responseBody, new TypeReference<CommonResult<Long>>() {});

            // 断言响应结果为成功，并且返回值不为空
            assertThat(commonResult.getCode()).isEqualTo(GlobalErrorCodeConstants.SUCCESS.getCode());
            assertThat(commonResult.getData()).isNotNull();
        }
    }

    @Test
    void testLoginAndExportSessionToCsv() throws Exception {
        // 获取所有用户列表
        List<User> list = userService.list();

        // 确保用户列表不为空
        assertThat(list).isNotEmpty();

        // 使用 try-with-resources 确保资源正确关闭
        try (PrintWriter writer = new PrintWriter(new FileWriter("token_output.csv", true))) {
            // 判断文件是否为空，如果是第一次写入，则添加表头
            if (new File("token_output.csv").length() == 0) {
                writer.println("userId,token,timestamp");
            }

            // 遍历用户列表
            for (User user : list) {
                long testUserId = user.getId();

                UserLoginReqDTO userLoginReqDTO = new UserLoginReqDTO();
                userLoginReqDTO.setUserAccount(user.getUserAccount());
                userLoginReqDTO.setUserPassword("123456789");
                if(user.getUserAccount().equals("admin")){
                    userLoginReqDTO.setUserPassword("12345678");
                }

                // 将请求体转换为 JSON 字符串
                String requestBody = new ObjectMapper().writeValueAsString(userLoginReqDTO);

                // 构建登录请求
                MvcResult result = mockMvc.perform(post("/user/login")
                                .content(requestBody)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk()) // 断言响应状态码为 200
                        .andReturn();

                // 获取 Set-Cookie 头
                List<String> setCookieHeaders = result.getResponse().getHeaders("Set-Cookie");
                assertThat(setCookieHeaders).isNotEmpty(); // 断言 Set-Cookie 头不为空

                Optional<String> tokenValue = extractCookieValue(setCookieHeaders, "ruogu-like-token");
                // 确保提取到值
                String token = tokenValue.orElseThrow(() -> new RuntimeException("No ruogu-like-token found in response"));



                // 写入 CSV 文件
                writer.printf("%d,%s,%s%n", testUserId, token, LocalDateTime.now());

                // 输出日志
                System.out.println("✅ 写入 CSV：" + testUserId + " -> " + token);
            }
        }
    }

    public static Optional<String> extractCookieValue(List<String> setCookieHeaders, String cookieName) {
        return setCookieHeaders.stream()
                .filter(cookie -> cookie.startsWith(cookieName + "=")) // 过滤包含指定 Cookie 名称的项
                .map(cookie -> cookie.split(";")[0]) // 提取 Cookie 名称=值 部分
                .map(cookie -> cookie.split("=")[1]) // 提取 Cookie 值部分
                .findFirst(); // 返回第一个匹配项，如果未找到则返回 Optional.empty()
    }




}