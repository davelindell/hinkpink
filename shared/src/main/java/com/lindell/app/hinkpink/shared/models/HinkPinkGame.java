package com.lindell.app.hinkpink.shared.models;
import org.apache.commons.lang3.ArrayUtils;

import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by lindell on 6/15/15.
 */
@Entity
public class HinkPinkGame implements Serializable {
    @Id private Long id;
    private Long hintPlayerID;
    private Long guessPlayerID;
    private String hint;
    private int numSyllables;
    private boolean guessedCorrect;
    private List<String> guesses;
    private List<String> extraHints;

    public HinkPinkGame() {
        hintPlayerID = null;
        guessPlayerID = null;
        hint = null;
        numSyllables = 0;
        guessedCorrect = false;
        guesses = new ArrayList<String>();
        extraHints = new ArrayList<String>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HinkPinkGame that = (HinkPinkGame) o;

        return id.equals(that.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getHintPlayerID() {
        return hintPlayerID;
    }

    public void setHintPlayerID(Long hintPlayerID) {
        this.hintPlayerID = hintPlayerID;
    }

    public Long getGuessPlayerID() {
        return guessPlayerID;
    }

    public void setGuessPlayerID(Long guessPlayerID) {
        this.guessPlayerID = guessPlayerID;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public int getNumSyllables() {
        return numSyllables;
    }

    public void setNumSyllables(int numSyllables) {
        this.numSyllables = numSyllables;
    }

    public boolean isGuessedCorrect() {
        return guessedCorrect;
    }

    public void setGuessedCorrect(boolean guessedCorrect) {
        this.guessedCorrect = guessedCorrect;
    }

    public List<String> getGuesses() {
        return guesses;
    }

    public void setGuesses(List<String> guesses) {
        this.guesses = guesses;
    }

    public List<String> getExtraHints() {
        return extraHints;
    }

    public void setExtraHints(List<String> extraHints) {
        this.extraHints = extraHints;
    }

    //    // Parcelling part
//    public HinkPinkGame(Parcel in){
//        id = in.readLong();
//
//        long[] idList = new long[playerIDList.size()];
//        in.readLongArray(idList);
//        playerIDList = Arrays.asList(ArrayUtils.toObject(idList));
//
//        hint = in.readString();
//        numSyllables = in.readInt();
//        guessedCorrect = in.readByte() != 0;
//
//        String[] guessList = new String[guesses.size()];
//        in.readStringArray(guessList);
//    }
//
//    @Override
//    public int describeContents(){
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeLong(id);
//
//        Long[] idList = new Long[playerIDList.size()];
//        idList = playerIDList.toArray(idList);
//        dest.writeLongArray(ArrayUtils.toPrimitive(idList));
//
//        dest.writeString(hint);
//        dest.writeInt(numSyllables);
//        dest.writeByte((byte) (guessedCorrect ? 1 : 0));
//
//        String[] guessList = new String[guesses.size()];
//        guessList = guesses.toArray(guessList);
//        dest.writeStringArray(guessList);
//    }
}
