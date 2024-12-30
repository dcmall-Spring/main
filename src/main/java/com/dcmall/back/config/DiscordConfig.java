package com.dcmall.back.config;

import com.dcmall.back.Service.DiscordService;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DiscordConfig {
    @Value("${discord.bot.token}")
    private String DiscordToken;

    @Bean
    public JDA jda() throws Exception {
        JDA jda = JDABuilder.createDefault(DiscordToken)
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .build();
        return jda;
    }

    @Bean
    public DiscordService discordService(JDA jda) {
        DiscordService discordService = new DiscordService(jda);
        jda.addEventListener(discordService);
        return discordService;
    }
}
