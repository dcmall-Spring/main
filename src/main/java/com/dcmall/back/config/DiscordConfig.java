package com.dcmall.back.config;

import com.dcmall.back.Service.DiscordService;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.security.auth.login.LoginException;

@Configuration
public class DiscordConfig {

    @Value("${discord.bot.token}")
    private String discordToken;

    /**
     * JDA 인스턴스를 생성하고 DiscordService를 이벤트 리스너로 추가합니다.
     * build() 메서드는 비동기적으로 JDA를 초기화합니다.
     */
    @Bean
    public JDA jda(DiscordService discordService) throws LoginException {
        JDA jda = JDABuilder.createDefault(discordToken)
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .addEventListeners(discordService) // DiscordService를 이벤트 리스너로 추가
                .setAutoReconnect(true) // 자동 재접속 설정
                .build(); // 비동기 초기화
        return jda;
    }

    /**
     * DiscordService 빈을 생성합니다.
     */
    @Bean
    public DiscordService discordService() {
        return new DiscordService();
    }
}
