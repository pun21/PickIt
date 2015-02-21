package com.spun.pickit.database.handling;

import com.spun.pickit.database.handling.crud.ChoiceCRUD;
import com.spun.pickit.database.handling.crud.FollowingCRUD;
import com.spun.pickit.database.handling.crud.LeaderCRUD;
import com.spun.pickit.database.handling.crud.PickitCRUD;
import com.spun.pickit.database.handling.crud.UserCRUD;
import com.spun.pickit.database.handling.crud.VotesChoiceCRUD;
import com.spun.pickit.database.handling.crud.VotesPickitCRUD;
import com.spun.pickit.database.handling.crud.VotesUserCRUD;


public class DatabaseHandler {
    private ChoiceCRUD choiceCRUD;
    private FollowingCRUD followingCRUD;
    private LeaderCRUD leaderCRUD;
    private PickitCRUD pickitCRUD;
    private UserCRUD userCRUD;
    private VotesChoiceCRUD votesChoiceCRUD;
    private VotesPickitCRUD votesPickitCRUD;
    private VotesUserCRUD votesUserCRUD;


    public void createUser(){
        //pass in the values to make the user
        //TODO need to use the correct constructor  ...
        this.userCRUD = new UserCRUD("USERNAME");
        this.userCRUD.create();
    }

    public void getUser(){
        //TODO need to have the correct format of this
        this.userCRUD = new UserCRUD("USERID");
        this.userCRUD.read();
    }

    public void updateUser(){
        //TODO need the correct format of this
        this.userCRUD = new UserCRUD("STUDD TO UPDATE");
        this.userCRUD.update();
    }

    public void getLeaders(){
        //TODO correct format
        this.followingCRUD = new FollowingCRUD("USERID");
        this.followingCRUD.read();
    }

    public void getFollowers(){
        //TODO correct format
        this.leaderCRUD = new LeaderCRUD("USERID");
        this.leaderCRUD.read();
    }

    public void createFollowing(){
        //TODO correct format
        this.followingCRUD = new FollowingCRUD("FOLLOWER,LEADER");
        this.followingCRUD.create();
    }

    public void deleteFollowing(){
        //TODO correct format
        this.followingCRUD = new FollowingCRUD("FOLLOW LEADER");
        this.followingCRUD.delete();
    }

    public void getPickItVotes(){
        //TODO correct format
        this.votesPickitCRUD = new VotesPickitCRUD("FORMAT");
        this.votesPickitCRUD.read();
    }

    public void getUserVotes(){
        //TODO correct format
        this.votesUserCRUD = new VotesUserCRUD("FORMAT");
        this.votesUserCRUD.read();
    }

    public void createUserVote(){
        //TODO correct format
        this.votesUserCRUD = new VotesUserCRUD("FORMAT");
        this.votesUserCRUD.create();
    }

    public void getChoiceVotes(){
        //TODO correct format
        this.votesChoiceCRUD = new VotesChoiceCRUD("FORMAT");
        this.votesUserCRUD.read();
    }



}
