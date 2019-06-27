package com.mr.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class isAspect {
    @Before(value="@annotation(com.mr.annotation.isLogin)")
    public void doBefore(){
        System.out.println("nice");
    }

}
