/*
 *     Copyright 2015-2016 Austin Keener & Michael Ritter
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.robrua.orianna.api.core.RiotAPI;
import com.robrua.orianna.type.core.championmastery.ChampionMastery;
import com.robrua.orianna.type.core.common.Region;
import com.robrua.orianna.type.core.currentgame.CurrentGame;
import com.robrua.orianna.type.core.staticdata.Champion;
import com.robrua.orianna.type.core.staticdata.ChampionSpell;
import com.robrua.orianna.type.core.stats.ChampionStats;
import com.robrua.orianna.type.core.summoner.Summoner;
import com.robrua.orianna.type.exception.APIException;
import net.dv8tion.jda.client.entities.Group;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class MessageListenerExample extends ListenerAdapter
{

    String dd_icon="http://ddragon.leagueoflegends.com/cdn/7.5.2/img/profileicon/";
    String dd_icon_format=".png";
    String dd_champ_icon="http://ddragon.leagueoflegends.com/cdn/7.5.2/img/champion/";
    public static void main(String[] args)
    {

        RiotAPI.setAPIKey(ApiTokens.getRiotToken());
        RiotAPI.setRegion(Region.EUW);
        JDA jda;


        //We construct a builder for a BOT account. If we wanted to use a CLIENT account
        // we would use AccountType.CLIENT
        try
        {
            Game game=Game.of("I AM ALIVE! (EUW only)"); //

            jda = new JDABuilder(AccountType.BOT)
                    .setToken(ApiTokens.getBotToken())           //The token of the account that is logging in.
                    .addListener(new MessageListenerExample())  //An instance of a class that will handle events.
                    .setGame(game)
                    .buildBlocking();  //There are 2 ways to login, blocking vs async. Blocking guarantees that JDA will be completely loaded.

        }
        catch (LoginException e)
        {
            //If anything goes wrong in terms of authentication, this is the exception that will represent it
            e.printStackTrace();
        }
        catch (InterruptedException e)
        {
            //Due to the fact that buildBlocking is a blocking method, one which waits until JDA is fully loaded,
            // the waiting can be interrupted. This is the exception that would fire in that situation.
            //As a note: in this extremely simplified example this will never occur. In fact, this will never occur unless
            // you use buildBlocking in a thread that has the possibility of being interrupted (async thread usage and interrupts)
            e.printStackTrace();
        }
        catch (RateLimitedException e)
        {
            //The login process is one which can be ratelimited. If you attempt to login in multiple times, in rapid succession
            // (multiple times a second), you would hit the ratelimit, and would see this exception.
            //As a note: It is highly unlikely that you will ever see the exception here due to how infrequent login is.
            e.printStackTrace();
        }
    }

    /**
     * NOTE THE @Override!
     * This method is actually overriding a method in the ListenerAdapter class! We place an @Override annotation
     *  right before any method that is overriding another to guarantee to ourselves that it is actually overriding
     *  a method from a super class properly. You should do this every time you override a method!
     *
     * As stated above, this method is overriding a hook method in the
     * {@link ListenerAdapter ListenerAdapter} class. It has convience methods for all JDA events!
     * Consider looking through the events it offers if you plan to use the ListenerAdapter.
     *
     * In this example, when a message is received it is printed to the console.
     *
     * @param event
     *          An event containing information about a {@link Message Message} that was
     *          sent in a channel.
     */
    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        //These are provided with every event in JDA
        JDA jda = event.getJDA();                       //JDA, the core of the api.
        long responseNumber = event.getResponseNumber();//The amount of discord events that JDA has received since the last reconnect.

        //Event specific information
        User author = event.getAuthor();                  //The user that sent the message
        Message message = event.getMessage();           //The message that was received.
        MessageChannel channel = event.getChannel();    //This is the MessageChannel that the message was sent to.
        //  This could be a TextChannel, PrivateChannel, or Group!

        String msg = message.getContent();              //This returns a human readable version of the Message. Similar to
        // what you would see in the client.

        boolean bot = author.isBot();                     //This boolean is useful to determine if the User that
        // sent the Message is a BOT or not!
        if(Arrays.stream(StaticStrings.COMMANDS).anyMatch(msg::contains)&&!bot&&msg!=null){ //Arrays.asList(StaticStrings.COMMANDS).contains(msg)

            logServer(msg,jda,event);
        }
        if (event.isFromType(ChannelType.TEXT))         //If this message was sent to a Guild TextChannel
        {
            //Because we now know that this message was sent in a Guild, we can do guild specific things
            // Note, if you don't check the ChannelType before using these methods, they might return null due
            // the message possibly not being from a Guild!

            Guild guild = event.getGuild();             //The Guild that this message was sent in. (note, in the API, Guilds are Servers)
            TextChannel textChannel = event.getTextChannel(); //The TextChannel that this message was sent to.
            Member member = event.getMember();          //This Member that sent the message. Contains Guild specific information about the User!

            String name = member.getEffectiveName();    //This will either use the Member's nickname if they have one,
            // otherwise it will default to their username. (User#getName())

            if(!"Discord Bots".equals(guild.getName())){

                System.out.printf("(%s)[%s]<%s>: %s\n", guild.getName(), textChannel.getName(), name, msg);

            }
        }
        else if (event.isFromType(ChannelType.PRIVATE)) //If this message was sent to a PrivateChannel
        {
            //The message was sent in a PrivateChannel.
            //In this example we don't directly use the privateChannel, however, be sure, there are uses for it!
            PrivateChannel privateChannel = event.getPrivateChannel();

            System.out.printf("[PRIV]<%s>: %s\n", author.getName(), msg);
        }
        else if (event.isFromType(ChannelType.GROUP))   //If this message was sent to a Group. This is CLIENT only!
        {
            //The message was sent in a Group. It should be noted that Groups are CLIENT only.
            Group group = event.getGroup();
            String groupName = group.getName() != null ? group.getName() : "";  //A group name can be null due to it being unnamed.

            System.out.printf("[GRP: %s]<%s>: %s\n", groupName, author.getName(), msg);


        }


        //Now that you have a grasp on the things that you might see in an event, specifically MessageReceivedEvent,
        // we will look at sending / responding to messages!
        //This will be an extremely simplified example of command processing.

        //Remember, in all of these .equals checks it is actually comparing
        // message.getContent().equals, which is comparing a string to a string.
        // If you did message.equals() it will fail because you would be comparing a Message to a String!
        if (msg.equals("!ping"))
        {
            //This will send a message, "pong!", by constructing a RestAction and "queueing" the action with the Requester.
            // By calling queue(), we send the Request to the Requester which will send it to discord. Using queue() or any
            // of its different forms will handle ratelimiting for you automatically!

            channel.sendMessage("pong!").queue();
        }else if(msg.equals("!bhelp")&&!bot){
            channel.sendMessage(StaticStrings.HELP).queue();
        }
        else if (msg.equals("!roll"))
        {
            //In this case, we have an example showing how to use the Success consumer for a RestAction. The Success consumer
            // will provide you with the object that results after you execute your RestAction. As a note, not all RestActions
            // have object returns and will instead have Void returns. You can still use the success consumer to determine when
            // the action has been completed!

            Random rand = new Random();
            int roll = rand.nextInt(6) + 1; //This results in 1 - 6 (instead of 0 - 5)
            channel.sendMessage("Your roll: " + roll).queue(sentMessage ->  //This is called a lambda statement. If you don't know
            {                                                               // what they are or how they work, try google!
                if (roll < 3)
                {
                    channel.sendMessage("The role for messageId: " + sentMessage.getId() + " wasn't very good... Must be bad luck!\n").queue();
                }
            });
        }else if(msg.equals("!slava")){
            channel.sendMessage("Gay! ").queue();

    }else if(msg.contains("!get")&&!bot){
            String word="";
            try{
                channel.sendTyping().queue();
                word=msg.replace("!get","").trim();

                Summoner summoner = RiotAPI.getSummonerByName(word);
                long sum_ico=summoner.getProfileIconID();

                String info="Summoner "+summoner.getName()+" is "+
                        summoner.getLeagueEntries().get(0).getTier()+" "+
                        summoner.getLeagues().get(0).getParticipantEntry().getDivision()+" with "+
                        summoner.getLeagues().get(0).getParticipantEntry().getLeaguePoints()+" LP.";
                channel.sendMessage(info).queue();
                channel.sendMessage("http://avatar.leagueoflegends.com/euw/"+word+dd_icon_format).queue();
            }catch (APIException e){
                channel.sendMessage("Ranked info not found!").queue();
                System.out.println(word);
                e.printStackTrace();
            }

        }else if(msg.contains("!curr")&&!bot){
            String summoner=msg.replace("!curr","");
            if (summoner.equals(""))summoner=author.getName();
            StringBuilder str=new StringBuilder();
            try{
                channel.sendMessage("__***Getting information***__").queue();
                channel.sendTyping().queue();
                CurrentGame curr_game= RiotAPI.getCurrentGame(summoner);
                int curr_game_players=curr_game.getParticipants().size();
                int amountA=0; int amountB=0;
                long biggest_mastery=0;
                String best_sum_name="";
                String best_sum_champ="";
                float win_p=0;
                long num_plays=0;
                Map<Champion, ChampionStats> stats;
                int number=1;
                for (int curr_sum=0;curr_sum<curr_game_players;curr_sum++){

                    Summoner summoner_curr=curr_game.getParticipants().get(curr_sum).getSummoner();
                    String champ_name=curr_game.getParticipants().get(curr_sum).getChampion().getName();
                    try{
                        stats= summoner_curr.getRankedStats();
                        String name = "";



                        for (Map.Entry<Champion, ChampionStats> entry : stats.entrySet()) {
                            if (entry.getKey() != null) {
                                name = entry.getKey().toString();
                            }
                            if (name.equals(champ_name)) {
                                num_plays = entry.getValue().getStats().getTotalGamesPlayed();
                                win_p = entry.getValue().getStats().getTotalWins() * 100 / num_plays;

                            }
                        }
                        try {
                            ChampionMastery curr_champ_mastery =
                                    curr_game.getParticipants().get(curr_sum)
                                                                .getChampion().getChampionMastery(summoner_curr);
                            str.append("**"+number + " " + summoner_curr.getName() + "** is playing as `" +
                                    champ_name + "` and he is " +"**"+

                                    curr_champ_mastery.getChampionLevel() + " LVL with " +
                                    curr_champ_mastery.getChampionPoints() + " points with " +
                                    num_plays + " games and " +
                                    win_p + "% win rate ** \n");

                                    number++;
                            if (curr_sum < curr_game_players / 2) {
                                amountA += curr_champ_mastery.getChampionPoints();
                            } else {
                                amountB += curr_champ_mastery.getChampionPoints();
                            }
                            if (biggest_mastery < curr_champ_mastery.getChampionPoints()) {
                                biggest_mastery = curr_champ_mastery.getChampionPoints();
                                best_sum_name = summoner_curr.getName();
                                best_sum_champ = champ_name;
                            }
                        } catch (IllegalArgumentException er) {
                            str.append("Unknown Player \n");
                            er.printStackTrace();
                        }
                    }catch (APIException e) {
                        str.append("No info");
                        e.printStackTrace();
                    }

                }
                String match=(amountA<amountB)?"<":">";
                str.append("`Team A="+
                        amountA/(curr_game_players/2)+
                        " "+match+
                        " Team B="+
                        amountB/(curr_game_players/2)+
                        "`\n");
                str.append("Best player of the game - **"+best_sum_name+"** with "+ biggest_mastery + " points as `"+best_sum_champ+"`.");
                channel.sendMessage(str.toString()).queue();
            }catch (APIException e){
                e.printStackTrace();
                System.out.println(summoner);
                channel.sendMessage("Error").queue();
            }catch (NullPointerException ne){
                channel.sendMessage("There is no Active Game!").queue();
            }
    }else if(msg.equals("!randChamp")||msg.equals("!rc")){
            channel.sendTyping().queue();
            List<Champion> champions = RiotAPI.getChampions();
            int champs=champions.size();
            Random rand = new Random();
            int index=rand.nextInt(champs);
            String champion=champions.get(index).getName();
            String champIcon=champions.get(index).getImage().getFull()  ;
            channel.sendMessage(author.getName()+" go play "+champion+ " "+ dd_champ_icon+champIcon).queue();
        }else if(msg.equals("!zalupa")||msg.equals("!denis")){
            channel.sendTyping().queue();
        channel.sendMessage(StaticStrings.ZALUPA).queue();
        }else if(msg.contains("!daily")&&!bot){
            String summoner=msg.replace("!daily","");
            try{
                channel.sendTyping().queue();
                Date date=RiotAPI.getRecentGames(summoner).get(0).getCreateDate();
                Date now=new Date();
                Long daily=now.getTime()-date.getTime();
                    Long dailyConv=TimeUnit.HOURS.convert(daily,  TimeUnit.MILLISECONDS);

                if(dailyConv<=22){
                    channel.sendMessage(summoner+"'s daily could not be available yet, maximum wait time is - "+(22-dailyConv)+" \nLast game "+date.toString()).queue();
                }else if(dailyConv>22){
                    channel.sendMessage(summoner+"'s daily must be available!"+" \nLast game "+date.toString()).queue();
                }else{
                    channel.sendMessage("Error "+author.getName()).queue();
                }
            }catch (APIException e){
                e.printStackTrace();
                channel.sendMessage("Error "+author.getName()).queue();
            }
        }else if(msg.contains("!rg")&&!bot){
            String summoner=msg.replace("!rg","");
            try{
                channel.sendTyping().queue();
                channel.sendMessage("__**Getting Information**__").queue();
                List<com.robrua.orianna.type.core.game.Game> recent = RiotAPI.getRecentGames(summoner);
                StringBuilder str=new StringBuilder();
                int iter=1;
                int win_c=0;
                for(int rec_n=0;rec_n<recent.size();rec_n++){
                    boolean inv=recent.get(rec_n).getInvalid();
                    String res="";
                    boolean result=recent.get(rec_n).getStats().getWin();
                    if(!inv){
                        if(result){
                            res="WIN";
                            win_c++;
                        }else{res="LOSE";}
                    }else{
                        res="Invalid";
                    }

                    String champName=recent.get(rec_n).getChampion().getName();
                    int kills=recent.get(rec_n).getStats().getKills();
                    int deaths=recent.get(rec_n).getStats().getDeaths();
                    int assists=recent.get(rec_n).getStats().getAssists();
                    String endl="**"+iter+" "+res+"** on "+champName+" with "+kills+"/"+deaths+"/"+assists+"\n";
                    str.append(endl);
                    iter++;
                }
                str.append("Won "+win_c+"/10");
                channel.sendMessage(str.toString()).queue();
            }catch (APIException e){
                e.printStackTrace();
                channel.sendMessage("Error").queue();
            }
        }else if(msg.contains("!ranks")&&!bot){
            String summoner=msg.replace("!ranks","");
            try{
                channel.sendTyping().queue();
                channel.sendMessage("__**Getting information**__").queue();
                List<ChampionMastery> ranks = RiotAPI.getSummonerByName(summoner).getChampionMastery();
                StringBuilder str = new StringBuilder();
                str.append("Summoner **"+summoner+"** have total **"+RiotAPI.getSummonerByName(summoner).getTotalMasteryLevel()+"** mastery points. \n");
                int iter=1;
                for (int i=0;i<10;i++){
                    String summ_name=ranks.get(i).getChampion().getName();
                    int summ_lvl=ranks.get(i).getChampionLevel();
                    long summ_points=ranks.get(i).getChampionPoints();
                    String result="**"+iter+" "+summ_name+"** with **"+summ_lvl+" LVL** and **"+summ_points+"** points.\n";
                    str.append(result);
                    iter++;
                }
                channel.sendMessage(str.toString()).queue();
            }catch (APIException e){
                e.printStackTrace();
                channel.sendMessage("Error").queue();
            }
        }else if(msg.contains("!info")&&!bot){
            String champ_name=msg.replace("!info","");
                try{
                    channel.sendTyping().queue();
                    Champion champion=RiotAPI.getChampionByName(champ_name.trim());
                    //System.out.println(champ_name);
                    String icon=dd_champ_icon+champion.getImage().getFull();
                    List<ChampionSpell> spells = champion.getSpells();
                    StringBuilder str=new StringBuilder();
                    str.append("__**"+champ_name+"**__\n");
                    for(ChampionSpell s:spells){
                        String name="**"+s.getName()+"**\n";
                        String desc=s.getDescription();
                        str.append(name+desc+"\n");
                    }

                    str.append(icon);
                    channel.sendMessage(str.toString()).queue();

                }catch (APIException e){
                    e.printStackTrace();
                    channel.sendMessage("Error").queue();
                }catch (NullPointerException en){
                    channel.sendMessage("No champion found").queue();
                }
            }else if(msg.contains("!call")&&!bot){
            MessageBuilder ms=new MessageBuilder();

            channel.sendMessage(ms.append("Let's go to the League of Legends!").setTTS(true).build()).queue();
        }else if(msg.contains("!write")&&!bot){
            String name=Translit.toTranslit(msg.replace("!write","").trim().toLowerCase()).replaceAll("[^a-zA-Z]", " ");

            //System.out.println(name);
            StringBuilder str=new StringBuilder();
            for(char ch : name.toCharArray()){
                if (!String.valueOf(ch).equals(" ")){
                    str.append(":regional_indicator_"+String.valueOf(ch)+": ");

                }else{
                    str.append("   ");
                }
            }
            channel.sendMessage(str.toString()).queue();
        }
        else if(msg.contains("!status")&&!bot){
            int guilds=jda.getGuilds().size();
            int people=0;
            StringBuilder str= new StringBuilder();
            str.append("Alive at "+guilds+" servers.\n");
            int number=1;
            for(int i=0;i<guilds;i++){
                Guild guild=jda.getGuilds().get(i);
                str.append(number+") "+guild.getName()+" with "+guild.getMembers().size()+" people. \n");
                people+=guild.getMembers().size();
                number++;
            }
            str.append("SUM Members "+people+"  .");
            MessageBuilder messageBuilder = new MessageBuilder();
            Queue<Message> m = messageBuilder.append(str).buildAll(MessageBuilder.SplitPolicy.NEWLINE);
            m.forEach(mss -> event.getChannel().sendMessage(mss.getRawContent()).queue());
        }else if(msg.contains("!donate")&&!bot){
            channel.sendMessage(StaticStrings.DONATE).queue();
        }else if(msg.contains("!invite")&&!bot){
            channel.sendMessage(StaticStrings.INVITE).queue();
        }else if(msg.contains("!bliz")&&!bot){
            channel.sendMessage(StaticStrings.SERVER).queue();
        }
        }
        public void logServer(String command,JDA jda,MessageReceivedEvent event){
            String guild=event.getGuild().getName();
            String author=event.getAuthor().getName();
            jda.getGuildById("335390036931379202").getTextChannelById("335392770510422016").sendMessage(
                    ":white_circle:  "+guild+" "+author+" ** "+ command+" **\n"
                    ).queue();
        }
        /*else if(event.isFromType(ChannelType.PRIVATE)&&msg.contains("!fromBot")&&!bot){
            TextChannel textChannel = event.getTextChannel();
            textChannel.sendMessage(msg).queue();
        }*/
    }