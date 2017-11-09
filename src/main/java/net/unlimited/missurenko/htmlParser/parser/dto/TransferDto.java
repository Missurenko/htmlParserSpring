package net.unlimited.missurenko.htmlParser.parser.dto;

import java.util.List;

public class TransferDto {

    private List<Boolean> deleteList;

    private boolean endRecursion;

    public List<Boolean> getDeleteList() {
        return deleteList;
    }

    public void setDeleteList(List<Boolean> deleteList) {
        this.deleteList = deleteList;
    }

    public boolean isEndRecursion() {
        return endRecursion;
    }

    public void setEndRecursion(boolean endRecursion) {
        this.endRecursion = endRecursion;
    }
}
