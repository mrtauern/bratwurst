package com.example.bratwurst.service;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.*;
import com.example.bratwurst.model.FriendsViewModel;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.bratwurst.model.User;
import com.example.bratwurst.repo.UserRepo;
import org.springframework.stereotype.Service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
// @EnableAutoConfiguration
public class UserServiceImpl implements UserService {

    Logger log = Logger.getLogger(UserServiceImpl.class.getName());

    @Autowired
    UserRepo userRepo;

    @Autowired
    AmazonSNS amazonSNS;

    private int authCode;

    private String topicArn = "arn:aws:sns:eu-west-1:966879952819:bratwurst-two-factor-auth";

    @Override
    public boolean validateAuthCode(int inputCode) {

        if (inputCode == this.authCode){

            return true;
        }else {

            return false;
        }
    }

    @Override
    public void publishMessage(String email) throws JSONException {

        Random rand = new Random();
        int authCodeInt = rand.nextInt(100000);

        this.authCode = authCodeInt;

        PublishRequest publishRequest = new PublishRequest(topicArn, "your code is: " + authCode, "Auth code");

        Map<String, MessageAttributeValue> messageAttributeValueMap = new HashMap<>();

        messageAttributeValueMap.put("email", new MessageAttributeValue().withDataType("String").withStringValue(email.toLowerCase()));

        publishRequest.withMessageAttributes(messageAttributeValueMap);

        amazonSNS.publish(publishRequest);
    }

    @Override
    public void subscribeToTopic(String email) throws JSONException {

        SubscribeRequest subscribeRequest = new SubscribeRequest(topicArn, "email", email.toLowerCase());

        amazonSNS.subscribe(subscribeRequest);

        // setPolicyFilter(email);

    }

    @Override
    public void setPolicyFilter(String email) throws JSONException {

        String[] s = {email.toLowerCase()};

        JSONObject jo = new JSONObject();

        jo.put("email", s);

        String filterP = jo.toString();

        ListSubscriptionsByTopicRequest listSubscriptionsByTopicRequest = new ListSubscriptionsByTopicRequest(topicArn);

        ListSubscriptionsByTopicResult list = amazonSNS.listSubscriptionsByTopic(listSubscriptionsByTopicRequest);

        List<Subscription> subArnList = list.getSubscriptions();

        for (int i = 0; i < subArnList.size(); i++){

            Subscription subArn = subArnList.get(i);

            if (subArn.getEndpoint().equals(email.toLowerCase())){

                String subArnValue = subArn.getSubscriptionArn();

                SetSubscriptionAttributesRequest request = new SetSubscriptionAttributesRequest(subArnValue, "FilterPolicy", jo.toString());

                amazonSNS.setSubscriptionAttributes(request);

                break;
            }
        }

    }


    @Override
    public User getLogin(String username, String password) {
        User user  = userRepo.getLogin(username, password);
        if (user == null){
            return null;
        }
        else{
            return user;
        }
    }

    public List<FriendsViewModel> getUsers(int id) {
        return userRepo.getUsers(id);
    }


    @Override
    public User addUser(User user, String confirm_password) {

        User matching_user = userRepo.findLogin(user.getUsername(), user.getEmail());

        String current_pass = user.getPassword();

        boolean strongPassword = passwordStrong(user.getPassword());

        if (strongPassword != true){
            return null;
        }

        // if the two passwords match
        if (current_pass.equals(confirm_password)){

            if (matching_user == null){
                return userRepo.addUser(user);
            }

            // if there is a match on email
            if (user.getEmail().equals(matching_user.getEmail())){

                matching_user.setUsername(null);

                return matching_user;
            // if there is a match on username
            } else if(user.getUsername().toLowerCase().equals(matching_user.getUsername().toLowerCase())) {

                matching_user.setEmail(null);

                return matching_user;
            }

            return null;

        } else{

            return null;
        }
    }

    @Override
    public boolean passwordStrong(String password) {

        boolean haveUpper = false;
        boolean haveLower = false;
        boolean length = false;
        boolean specialCharacter = false;

        // Checks for lowercase and upercase
        for( int i = 0; i< password.length(); i++){

            char ch = password.charAt(i);

            if (Character.isUpperCase(ch)){
                haveUpper = true;
            }else if(Character.isLowerCase(ch)){
                haveLower = true;
            }
        }
        // Checks for length
        if (password.length() > 7) {
            length = true;
        }

        // Checks for special character
        Pattern p = Pattern.compile("[A-Za-z0-9]");
        Matcher m = p.matcher(password);
        boolean b = m.find();
        if (b){
            specialCharacter = true;
        }

        if (haveUpper && haveLower && length && specialCharacter){
            return true;
        }else{

            return false;
        }
    }


    public User getUserById(int id) {
        return userRepo.getUserById(id);
    }

    public void friendRequest(int userId, int receiverId){
        userRepo.friendRequest(userId, receiverId);
    }

    public List<User> notifications(int id){

        return userRepo.notifications(id);
    }

    public void acceptRequest(int receiverId, int userId) {
        userRepo.acceptRequest(receiverId, userId);
    }

    @Override
    public boolean isAdmin(User user){

        if (user.getEmail().equals("tobias.ku22@gmail.com")){

            return true;
        }else {

            return false;

        }
    }

    @Override
    public void deleteById(int id) {
        userRepo.deleteById(id);
    }

}
