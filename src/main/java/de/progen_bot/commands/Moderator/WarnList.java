package de.progen_bot.commands.Moderator;

import de.mtorials.config.GuildConfiguration;
import de.mtorials.models.Warn;
import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WarnList extends CommandHandler {
    /**
     * Instantiates a new de.progen_bot.command handler.
     */
    public WarnList() {
        super("warnlist", "warnlist or warnlist<user>", "list all active alerts of your server in a table or retrieve custom alerts from specific users"); //TODO warnlist desc
    }

    @Override
    public void execute(CommandManager.ParsedCommandString parsedCommand, MessageReceivedEvent event, GuildConfiguration configuration) {
        if (event.getMessage().getMentionedMembers().size() == 1) {
            List<String> warnTable = new ArrayList<>();

            for (Warn w : super.getDAOs().getWarnList().getWarnsByMember(event.getMessage().getMentionedMembers().get(0))) {

                warnTable.add(w.getReason());
            }

            if (!warnTable.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                EmbedBuilder eb = new EmbedBuilder().setTitle("WarnTable of " + event.getMember().getEffectiveName()).setColor(Color.ORANGE);
                int count = 1;
                for (String reason : warnTable) {
                    sb.append(count + ". " + reason + "\n");
                    count++;
                }
                event.getTextChannel().sendMessage(eb.setDescription(sb.toString()).build()).queue();
            } else {
                event.getChannel().sendMessage(super.messageGenerators.generateErrorMsg("No Warns")).queue();
            }
        } else {

            HashMap<Member, ArrayList<Warn>> x = super.getDAOs().getWarnList().getWarnsByMembersForGuild(event.getGuild());

            EmbedBuilder embedBuilder = new EmbedBuilder()
                    .setTitle("The warns board of your guild")
                    .setColor(Color.ORANGE);

            for (Member m : x.keySet()) {

                embedBuilder.addField(m.getEffectiveName(), String.valueOf(x.get(m).size()), true);
            }

            event.getMessage().getTextChannel().sendMessage(embedBuilder.build()).queue();
        }
    }

    @Override
    public String help() {
        return null;
    }
}
