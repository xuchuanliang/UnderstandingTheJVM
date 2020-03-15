package spring;

import java.util.concurrent.TimeUnit;

public class UserService {
    public void login(){
        System.out.println("login....");
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
        }
    }
}
