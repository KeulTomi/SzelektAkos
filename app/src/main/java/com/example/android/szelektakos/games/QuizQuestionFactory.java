package com.example.android.szelektakos.games;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by Tomi on 2017. 04. 10..
 */

public class QuizQuestionFactory {

    public static String mFirstAnswer;
    public static String mSecondAnswer;
    public static String mThirdAnswer;
    public static int mCorrectAnswer;
    public static String mQuestion;
    public static String[] firstAnswersArray = {"Oros, Szállási u. 72., Bokréta u. 22.","gumiabroncs, papír, fém, üveg","anyaguk alapján","A papír esztétikusabb, mint a műanyag csomagoló.","Nem, mert fárasztó.","Mindkét oldalára írunk, rajzolunk.","sárga/kék","sárga/kék","sárga","ceruzaelem","Túrót, vajat, sajtot.","Mindig szabad.","Városüzemeltetési KHT.","papírt","Fák kivágását kerülhetjük el.","víz","A papírok közé – kék.","almahéjat","földgáz","Hulladékudvarokban","szaru","kínaiak","25 kg","A papír esztétikusabb, mint a műanyag csomagoló.","Mezopotámia","vasion","tükör","5%","Papírból, üvegből és fémből.","25%","A svédek, kb. 50 éve.","A papírok közé – kék.","kőszénből","timföld","a cinket és az ónt","a zöld színű hulladékok","hetente egyszer","hetente egyszer","30%","használt papír zsebkendő","30-40 –szer","törött üveg","évente kétszer","lakossági veszélyes hulladék","háztartási „vegyes" + "hulladékot","csomagolódoboz","tojástartó","sörös üveg","tojás","villanykörte","kő, üveg","festékes edény  - fémgyűjtő","visszaváltható sörösüveg","száraz hulladék","családi házas negyed","az önkormányzaté","vegyszeres műanyag doboz","szemétlerakó","fém italosdoboz – sárga/kék gyűjtő","mikrohullámú, átlátszó tálca","műanyag gyermekjáték","csomagolódoboz","PVC","sörös doboz","papír alapú matrica","80%","50 év","100 – 500 év","mulchot","A Korányi Frigyes úton","akár 40 x is","A hulladék a kukában van, ebből szemét lesz, ha a szemétlerakóba kerül."};
    public static String[] secondAnswersArray =  {"Hősök tere 15., Tüzér u. 2-4.","műanyag, papír, fém, üveg, italos-kartondoboz","keménységük alapján","A papír lebomlik, míg a műanyag nem.","Igen, hisz „újrapapír" + "lesz belőle, kevesebb fát fognak kivágni.","Csak az egyik oldalára rajzolunk.","fehér/zöld","fehér/zöld","zöld","kartonpapír","Befőttet, lekvárt, savanyúságot.", "Csak ha átválogatjuk","Térségi Hulladék-Gazdálkodási Nonprofit Kft.","nem hasznosítja","Tüzet okozhatunk a sok papírral.","kőolaj","A műanyagok közé – sárga.","falevelet","kőolaj","Regionális Hulladékkezelő Telepen","növényi rost","egyiptomiak","50 kg","A papír lebomlik, míg a műanyag nem.","Egyiptom","rézion","obszidián","20%","Papírból, műanyagból és alumíniumból.","75%","Az amerikaiak az 1980-as évektől.","A műanyagok közé – sárga.","ércekből","bauxit","az alumíniumot és a vasat","a növényi eredetű bomló szerves anyagok","havonta egyszer","havonta egyszer","5%","műanyag reklámtáska","2-3-szor","szélvédő üveg","évente egyszer","nagy darabos lom","az újrahasznosítható hulladékot","tükör","almacsutka","fém kupak","üdítő","italos üveg","konyhai zöldségmaradék","kiégett izzó – üveggyűjtő","díszdobozos csokoládé","üveghulladék","külváros","mindannyiunké","italoskarton doboz","hulladékudvar","matrica – sárga/kék gyűjtő","rövid italos átlátszó üveg","műanyag reklámtáska","átlátszó boros üveg","mobiltelefon akkumulátor nélkül","műszálas ruhanemű","műanyag szemüveg keret","8%","1000 év","1-2 hónap","komposztot","Nyírszőlősön","3-6-szor","A hulladék az, ami a kukába kerül, a szemét az utcán van."};
    public static String[] thirdAnswersArray = {"Korányi F. u. 3., Kerék u. 1.","fém, üveg műanyag,elektronikai hulladék","színük szerint.","Nincs köztük különbség.","Igen, mert akkor nem kell tanulni.","Egyáltalán nem rajzolunk, nem fogyasztjuk a papírt.","barna","barna","kék","eltörött üvegpohár","Tejet és gyümölcsleveket.","Sohasem szabad.","ÖKO-Pannon Kht.","komposztot","Nem bomlik le.","kvarchomok.","Sárgába is, kékbe is.","használt sütőolajat","vasérc","Ügyfélszolgálatunkon","kőolaj","görögök","75 kg","Nincs köztük különbség.","Római Birodalom","oxidion","PET palack","70%","Papírból, fémből és fából.","90%","A finnek az első világháború óta.","Sárga/kék.","kőolajból","vörösiszap","a rezet és az ólomot","amelyeket a zöld harang alakú gyűjtőkonténerbe lehet elhelyezni","havonta kétszer","kéthetente","90%","matrica","egyszer sem","olajos, vegyszerrel szennyezett, gyógyszeres üveg","kétévente","háztartási hulladék","a komposztálható hulladékot","bébiételes üveg","porcelán bögre","folpack","nutella","lekváros üveg","csalánlevél","kerti nyesedék – zöldhulladékgyűjtő","eldobható üdítős pillepalack","szerves hulladék","lakótelep","a felnőtteké","üdítős palack","hulladékégető","fax papír – sárga/kék gyűjtő","gyógyszeres fehér üveg","faxpapír","lejárt szavatosságú elem","kiürült gyümölcsleves doboz","porcelán bögre","műanyag palack","15%","soha nem bomlik le","50-10 év","műtrágyát","Oroson","12-15-ször","A hulladék újrahasznosítható, a szemét nem."};
    public static Integer[] correctAnswersArray = {3,2,3,2,2,1,1,1,2,1,3,3,2,3,1,3,3,3,2,1,2,1,2,2,2,3,3,2,2,2,1,3,2,2,2,2,1,3,1,2,1,3,1,3,1,2,1,1,2,1,1,3,1,2,1,2,1,2,1,2,2,1,3,1,3,3,3,1,2,3,2,3};
    public static String[] questions = {"Hol található Nyíregyházán a két Hulladékgyűjtő udvar","Milyen anyagtípusokat lehet a szelektív gyűjtőszigeten különgyűjteni" + ", hasznosítani?"
            ,"Milyen tulajdonsága alapján különböztetjük meg egymástól az üveghulladékokat?","Miért környezetbarátabb a papírcsomagolás a műanyag csomagolásnál?"
            ,"Hasznos-e papírgyűjtés?","Hogyan spórolhatunk a papírral?","Milyen színnel jelzett konténerbe gyűjtjük a papírt?"
            ,"Milyen színnel jelzett konténerbe gyűjtjük a műanyagot?","Milyen színnel jelzett konténerbe gyűjtjük a színes üveget?","Melyik veszélyes hulladék?"
            ,"Mit csomagolnak jellemzően italos-karton dobozba?","Szabad-e zsíros, olajos, vegyszeres, vagy más mérgező anyaggal szennyezett hulladékot a szelektív gyűjtőbe tenni?"
            ,"Ki végzi Nyíregyházán  a háztartási hulladék gyűjtését és szállítását?","Mit állít elő a THG Nonprofit Kft. a begyűjtött biohulladékból?"
            ,"Miért fontos, hogy különgyűjtsük a papírt?","Miből készül az üveg?","Melyik gyűjtőedénybe, kell tenni az italos kartondobozokat és a fémhulladékot?"
            ,"Mit nem szabad a komposztba tenni?","Miből készül a műanyag?","Hol adhatja le a nyíregyházi lakos a háztartásában keletkező kis mennyiségű veszélyes hulladékot térítésmentesen?"
            ,"Miből készül a papír?","Kik használták először a papírt?","A világ lakosai fejenként hány kg papírt használnak évente?","Miért környezetbarátabb a papírcsomagolás a műanyagcsomagolásnál?","Hol használtak először üveget?","Mitől kapja zöld és barna színét az üveg?","Az alábbi anyagok közül melyik nem üveg?","A háztartási hulladéknak hány tömegszázalékát teszi ki a műanyag csomagolási hulladék?","Milyen anyagtípusból készül az italos kartondoboz?","Az italos-kartonok minimum hány százaléka papír?","Mikortól és kik használták először az italos kartondobozt?","Melyik gyűjtőedénybe kell tenni az italos kartondobozokat?","Miből állítják elő a fémeket?","Melyik ércből készül az alumínium?","Melyik két fémet használja leggyakrabban a csomagolóipar?","Milyen hulladékok tartoznak a zöld hulladékok csoportjába?","A biohulladék szállítása milyen rendszerességgel történik Nyíregyházán a nyári időszakban?","A biohulladék szállítása milyen rendszerességgel történik Nyíregyházán a nyári időszakban?","A háztartási hulladék hány százaléka komposztálható?","Az alábbi hulladékok közül melyeket lehet a kék konténerbe dobni a papíron kívül?","Hányszor tölthető újra a visszaváltható üvegpalack?","Milyen üveghulladék minősül veszélyes hulladéknak?","Hány alkalommal rendez a THG Nonprofit Kft. egy adott évben Hulladékért virágot akciót?","Milyen hulladék nem adható le a hulladékudvarban?","Mit nevezünk kommunális hulladéknak?","Az alábbi hulladékok közül melyik nem hasznosítható?","Az alábbi hulladékok közül melyik dobható a sárga, vagy kék színű hulladékgyűjtőbe?","Az alábbi hulladékok közül melyik dobható a zöld harang alakú konténerbe?","Mihez használunk általában fém alapanyagú csomagolást?","Melyik hulladék nem dobható az üveggyűjtő konténerbe?","Mit nem ajánlatos a komposztba tenni?","Melyik a helyes párosítás?","Melyik csomagolás jár kevesebb hulladékkal?","Melyik hulladéktípus gyűjtése nem jellemző a házhoz menő gyűjtési módszerre?","Milyen területen, lakókörzetben jellemző leginkább a házhoz menő gyűjtés?","Kinek a feladata a hulladékok megfelelő kezelése?","Melyik hulladékot nem dobjuk a sárga/kék színnel jelölt gyűjtőedénybe?","Hogyan nevezzük a személyzettel ellátott, kerítéssel körülvett, szelektív hulladékgyűjtésre és tárolásra alkalmas helyet?","Melyik a helyes párosítás?","Melyiket dobhatjuk a fehér harang alakú konténerbe?","Az alábbiak közül melyiket dobhatjuk a sárga/kék gyűjtőbe?","Az alábbiak közül melyiket dobhatjuk a sárga gyűjtőbe?","Az alábbiak közül melyiket dobhatjuk a sárga gyűjtőbe?","Az alábbiak közül melyiket dobhatjuk a sárga gyűjtőbe?","Az alábbiak közül melyiket dobhatjuk a kék gyűjtőbe?","A háztartási hulladéknak hány tömegszázalékát teszi ki a papír csomagolási hulladék?","Mennyi idő alatt bomlik az üveg?","Mennyi idő alatt bomlik az alumínium doboz?","Mit készít a THG Nonprofit Kft. a begyűjtött biohulladékból?","Hol van Nyíregyházán Hulladéklerakó?","Hányszor lehet újratölteni a visszaváltható műanyag palackot?","Mi a különbség a HULLADÉK és SZEMÉT között?"};

