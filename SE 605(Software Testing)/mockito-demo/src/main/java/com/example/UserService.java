package com.example;

public class UserService {
    UserRepository userRepository;
    Hashing hashing;

    public UserService(UserRepository userRepository,Hashing hashing){
        this.userRepository = userRepository;
        this.hashing = hashing;
    }
    public void login(String email, String password){
       User user = userRepository.getUserByEmail(email);

       if(user != null){
           String hashedPassword = hashing.hashing(password);
           if(hashedPassword.equals(user.pass)){
               System.out.println("Login Successful");
           }
           else{
               System.out.println("Unsuccessful");
           }
       }

    }
}
