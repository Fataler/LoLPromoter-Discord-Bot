import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Fataler on 13.03.2017.
 */
public class StaticStrings {
    public static String ZALUPA="" +
            "───────────────▄▄▄▄▄▄▄───────────\n" +
            "─────────────▄█▒▒▒█▒▒▒█▄─────────\n" +
            "────────────█▒▒▒▒▒▒▒▒▒▒█▌────────\n" +
            "───────────█▒▒▒▒▒▒▒▒▒▒▒▒█────────\n" +
            "───────────█▒▒▒▒▒▒▒▒▒▒▒█▌────────\n" +
            "──────────██████████████─────────\n" +
            "──────────█▒▒▒▒▒▒▒▒▒▒▒█▌─────────\n" +
            "─────────█▒████▒████▒▒█──────────\n" +
            "─────────█▒▒▒▒▒▒▒▒▒▒▒▒█──────────\n" +
            "─────────█▒────▒▒────▒█▌─────────\n" +
            "─────────█▒██──▒▒██──▒▒█─────────\n" +
            "─────────█▒────▒▒────▒▒█─────────\n" +
            "────────▄█▒▒▒▒▒▒▒▒▒▒▒▒▒██────────\n" +
            "───────██▒▒▒████████▒▒▒▒██───────\n" +
            "─────██▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒██─────\n" +
            "───██▒▒▒▒▒▒▒▒▒▒▒█▒▒▒▒▒▒▒▒▒▒▒██───\n" +
            "─██▒▒▒▒▒▒▒▒▒▒▒▒██▒▒▒▒▒▒▒▒▒▒▒▒▓██─\n" +
            "█▒▒▒▒▒▒▒▒▒▒▒▒▒██▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒██\n" +
            "█▒▒▒▒▒▒▒▒▒▒▒▒▓█▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒█\n" +
            "█▓▒▒▒▒▒▒▒▒▒▒▒▓██▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒█\n" +
            "▀██▒▒▒▒▒▒▒▒▒▒▒▓██▒▒▒▒▒▒▒▒▒▒▒▒▒██▀\n" +
            "──██▒▒▒▒▒▒▒▒▒██████▒▒▒▒▒▒▒▒▒▒██──\n" +
            "───███████████▌▌▌▌████████████───";
    public static String HELP=
                    "!invite - get **invite** link. \n" +
                    "!bhelp - this message. \n" +
                    "!donate - get link for **donations**. \n" +
                    "!rc [!randChamp] - get random **champ** to play.\n" +
                    "!curr {Summoner Name} - information about **currient game**.\n" +
                    "!get {Summoner Name} - ranked info about **summoner**. \n" +
                    "!daily {Summoner Name} - information about summoner's **daily** bonus.\n" +
                    "!rg {Summoner Name} - information about **last 10 games**.\n"+
                    "!ranks {Summoner Name} - information about **champ mastery**. \n"+
                    "!info {Champion Name} - information about **champion**. \n"+
                    "!call - call to the League.\n"+
                    "!write {Text} - Write text with **Emoji**.\n"+
                    "!blitz - Our support and log server.\n"+
                    "!zalupa [!denis] - hmmm.\n"+
                    "!slava - info about Slava.\n";
    public static String[] COMMANDS=new String[] {"!bhelp","!donate","!invite","!daily","!rc","!curr","!get","!rg ","!ranks","!info",
            "!call","!write","!blitz","!zalupa","!slava"};
    public static final Set<String> VALUES = new HashSet<String>(Arrays.asList(
            new String[] {"!help","!donate","!invite","!daily","!rc","!curr","!get","!rg","!ranks","!info",
                    "!call","!write","!blitz","!zalupa","!slava"}));
    public static String DONATE="http://yasobe.ru/na/lolpromoter \n"+
            "https://qiwi.me/blitz \n";
    public static String INVITE=
            "https://discordapp.com/oauth2/authorize?client_id=290225453354975232&scope=bot&permissions=7168";
    public static String SERVER="https://discord.gg/FXTJCQR";
}
