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
    private int startEnd;

    public Parser(Element element, List<String> keyWordList, List<String> filterTag) {
        this.element = element;
        this.keyWordList = keyWordList;
        this.filterTag = filterTag;
    }

    public Parser() {
    }

    public Element start() {
        recursiveMetod(element);
        return element;
    }

    private void deleteMetod(Element mainElement) {
        List<Boolean> flagDeleteOrNot = new ArrayList<>();

        for (int i = 0; i < mainElement.children().size(); i++) {
            boolean flag = getFlagDeleteByFilters(mainElement.child(i), mainElement);

            flagDeleteOrNot.add(flag);
        }
        String ss = "ss";
        for (int i = flagDeleteOrNot.size() - 1; i >= 0; --i) {
            if (flagDeleteOrNot.get(i)) {
                mainElement.child(i).remove();

            }
        }
    }


    private void deleteOnly(Element mainElement, List<Boolean> whatDelete) {
        for (int i = whatDelete.size() - 1; i >= 0; --i) {
            if (whatDelete.get(i)) {
                mainElement.child(i).remove();

            }
        }
    }


    private void shortRecursive(Element mainElement) {
        deleteMetod(mainElement);
        for (Element child : mainElement.children()) {
            shortRecursive(child);
        }
    }

    private void recursiveMetod(Element mainElement) {
        List<Boolean> deleteBlockList = new ArrayList<>();
        for (Element child : mainElement.children()) {
            if (mainElement.tag().toString().equals("body") &
                    child.tag().toString().equals("header") |
                    mainElement.tag().toString().equals("body") &
                            child.tag().toString().equals("footer")) {
                deleteBlockList.add(true);
            } else if (flagDeleteNoNeedTags(child)) {
                deleteBlockList.add(true);
            } else {
                deleteBlockList.add(false);
            }
            if (child.tag().toString().equals("head")) {
                shortRecursive(child);
            }

        }


        deleteOnly(mainElement, deleteBlockList);

        List<Boolean> whatDelete = new ArrayList<>();
        BooleanDto booleanDtoParant = booleanMetodGlobal(mainElement);
        if (mainElement.children().size() > 1) {
            if (!mainElement.tag().toString().equals("head") &
                    !mainElement.tag().toString().equals("html") &
                    !mainElement.tag().toString().equals("#root")) {
                List<BooleanDto> booleanDtoChilds = new ArrayList<>();
                for (Element child : mainElement.children()) {

                    BooleanDto childDto = booleanMetodGlobal(child);
                    booleanDtoChilds.add(childDto);
                }

// return list true false what need delete
                whatDelete = flagForDalete(booleanDtoParant, booleanDtoChilds, mainElement);

            }
        }

        deleteOnly(mainElement, whatDelete);
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

    private boolean htmlTagMetod(Element mainElement) {
        if (mainElement.tag().toString().equals("html")) {

            if (mainElement.getElementsByTag("h1").size() == 0 &
                    mainElement.getElementsByTag("h2").size() == 0) {

                element = null;
                System.out.println("Parser element set null");
                return false;

            }
            if (!containKeyWord(mainElement)) {
                element = null;
                System.out.println("Parser element set null");
                return false;
            }
        }
        return true;

    }

    // основной метод для виставление флагов чтоб потом удалить
    private List<Boolean> flagForDalete(BooleanDto booleanDtoParant, List<BooleanDto> booleanDtoChildList, Element mainElement) {
        List<Boolean> result = new ArrayList<>();

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
            if (lenghtClearText < cleanText(lenght, lenghtTagA)) {
                lenghtClearText = cleanText(lenght, lenghtTagA);
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
                    if (cleanText(booleanDtoChildList.get(i).getTextLenght(),
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

    // если длина всего текста длинне чем текс в тагах хрень собачья
    // TODO изменить на чистий текст и таг <a
    private int cleanText(int lenghtAllText, int lenghtTextTagA) {
        return lenghtAllText - lenghtTextTagA;
    }

    // this metod count all position what can be use for understand about what block need stay alive
    private BooleanDto booleanMetodGlobal(Element mainElement) {
        BooleanDto booleanDto = new BooleanDto();

        if (mainElement.tag().toString().equals("head")) {
            booleanDto.setDeleteOrNot(false);
        }
        booleanDto.setContainH1(containH1Orh2(mainElement));
        booleanDto.setContaineImage(containeImage(mainElement));
        booleanDto.setContainIframeVideo(containIframeVideo(mainElement));

//        if (!noHaveDateFlag(mainElement)) {
//            booleanDto.setContainDate(true);
//        }
        countForBoolen(mainElement, booleanDto);
        booleanDto.setTextLenght(mainElement.text().length());
        return booleanDto;
    }


    // если у хотяби одного наследника есть н1 н2 то будет true
    private boolean containH1Orh2(Element element) {
        return element.getElementsByTag("h1").size() != 0 |
                element.getElementsByTag("h2").size() != 0;
    }

    // если у хотяби одного наследника есть img то будет true
    private boolean containeImage(Element element) {
        return element.getElementsByTag("img").size() != 0;
    }

    // если у хотяби одного наследника есть iframe то будет true  думаю удалить
    private boolean containIframeVideo(Element element) {
        return element.getElementsByTag("iframe").size() != 0;
    }

    // метод которий считает таг keyWord Text and other
    private void countForBoolen(Element element, BooleanDto booleanDto) {

        Element clone = element.clone();
        List<Element> elementDepthOne = recursiveMetodGetAllChildByDepth0(clone, new ArrayList<>());
        int countTagA = 0;
        int countTextKeyWord = 0;
        int containText = 0;
        int keyWordInTagA = 0;
        int lenghtTextInATagLenght = 0;
        int lenghtClearText = 0;
        int countClearTextKeyWord = 0;


        for (Element byOneDepthToEnd : elementDepthOne) {
            RecursiaForTagADto checkTagA = new RecursiaForTagADto();
            checkTagA.setElement(byOneDepthToEnd);

            // если содержит таг а на глубину 3
            if (containTagAByThreeDepth(checkTagA)) {
                lenghtTextInATagLenght += byOneDepthToEnd.text().length();
                countTagA++;
                // ксли содержит дополнительно ключевое слово
                if (containKeyWord(byOneDepthToEnd)) {
                    keyWordInTagA++;
                }
                //  нет на глубине 3
            } else {
                // длина всего текста
                lenghtClearText += byOneDepthToEnd.text().length();
                if (containKeyWord(byOneDepthToEnd)) {
                    countClearTextKeyWord++;
                }
            }
            // текст считаю по пробелам
            if (!Objects.equals(byOneDepthToEnd.text(), "")) {

                containText++;
            }
            if (containKeyWord(byOneDepthToEnd)) {
                countTextKeyWord++;
            }

        }
        booleanDto.setLenghtClearText(lenghtClearText);
        booleanDto.setCountKeyWordInClearText(countClearTextKeyWord);
        booleanDto.setKeyWordInTagA(keyWordInTagA);
        booleanDto.setCountTagA(countTagA);
        booleanDto.setCountTextBy1Depth0(containText);
        booleanDto.setCountTextKeyWord(countTextKeyWord);
        booleanDto.setLenghtTextInATag(lenghtTextInATagLenght);
    }

    // если родитель содержит таг и имеиит родителя
    private boolean containParantThisTagA(Element element) {
        return null != element.parent() &
                element.tag().toString().equals("a");

    }

    // ищем содержимое <a таг
    private boolean containTagAByThreeDepth(RecursiaForTagADto recursiaForTagADto) {
// если глубина 3
        if (recursiaForTagADto.getCountDepth() == 3) {
            return false;
        } else {
            Element element = recursiaForTagADto.getElement();
            int count = recursiaForTagADto.getCountDepth();
            if (containParantThisTagA(element)) {
                return true;
            } else {
                recursiaForTagADto.setCountDepth(count + 1);
                if (element.parent() != null) {
                    recursiaForTagADto.setElement(element.parent());
                    return containTagAByThreeDepth(recursiaForTagADto);
                } else {
                    return false;
                }

            }
        }

    }

    // содержит ли ключевие слова
    private boolean containKeyWord(Element element) {
        for (String keyWord : keyWordList) {
            if (element.text().contains(keyWord)) {
                return true;
            }

        }
        return false;
    }

//    private boolean leaveCss(Element child) {
//        return child.getElementsByTag("link").size() != 0;
//    }

    // TODO возможно поменять на дерево
    // не очень ефективний способ получить всех наследников
    private List<Element> recursiveMetodGetAllChildByDepth0(Element element, List<Element> listOneDepthElem) {
        // получаем все наследников
        List<Element> supportListElem = element.children();
        if (supportListElem.size() == 0) {
            listOneDepthElem.add(element);
        }
        for (Element child : element.children()) {

            String ss = "ss";
            // список елементи одной глубини
            List<Element> oneDepthElement = recursiveMetodGetAllChildByDepth0(child, new ArrayList<>());
            // идем и собираем всех возможно переделать на дерево всетаки будет проще
            if (oneDepthElement.size() > 0) {
                listOneDepthElem.addAll(oneDepthElement);
            }
        }
        String sss = "ss";
        return listOneDepthElem;
    }

    // TODO Оставлять всю верхушку
    // флаг для оставления линков
    private boolean getFlagDeleteByFilters(Element child, Element parant) {
        boolean flag = true;
        // if contain tag script,noscript, style
        if (flagDeleteNoNeedTags(child)) {
            return true;
        }
        // if contain css
        if (child.tag().toString().equals("link")) {
            return false;
        }
// what we need by keyWord
        return flag;
    }


    // удаляет все из списка тагов
    private boolean flagDeleteNoNeedTags(Element element) {
        for (String string : filterTag) {
            if (element.tag().toString().equals(string)) {
                return true;
            }
        }
        return false;
    }
}

//    // can be change
//    private boolean noHaveDateFlag(Element child) {
//
//        List<Pattern> patternList = new ArrayList<>();
//        // TODO find or do some more pattern this date
//        Pattern pattern0 = Pattern.compile("(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[012])/((19|20)\\d\\d)");
//        Pattern pattern1 = Pattern.compile("(0?[1-9]|[12][0-9]|3[01]).(0?[1-9]|1[012]).((19|20)\\d\\d)");
//        Pattern pattern2 = Pattern.compile("(0?[1-9]|[12][0-9]|3[01]) ([^\\s]) ((19|20)\\d\\d)");
//
//        patternList.add(pattern0);
//        patternList.add(pattern1);
//        //Here we find all document elements which have some element with the searched pattern
//        for (Pattern pattern : patternList) {
//            Elements elements = child.getElementsMatchingText(pattern);
//            if (elements.size() != 0) {
//                return false;
//            }
////                    List<Element> finalElements = elements.stream().filter(elem -> isLastElem(elem, pattern)).collect(Collectors.toList());
////                    finalElements.stream().forEach(elem ->
////                            System.out.println("Node: " + elem.html())
////                    );
////                    String ss = "ss";
//        }
//        return true;
//    }
//
//    // use for doc in other location
//    public boolean containKeyWordInDoc(Document doc, List<String> keyWords) {
//        for (String keyWord : keyWords) {
//            if (doc.text().contains(keyWord)) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    private boolean noContainKeyWordInElement(Element element, List<String> keyWords) {
//        for (String keyWord : keyWords) {
//            if (element.text().contains(keyWord)) {
//                return false;
//            }
//        }
//        return true;
//    }


