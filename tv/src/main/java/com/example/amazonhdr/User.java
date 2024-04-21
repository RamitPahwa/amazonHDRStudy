package com.example.amazonhdr;

public class User {

    int id;
    String name;
    int sessID;
    int vidCompleted = 0;

    int trainVidCompleted = 0;

    boolean isTrain = false;

    // This should be singleton class, but no worries
    public User(int id, String name, int sessID) {
        this.id = id;
        this.name = name;
        this.sessID = sessID;
        this.vidCompleted = 0;
        this.trainVidCompleted = 0;
        this.isTrain = false;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSessID() {
        return sessID;
    }

    public void setSessID(int sessID) {
        this.sessID = sessID;
    }

    public int getVidCompleted() {
        return vidCompleted;
    }

    public int getTrainVidCompleted()
    {
        return trainVidCompleted;
    }

    public boolean getIsTrain()
    {
        return isTrain;
    }

    public void setIsTrain(boolean isTrain)
    {
        this.isTrain = isTrain;
    }

    public void setVidCompleted(int vidCompleted) {
        this.vidCompleted = vidCompleted;
    }
}