    public static List<String> dinamicQuestionList = new ArrayList<String>(Arrays.asList(questions));
    public static List<String> dinamicFirstAList = new ArrayList<String>(Arrays.asList(firstAnswersArray));
    public static List<String> dinamicSecondAList = new ArrayList<String>(Arrays.asList(secondAnswersArray));
    public static List<String> dinamicThirdAList = new ArrayList<String>(Arrays.asList(thirdAnswersArray));
    public static List<Integer> dinamicCorrectAList = new ArrayList<Integer>(Arrays.asList(correctAnswersArray));

    public static void getTheCorrectAnswerList() {
        dinamicCorrectAList = new ArrayList<Integer>();
        for (int index = 0; index < correctAnswersArray.length; index++) {
            dinamicCorrectAList.add(correctAnswersArray[index]);
        }
    }

    public static void questionFactory () {
        Random r = new Random();

        //Csak addig remove-olunk amíg vannak kérdések, ha elfogytak újratöltjük
        if (QuizQuestionFactory.dinamicQuestionList.size() > 0) {

            int questionNumber = r.nextInt(QuizQuestionFactory.dinamicQuestionList.size());

            QuizQuestionFactory.mQuestion = QuizQuestionFactory.dinamicQuestionList.get(questionNumber);
            dinamicQuestionList.remove(questionNumber);
            QuizQuestionFactory.mFirstAnswer = QuizQuestionFactory.dinamicFirstAList.get(questionNumber);
            dinamicFirstAList.remove(questionNumber);
            QuizQuestionFactory.mSecondAnswer = QuizQuestionFactory.dinamicSecondAList.get(questionNumber);
            dinamicSecondAList.remove(questionNumber);
            QuizQuestionFactory.mThirdAnswer = QuizQuestionFactory.dinamicThirdAList.get(questionNumber);
            dinamicThirdAList.remove(questionNumber);
            //TODO Ugyanolyan listába kell tenni mint a többit
            QuizQuestionFactory.mCorrectAnswer = QuizQuestionFactory.dinamicCorrectAList.get(questionNumber);
            dinamicCorrectAList.remove(questionNumber);
        }
        else {
            dinamicQuestionList = new ArrayList<String>(Arrays.asList(questions));
            dinamicFirstAList = new ArrayList<String>(Arrays.asList(firstAnswersArray));
            dinamicSecondAList = new ArrayList<String>(Arrays.asList(secondAnswersArray));
            dinamicThirdAList = new ArrayList<String>(Arrays.asList(thirdAnswersArray));
            questionFactory();
        }
    }


    public static String getmFirstAnswer() {
        return mFirstAnswer;
    }

    public static String getmSecondAnswer() {
        return mSecondAnswer;
    }

    public static String getmThirdAnswer() {
        return mThirdAnswer;
    }

    public static int getmCorrectAnswer() {
        return mCorrectAnswer;
    }

    public static String getmQuestion() {
        return mQuestion;
    }
}
