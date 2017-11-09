package net.unlimited.missurenko.htmlParser.parser.dto;

/**
 * Created by bm on 16.10.17.
 */

public class BooleanDto {

// delete
    private boolean containeImage;
// delete
    private boolean containIframeVideo;

    // ok
    private boolean containH1;
    // ok
    private boolean containTagA;
    // ok
    private boolean containKeyWord;
// delete
    private boolean containDate;

    private int textLenght;
//????
    private int countTextBy1Depth0;
// delete
    private int countTagA;
// delete
    private int countTextKeyWord;

    private int lenghtTextInATag;

    private int lenghtClearText;

    private int countKeyWordInClearText;
// delete
    private int keyWordInTagA;

    private boolean deleteOrNot;

    public boolean isDeleteOrNot() {
        return deleteOrNot;
    }

    public int getKeyWordInTagA() {
        return keyWordInTagA;
    }

    public int getLenghtClearText() {
        return lenghtClearText;
    }

    public void setLenghtClearText(int lenghtClearText) {
        this.lenghtClearText = lenghtClearText;
    }

    public int getCountKeyWordInClearText() {
        return countKeyWordInClearText;
    }

    public void setCountKeyWordInClearText(int countKeyWordInClearText) {
        this.countKeyWordInClearText = countKeyWordInClearText;
    }

    public void setKeyWordInTagA(int keyWordInTagA) {
        this.keyWordInTagA = keyWordInTagA;
    }

    public void setDeleteOrNot(boolean deleteOrNot) {
        this.deleteOrNot = deleteOrNot;
    }

    public int getCountTextBy1Depth0() {
        return countTextBy1Depth0;
    }

    public void setCountTextBy1Depth0(int countTextBy1Depth0) {
        this.countTextBy1Depth0 = countTextBy1Depth0;
    }

    public int getCountTagA() {
        return countTagA;
    }

    public void setCountTagA(int countTagA) {
        this.countTagA = countTagA;
    }

    public int getCountTextKeyWord() {
        return countTextKeyWord;
    }

    public void setCountTextKeyWord(int countTextKeyWord) {
        this.countTextKeyWord = countTextKeyWord;
    }


    public boolean isContaineImage() {
        return containeImage;
    }

    public void setContaineImage(boolean containeImage) {
        this.containeImage = containeImage;
    }

    public boolean isContainIframeVideo() {
        return containIframeVideo;
    }

    public void setContainIframeVideo(boolean containIframeVideo) {
        this.containIframeVideo = containIframeVideo;
    }

    public boolean isContainH1() {
        return containH1;
    }

    public void setContainH1(boolean containH1) {
        this.containH1 = containH1;
    }

    public boolean isContainTagA() {
        return containTagA;
    }

    public void setContainTagA(boolean containTagA) {
        this.containTagA = containTagA;
    }

    public boolean isContainKeyWord() {
        return containKeyWord;
    }

    public void setContainKeyWord(boolean containKeyWord) {
        this.containKeyWord = containKeyWord;
    }

    public boolean isContainDate() {
        return containDate;
    }

    public void setContainDate(boolean containDate) {
        this.containDate = containDate;
    }

    public int getTextLenght() {
        return textLenght;
    }

    public void setTextLenght(int textLenght) {
        this.textLenght = textLenght;
    }

    public int getLenghtTextInATag() {
        return lenghtTextInATag;
    }

    public void setLenghtTextInATag(int containTextInATag) {
        this.lenghtTextInATag = containTextInATag;
    }
}
