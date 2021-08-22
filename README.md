# FoodOrder
Food ordering application with Kotlin for Yemeksepeti Android Bootcamp.

## Technologies and Libraries
- MVVM
- Dagger Hilt
- Coroutines
- Retrofit2
- Room
- ViewBinding
- Navigation
- SafeArgs
- OkHttp
- Glide
- Gson
- Lottie

## Back-end
I developed a RESTful API for application back-end with Node.js. It's live on [Heroku](https://www.heroku.com/), can look to the [repository](https://github.com/yusufarisoy/yemeksepeti-bootcamp-final-project-server) for more details.
```
https://yemeksepeti-bootcamp-project.herokuapp.com/
```

## App Content - User Account
#### Splash, OnBoarding, Login and Register
- Authentication provided with **JWT**
- JWT token saved in **Shared Preferences**
<img src="./app_preview/recording_1.gif" alt="gif" height="500"/>

#### Basket, Foods and Popular Restaurant
- Cart (or basket) handled with **Room** and **Shared View Model**
- Check for session in splash
- Can't add foods from different restaurants, if user wants can clear other foods and add the new one
<img src="./app_preview/recording_2.gif" alt="gif" height="500"/>

#### Profile, Addresses and Restaurant list of address
<img src="./app_preview/recording_3.gif" alt="gif" height="500"/>

#### Cart and Order Confirm
<img src="./app_preview/recording_4.gif" alt="gif" height="500"/>

#### Order History, Order Review and Change Password
- Regex used for password validation
- Can't rate the already rated orders
<img src="./app_preview/recording_5.gif" alt="gif" height="500"/>

#### Edit Profile
<img src="./app_preview/recording_6.gif" alt="gif" height="500"/>

## Restaurant Owner Account
#### Register, Update Profile and Change Password
<img src="./app_preview/recording_7.gif" alt="gif" height="500"/>

#### Add and Edit Restaurant
<img src="./app_preview/recording_8.gif" alt="gif" height="500"/>

#### Add and Update Food
<img src="./app_preview/recording_9.gif" alt="gif" height="500"/>

#### Order from new restaurant to new address
<img src="./app_preview/recording_10.gif" alt="gif" height="500"/>

#### Confirm Order
<img src="./app_preview/recording_11.gif" alt="gif" height="500"/>