package com.example.murthyavanithsa.wishlistapp;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by durga on 6/3/16.
 */
public class Urlendpoints extends AppCompatActivity {
//    public String BASE_URL = "http://139.59.255.197/";
public String BASE_URL = "http://192.168.2.12/";
    public String getsignup(){
        String signup = "users/signup";
        return BASE_URL+signup;
    }
    public String getloginurl(){
        String loginurl = "users/login";
        return BASE_URL+loginurl;
    }
    public String getusertasksurl(){
        String  usertaskurl= "usertasks/get_user_tasks";
        return BASE_URL+usertaskurl;
    }
    public String getupdateurl(){
        String updateurl = "usertasks/update_user_task";
        return BASE_URL+updateurl;
    }
    public String getdeleteurl(){
        String deleteurl = "usertasks/delete_user_task";
        return BASE_URL+deleteurl;
    }
    public String getaddtasksurl(){
        String addtasksurl = "usertasks/add_user_task";
        return BASE_URL+addtasksurl;
    }
    public String searchitems(){
        String searchitems = "basket/item_search_results";
        return BASE_URL+searchitems;
    }
}
