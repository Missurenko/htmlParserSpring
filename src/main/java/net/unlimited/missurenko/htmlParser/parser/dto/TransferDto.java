package net.unlimited.missurenko.htmlParser.parser.dto;

import java.util.List;

public class TransferDto {

    private List<Boolean> delete;

    private int countFalse;

    public List<Boolean> getDelete() {
        return delete;
    }

    public void setDelete(List<Boolean> delete) {
        this.delete = delete;
    }

    public int getCountFalse() {
        return countFalse;
    }

    public void setCountFalse(int countFalse) {
        this.countFalse = countFalse;
    }
}
