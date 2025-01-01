//package com.dcmall.back.Service;
//
//import com.dcmall.back.model.DiscordDAO;
//import com.dcmall.back.model.MyNotificationDAO;
//
//import net.dv8tion.jda.api.JDA;
//import net.dv8tion.jda.api.entities.Guild;
//import net.dv8tion.jda.api.entities.Member;
//import net.dv8tion.jda.api.entities.Message;
//import net.dv8tion.jda.api.entities.Role;
//import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
//import net.dv8tion.jda.api.hooks.ListenerAdapter;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Lazy;
//import org.springframework.stereotype.Service;
//import java.util.HashMap;
//import java.util.Map;
//
//@Service
//public class DiscordService extends ListenerAdapter {
//
//    @Autowired
//    private DiscordDAO Ddao;
//
//    @Autowired
//    private MyNotificationDAO Mdao;
//
//    @Autowired
//    @Lazy // JDA가 초기화될 때까지 주입을 지연시킵니다.
//    private JDA jda;
//
//    private final String AUTH_COMMAND = "!인증";
//    private final String VERIFIED_ROLE_NAME = "멤버"; // 부여할 역할 이름
//
//    private final Long SIGN_UP_CHANNEL_ID = 1280433116342779904L; // 여기에 특정 채널 ID를 입력하세요
//
//    private final Map<Long, String> userStates = new HashMap<>();
//
//    int num = 0;
//
//    @Override
//    public void onMessageReceived(MessageReceivedEvent event) {
//        if (event.getAuthor().isBot()) {
//            return;
//        }
//
//        if(event.getChannel().getIdLong() == SIGN_UP_CHANNEL_ID){
//            String message = event.getMessage().getContentRaw();
//            if (message.startsWith(AUTH_COMMAND)) {
//                handleAuthRequest(event);
//            } else {
//                event.getChannel().sendMessage("올바른 인증 명령어가 아닙니다").queue();
//                deleteAuthMessage(event);
//            }
//        }
//    }
//
//    private void handleAuthRequest(MessageReceivedEvent event) {
//        String[] parts = event.getMessage().getContentRaw().split("\\s+", 2);
//        deleteAuthMessage(event);
//        if (parts.length != 2) {
//            event.getChannel().sendMessage("올바른 형식: !인증 [인증코드]").queue();
//            return;
//        }
//
//        String inputCode = parts[1];
//        String userId = event.getAuthor().getId();
//
//        num = this.Ddao.certification(inputCode);
//        System.out.println("userId : " + userId);
//        if (num != 0) {
//            // 인증 성공
//            event.getChannel().sendMessage("인증이 성공적으로 완료되었습니다!").queue();
//            // 인증 후 처리 로직 (예: 역할 부여)
//            Ddao.deleteCheckcode(num);
//            Mdao.insertDiscord(num, userId);
//
//            Guild guild = event.getGuild();
//            Member member = event.getMember();
//            if (guild != null && member != null) {
//                Role verifiedRole = guild.getRolesByName(VERIFIED_ROLE_NAME, true).stream().findFirst().orElse(null);
//                if (verifiedRole != null) {
//                    guild.addRoleToMember(member, verifiedRole).queue(
//                            success -> event.getChannel().sendMessage("인증된 사용자 역할이 부여되었습니다.").queue(),
//                            error -> event.getChannel().sendMessage("역할 부여 중 오류가 발생했습니다.").queue()
//                    );
//                } else {
//                    event.getChannel().sendMessage("인증된 사용자 역할을 찾을 수 없습니다.").queue();
//                }
//            }
//
//        } else {
//            // 인증 실패
//            event.getChannel().sendMessage("인증에 실패했습니다. 올바른 인증 코드를 입력해주세요.").queue();
//        }
//    }
//
//    public void deleteAuthMessage(MessageReceivedEvent event) {
//        Message message = event.getMessage();
//        message.delete().queue(
//                success -> System.out.println("메시지가 성공적으로 삭제되었습니다."),
//                error -> System.err.println("메시지 삭제 중 오류 발생: " + error.getMessage())
//        );
//    }
//
//    public void sendMessage(String discordId, String title, int url, int siteNum){
//        jda.retrieveUserById(discordId).queue(user -> {
//            if (user != null) {
//                user.openPrivateChannel().queue(privateChannel -> {
//                    String webSite = "";
//                    if(siteNum == 1){
//                        webSite = "https://quasarzone.com/bbs/qb_saleinfo/views";
//                    } else if (siteNum == 2) {
//                        webSite = "https://www.fmkorea.com/hotdeal";
//                    } else if (siteNum == 3) {
//                        webSite = "https://m.ruliweb.com/market/board/1020/read";
//                    } else if (siteNum == 4) {
//                        webSite = "https://arca.live/b/hotdeal";
//                    }
//                    String message = String.format("제목: %s\n링크: %s/%s\n", title, webSite, url);
//                    privateChannel.sendMessage(message).queue(
//                            success -> System.out.println("개인 메시지가 성공적으로 전송되었습니다."),
//                            error -> System.err.println("개인 메시지 전송 중 오류 발생: " + error.getMessage())
//                    );
//                });
//            } else {
//                System.err.println("사용자를 찾을 수 없습니다: " + discordId);
//            }
//        });
//    }
//
//}
