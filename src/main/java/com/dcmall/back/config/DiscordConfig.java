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
     * DiscordService 빈을 먼저 생성합니다.
     * @return DiscordService 인스턴스
     */
    @Bean
    public DiscordService discordService() {
        return new DiscordService();
    }

    /**
     * JDA 빈을 생성하고 DiscordService를 이벤트 리스너로 추가합니다.
     * @param discordService DiscordService 인스턴스
     * @return JDA 인스턴스
     * @throws LoginException 로그인 예외 발생 시
     */
    @Bean
    public JDA jda(DiscordService discordService) throws LoginException {
        try {
            JDA jda = JDABuilder.createDefault(discordToken)
                    .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                    .addEventListeners(discordService) // DiscordService를 이벤트 리스너로 추가
                    .setAutoReconnect(true) // 자동 재접속 설정
                    .build(); // 비동기 초기화

            jda.addEventListener(new net.dv8tion.jda.api.hooks.ListenerAdapter() {
                @Override
                public void onReady(net.dv8tion.jda.api.events.session.ReadyEvent event) {
                    System.out.println("JDA has been initialized successfully!");
                }

                @Override
                public void onShutdown(net.dv8tion.jda.api.events.session.ShutdownEvent event) {
                    System.out.println("JDA is shutting down.");
                }

            });

            return jda;
        } catch (Exception e) {
            System.err.println("Unexpected error during JDA initialization: " + e.getMessage());
            throw e;
        }
    }
}
