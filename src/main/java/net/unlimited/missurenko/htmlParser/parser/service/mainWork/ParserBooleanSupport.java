package net.unlimited.missurenko.htmlParser.parser.service.mainWork;

import net.unlimited.missurenko.htmlParser.parser.dto.BooleanDto;
import net.unlimited.missurenko.htmlParser.parser.dto.RecursiaForTagADto;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

class ParserBooleanSupport {

    /**
     * @param mainElement     html what do parsing
     * @param flagDeleteOrNot list what contain boolean what need delete
     *                        return cut html
     */
    private void deleteByFlag(Element mainElement, List<Boolean> flagDeleteOrNot) {

        for (int i = flagDeleteOrNot.size() - 1; i >= 0; --i) {
            if (flagDeleteOrNot.get(i)) {
                mainElement.child(i).remove();
            }
        }
    }
    // this metod like deleteByFlag
    void deleteOnly(Element mainElement, List<Boolean> whatDelete) {
        for (int i = whatDelete.size() - 1; i >= 0; --i) {
            if (whatDelete.get(i)) {
                mainElement.child(i).remove();

            }
        }
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
    // если родитель содержит таг и имеиит родителя
    private boolean containParantThisTagA(Element element) {
        return null != element.parent() &
                element.tag().toString().equals("a");

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

    // содержит ли ключевие слова
    boolean containKeyWord(Element element,List<String> keyWordList) {
        for (String keyWord : keyWordList) {
            if (element.text().contains(keyWord)) {
                return true;
            }

        }
        return false;
    }
    // TODO Оставлять всю верхушку
    // флаг для оставления линков

    /**
     * @param child
     * @return
     */
    private boolean takeFlagsForDeleteByFilters(Element child, List<String> filterTag) {
        boolean flag = true;
        // if contain tag script,noscript, style
        if (flagDeleteNoNeedTags(child,filterTag)) {
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

    /**
     * @param element child element
     * @return false if part alive
     */
    private boolean flagDeleteNoNeedTags(Element element, List<String> filterTag) {
        for (String string : filterTag) {
            if (element.tag().toString().equals(string)) {
                return true;
            }
        }
        return false;
    }
    private boolean containTagHeaderBody(Element mainElement, Element child) {
        return mainElement.tag().toString().equals("body") &
                child.tag().toString().equals("header") |
                mainElement.tag().toString().equals("body") &
                        child.tag().toString().equals("footer");
    }

    /**
     * @param mainElement html what do parsing
     */
    private void deleteMetod(Element mainElement,List<String> filterTag) {
        List<Boolean> flagDeleteOrNot = new ArrayList<>();
        for (int i = 0; i < mainElement.children().size(); i++) {
            // take flag for delete
            boolean flag = takeFlagsForDeleteByFilters(mainElement.child(i), filterTag);
            flagDeleteOrNot.add(flag);
        }
        // do function deleting
        deleteByFlag(mainElement, flagDeleteOrNot);
    }
    /**
     * have short recurtion
     *
     * @param mainElement
     */
    private void shortRecursive(Element mainElement,List<String> filterTag) {

        deleteMetod(mainElement,filterTag);
        for (Element child : mainElement.children()) {
            shortRecursive(child,filterTag);
        }
    }
    /**
     *
     * @param mainElement parsered html
     * metod delete tags, header, footer
     */
    void deleteHeaderFoaterTags(Element mainElement,List<String> filterTag) {
        List<Boolean> deleteBlockList = new ArrayList<>();
        for (Element child : mainElement.children()) {
            if (containTagHeaderBody(mainElement, child)) {
                deleteBlockList.add(true);
            } else if (flagDeleteNoNeedTags(child, filterTag)) {
                deleteBlockList.add(true);
            } else {
                deleteBlockList.add(false);
            }
            if (child.tag().toString().equals("head")) {
                shortRecursive(child,filterTag);
            }
        }
        deleteOnly(mainElement, deleteBlockList);
    }
    /**
     *
     * @param lenghtAllText
     * @param lenghtTextTagA
     * @return
     */
    int cleanText(int lenghtAllText, int lenghtTextTagA) {
        return lenghtAllText - lenghtTextTagA;
    }

    // this metod count all position what can be use for understand about what block need stay alive
    BooleanDto booleanMetodGlobal(Element mainElement,List<String> keyWordList) {
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
        countForBoolen(mainElement, booleanDto,keyWordList);
        booleanDto.setTextLenght(mainElement.text().length());
        return booleanDto;
    }

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

    // метод которий считает таг keyWord Text and other
    private void countForBoolen(Element element, BooleanDto booleanDto,List<String> keyWordList) {

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
                if (containKeyWord(byOneDepthToEnd, keyWordList)) {
                    keyWordInTagA++;
                }
                //  нет на глубине 3
            } else {
                // длина всего текста
                lenghtClearText += byOneDepthToEnd.text().length();
                if (containKeyWord(byOneDepthToEnd, keyWordList)) {
                    countClearTextKeyWord++;
                }
            }
            // текст считаю по пробелам
            if (!Objects.equals(byOneDepthToEnd.text(), " ")) {

                containText++;
            }
            if (containKeyWord(byOneDepthToEnd, keyWordList)) {
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

}
