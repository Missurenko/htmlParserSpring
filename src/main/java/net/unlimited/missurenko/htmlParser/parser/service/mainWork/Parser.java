package net.unlimited.missurenko.htmlParser.parser.service.mainWork;

import net.unlimited.missurenko.htmlParser.parser.dto.BooleanDto;
import net.unlimited.missurenko.htmlParser.parser.dto.RecursiaForTagADto;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * Created by bm on 08.10.17.
 */
public class Parser {

    private Element element;
    private List<String> keyWordList;
    private List<String> filterTag;

    ParserBooleanSupport parserBooleanSupport = new ParserBooleanSupport();

    public Parser(Element element, List<String> keyWordList, List<String> filterTag) {
        this.element = element;
        this.keyWordList = keyWordList;
        this.filterTag = filterTag;
    }

    public Element start() {
        recursiveMetod(element);
        return element;
    }

    private void recursiveMetod(Element mainElement) {

        parserBooleanSupport.deleteHeaderFoaterTags(mainElement, filterTag);
        List<Boolean> whatDelete = new ArrayList<>();
        BooleanDto booleanDtoParant = parserBooleanSupport.booleanMetodGlobal(mainElement, keyWordList);
        if (mainElement.children().size() > 1) {
            if (!mainElement.tag().toString().equals("head") &
                    !mainElement.tag().toString().equals("html") &
                    !mainElement.tag().toString().equals("#root")) {
                List<BooleanDto> booleanDtoChilds = new ArrayList<>();
                for (Element child : mainElement.children()) {

                    BooleanDto childDto = parserBooleanSupport.booleanMetodGlobal(child, keyWordList);
                    booleanDtoChilds.add(childDto);
                }

// return list true false what need delete
                whatDelete = flagForDalete(booleanDtoParant, booleanDtoChilds);

            }
        }

        parserBooleanSupport.deleteOnly(mainElement, whatDelete);

        int countForEnd = 0;

//        // count false flag for know go deap or not
//        int coutnFalseGoDeepOrNot = 0;
//        // all childs flag delete or not
//        for (boolean flagChild : deleteBlockList) {
//            if (!flagChild) {
//                coutnFalseGoDeepOrNot++;
//            }
//        }
//
//        // if have head allweys 1 and if have
//        if (mainElement.child(0).tag().toString().equals("head") & coutnFalseGoDeepOrNot > 0) {
//            coutnFalseGoDeepOrNot = 1;
//        }
//        // if we have one child this undestand need go more deep and your have one block what you need
//        if (coutnFalseGoDeepOrNot == 1) {
//            for (Element child : mainElement.children()) {
//                // head can be changed in future can be delete or another
//                if (!child.tag().toString().equals("head")) {
//                    recursiveMetod(child);
//                }
//            }
//        }
        // this cout how many stay child in main element and if you have more then two go deeper
        for (Element child : mainElement.children()) {
            if (!child.tag().toString().equals("head")) {
                countForEnd++;
            }
        }
// tommorow need understand why no work this part of code
        if (countForEnd < 2 & htmlTagMetod(mainElement)) {
            for (Element child : mainElement.children()) {
                if (!child.tag().toString().equals("head")) {
                    recursiveMetod(child);
                }
            }
        }


    }

    // завязан на основний елемент в филдах
    private boolean htmlTagMetod(Element mainElement) {
        if (mainElement.tag().toString().equals("html")) {
            if (mainElement.getElementsByTag("h1").size() == 0 &
                    mainElement.getElementsByTag("h2").size() == 0) {
                element = null;
                System.out.println("Parser element set null");
                return false;
            }
            if (!parserBooleanSupport.containKeyWord(mainElement, keyWordList)) {
                element = null;
                System.out.println("Parser element set null");
                return false;
            }
        }
        return true;
    }

