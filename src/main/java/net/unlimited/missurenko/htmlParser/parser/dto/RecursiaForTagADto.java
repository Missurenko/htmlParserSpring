package net.unlimited.missurenko.htmlParser.parser.dto;

import org.jsoup.nodes.Element;

public class RecursiaForTagADto {

    private Element element;

    private int countDepth;




    public Element getElement() {
        return element;
    }

    public void setElement(Element element) {
        this.element = element;
    }

    public int getCountDepth() {
        return countDepth;
    }

    public void setCountDepth(int countDepth) {
        this.countDepth = countDepth;
    }
}
