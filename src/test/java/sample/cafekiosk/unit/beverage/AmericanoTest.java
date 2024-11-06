package sample.cafekiosk.unit.beverage;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class AmericanoTest {

    @Test
    void getName(){
        Americano americano = new Americano();
        //assertEquals("아메리카노", americano.getName()); //jupiter.api
        assertThat(americano.getName()).isEqualTo("아메리카노"); //assertj

    }

    @Test
    void getPrice(){
        Americano americano = new Americano();
        assertThat(americano.getPrice()).isEqualTo(4000);
    }

}