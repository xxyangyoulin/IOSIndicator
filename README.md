# IOSIndicator
- 仿IOS风格ViewPager or Fragment 指示器.
- 在模仿《你的名字》日记app的时候, 没有找到同样风格的指示器,自己动手造了一个。

<img src="https://github.com/mnnyang/IOSIndicator/blob/master/screenshot/screenshot1.png" width="25%" /><img src="https://github.com/mnnyang/IOSIndicator/blob/master/screenshot/screenshot2.png" width="25%" />

1.  

    ```java
    allprojects {
        repositories {
            maven { url "https://jitpack.io" }
        }
    }
    ```
    
2. 
    ```java
    dependencies {
        compile 'com.github.mnnyang:IOSIndicator:1.0.0'
    }
    ```
    
3. 
    ```java
    indicator = (IOSIndicator) findViewById(R.id.indicator);
            indicator.setItemTitles(titles);
    
            //主动切换 切换监听
            indicator.setSwitchListener(new IOSIndicator.ClickListener() {
                @Override
                public void onClick(int currentIndex, boolean isRepeat) {
                    if (!isRepeat) {
                        System.out.println("点击了--" + currentIndex);
                    }
                }
            });
         
         
    //switch
    indicator.setCurrentPage(currentPage++);
```