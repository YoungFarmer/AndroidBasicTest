package com.example.anotherapp;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        String joinedString = String.join(" ", new String[]{"Convert", "With", "Java", "Streams"});
        System.out.println(joinedString);
    }

}