    // основной метод для виставление флагов чтоб потом удалить
    private List<Boolean> flagForDalete(BooleanDto booleanDtoParant, List<BooleanDto> booleanDtoChildList) {
        List<Boolean> result = new ArrayList<>();

        // if I have DTO why I need fields ????
        int lenght = 0;
        int lenghtTagA = 0;
        int lenghtClearText = 0;
        int falseFlag = 0;
        int positionMoreCleanText = -1;
        int firstPositionH1 = -1;
        int countH1H2 = 0;
        for (int i = 0; i < booleanDtoChildList.size(); i++) {
            BooleanDto child = booleanDtoChildList.get(i);
            lenght = child.getTextLenght();
            lenghtTagA = child.getLenghtTextInATag();
            // если чистого текста больше чем позиции с чистим текстом ставим такую позицию
            if (lenghtClearText < parserBooleanSupport.cleanText(lenght, lenghtTagA)) {
                lenghtClearText = parserBooleanSupport.cleanText(lenght, lenghtTagA);
                positionMoreCleanText = i;
            }
            // если содержит Н1 или Н2 то ставим позицию
            if (child.isContainH1()) {
                countH1H2++;
                if (firstPositionH1 == -1) {
                    firstPositionH1 = i;
                }
            }
            // и по дефолту добавляем true
            result.add(true);
        }
        BooleanDto moreTextThenOther = new BooleanDto();
        // если позиция больше всего текста не есть -1 то ставим позицию
        if (positionMoreCleanText != -1) {
            moreTextThenOther = booleanDtoChildList.get(positionMoreCleanText);

        }
        // если содержит ключевое словл
        if (booleanDtoParant.getCountKeyWordInClearText() > 0) {

            // если имеим один Н1 то действуем
            if (countH1H2 == 1) {
                // page this one h1 set for block
                // если содержит H1 и больше всего ключевих слов наверно взял снизу
                if (booleanDtoParant.isContainH1() &
                        moreTextThenOther.getCountKeyWordInClearText() > 0 &
                        moreTextThenOther.isContainH1()) {
                    result.set(positionMoreCleanText, false);
                    falseFlag++;
                }
                // если позиция с больше всего текстом true и флагов с false 0 то
                if (result.get(positionMoreCleanText) & falseFlag == 0) {
                    for (int i = firstPositionH1; i < positionMoreCleanText + 1; i++) {
                        // TODO сделать рекурсию для удаление все наследников с тагом <a
                        // think here do recurthin more deep
                        result.set(i, false);
                        falseFlag++;
                    }

                    boolean end = false;
                    for (int i = positionMoreCleanText + 1; i < result.size() + 1; i++) {
                        BooleanDto child = booleanDtoChildList.get(i);
                        // если чистого текста больше чем ключевих слов в тагазх ???
                        // TODO изменить сдесь хрень полнейшая
                        //
                        if (child.getLenghtClearText() > child.getKeyWordInTagA() &
                                !end) {
                            result.set(i, false);
                            falseFlag++;
                        } else {
                            end = true;
                        }
                    }
                }
            }
        } else {
            return result;
        }
        // if тут ждем разделение пока не разделиться Н1 и больше всего чистого текста
        if (booleanDtoParant.isContainH1() &
                moreTextThenOther.getCountKeyWordInClearText() > 0 &
                moreTextThenOther.isContainH1()) {
            result.set(positionMoreCleanText, false);
            falseFlag++;

        } else {
            // если наследник имее Н! и больше всего текста  протеворечит верхнему
            if (booleanDtoChildList.get(positionMoreCleanText).isContainH1()) {
                for (int i = 0; i < positionMoreCleanText + 1; i++) {
                    result.set(i, false);
                    falseFlag++;
                }
                // если больше всего текста находиться на позииции меньше чем максимальная длина наследников +1 думаю убрать
                if (result.size() > positionMoreCleanText + 1) {
                    result.set(positionMoreCleanText + 1, false);
                    falseFlag++;
                }
                // фор сделан глупо если  переделать в последствии на рекурсию если чистго текста меньше чем с а
                for (int i = positionMoreCleanText + 2; i < booleanDtoChildList.size(); i++) {
                    if (parserBooleanSupport.cleanText(booleanDtoChildList.get(i).getTextLenght(),
                            booleanDtoChildList.get(i).getLenghtTextInATag()) > 0) {
                        result.set(i, false);
                    } else {
                        break;
                    }
                }
                // если Там где бальше всего текста содержит Н1 и
                // и больше всего текста содержит ключевих слов больше 0
                // ищем первую позицию где есть Н1 нужно подумать как использивать
            } else if (!booleanDtoChildList.get(positionMoreCleanText).isContainH1() &
                    moreTextThenOther.getCountTextKeyWord() > 0 |
                    countH1H2 > 1 & moreTextThenOther.getCountTextKeyWord() > 0) {
                int position = -1;
                for (int i = 0; i < booleanDtoChildList.size(); i++) {

                    if (booleanDtoChildList.get(i).isContainH1() &
                            position == -1) {
                        position = i;
                    }
                }
                // фолсе между больше всего текста и Н1 Думаю нужно просто дойти до разделение и рекурсией убрать все <a таг
                for (int i = position; i < positionMoreCleanText; i++) {
                    result.set(i, false);
                    falseFlag++;
                }
            }
            String ss = "ss";
        }


        //сли фолсе флаг больше 0
        if (falseFlag > 1) {
            for (int i = 0; i < booleanDtoChildList.size(); i++) {
                // зачемто беру длину
                // длину <a таг
                // и длину чистого текста
                //  и сечу значения
                lenght = booleanDtoChildList.get(i).getTextLenght();
                lenghtTagA = booleanDtoChildList.get(i).getLenghtTextInATag();
                lenghtClearText = lenght - lenghtTagA;
                // и если чистого тексста меньше то ставлю фолсе
                if (lenghtClearText < lenghtTagA) {
                    result.set(i, true);
                }
                result.set(positionMoreCleanText, false);
            }
        }
        return result;
    }
}


