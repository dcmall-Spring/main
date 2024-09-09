package com.dcmall.back.Service;

import com.dcmall.back.model.DiscordDAO;
import com.dcmall.back.model.MyNotificationDAO;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.PrivateChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;



import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class DiscordService extends ListenerAdapter {

    @Autowired
    private DiscordDAO Ddao;

    @Autowired
    private MyNotificationDAO Mdao;

    private final String AUTH_COMMAND = "!인증";
    private final String VERIFIED_ROLE_NAME = "멤버"; // 부여할 역할 이름


    private final Long SIGN_UP_CHANNEL_ID = 1280433116342779904L; // 여기에 특정 채널 ID를 입력하세요

    private final Map<Long, String> userStates = new HashMap<>();

    int num = 0;

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;

        String message = event.getMessage().getContentRaw();
        if (message.startsWith(AUTH_COMMAND)) {
            handleAuthRequest(event);
        }
    }

    private void handleAuthRequest(MessageReceivedEvent event) {
        String[] parts = event.getMessage().getContentRaw().split("\\s+", 2);
        if (parts.length != 2) {
            event.getChannel().sendMessage("올바른 형식: !인증 [인증코드]").queue();
            return;
        }

        String inputCode = parts[1];
        String userId = event.getAuthor().getId();

        num = this.Ddao.certification(inputCode);
        System.out.println("userId : " + userId);
        if (num != 0) {
            // 인증 성공
            event.getChannel().sendMessage("인증이 성공적으로 완료되었습니다!").queue();
            // 여기에 추가적인 인증 후 처리 로직 (예: 역할 부여 등)
            Ddao.deleteCheckcode(num);
            Mdao.insertDiscord(num, userId);

            Guild guild = event.getGuild();
            Member member = event.getMember();
            if (guild != null && member != null) {
                Role verifiedRole = guild.getRolesByName(VERIFIED_ROLE_NAME, true).stream().findFirst().orElse(null);
                if (verifiedRole != null) {
                    guild.addRoleToMember(member, verifiedRole).queue(
                        success -> event.getChannel().sendMessage("인증된 사용자 역할이 부여되었습니다.").queue(),
                        error -> event.getChannel().sendMessage("역할 부여 중 오류가 발생했습니다.").queue()
                    );
                } else {
                    event.getChannel().sendMessage("인증된 사용자 역할을 찾을 수 없습니다.").queue();
                }
            }
            

        } else {
            // 인증 실패
            event.getChannel().sendMessage("인증에 실패했습니다. 올바른 인증 코드를 입력해주세요.").queue();
        }
    }
}


