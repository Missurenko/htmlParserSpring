package net.unlimited.missurenko.htmlParser.parser.service.mainWork;

public class CommitCode {



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

}
