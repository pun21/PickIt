package com.bcdevops.pickit.database.handling.crud;

import com.bcdevops.pickit.model.Vote;

public class VoteCRUD extends CRUD{
    final private String KEY_CREATE_VOTE = "PickIt/php/create_vote.php";
    final private String KEY_READ_VOTES = "PickIt/php/retrieve_pickit_votes.php";
    private Vote vote;

    public VoteCRUD(Vote vote){
        this.vote = vote;
    }
    public VoteCRUD(int pickItID) {
        vote = new Vote();
        vote.setPickItID(pickItID);
    }

    @Override
    protected String createExtension() {
        return KEY_CREATE_VOTE + "?PickItID=" + vote.getPickItID() + "&ChoiceID=" + vote.getChoiceID() + "&UserID=" + vote.getUserID();
    }

    @Override
    protected String readExtension() {
        return KEY_READ_VOTES + "?PickItID=" + vote.getPickItID();
    }

    @Override
    protected String updateExtension() {
        try {
            throw new Exception("Not Implemented");
        }catch(Exception e){
        }
        return null;
    }

    @Override
    protected String deleteExtension() {
        try {
            throw new Exception("Not Implemented");
        }catch(Exception e){
        }
        return null;
    }
}
